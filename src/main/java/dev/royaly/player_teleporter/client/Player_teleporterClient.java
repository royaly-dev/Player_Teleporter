package dev.royaly.player_teleporter.client;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import dev.royaly.player_teleporter.item.ModItems;
import net.fabricmc.api.ClientModInitializer;

public class Player_teleporterClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        TrinketRendererRegistry.registerRenderer(ModItems.MAGIC_STONE, new MagicStoneRenderer());
    }
}
