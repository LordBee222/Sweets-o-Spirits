package net.beelabs.sos.common.init;

import net.minecraft.item.FoodComponent;

public class SoSFoodComponents {
    public static final FoodComponent MARSHMALLOW = new FoodComponent
            .Builder()
            .hunger(4)
            .saturationModifier(0.5f)
            .snack()
            .alwaysEdible()
            .build();
}
