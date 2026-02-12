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

public class ChallengeMenuGUI {

    public static void open(ServerPlayerEntity player) {
        SimpleInventory inventory = new SimpleInventory(27);


        ItemStack soloItem = new ItemStack(Items.PLAYER_HEAD);
        soloItem.set(DataComponentTypes.ITEM_NAME, Text.literal("§a§l1 Player"));
        inventory.setStack(11, soloItem);


        ItemStack duoItem = new ItemStack(Items.SKELETON_SKULL);
        duoItem.set(DataComponentTypes.ITEM_NAME, Text.literal("§e§l2 Players"));
        inventory.setStack(15, duoItem);


        ItemStack filler = new ItemStack(Items.BLACK_STAINED_GLASS_PANE);
        filler.set(DataComponentTypes.ITEM_NAME, Text.literal(" "));
        for (int i = 0; i < 27; i++) {
            if (inventory.getStack(i).isEmpty()) {
                inventory.setStack(i, filler.copy());
            }
        }

        player.openHandledScreen(new ChallengeMenuFactory(inventory, player, ChallengeMenuScreenHandler.MenuType.MAIN));
    }

    private static class ChallengeMenuFactory implements net.minecraft.screen.NamedScreenHandlerFactory {
        private final SimpleInventory inventory;
        private final ServerPlayerEntity player;
        private final ChallengeMenuScreenHandler.MenuType menuType;

        public ChallengeMenuFactory(SimpleInventory inventory, ServerPlayerEntity player, ChallengeMenuScreenHandler.MenuType menuType) {
            this.inventory = inventory;
            this.player = player;
            this.menuType = menuType;
        }

        @Override
        public Text getDisplayName() {
            return Text.literal("§6§lBadLuck Challenges");
        }

        @Override
        public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
            return new ChallengeMenuScreenHandler(syncId, playerInventory, inventory, player, menuType);
        }
    }
}