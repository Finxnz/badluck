package com.badluck.challenge;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractChallenge implements Challenge {
    protected List<ServerPlayerEntity> players = new ArrayList<>();
    protected boolean active = false;
    protected final String name;
    protected final String description;
    protected final ChallengeType type;

    public AbstractChallenge(String name, String description, ChallengeType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ChallengeType getType() {
        return type;
    }

    @Override
    public void start(List<ServerPlayerEntity> players) {
        this.players = new ArrayList<>(players);
        this.active = true;

        for (ServerPlayerEntity player : players) {
            player.sendMessage(Text.literal("§6§l[BadLuck] §eChallenge started: §f" + name), false);
        }
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void stop() {
        this.active = false;
    }

    @Override
    public List<ServerPlayerEntity> getPlayers() {
        return players;
    }

    @Override
    public void tick(MinecraftServer server) {

    }

    protected void failChallenge(String reason) {
        for (ServerPlayerEntity player : players) {
            if (player != null) {
                player.sendMessage(Text.literal("§4§lCHALLENGE FAILED! §c" + reason), false);
                player.setHealth(0.0f);
            }
        }

        this.active = false;
    }
}