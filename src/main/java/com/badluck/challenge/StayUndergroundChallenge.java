package com.badluck.challenge;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class StayUndergroundChallenge extends AbstractChallenge {
    private static final int MAX_Y_LEVEL = 62;
    private static final int WARNING_TICKS = 60;
    private int warningCounter = 0;

    public StayUndergroundChallenge() {
        super("Stay Underground", "Stay below Y-level 63!", ChallengeType.SOLO);
    }

    @Override
    public void tick(MinecraftServer server) {
        if (!active) return;

        for (ServerPlayerEntity player : players) {
            if (player == null || player.isRemoved()) continue;

            if (player.getY() > MAX_Y_LEVEL) {
                warningCounter++;

                if (warningCounter % 20 == 0) {
                    int secondsLeft = (WARNING_TICKS - warningCounter) / 20;
                    player.sendMessage(Text.literal("§c§lWARNING! §eGo back underground! §c" + secondsLeft + "s"), true);
                }

                if (warningCounter >= WARNING_TICKS) {
                    failChallenge("You stayed above ground for too long!");
                    return;
                }
            } else {
                warningCounter = 0;
            }
        }
    }

    @Override
    public void onPlayerDeath(ServerPlayerEntity player) {
        if (active && players.contains(player)) {
            failChallenge("You died!");
        }
    }
}