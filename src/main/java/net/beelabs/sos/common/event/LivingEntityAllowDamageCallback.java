package net.beelabs.sos.common.event;

import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.entity.SoulmallowEntity;
import net.beelabs.sos.common.init.SoSMemoryModules;
import net.beelabs.sos.common.init.SoSStatusEffects;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class LivingEntityAllowDamageCallback implements ServerLivingEntityEvents.AllowDamage {
    @Override
    public boolean allowDamage(LivingEntity owner, DamageSource source, float amount) {
        if (owner.hasStatusEffect(SoSStatusEffects.MALLOWED) && source.isOf(DamageTypes.FALL)){
            owner.removeStatusEffect(SoSStatusEffects.MALLOWED);
            if (owner.getWorld() instanceof ServerWorld server) {
                server.playSound(null, owner.getBlockPos(), SoundEvents.BLOCK_HONEY_BLOCK_BREAK, SoundCategory.NEUTRAL,1f, 0.8f);
                server.spawnParticles(ParticleTypes.EXPLOSION, owner.getX(), owner.getY(), owner.getZ(), 1, 0, 0, 0, 0);
                for (int i = 0; i < 50; i++) {
                    server.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Items.SNOW_BLOCK, 1)), owner.getX(), owner.getRandomBodyY(), owner.getZ(), 1, 0.2, 0.2, 0.2, 0.1);
                }
            }
            return false;
        }
        if (owner.getWorld() instanceof ServerWorld serverWorld && !(owner instanceof SoulmallowEntity)) {
            SoulmallowEntity soulmallow = SweetsOSpirits.findOwnedSoulmallow(owner);
            if (soulmallow != null && soulmallow.canRedirectDamage()) {
                if (amount > 1) {
                    if (!(soulmallow.getHealth() <= amount)) {
                        SweetsOSpirits.onRedirectDamage(soulmallow, source, amount, owner, serverWorld);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
