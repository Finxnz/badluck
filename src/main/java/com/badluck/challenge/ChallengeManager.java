package com.badluck.challenge;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.*;

public class ChallengeManager {
    private final Map<UUID, Challenge> activeChallenges = new HashMap<>();
    private final Map<UUID, UUID> invitations = new HashMap<>();
    private final Map<UUID, Challenge> pendingDuoChallenges = new HashMap<>();
    private final ChallengeTimer timer = new ChallengeTimer();

    public void tick(MinecraftServer server) {

        for (Challenge challenge : new ArrayList<>(activeChallenges.values())) {
            if (challenge.isActive()) {
                challenge.tick(server);
            }
        }


        for (UUID uuid : new ArrayList<>(activeChallenges.keySet())) {
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(uuid);
            if (player != null) {
                timer.updateActionBar(player);
            }
        }
    }

    public void onPlayerDeath(ServerPlayerEntity player) {
        Challenge challenge = activeChallenges.get(player.getUuid());
        if (challenge != null && challenge.isActive()) {
            challenge.onPlayerDeath(player);

            if (!challenge.isActive()) {
                for (ServerPlayerEntity p : challenge.getPlayers()) {
                    activeChallenges.remove(p.getUuid());
                    timer.stopTimer(p.getUuid());
                }
            }
        }
    }

    public void startSoloChallenge(ServerPlayerEntity player, ChallengeFactory factory) {
        if (activeChallenges.containsKey(player.getUuid())) {
            player.sendMessage(Text.literal("§6§l[BadLuck] §cYou already have an active challenge!"), false);
            return;
        }

        Challenge challenge = factory.create();
        challenge.start(Collections.singletonList(player));
        activeChallenges.put(player.getUuid(), challenge);
        timer.startTimer(player.getUuid());
    }

    public void inviteToDuoChallenge(ServerPlayerEntity inviter, ServerPlayerEntity invited, ChallengeFactory factory) {
        if (activeChallenges.containsKey(inviter.getUuid())) {
            inviter.sendMessage(Text.literal("§6§l[BadLuck] §cYou already have an active challenge!"), false);
            return;
        }

        if (activeChallenges.containsKey(invited.getUuid())) {
            inviter.sendMessage(Text.literal("§6§l[BadLuck] §c" + invited.getName().getString() + " already has an active challenge!"), false);
            return;
        }

        Challenge challenge = factory.create();
        pendingDuoChallenges.put(inviter.getUuid(), challenge);
        invitations.put(invited.getUuid(), inviter.getUuid());

        inviter.sendMessage(Text.literal("§6§l[BadLuck] §eInvitation sent to " + invited.getName().getString() + "!"), false);
        invited.sendMessage(Text.literal("§6§l[BadLuck] §e" + inviter.getName().getString() + " invited you to a challenge!"), false);
        invited.sendMessage(Text.literal("§6§l[BadLuck] §aUse §f/badluck accept §ato accept!"), false);
    }

    public void acceptInvitation(ServerPlayerEntity player, MinecraftServer server) {
        UUID inviterUuid = invitations.get(player.getUuid());

        if (inviterUuid == null) {
            player.sendMessage(Text.literal("§6§l[BadLuck] §cYou have no pending invitation!"), false);
            return;
        }

        ServerPlayerEntity inviter = server.getPlayerManager().getPlayer(inviterUuid);

        if (inviter == null) {
            player.sendMessage(Text.literal("§6§l[BadLuck] §cThe inviter is no longer online!"), false);
            invitations.remove(player.getUuid());
            pendingDuoChallenges.remove(inviterUuid);
            return;
        }

        Challenge challenge = pendingDuoChallenges.get(inviterUuid);

        if (challenge == null) {
            player.sendMessage(Text.literal("§6§l[BadLuck] §cThe challenge is no longer available!"), false);
            invitations.remove(player.getUuid());
            return;
        }

        challenge.start(Arrays.asList(inviter, player));
        activeChallenges.put(inviter.getUuid(), challenge);
        activeChallenges.put(player.getUuid(), challenge);


        timer.startTimer(inviter.getUuid());
        timer.startTimer(player.getUuid());

        invitations.remove(player.getUuid());
        pendingDuoChallenges.remove(inviterUuid);
    }

    public void stopChallenge(ServerPlayerEntity player) {
        Challenge challenge = activeChallenges.get(player.getUuid());

        if (challenge == null) {
            player.sendMessage(Text.literal("§6§l[BadLuck] §cYou have no active challenge!"), false);
            return;
        }

        challenge.stop();

        for (ServerPlayerEntity p : challenge.getPlayers()) {
            activeChallenges.remove(p.getUuid());
            timer.stopTimer(p.getUuid());
            if (p != null) {
                p.sendMessage(Text.literal("§6§l[BadLuck] §eChallenge stopped."), false);
            }
        }
    }

    public void pauseTimer(ServerPlayerEntity player) {
        if (!activeChallenges.containsKey(player.getUuid())) {
            player.sendMessage(Text.literal("§6§l[BadLuck] §cYou have no active challenge!"), false);
            return;
        }

        timer.pauseTimer(player.getUuid());
        player.sendMessage(Text.literal("§6§l[BadLuck] §eTimer paused."), false);
    }

    public void resumeTimer(ServerPlayerEntity player) {
        if (!activeChallenges.containsKey(player.getUuid())) {
            player.sendMessage(Text.literal("§6§l[BadLuck] §cYou have no active challenge!"), false);
            return;
        }

        timer.resumeTimer(player.getUuid());
        player.sendMessage(Text.literal("§6§l[BadLuck] §eTimer resumed."), false);
    }

    public boolean hasActiveChallenge(ServerPlayerEntity player) {
        return activeChallenges.containsKey(player.getUuid());
    }

    public Challenge getActiveChallenge(ServerPlayerEntity player) {
        return activeChallenges.get(player.getUuid());
    }


    public interface ChallengeFactory {
        Challenge create();
    }
}