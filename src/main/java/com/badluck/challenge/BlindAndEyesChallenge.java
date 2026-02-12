package com.badluck.challenge;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

public class BlindAndEyesChallenge extends AbstractChallenge {
    private ServerPlayerEntity blindPlayer;
    private ServerPlayerEntity seeingPlayer;

    public BlindAndEyesChallenge() {
        super("Blind & Eyes", "One sees, one is blind!", ChallengeType.DUO);
    }

    @Override
    public void start(List<ServerPlayerEntity> players) {
        super.start(players);

        if (players.size() >= 2) {
            blindPlayer = players.get(0);
            seeingPlayer = players.get(1);

            blindPlayer.sendMessage(Text.literal("§6§l[BadLuck] §cYou are BLIND! Press F1 and follow instructions!"), false);
            seeingPlayer.sendMessage(Text.literal("§6§l[BadLuck] §aYou can see! Give " + blindPlayer.getName().getString() + " instructions!"), false);


            applyBlindness();
        }
    }

    @Override
    public void tick(MinecraftServer server) {
        if (!active) return;

        if (blindPlayer != null && !blindPlayer.isRemoved()) {
            applyBlindness();
        }
    }

    private void applyBlindness() {
        if (blindPlayer != null) {
            blindPlayer.sendMessage(Text.literal("§8[Blind] Follow your partner's instructions!"), true);
        }
    }

    @Override
    public void onPlayerDeath(ServerPlayerEntity player) {
        if (active && players.contains(player)) {
            failChallenge("One player died!");
        }
    }
}