package com.asteroids.game.client.connection.synchronization;

import com.asteroids.game.controls.Controls;
import com.asteroids.game.controls.RemoteControls;
import com.asteroids.game.dto.ControlsDto;
import com.asteroids.game.dto.GameStateDto;
import com.asteroids.game.dto.IndexedDto;
import com.asteroids.game.dto.mapper.ControlsMapper;
import com.asteroids.game.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LocalStateSynchronizer {
    private long currentIndex;
    private final List<LocalState> recordedStates;
    private final RemoteControls synchronizationControls;
    private Consumer<GameStateDto> gameStateUpdater;
    private Consumer<Float> gameLogicRunner;
    private Supplier<GameStateDto> gameStateSupplier;
    private Player localPlayer;

    public LocalStateSynchronizer(List<LocalState> recordedStates, RemoteControls synchronizationControls) {
        this.recordedStates = recordedStates;
        this.synchronizationControls = synchronizationControls;
    }

    public LocalStateSynchronizer() {
        this(Collections.synchronizedList(new ArrayList<>()), new RemoteControls());
    }

    public void updateAccordingToGameState(Consumer<GameStateDto> updater) {
        gameStateUpdater = updater;
    }

    public void runGameLogic(Consumer<Float> runner) {
        gameLogicRunner = runner;
    }

    public void setLocalPlayer(Player localPlayer) {
        this.localPlayer = localPlayer;
    }

    public void supplyGameState(Supplier<GameStateDto> supplier) {
        gameStateSupplier = supplier;
    }

    public long getCurrentIndex() {
        return currentIndex;
    }

    public void recordState(float delta, ControlsDto controlsDto) {
        recordedStates.add(new LocalState(currentIndex, delta, controlsDto, gameStateSupplier.get()));
        currentIndex++;
    }

    public void synchronize(IndexedDto<GameStateDto> latestState) {
        discardSnapshotsUntil(latestState.getIndex());
        if(recordedStates.size() == 0) return;
        LocalState latestLocalState = recordedStates.get(0);
        if(latestLocalState.getIndex() != latestState.getIndex()) return;
        if(latestLocalState.gameStateMatches(latestState.getDto())) return;

        returnToLatestServerState(latestState);
        rerunGameLogic();
    }

    private void rerunGameLogic() {
        Controls playerOriginalControls = localPlayer.getControls();
        localPlayer.setControls(synchronizationControls);
        for(int i = 1; i < recordedStates.size(); i++) {
            LocalState localState = recordedStates.get(i);
            ControlsMapper.setRemoteControlsByDto(localState.getControlsDto(), synchronizationControls);
            gameLogicRunner.accept(localState.getDelta());
            recordedStates.set(i, updateState(localState));
        }
        localPlayer.setControls(playerOriginalControls);
    }

    private void returnToLatestServerState(IndexedDto<GameStateDto> latestState) {
        gameStateUpdater.accept(latestState.getDto());
        recordedStates.set(0, updateState(recordedStates.get(0)));
    }

    private void discardSnapshotsUntil(long boundaryIndex) {
        recordedStates.removeIf(localState -> localState.getIndex() < boundaryIndex);
    }

    private LocalState updateState(LocalState oldState) {
        return new LocalState(oldState.getIndex(), oldState.getDelta(), oldState.getControlsDto(), gameStateSupplier.get());
    }
}
