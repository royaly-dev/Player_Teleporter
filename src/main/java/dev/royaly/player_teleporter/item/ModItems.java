package dev.royaly.player_teleporter.item;

import dev.royaly.player_teleporter.Player_teleporter;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ModItems {

    public static final Item PLAYER_FINDER = registerItem(
            new PlayerFinderItem(new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC).fireproof().maxDamage(5)),
            "player_finder"
    );

    public static final Item MAGIC_STONE = registerItem(
            new MagicStone(new FabricItemSettings()),
            "magic_stone"
    );

    public static Item registerItem(Item item, String id) {
        ResourceLocation itemID = new ResourceLocation(Player_teleporter.MOD_ID, id);
        Item registredItem = Registry.register(BuiltInRegistries.ITEM, itemID, item);
        return registredItem;
    }

    public static void register() {
        Player_teleporter.LOGGER.info("Registering Items for : " + Player_teleporter.MOD_ID);
    }
}
