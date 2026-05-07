package dev.royaly.player_teleporter.itemGroup;

import dev.royaly.player_teleporter.Player_teleporter;
import dev.royaly.player_teleporter.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroup {

    public static final ResourceKey<CreativeModeTab> CUSTOM_CREATIVE_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            new ResourceLocation(Player_teleporter.MOD_ID, "item_group")
    );

    public static final CreativeModeTab CUSTOM_TAB = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.MAGIC_STONE))
            .title(Component.literal("Player Téléporter"))
            .build();

    public static void registerItemGroup() {
        Player_teleporter.LOGGER.info("Registering Item Group for : " + Player_teleporter.MOD_ID);

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_CREATIVE_TAB_KEY, CUSTOM_TAB);
        ItemGroupEvents.modifyEntriesEvent(CUSTOM_CREATIVE_TAB_KEY).register(itemGroup -> {
            itemGroup.accept(ModItems.MAGIC_STONE);
            itemGroup.accept(ModItems.PLAYER_FINDER);
        });
    }

}
