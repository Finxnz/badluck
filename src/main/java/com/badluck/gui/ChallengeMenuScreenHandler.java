package com.badluck.gui;

import com.badluck.BadLuckMod;
import com.badluck.challenge.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ChallengeMenuScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final ServerPlayerEntity player;
    private MenuType menuType;

    public enum MenuType {
        MAIN, SOLO, DUO
    }

    public ChallengeMenuScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, ServerPlayerEntity player, MenuType menuType) {
        super(ScreenHandlerType.GENERIC_9X3, syncId);
        this.inventory = inventory;
        this.player = player;
        this.menuType = menuType;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new LockedSlot(inventory, col + row * 9, 8 + col * 18, 18 + row * 18));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        if (slotIndex < 27 && actionType == SlotActionType.PICKUP && button == 0) {
            handleMenuClick(slotIndex);
            return;
        }

        if (slotIndex >= 27) {
            super.onSlotClick(slotIndex, button, actionType, player);
        }
    }

    private void handleMenuClick(int slot) {
        switch (menuType) {
            case MAIN:
                handleMainMenuClick(slot, player);
                break;
            case SOLO:
                handleSoloMenuClick(slot, player);
                break;
            case DUO:
                handleDuoMenuClick(slot, player);
                break;
        }
    }

    private void handleMainMenuClick(int slot, ServerPlayerEntity player) {
        if (slot == 11) {
            player.closeHandledScreen();
            SoloChallengeGUI.open(player);
        } else if (slot == 15) {
            player.closeHandledScreen();
            DuoChallengeGUI.open(player);
        }
    }

    private void handleSoloMenuClick(int slot, ServerPlayerEntity player) {
        player.closeHandledScreen();

        ChallengeManager.ChallengeFactory factory = null;

        switch (slot) {
            case 10:
                factory = OneLifeHardcoreChallenge::new;
                break;
            case 12:
                factory = StayUndergroundChallenge::new;
                break;
            case 14:
                factory = NoTotemChallenge::new;
                break;
            case 22:
                ChallengeMenuGUI.open(player);
                return;
        }

        if (factory != null) {
            BadLuckMod.getChallengeManager().startSoloChallenge(player, factory);
        }
    }

    private void handleDuoMenuClick(int slot, ServerPlayerEntity player) {
        player.closeHandledScreen();

        if (slot == 22) {
            ChallengeMenuGUI.open(player);
            return;
        }

        String challengeType = switch (slot) {
            case 10 -> "sharedlives";
            case 11 -> "stayclose";
            case 12 -> "sharedinventory";
            case 14 -> "blindeyes";
            default -> null;
        };

        if (challengeType != null) {
            player.sendMessage(Text.literal("§6§l[BadLuck] §eUse: §f/badluck invite <player> " + challengeType), false);
        }
    }

    private static class LockedSlot extends Slot {
        public LockedSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return false;
        }

        @Override
        public boolean canTakeItems(PlayerEntity playerEntity) {
            return false;
        }

        @Override
        public ItemStack takeStack(int amount) {
            return ItemStack.EMPTY;
        }
    }
}