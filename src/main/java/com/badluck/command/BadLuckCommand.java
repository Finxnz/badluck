package com.badluck.command;

import com.badluck.BadLuckMod;
import com.badluck.challenge.*;
import com.badluck.gui.ChallengeMenuGUI;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class BadLuckCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("badluck")
                .executes(BadLuckCommand::openMenu)
                .then(CommandManager.literal("accept")
                        .executes(BadLuckCommand::acceptInvitation))
                .then(CommandManager.literal("stop")
                        .executes(BadLuckCommand::stopChallenge))
                .then(CommandManager.literal("timer")
                        .then(CommandManager.literal("pause")
                                .executes(BadLuckCommand::pauseTimer))
                        .then(CommandManager.literal("resume")
                                .executes(BadLuckCommand::resumeTimer)))
                .then(CommandManager.literal("invite")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .then(CommandManager.argument("challenge", StringArgumentType.word())
                                        .suggests((context, builder) -> {
                                            builder.suggest("sharedlives");
                                            builder.suggest("stayclose");
                                            builder.suggest("sharedinventory");
                                            builder.suggest("blindeyes");
                                            return builder.buildFuture();
                                        })
                                        .executes(BadLuckCommand::invitePlayer))))
        );
    }

    private static int openMenu(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
            source.sendError(Text.literal("Only players can use this command!"));
            return 0;
        }

        ChallengeMenuGUI.open(player);
        return 1;
    }

    private static int acceptInvitation(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
            source.sendError(Text.literal("Only players can use this command!"));
            return 0;
        }

        BadLuckMod.getChallengeManager().acceptInvitation(player, source.getServer());
        return 1;
    }

    private static int stopChallenge(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
            source.sendError(Text.literal("Only players can use this command!"));
            return 0;
        }

        BadLuckMod.getChallengeManager().stopChallenge(player);
        return 1;
    }

    private static int pauseTimer(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
            source.sendError(Text.literal("Only players can use this command!"));
            return 0;
        }

        BadLuckMod.getChallengeManager().pauseTimer(player);
        return 1;
    }

    private static int resumeTimer(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
            source.sendError(Text.literal("Only players can use this command!"));
            return 0;
        }

        BadLuckMod.getChallengeManager().resumeTimer(player);
        return 1;
    }

    private static int invitePlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();

        if (!(source.getEntity() instanceof ServerPlayerEntity inviter)) {
            source.sendError(Text.literal("Only players can use this command!"));
            return 0;
        }

        ServerPlayerEntity invited = EntityArgumentType.getPlayer(context, "player");
        String challengeType = StringArgumentType.getString(context, "challenge");

        ChallengeManager.ChallengeFactory factory = switch (challengeType.toLowerCase()) {
            case "sharedlives" -> SharedLivesChallenge::new;
            case "stayclose" -> StayCloseChallenge::new;
            case "sharedinventory" -> SharedInventoryChallenge::new;
            case "blindeyes" -> BlindAndEyesChallenge::new;
            default -> null;
        };

        if (factory == null) {
            inviter.sendMessage(Text.literal("§6§l[BadLuck] §cUnknown challenge!"), false);
            return 0;
        }

        BadLuckMod.getChallengeManager().inviteToDuoChallenge(inviter, invited, factory);
        return 1;
    }
}