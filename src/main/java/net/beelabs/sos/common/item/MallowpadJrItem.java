package net.beelabs.sos.common.item;

import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.entity.MallowpadJrProjectileEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MallowpadJrItem extends Item {
    public MallowpadJrItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            MallowpadJrProjectileEntity projectile = new MallowpadJrProjectileEntity(world, user);
            projectile.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 0.5f, 0f);
            projectile.setOwner(user);
            world.spawnEntity(projectile);
/*
            Vec3d startPos = user.getEyePos();
            Vec3d direction = user.getRotationVec(1.0F);

            for (int i = 1; i < MathHelper.floor(vec3d2.length()) + 7; i++) {
                Vec3d vec3d4 = vec3d.add(vec3d3.multiply((double)i));
                serverWorld.spawnParticles(ParticleTypes.SONIC_BOOM, vec3d4.x, vec3d4.y, vec3d4.z, 1, 0.0, 0.0, 0.0, 0.0);
            }

            wardenEntity.playSound(SoundEvents.ENTITY_WARDEN_SONIC_BOOM, 3.0F, 1.0F);
            target.damage(serverWorld.getDamageSources().sonicBoom(wardenEntity), 10.0F);
            double d = 0.5 * (1.0 - target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
            double e = 2.5 * (1.0 - target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
            target.addVelocity(vec3d3.getX() * e, vec3d3.getY() * d, vec3d3.getZ() * e);

 */
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }
}
