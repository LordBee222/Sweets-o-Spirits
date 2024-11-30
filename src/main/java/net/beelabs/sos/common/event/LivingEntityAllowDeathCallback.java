package net.beelabs.sos.common.event;

import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.entity.SoulmallowEntity;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class LivingEntityAllowDeathCallback implements ServerLivingEntityEvents.AllowDeath {

    @Override
    public boolean allowDeath(LivingEntity self, DamageSource source, float amount) {
        if (self.getWorld() instanceof ServerWorld serverWorld && !(self instanceof SoulmallowEntity)) {
            SoulmallowEntity soulmallow = SweetsOSpirits.findOwnedSoulmallow(self);
            if (soulmallow != null && soulmallow.isAlive()) { 
                SweetsOSpirits.onDeathBarter(soulmallow, self, serverWorld);
                soulmallow.damage(source, Float.MAX_VALUE);
                self.setHealth(self.getMaxHealth());
                return false;
            }
        }
        return true;
    }
}
