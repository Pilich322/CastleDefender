package ru.itcube.PilichevDeveloper.utils;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

import ru.itcube.PilichevDeveloper.manager.MemoryManager;

public class GameSession {

    public GameState state;
    long nextEnemyTime;
    long sessionStartTime;
    long pauseStartTime;
    private int score;
    int killedEnemyCount;

    public GameSession() {
    }

    public void startGame() {
        state = GameState.PLAYING;
        score = 0;
        killedEnemyCount = 0;
        sessionStartTime = TimeUtils.millis();
        nextEnemyTime = sessionStartTime + (long) (GameSettings.STARTING_ENEMY_APPEARANCE_COOL_DOWN
            * getEnemyPeriodCoolDown());
    }

    public void pauseGame() {
        state = GameState.PAUSED;
        pauseStartTime = TimeUtils.millis();
    }


    public void resumeGame() {
        state = GameState.PLAYING;
        sessionStartTime += TimeUtils.millis() - pauseStartTime;
    }

    public void endGame() {
        updateScore();
        state = GameState.ENDED;
        ArrayList<Integer> recordsTable = MemoryManager.loadRecordsTable();
        if (recordsTable == null) {
            recordsTable = new ArrayList<>();
        }
        int foundIdx = 0;
        for (; foundIdx < recordsTable.size(); foundIdx++) {
            if (recordsTable.get(foundIdx) < getScore()) break;
        }
        recordsTable.add(foundIdx, getScore());
        MemoryManager.saveTableOfRecords(recordsTable);
    }

    public void destructionRegistration() {
        killedEnemyCount ++;
    }

    public void updateScore() {
        score = killedEnemyCount *10;
    }

    public int getScore() {
        return score;
    }

    public boolean shouldSpawnEnemy() {
        if (nextEnemyTime <= TimeUtils.millis()) {
            nextEnemyTime = TimeUtils.millis() + (long) (GameSettings.STARTING_ENEMY_APPEARANCE_COOL_DOWN
                * getEnemyPeriodCoolDown());
            return true;
        }
        return false;
    }

    private float getEnemyPeriodCoolDown() {
        return (float) Math.exp(-0.001 * (TimeUtils.millis() - sessionStartTime + 1) / 1000);
    }
}
