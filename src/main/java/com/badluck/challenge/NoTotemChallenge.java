package com.badluck.challenge;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class NoTotemChallenge extends AbstractChallenge {

    public NoTotemChallenge() {
        super("No Totem Run", "Totems don't work!", ChallengeType.SOLO);
    }

    @Override
    public void tick(MinecraftServer server) {
        if (!active) return;

        for (ServerPlayerEntity player : players) {
            if (player == null || player.isRemoved()) continue;


            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack stack = player.getInventory().getStack(i);
                if (stack.isOf(Items.TOTEM_OF_UNDYING)) {
                    player.getInventory().setStack(i, ItemStack.EMPTY);
                }
            }


            if (player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
                player.setStackInHand(net.minecraft.util.Hand.OFF_HAND, ItemStack.EMPTY);
            }


            if (player.getMainHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
                player.setStackInHand(net.minecraft.util.Hand.MAIN_HAND, ItemStack.EMPTY);
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