package dev.royaly.player_teleporter.item;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class PlayerFinderItem extends Item {

    public PlayerFinderItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        if (level.isClientSide) {
            return InteractionResultHolder.pass(player.getItemInHand(interactionHand));
        }

        ItemStack stack = player.getItemInHand(interactionHand);

        List<ServerPlayer> AllPlayer = ((ServerLevel)level).getServer().getPlayerList().getPlayers();

        Container container = new SimpleContainer(45);

        for (Player p : AllPlayer) {
            if (p.getUUID().equals(player.getUUID())) {
                continue;
            }
            Optional<TrinketComponent> API = TrinketsApi.getTrinketComponent(p);
            if (API.get().isEquipped(ModItems.MAGIC_STONE.asItem())) {
                ItemStack head = new ItemStack(Items.PLAYER_HEAD);
                head.getOrCreateTag().putString("SkullOwner", p.getGameProfile().getName());
                head.setHoverName(Component.literal("Ce TP sur " + p.getName().getString()));

                int LevelCost = 0;

                if (!player.level().dimension().equals(p.level().dimension())) {
                    LevelCost = 5;
                } else {
                    double distance = p.position().distanceTo(player.position());
                    LevelCost = (int)distance/200;
                }

                if (!(LevelCost > 0)) {
                    continue;
                }

                CompoundTag display = head.getOrCreateTagElement("display");
                CompoundTag nbt = head.getOrCreateTag();

                nbt.putUUID("player",p.getUUID());

                ListTag lore = new ListTag();
                lore.add(StringTag.valueOf(Component.Serializer.toJson(
                        Component.literal("Le TP vous prendra " + LevelCost + " Level d'XP")
                )));

                display.put("Lore", lore);

                container.setItem(container.countItem(Items.PLAYER_HEAD), head);
            }
        }

        MenuProvider provider = new SimpleMenuProvider(
                (id, playerInv, p) -> new ChestMenu(MenuType.GENERIC_9x5,id, playerInv, container,5) {

                    @Override
                    public ItemStack quickMoveStack(Player player, int index) {
                        return ItemStack.EMPTY;
                    }

                    @Override
                    public void clicked(int i, int j, ClickType clickType, Player player) {
                        if (i >= 0) {

                            if (container.getItem(i).equals(ItemStack.EMPTY)) {
                                return;
                            }

                            ServerPlayer playerToTP = player.getServer().getPlayerList().getPlayer(container.getItem(i).getTag().getUUID("player"));

                            if (playerToTP == null) return;

                            int LevelCost = 0;

                            if (!player.level().dimension().equals(playerToTP.level().dimension())) {
                                LevelCost = 5;
                            } else {
                                double distance = playerToTP.position().distanceTo(player.position());
                                LevelCost = (int)distance/200;
                            }

                            if (LevelCost > player.experienceLevel) {
                                player.playNotifySound(SoundEvents.VILLAGER_NO, SoundSource.PLAYERS, 1,1);
                                return;
                            }

                            if (!level.isClientSide()) {
                                if (player instanceof ServerPlayer serverPlayer) {
                                    serverPlayer.giveExperienceLevels(-LevelCost);

                                    if (level instanceof ServerLevel serverLevel) {
                                        serverLevel.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1, 1);
                                        serverLevel.sendParticles(
                                                ParticleTypes.PORTAL,
                                                player.getX(), player.getY(), player.getZ(),
                                                150,
                                                0.2, 0.8, 0.2,
                                                0.2
                                        );
                                    }

                                    if (serverPlayer.level().dimension().equals(playerToTP.level().dimension())) {
                                        serverPlayer.teleportTo(playerToTP.position().x,playerToTP.position().y,playerToTP.position().z);
                                        stack.hurtAndBreak(1, player, player1 -> {
                                            p.broadcastBreakEvent(interactionHand);
                                        });
                                    } else {
                                        serverPlayer.teleportTo((ServerLevel) playerToTP.level(), playerToTP.position().x, playerToTP.position().y, playerToTP.position().z, 0, 0);
                                        stack.hurtAndBreak(1, player, player1 -> {
                                            p.broadcastBreakEvent(interactionHand);
                                        });
                                    }

                                    if (level instanceof ServerLevel serverLevel) {
                                        serverLevel.playSound(null, playerToTP.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1, 1);
                                        serverLevel.sendParticles(
                                                ParticleTypes.PORTAL,
                                                playerToTP.getX(), playerToTP.getY(), playerToTP.getZ(),
                                                150,
                                                0.2, 0.8, 0.2,
                                                0.2
                                        );
                                    }
                                }
                            }
                        }
                    }
                },
                Component.literal("Player Finder")
        );

        player.openMenu(provider);

        return InteractionResultHolder.success(stack);
    }
}
