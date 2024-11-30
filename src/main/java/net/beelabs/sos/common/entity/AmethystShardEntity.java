package net.beelabs.sos.common.entity;

import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.init.SoSEntities;
import net.beelabs.sos.common.util.IEntityImpaledShardGetter;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.Optional;

public class AmethystShardEntity extends PersistentProjectileEntity {
    public int ticksUntilRemoval = -1;

    public AmethystShardEntity(EntityType<? extends AmethystShardEntity> entityType, World world) {
        super(entityType, world);
        this.setSound(this.getHitSound());
        this.setDamage(4);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    public AmethystShardEntity(LivingEntity owner, World world) {
        super(SoSEntities.AMETHYST_SHARD, owner, world);
    }

    public AmethystShardEntity(World world, double x, double y, double z) {
        super(SoSEntities.AMETHYST_SHARD, x, y, z, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(Items.AMETHYST_SHARD);
    }


    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK;
    }

    public Item getBreakItemParticle() {
        return Items.AMETHYST_BLOCK;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.inGround) {
            if (this.ticksUntilRemoval == -1) {
                for (int i = 0; i < 8; ++i) this.getWorld().addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(getBreakItemParticle(), 1)), this.getX() + random.nextGaussian() / 20f, this.getY() + random.nextGaussian() / 20f, this.getZ() + random.nextGaussian() / 20f, random.nextGaussian() / 20f, 0.2D + random.nextGaussian() / 20f, random.nextGaussian() / 20f);
                this.ticksUntilRemoval = 2;
            }
            if (this.ticksUntilRemoval > 0) {
                this.ticksUntilRemoval--;
                if (this.ticksUntilRemoval <= 0) this.remove(RemovalReason.DISCARDED);
            }
        }

        if (this.age < 10) {
            for (LivingEntity livingEntity : this.getWorld().getEntitiesByClass(LivingEntity.class, this.getBoundingBox().expand(1f), LivingEntity::isAlive)) {
                this.onEntityHit(new EntityHitResult(livingEntity));
                this.kill();
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity target = entityHitResult.getEntity();
        Entity owner = this.getOwner();
        DamageSource damageSource;
        if (owner == null) {
            damageSource = this.getWorld().getDamageSources().arrow(this, this);
        } else {
            damageSource = owner.getWorld().getDamageSources().arrow(this, owner);
            if (owner instanceof LivingEntity) ((LivingEntity) owner).onAttacking(target);
        }

        if (target.damage(damageSource, (float) this.getDamage())) {
            if (target instanceof LivingEntity livingTarget) {
                if (!this.getWorld().isClient && owner instanceof LivingEntity) {
                    livingTarget.hurtTime = 0; // Resets hurt animation
                    livingTarget.timeUntilRegen = 0;

                    EnchantmentHelper.onUserDamaged(livingTarget, owner);
                    EnchantmentHelper.onTargetDamaged((LivingEntity) owner, livingTarget);
                    Optional<Integer> impaledShardsCount = Optional.of((((IEntityImpaledShardGetter) livingTarget).getImpaledShardCount()));
                    ((IEntityImpaledShardGetter) livingTarget).setImpaledShardCount(impaledShardsCount.orElse(0) + 1);
                }
                this.onHit(livingTarget);
                if (owner != null && livingTarget != owner && livingTarget instanceof PlayerEntity && owner instanceof ServerPlayerEntity && !this.isSilent())
                    ((ServerPlayerEntity) owner).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, GameStateChangeS2CPacket.DEMO_OPEN_SCREEN));
            }
        } else {
          this.discard();
        }
        this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, SoundCategory.NEUTRAL, 1.0f, 1.5f);
    }
}
