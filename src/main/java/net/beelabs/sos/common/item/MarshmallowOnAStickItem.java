package net.beelabs.sos.common.item;

import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.init.SoSItems;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class MarshmallowOnAStickItem extends Item {
    private static final HashMap<UUID, Vec3d> lastPositions = new HashMap<>();
    private static final HashMap<UUID, Float> lastYaw = new HashMap<>();
    private static final HashMap<UUID, Float> lastPitch = new HashMap<>();
    public static final String COOKING_KEY = "cooking";

    public MarshmallowOnAStickItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        if (stack.getNbt() == null) return false;
        return stack.getNbt().contains(COOKING_KEY);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getBlockPos();
        ItemStack stack = context.getStack();
        BlockState state = world.getBlockState(pos);
        //float distanceTo = MathHelper.sqrt((float) (pos.getX() - pos.getX()) * (float) (pos.getX() - pos.getX()) + (float) (pos.getY() - pos.getY()) * (float) (pos.getY() - pos.getY()) + (float) (pos.getZ() - pos.getZ()) * (float) (pos.getZ() - pos.getZ()));

        if (stack.getNbt().contains(COOKING_KEY)) stack.getNbt().remove(COOKING_KEY);

        if (!world.isClient && state.getBlock() == Blocks.CAMPFIRE && !stack.getOrCreateNbt().contains(COOKING_KEY)) {
            stack.getOrCreateNbt().putBoolean(COOKING_KEY, true);
        }
        return super.useOnBlock(context);

    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!world.isClient && !selected && stack.getOrCreateNbt().contains(COOKING_KEY)){
            stack.getOrCreateNbt().remove(COOKING_KEY);
        }
    }

    private static void onPlayerMoveWhileHoldingItem(ServerPlayerEntity player) {
        // Action to perform when the player moves while holding the item
        ItemStack stack = player.getMainHandStack();
        stack.getOrCreateNbt().remove(COOKING_KEY);
        //player.sendMessage(Text.literal("You moved while holding the special item!"), true);
    }


    public static void registerCookingTickEvent() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                UUID playerId = player.getUuid();
                Vec3d currentPosition = player.getPos();
                float currentYaw = player.getYaw();
                float currentPitch = player.getPitch();

                // Retrieve last known values or initialize them
                Vec3d lastPosition = lastPositions.get(playerId);
                Float lastYawValue = lastYaw.get(playerId);
                Float lastPitchValue = lastPitch.get(playerId);

                if (lastPosition == null || lastYawValue == null || lastPitchValue == null) {
                    // Initialize tracking for new players
                    lastPositions.put(playerId, currentPosition);
                    lastYaw.put(playerId, currentYaw);
                    lastPitch.put(playerId, currentPitch);
                    continue;
                }

                boolean moved = !currentPosition.equals(lastPosition);
                boolean lookedAround = currentYaw != lastYawValue || currentPitch != lastPitchValue;

                if ((moved || lookedAround) && player.getMainHandStack().getItem() == SoSItems.MARSHMALLOW_ON_A_STICK) {
                    onPlayerMoveWhileHoldingItem(player);
                }
                    // Update position
                lastPositions.put(playerId, currentPosition);
                lastYaw.put(playerId, currentYaw);
                lastPitch.put(playerId, currentPitch);
            }
        });
    }

}
