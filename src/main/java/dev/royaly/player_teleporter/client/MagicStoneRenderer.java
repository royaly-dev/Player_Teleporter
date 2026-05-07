package dev.royaly.player_teleporter.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class MagicStoneRenderer implements TrinketRenderer {

    @Override
    public void render(
            ItemStack stack,
            SlotReference slotReference,
            EntityModel<? extends LivingEntity> contextModel,
            PoseStack matrices,
            MultiBufferSource vertexConsumers,
            int light,
            LivingEntity entity,
            float limbAngle,
            float limbDistance,
            float tickDelta,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {

        matrices.pushPose();

        if (contextModel instanceof net.minecraft.client.model.PlayerModel<?> playerModel) {
            playerModel.body.translateAndRotate(matrices);
        }

        matrices.translate(-0.01, 0.08, -0.15);
        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.mulPose(Axis.XP.rotationDegrees(10f));

        Minecraft.getInstance().getItemRenderer().renderStatic(
                stack,
                ItemDisplayContext.GROUND, // ou FIXED / HEAD selon rendu voulu
                light,
                OverlayTexture.NO_OVERLAY,
                matrices,
                vertexConsumers,
                entity.level(),
                0
        );

        matrices.popPose();
    }
}