package net.beelabs.sos.common.init;

import net.beelabs.sos.common.SweetsOSpirits;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class SoSMemoryModules {
        public static final MemoryModuleType<Float> REDIRECTED_DAMAGE_TAKEN = register("redirected_damage_taken");
        public static final MemoryModuleType<LivingEntity> PROVOKED_AT = register("provoked_at");
        public static final MemoryModuleType<Boolean> REDIRECT_DAMAGE_ON_COOLDOWN = register("redirect_damage_on_cooldown");


        private static <U> MemoryModuleType<U> register(String id) {
                return Registry.register(Registries.MEMORY_MODULE_TYPE, new Identifier(id), new MemoryModuleType<U>(Optional.empty()));
        }

        public static void registerMemoryModules(){
                SweetsOSpirits.LOGGER.info("Registering Brain Memory Modules for " + SweetsOSpirits.MOD_ID);
        }
}
