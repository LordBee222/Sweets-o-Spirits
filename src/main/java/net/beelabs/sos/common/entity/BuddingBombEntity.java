package net.beelabs.sos.common.entity;

import net.beelabs.sos.common.init.SoSEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class BuddingBombEntity extends ThrownItemEntity {
    public EntityType<? extends Entity> entityToSpawn;

    public BuddingBombEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public BuddingBombEntity(LivingEntity owner, World world) {
        super(SoSEntities.BUDDING_BOMB, owner, world);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.explode();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        this.explode();
    }

    private void explode(){
        this.discard();
        if (this.getWorld() instanceof ServerWorld server) {
            //this.getWorld().createExplosion(this.getOwner(), this.getX(), this.getY(), this.getZ(), 3f, World.ExplosionSourceType.MOB);
            server.spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
            for (int i = 0; i < 70; i++) {
                AmethystShardEntity shard = SoSEntities.AMETHYST_SHARD.create(this.getWorld());
                if (shard != null) {
                    shard.setOwner(this.getOwner());
                    shard.setPos(this.getX(), this.getY(), this.getZ());
                    shard.updateTrackedPosition(this.getX(), this.getY() + .5, this.getZ());
                    shard.setVelocity(random.nextGaussian() * 0.5f, random.nextGaussian() * 0.5f, random.nextGaussian() * 0.5f);
                    this.getWorld().spawnEntity(shard);
                }
            }
        }
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.AMETHYST_SHARD;
    }
}
