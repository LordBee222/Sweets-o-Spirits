package net.beelabs.sos.mixin;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.PiglinBruteEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PiglinBruteEntity.class)
public class PiglinBruteMixin {

    @Inject(method = "initEquipment", at = @At("TAIL"))
    private void initEquiped(Random random, LocalDifficulty localDifficulty, CallbackInfo ci){
        PiglinBruteEntity brute = (PiglinBruteEntity) (Object) this;
        ItemStack stack = new ItemStack(Items.SHIELD);
        if (random.nextInt(10) == 0) stack.addEnchantment(Enchantments.UNBREAKING, 3);
        if (random.nextInt(20) == 0) stack.addEnchantment(Enchantments.MENDING, 1);
        brute.equipStack(EquipmentSlot.OFFHAND, stack);

    }
}
