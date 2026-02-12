package com.badluck.challenge;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

public class SharedLivesChallenge extends AbstractChallenge {
    private int sharedLives = 6;

    public SharedLivesChallenge() {
        super("Shared Lives", "You share 6 lives!", ChallengeType.DUO);
    }

    @Override
    public void start(List<ServerPlayerEntity> players) {
        super.start(players);
        updateLivesDisplay();
    }

    @Override
    public void onPlayerDeath(ServerPlayerEntity player) {
        if (!active || !players.contains(player)) return;

        sharedLives--;

        if (sharedLives <= 0) {
            failChallenge("All lives lost!");
        } else {

            player.setHealth(20.0f);

            for (ServerPlayerEntity p : players) {
                if (p != null) {
                    p.sendMessage(Text.literal("§6§l[BadLuck] §c" + player.getName().getString() + " died! §eLives remaining: §6" + sharedLives), false);
                }
            }
            updateLivesDisplay();
        }
    }

    private void updateLivesDisplay() {
        for (ServerPlayerEntity player : players) {
            if (player != null) {
                player.sendMessage(Text.literal("§6§l[BadLuck] §eShared Lives: §6" + sharedLives), true);
            }
        }
    }
}