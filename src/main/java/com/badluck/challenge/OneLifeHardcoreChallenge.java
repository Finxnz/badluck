package com.badluck.challenge;

import net.minecraft.server.network.ServerPlayerEntity;

public class OneLifeHardcoreChallenge extends AbstractChallenge {

    public OneLifeHardcoreChallenge() {
        super("One Life Hardcore", "You only have one life!", ChallengeType.SOLO);
    }

    @Override
    public void onPlayerDeath(ServerPlayerEntity player) {
        if (active && players.contains(player)) {
            failChallenge("You died!");
        }
    }
}