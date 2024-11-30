package net.beelabs.sos.common.event;

import net.beelabs.sos.common.util.IRocketJumpVelocityGetter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class RocketJumpAirMobilityEvent implements MultiplyMovementSpeedEvent {
    @Override
    public float multiply(float currentMultiplier, World world, LivingEntity living) {
        if (!living.isOnGround() && ((IRocketJumpVelocityGetter)living).isRocketJumping()) {
                return currentMultiplier * 5;
        }
        return currentMultiplier;
    }

    @Override
    public int getPriority() {
        return 1001;
    }
}
