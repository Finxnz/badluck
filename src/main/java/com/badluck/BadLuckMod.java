package com.badluck;

import com.badluck.challenge.ChallengeManager;
import com.badluck.command.BadLuckCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BadLuckMod implements ModInitializer {
    public static final String MOD_ID = "badluck";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    private static ChallengeManager challengeManager;

    @Override
    public void onInitialize() {
        LOGGER.info("BadLuck Challenges Mod wird geladen!");
        
        challengeManager = new ChallengeManager();
        
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            BadLuckCommand.register(dispatcher);
        });
        
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            challengeManager.tick(server);
        });
        
        LOGGER.info("BadLuck Challenges Mod erfolgreich geladen!");
    }
    
    public static ChallengeManager getChallengeManager() {
        return challengeManager;
    }
}
