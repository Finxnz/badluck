package com.badluck.gui;

import com.badluck.BadLuckMod;
import com.badluck.challenge.*;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class GUIClickHandler {

    public static void register() {
    }

    public static void handleClick(ServerPlayerEntity player, ScreenHandler handler, int slot, SlotActionType actionType) {
        if (actionType != SlotActionType.PICKUP) return;
        if (!(handler instanceof ChallengeMenuScreenHandler)) return;

        ChallengeMenuScreenHandler menuHandler = (ChallengeMenuScreenHandler) handler;

        if (slot == 11 || slot == 15) {
            player.closeHandledScreen();
            if (slot == 11) {
                SoloChallengeGUI.open(player);
            } else if (slot == 15) {
                DuoChallengeGUI.open(player);
            }
        } else if (slot == 10 || slot == 12 || slot == 14) {
            player.closeHandledScreen();

            if (slot == 10) {
                BadLuckMod.getChallengeManager().startSoloChallenge(player, OneLifeHardcoreChallenge::new);
            } else if (slot == 12) {
                BadLuckMod.getChallengeManager().startSoloChallenge(player, StayUndergroundChallenge::new);
            } else if (slot == 14) {
                BadLuckMod.getChallengeManager().startSoloChallenge(player, NoTotemChallenge::new);
            }
        } else if (slot == 22) {
            player.closeHandledScreen();
            ChallengeMenuGUI.open(player);
        }
    }
}