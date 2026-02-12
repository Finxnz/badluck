package com.badluck.challenge;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChallengeTimer {
    private final Map<UUID, TimerData> timers = new HashMap<>();

    private static class TimerData {
        long startTime;
        long pausedTime;
        boolean paused;

        TimerData() {
            this.startTime = System.currentTimeMillis();
            this.pausedTime = 0;
            this.paused = false;
        }

        long getElapsedSeconds() {
            if (paused) {
                return pausedTime / 1000;
            }
            return (System.currentTimeMillis() - startTime - pausedTime) / 1000;
        }

        void pause() {
            if (!paused) {
                paused = true;
                pausedTime = System.currentTimeMillis() - startTime - pausedTime;
            }
        }

        void resume() {
            if (paused) {
                paused = false;
                startTime = System.currentTimeMillis() - pausedTime;
                pausedTime = 0;
            }
        }
    }

    public void startTimer(UUID playerUuid) {
        timers.put(playerUuid, new TimerData());
    }

    public void stopTimer(UUID playerUuid) {
        timers.remove(playerUuid);
    }

    public void pauseTimer(UUID playerUuid) {
        TimerData timer = timers.get(playerUuid);
        if (timer != null) {
            timer.pause();
        }
    }

    public void resumeTimer(UUID playerUuid) {
        TimerData timer = timers.get(playerUuid);
        if (timer != null) {
            timer.resume();
        }
    }

    public boolean isPaused(UUID playerUuid) {
        TimerData timer = timers.get(playerUuid);
        return timer != null && timer.paused;
    }

    public void updateActionBar(ServerPlayerEntity player) {
        TimerData timer = timers.get(player.getUuid());
        if (timer == null) return;

        long totalSeconds = timer.getElapsedSeconds();
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        String timeStr;
        if (hours > 0) {
            timeStr = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeStr = String.format("%02d:%02d", minutes, seconds);
        }

        String pauseIndicator = timer.paused ? " §c[PAUSED]" : "";
        player.sendMessage(Text.literal("§6" + timeStr + pauseIndicator), true);
    }
}