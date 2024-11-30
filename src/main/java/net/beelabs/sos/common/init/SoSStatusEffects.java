package net.beelabs.sos.common.init;

import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.effect.HauntedEffect;
import net.beelabs.sos.common.effect.MallowedEffect;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class SoSStatusEffects {
        public static final StatusEffect MALLOWED = register("mallowed",
                new MallowedEffect(StatusEffectCategory.BENEFICIAL, 0x36ebab));

        public static final StatusEffect HAUNTED = register("haunted",
                new HauntedEffect(StatusEffectCategory.HARMFUL, 3694022));

        public static final StatusEffect UNLEASHED = register("unleashed",
                new HauntedEffect(StatusEffectCategory.BENEFICIAL, 3694022));

        private static StatusEffect register(String name, StatusEffect statusEffect){
                return Registry.register(Registries.STATUS_EFFECT, new Identifier(SweetsOSpirits.MOD_ID, name), statusEffect);
        }

        public static void registerEffects(){
                SweetsOSpirits.LOGGER.info("Registering Mod Effects For " + SweetsOSpirits.MOD_ID);
        }
}
