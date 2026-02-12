package com.badluck.challenge;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class SharedInventoryChallenge extends AbstractChallenge {

    public SharedInventoryChallenge() {
        super("Shared Inventory", "You share one inventory!", ChallengeType.DUO);
    }

    @Override
    public void tick(MinecraftServer server) {
        if (!active || players.size() < 2) return;

        ServerPlayerEntity player1 = players.get(0);
        ServerPlayerEntity player2 = players.get(1);

        if (player1 == null || player2 == null || player1.isRemoved() || player2.isRemoved()) return;


        syncInventories(player1, player2);
    }

    private void syncInventories(ServerPlayerEntity master, ServerPlayerEntity slave) {
        Inventory masterInv = master.getInventory();
        Inventory slaveInv = slave.getInventory();

        for (int i = 0; i < masterInv.size(); i++) {
            ItemStack masterStack = masterInv.getStack(i);
            ItemStack slaveStack = slaveInv.getStack(i);


            if (!ItemStack.areEqual(masterStack, slaveStack)) {
                slaveInv.setStack(i, masterStack.copy());
            }
        }
    }

    @Override
    public void onPlayerDeath(ServerPlayerEntity player) {
        if (active && players.contains(player)) {
            failChallenge("One player died!");
        }
    }
}