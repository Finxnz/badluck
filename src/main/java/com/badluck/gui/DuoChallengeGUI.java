package com.badluck.gui;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DuoChallengeGUI {

    public static void open(ServerPlayerEntity player) {
        SimpleInventory inventory = new SimpleInventory(27);

        Item heartOfTheSea = Registries.ITEM.get(Identifier.ofVanilla("heart_of_the_sea"));
        Item chain = Registries.ITEM.get(Identifier.ofVanilla("chain"));
        Item chest = Registries.ITEM.get(Identifier.ofVanilla("chest"));
        Item enderEye = Registries.ITEM.get(Identifier.ofVanilla("ender_eye"));
        Item arrow = Registries.ITEM.get(Identifier.ofVanilla("arrow"));
        Item glassPane = Registries.ITEM.get(Identifier.ofVanilla("gray_stained_glass_pane"));

        ItemStack sharedLivesItem = new ItemStack(heartOfTheSea);
        sharedLivesItem.set(DataComponentTypes.ITEM_NAME, Text.literal("§d§lShared HP"));
        inventory.setStack(10, sharedLivesItem);

        ItemStack stayCloseItem = new ItemStack(chain);
        stayCloseItem.set(DataComponentTypes.ITEM_NAME, Text.literal("§e§lChained (Stay Close)"));
        inventory.setStack(11, stayCloseItem);

        ItemStack sharedInvItem = new ItemStack(chest);
        sharedInvItem.set(DataComponentTypes.ITEM_NAME, Text.literal("§b§lShare Inventory"));
        inventory.setStack(12, sharedInvItem);

        ItemStack blindEyesItem = new ItemStack(enderEye);
        blindEyesItem.set(DataComponentTypes.ITEM_NAME, Text.literal("§8§lBlind & Eyes"));
        inventory.setStack(14, blindEyesItem);

        ItemStack backItem = new ItemStack(arrow);
        backItem.set(DataComponentTypes.ITEM_NAME, Text.literal("§7Back"));
        inventory.setStack(22, backItem);

        ItemStack filler = new ItemStack(glassPane);
        filler.set(DataComponentTypes.ITEM_NAME, Text.literal(" "));
        for (int i = 0; i < 27; i++) {
            if (inventory.getStack(i).isEmpty()) {
                inventory.setStack(i, filler.copy());
            }
        }

        player.openHandledScreen(new DuoChallengeFactory(inventory, player));
    }

    private static class DuoChallengeFactory implements net.minecraft.screen.NamedScreenHandlerFactory {
        private final SimpleInventory inventory;
        private final ServerPlayerEntity player;

        public DuoChallengeFactory(SimpleInventory inventory, ServerPlayerEntity player) {
            this.inventory = inventory;
            this.player = player;
        }

        @Override
        public Text getDisplayName() {
            return Text.literal("§e§lDuo Challenges");
        }

        @Override
        public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
            return new ChallengeMenuScreenHandler(syncId, playerInventory, inventory, player, ChallengeMenuScreenHandler.MenuType.DUO);
        }
    }
}