package net.beelabs.sos.common.item;

import net.beelabs.sos.common.entity.AmethystShardEntity;
import net.beelabs.sos.common.entity.BuddingBombEntity;
import net.beelabs.sos.common.entity.MallowpadJrProjectileEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BuddingBombItem extends Item {
    public BuddingBombItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            BuddingBombEntity entity = new BuddingBombEntity(user, user.getWorld());
            entity.setItem(this.getDefaultStack());
            entity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
            entity.setPos(user.getX(), user.getY() + (double) user.getStandingEyeHeight() - 0.10000000149011612D, user.getZ());
            entity.setOwner(user);
            world.spawnEntity(entity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) itemStack.decrement(1);
        return TypedActionResult.success(itemStack, world.isClient());
    }
}
