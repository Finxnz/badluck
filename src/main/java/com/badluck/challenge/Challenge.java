package com.badluck.challenge;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public interface Challenge {
    String getName();
    String getDescription();
    ChallengeType getType();
    
    void start(List<ServerPlayerEntity> players);
    void tick(MinecraftServer server);
    void onPlayerDeath(ServerPlayerEntity player);
    boolean isActive();
    void stop();
    
    List<ServerPlayerEntity> getPlayers();
}
