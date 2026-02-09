package dev.royaly.player_teleporter;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Player_teleporter implements ModInitializer {

    public static final String MOD_ID = "PlayerTeleporter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Player Teleporter Starting...");
    }
}
