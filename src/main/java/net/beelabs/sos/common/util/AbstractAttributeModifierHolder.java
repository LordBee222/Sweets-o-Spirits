package net.beelabs.sos.common.util;

import net.beelabs.sos.common.SweetsOSpirits;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.event.listener.GameEventListener;

import java.util.UUID;

import static net.minecraft.item.ItemStack.MODIFIER_FORMAT;

public abstract class AbstractAttributeModifierHolder {
    public final EntityAttribute attribute;
    public final UUID uuid;
    public final String name;

    public AbstractAttributeModifierHolder(EntityAttribute attribute, UUID uuid, String name) {
        this.attribute = attribute;
        this.uuid = uuid;
        this.name = name;
    }

    public abstract Instance get();

    public abstract Instance getWithMultiplier(double multiplier);

    public abstract Instance getWithSummand(double summand);

    public boolean hasModifier(LivingEntity entity) {
        return entity.getAttributes().hasModifierForAttribute(attribute, uuid);
    }

    public void removeModifier(LivingEntity entity) {
        EntityAttributeInstance instance = entity.getAttributeInstance(attribute);
        if (instance != null)
            instance.removeModifier(uuid);
    }

    public abstract class Instance {

        public void resetTransientModifier(LivingEntity entity) {
            removeModifier(entity);
            addTransientModifier(entity);
        }

        public void resetPermanentModifier(LivingEntity entity) {
            removeModifier(entity);
            addPermanentModifier(entity);
        }

        public abstract void addTransientModifier(LivingEntity entity);

        public abstract void addPermanentModifier(LivingEntity entity);

        protected void addTransientInternal(EntityAttributeModifier modifier, LivingEntity entity) {
            EntityAttributeInstance instance = entity.getAttributeInstance(attribute);
            if (instance != null)
                instance.addTemporaryModifier(modifier);
        }

        protected void addPermanentInternal(EntityAttributeModifier modifier, LivingEntity entity) {
            EntityAttributeInstance instance = entity.getAttributeInstance(attribute);
            if (instance != null)
                instance.addTemporaryModifier(modifier);
        }

        public abstract MutableText translatable();

        public abstract MutableText translatable(double baseAmount);

        protected MutableText translatableInternal(double amount, EntityAttributeModifier.Operation operation, boolean displaysBase, double base) {
            amount = formattedAmount(amount, operation, displaysBase, base);
            String key = "attribute.modifier.equals.";
            Formatting style = Formatting.DARK_GREEN;
            if (!displaysBase) {
                if (amount > 0) {
                    key = "attribute.modifier.plus.";
                    style = Formatting.BLUE;
                }
                if (amount < 0) {
                    amount *= -1.0;
                    key = "attribute.modifier.take.";
                    style = Formatting.RED;
                }
            }
            return Text.translatable(key + operation.getId(), MODIFIER_FORMAT.format(amount), Text.translatable(attribute.getTranslationKey())).formatted(style);
        }

        protected double formattedAmount(double amount, EntityAttributeModifier.Operation operation, boolean displaysBase, double base) {
            if (displaysBase)
                amount += base;
            switch (operation) {
                case ADDITION -> amount *= attribute.equals(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE) ? 10 : 1;
                case MULTIPLY_BASE, MULTIPLY_TOTAL -> amount *= 100.0;
            }
            return amount;
        }
    }
}