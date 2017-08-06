package com.asteroids.game.client.connection;

import com.asteroids.game.connection.Event;
import com.asteroids.game.dto.*;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
import java.util.function.Consumer;

public class SocketIoClient implements Client {
    private final Socket socket;
    private ConnectionState state = ConnectionState.NOT_CONNECTED;
    private Consumer<IntroductoryStateDto> playerConnectedHandler;
    private Consumer<PlayerDto> otherPlayerConnectedHandler;
    private Consumer<UuidDto> otherPlayerDisconnectedHandler;
    private Consumer<GameStateDto> gameStateReceivedHandler;

    private enum ConnectionState {
        NOT_CONNECTED,
        CONNECTING,
        CONNECTED;
    }

    public SocketIoClient(String protocol, String host, int port) {
        String url = protocol + "://" + host + ":" + port;
        try {
            this.socket = IO.socket(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Wrong URL provided for socket connection: " + url, e);
        }
    }

    @Override
    public void connect(PlayerDto playerDto) {
        if(state == ConnectionState.NOT_CONNECTED) {
            state = ConnectionState.CONNECTING;
            socket.on(Socket.EVENT_CONNECT, response -> emit(socket, Event.PLAYER_CONNECTING, playerDto));
            on(socket, Event.PLAYER_CONNECTED, response -> {
                String introductoryStateJson = (String)response[0];
                playerConnectedHandler.accept(Dto.fromJsonString(introductoryStateJson, IntroductoryStateDto.class));
                state = ConnectionState.CONNECTED;
                setupEvents();
            });
            socket.connect();
        }
    }

    @Override
    public void onConnected(Consumer<IntroductoryStateDto> handler) {
        playerConnectedHandler = handler;
    }

    @Override
    public void onOtherPlayerConnected(Consumer<PlayerDto> handler) {
        otherPlayerConnectedHandler = handler;
    }

    @Override
    public void onOtherPlayerDisconnected(Consumer<UuidDto> handler) {
        otherPlayerDisconnectedHandler = handler;
    }

    @Override
    public void onGameStateReceived(Consumer<GameStateDto> handler) {
        gameStateReceivedHandler = handler;
    }

    @Override
    public void sendControls(ControlsDto controlsDto) {
        emit(socket, Event.CONTROLS_SENT, controlsDto);
    }

    @Override
    public boolean isConnected() {
        return state == ConnectionState.CONNECTED;
    }

    private void setupEvents() {
        on(socket, Event.OTHER_PLAYER_CONNECTED, response -> {
            String gameStateDtoJson = (String) response[0];
            otherPlayerConnectedHandler.accept(Dto.fromJsonString(gameStateDtoJson, PlayerDto.class));
        });

        on(socket, Event.OTHER_PLAYER_DISCONNECTED, response -> {
            String playerIdJson = (String) response[0];
            otherPlayerDisconnectedHandler.accept(Dto.fromJsonString(playerIdJson, UuidDto.class));
        });

        on(socket, Event.GAME_STATE_SENT, response -> {
            String gameStateDtoJson = (String) response[0];
            gameStateReceivedHandler.accept(Dto.fromJsonString(gameStateDtoJson, GameStateDto.class));
        });
    }

    private void emit(Socket socket, Event eventName, Dto payload) {
        socket.emit(eventName.toString(), payload.toJsonString());
    }

    private void on(Socket socket, Event eventName, Emitter.Listener handler) {
        socket.on(eventName.toString(), handler);
    }
}
