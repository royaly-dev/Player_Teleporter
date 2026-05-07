package dev.royaly.player_teleporter.item;

import dev.emi.trinkets.api.SlotAttributes;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import com.google.common.collect.Multimap;

import java.util.UUID;

public class MagicStone extends TrinketItem {

    public MagicStone (Properties properties) {
        super(properties);
    }

    public Multimap<Attribute, AttributeModifier> getModifiers(
            ItemStack stack,
            SlotReference slot,
            LivingEntity entity,
            UUID uuid
    ) {
        var modifiers = super.getModifiers(stack, slot, entity, uuid);

        SlotAttributes.addSlotModifier(
                modifiers,
                "chest/necklace",
                uuid,
                1.0,
                AttributeModifier.Operation.ADDITION
        );

        return modifiers;
    }

}
