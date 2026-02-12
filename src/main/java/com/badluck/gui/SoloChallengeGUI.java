package com.badluck.gui;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SoloChallengeGUI {

    public static void open(ServerPlayerEntity player) {
        SimpleInventory inventory = new SimpleInventory(27);


        ItemStack oneLifeItem = new ItemStack(Items.TOTEM_OF_UNDYING);
        oneLifeItem.set(DataComponentTypes.ITEM_NAME, Text.literal("§c§lOne Life Hardcore"));
        inventory.setStack(10, oneLifeItem);


        ItemStack undergroundItem = new ItemStack(Items.DIRT);
        undergroundItem.set(DataComponentTypes.ITEM_NAME, Text.literal("§6§lStay Underground"));
        inventory.setStack(12, undergroundItem);

        ItemStack noTotemItem = new ItemStack(Items.BARRIER);
        noTotemItem.set(DataComponentTypes.ITEM_NAME, Text.literal("§4§lNo Totem Run"));
        inventory.setStack(14, noTotemItem);

        ItemStack backItem = new ItemStack(Items.ARROW);
        backItem.set(DataComponentTypes.ITEM_NAME, Text.literal("§7Zurück"));
        inventory.setStack(22, backItem);

        ItemStack filler = new ItemStack(Items.GRAY_STAINED_GLASS_PANE);
        filler.set(DataComponentTypes.ITEM_NAME, Text.literal(" "));
        for (int i = 0; i < 27; i++) {
            if (inventory.getStack(i).isEmpty()) {
                inventory.setStack(i, filler.copy());
            }
        }

        player.openHandledScreen(new SoloChallengeFactory(inventory, player));
    }

    private static class SoloChallengeFactory implements net.minecraft.screen.NamedScreenHandlerFactory {
        private final SimpleInventory inventory;
        private final ServerPlayerEntity player;

        public SoloChallengeFactory(SimpleInventory inventory, ServerPlayerEntity player) {
            this.inventory = inventory;
            this.player = player;
        }

        @Override
        public Text getDisplayName() {
            return Text.literal("§a§lSolo Challenges");
        }

        @Override
        public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
            return new ChallengeMenuScreenHandler(syncId, playerInventory, inventory, player, ChallengeMenuScreenHandler.MenuType.SOLO);
        }
    }
}