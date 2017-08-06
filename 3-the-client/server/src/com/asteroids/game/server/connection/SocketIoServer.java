package com.asteroids.game.server.connection;

import com.asteroids.game.connection.Event;
import com.asteroids.game.dto.*;
import com.corundumstudio.socketio.ClientOperations;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.ExceptionListenerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SocketIoServer implements Server {
    private static final Logger logger = LoggerFactory.getLogger(SocketIoServer.class);
    private final SocketIOServer socketio;
    private Consumer<PlayerDto> playerJoinedHandler;
    private BiConsumer<UUID, ControlsDto> playerSentControlsHandler;
    private Consumer<UUID> playerLeftHandler;

    public SocketIoServer(String host, int port) {
        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);

        config = setupExceptionListener(config);
        socketio = new SocketIOServer(config);
    }

    @Override
    public void start() {
        Configuration config = socketio.getConfiguration();
        socketio.start();
        logger.info("Game server listening at " + config.getHostname() + ":" + config.getPort());
        setupEvents();
    }

    @Override
    public void onPlayerConnected(Consumer<PlayerDto> handler) {
        playerJoinedHandler = handler;
    }

    @Override
    public void onPlayerDisconnected(Consumer<UUID> handler) {
        playerLeftHandler = handler;
    }

    @Override
    public void onPlayerSentControls(BiConsumer<UUID, ControlsDto> handler) {
        playerSentControlsHandler = handler;
    }

    @Override
    public void broadcast(GameStateDto gameState) {
        sendEvent(socketio.getBroadcastOperations(), Event.GAME_STATE_SENT, gameState);
    }

    @Override
    public void sendIntroductoryDataToConnected(PlayerDto connected, GameStateDto gameState) {
        socketio.getAllClients().stream()
                .filter(client -> client.getSessionId().equals(UUID.fromString(connected.getId())))
                .findAny()
                .ifPresent(client -> sendEvent(client, Event.PLAYER_CONNECTED,
                        new IntroductoryStateDto(connected, gameState)));
    }

    @Override
    public void notifyOtherPlayersAboutConnected(PlayerDto connected) {
        socketio.getAllClients().stream()
                .filter(client -> !client.getSessionId().equals(UUID.fromString(connected.getId())))
                .forEach(client -> sendEvent(client, Event.OTHER_PLAYER_CONNECTED, connected));
    }

    private void setupEvents() {
        addEventListener(Event.PLAYER_CONNECTING, (client, json, ackSender) -> {
            PlayerDto connecting = Dto.fromJsonString(json, PlayerDto.class);
            PlayerDto withAssignedId = new PlayerDto(client.getSessionId().toString(),
                    connecting.getColor(), connecting.getShipDto());
            playerJoinedHandler.accept(withAssignedId);
        });

        addEventListener(Event.CONTROLS_SENT, (client, json, ackSender) -> {
            ControlsDto dto = Dto.fromJsonString(json, ControlsDto.class);
            playerSentControlsHandler.accept(client.getSessionId(), dto);
        });

        socketio.addDisconnectListener(client -> {
            UUID id = client.getSessionId();
            playerLeftHandler.accept(id);
            sendEvent(socketio.getBroadcastOperations(), Event.OTHER_PLAYER_DISCONNECTED, new UuidDto(id.toString()));
        });
    }

    private Configuration setupExceptionListener(Configuration config) {
        // event exception handling - to keep it simple just throw them as runtime exceptions
        config.setExceptionListener(new ExceptionListenerAdapter() {
            @Override
            public void onEventException(Exception e, List<Object> data, SocketIOClient client) {
                throw new RuntimeException(e);
            }

            @Override
            public void onDisconnectException(Exception e, SocketIOClient client) {
                throw new RuntimeException(e);
            }

            @Override
            public void onConnectException(Exception e, SocketIOClient client) {
                throw new RuntimeException(e);
            }

            @Override
            public boolean exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                // connection error, log and move along
                if(cause instanceof IOException) {
                    logger.warn(cause.getMessage());
                    return true;
                }
                return false;
            }
        });

        return config;
    }

    private void addEventListener(Event eventName, DataListener<String> listener) {
        socketio.addEventListener(eventName.toString(), String.class, listener);
    }

    private void sendEvent(ClientOperations client, Event eventName, Dto data) {
        client.sendEvent(eventName.toString(), data.toJsonString());
    }
}
