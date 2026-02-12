package com.badluck.challenge;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class StayCloseChallenge extends AbstractChallenge {
    private static final double MAX_DISTANCE = 30.0;
    private static final int WARNING_TICKS = 100;
    private int warningCounter = 0;

    public StayCloseChallenge() {
        super("Stay Close", "Stay within 30 blocks of each other!", ChallengeType.DUO);
    }

    @Override
    public void tick(MinecraftServer server) {
        if (!active || players.size() < 2) return;

        ServerPlayerEntity player1 = players.get(0);
        ServerPlayerEntity player2 = players.get(1);

        if (player1 == null || player2 == null || player1.isRemoved() || player2.isRemoved()) return;

        double dx = player1.getX() - player2.getX();
        double dy = player1.getY() - player2.getY();
        double dz = player1.getZ() - player2.getZ();
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (distance > 10000) {
            failChallenge("You are too far apart (different dimensions?)");
            return;
        }

        if (distance > MAX_DISTANCE) {
            warningCounter++;

            if (warningCounter % 20 == 0) {
                int secondsLeft = (WARNING_TICKS - warningCounter) / 20;
                String message = String.format("§c§lTOO FAR! §eDistance: §c%.1f§e/§a30 §c%ds", distance, secondsLeft);

                player1.sendMessage(Text.literal(message), true);
                player2.sendMessage(Text.literal(message), true);
            }

            if (warningCounter >= WARNING_TICKS) {
                failChallenge("You were too far apart for too long!");
                return;
            }
        } else {
            if (warningCounter > 0) {
                player1.sendMessage(Text.literal("§aClose enough again!"), true);
                player2.sendMessage(Text.literal("§aClose enough again!"), true);
            }
            warningCounter = 0;
        }
    }

    @Override
    public void onPlayerDeath(ServerPlayerEntity player) {
        if (active && players.contains(player)) {
            failChallenge("One player died!");
        }
    }
}