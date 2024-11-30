package net.beelabs.sos.mixin;

import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.init.SoSItems;
import net.minecraft.entity.ItemEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void initSetThrower(CallbackInfo ci) {
        ItemEntity item = (ItemEntity) (Object) this;
        if (item != null) { // If the item is real
            if (item.getWorld() instanceof ServerWorld server) { // Run on server-side
                if (item.getStack().isOf(SoSItems.MARSHMALLOW)) { // If Item has owner
                    if (item.getOwner() != null) { // if Item is of marshmallow
                        //if (isNearSoulSource(item, item.getWorld(), item.getBlockPos())) { // if is near souls
                        if (item.getItemAge() >= 100) {
                            if (item.getItemAge() % 5 == 0)
                                server.spawnParticles(ParticleTypes.SCULK_SOUL, item.getX(), item.getY(), item.getZ(), 1, 0.1, 0.1, 0.1, 0.02);
                            if (item.getItemAge() % 40 == 0)
                                item.playSound(SoundEvents.PARTICLE_SOUL_ESCAPE, 7.0f, 0.8f);
                            if (item.getItemAge() % 20 == 0)
                                item.playSound(SoundEvents.ENTITY_WARDEN_HEARTBEAT, 10.0f, 0.5f);
                            if (item.getItemAge() >= 400) { // if age is of time
                                item.getWorld().playSound(null, item.getX(), item.getY(), item.getZ(), SoundEvents.ENTITY_SHULKER_BULLET_HIT, SoundCategory.NEUTRAL, 1, 2);
                                server.spawnParticles(ParticleTypes.EXPLOSION, item.getX(), item.getY(), item.getZ(), 1, 0, 0, 0, 0);
                                item.discard();
                                SweetsOSpirits.createWithOwner(server, item);
                            }
                        }
                    }
                }
            }
        }
    }
}
