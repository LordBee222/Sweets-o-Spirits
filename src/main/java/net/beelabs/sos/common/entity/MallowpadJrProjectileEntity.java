package net.beelabs.sos.common.entity;

import net.beelabs.sos.client.entity.model.MallowpadJrModel;
import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.init.SoSEntities;
import net.beelabs.sos.common.init.SoSItems;
import net.beelabs.sos.common.init.SoSStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class MallowpadJrProjectileEntity extends PersistentProjectileEntity {

    public MallowpadJrProjectileEntity(EntityType<? extends MallowpadJrProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public MallowpadJrProjectileEntity(World world, LivingEntity owner) {
        this(SoSEntities.MALLOWPAD_JR_PROJECTILE, world);
        this.setOwner(owner);
        this.setPos(owner.getX(), owner.getEyeY(), owner.getZ());
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        World world = this.getWorld();

        this.setRotation(this.getYaw(), this.getPitch() + 20f);
       // this.setPitch(this.getPitch() + 20.0F); // Adjust rotation speed
        this.updateRotation();

        MallowpadJrEntity mallowpad = new MallowpadJrEntity(world);
        mallowpad.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
        mallowpad.calculateDimensions();
        mallowpad.setPos(this.getX(), this.getY(), this.getZ()); // Set position at the projectile's location
        mallowpad.setVelocity(Vec3d.ZERO); // Optionally set velocity if needed



        // Spawn the MallowpadJrEntity
        if (world instanceof ServerWorld server) {
            world.spawnEntity(mallowpad);
            server.spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        }
        // Play sound and discard the projectile
        world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_HONEY_BLOCK_PLACE, SoundCategory.NEUTRAL, 1.0F, 1.5F);
        this.discard();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        World world = this.getWorld();
        //if (world.isClient) return;
        MallowpadJrEntity mallowpad = new MallowpadJrEntity(world);
        mallowpad.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
        mallowpad.calculateDimensions();
        mallowpad.setPos(this.getX(), this.getY(), this.getZ()); // Set position at the projectile's location
        mallowpad.setVelocity(Vec3d.ZERO); // Optionally set velocity if needed

        // Spawn the MallowpadJrEntity
        if (world instanceof ServerWorld server) {
            world.spawnEntity(mallowpad);
            server.spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        }

        // Play sound and discard the projectile
        world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_HONEY_BLOCK_PLACE, SoundCategory.NEUTRAL, 1.0F, 1.5F);
        this.discard();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }

    @Override
    protected ItemStack asItemStack() {
        return SoSItems.MALLOWPAD_JR.getDefaultStack();
    }
}
