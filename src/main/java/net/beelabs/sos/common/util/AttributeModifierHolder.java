package net.beelabs.sos.common.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;

import java.util.UUID;

public class AttributeModifierHolder extends AbstractAttributeModifierHolder {
    public final double defaultAmount;
    public final EntityAttributeModifier.Operation defaultOperation;

    protected final Instance defaultInstance;

    public AttributeModifierHolder(EntityAttribute attribute, UUID uuid, String name, double defaultAmount, EntityAttributeModifier.Operation defaultOperation) {
        super(attribute, uuid, name);
        this.defaultAmount = defaultAmount;
        this.defaultOperation = defaultOperation;
        this.defaultInstance = new Instance(defaultAmount, defaultOperation);
    }

    @Override
    public Instance get() {
        return defaultInstance;
    }

    @Override
    public Instance getWithMultiplier(double multiplier) {
        return new Instance(defaultAmount * multiplier, defaultOperation);
    }

    @Override
    public Instance getWithSummand(double summand) {
        return new Instance(defaultAmount + summand, defaultOperation);
    }

    public Instance getWithNewAmount(double newAmount) {
        return new Instance(newAmount, defaultOperation);
    }

    public Instance getWithNewAmountAndOperation(double newAmount, EntityAttributeModifier.Operation operation) {
        return new Instance(newAmount, operation);
    }

    public class Instance extends AbstractAttributeModifierHolder.Instance {
        public EntityAttributeModifier modifier;

        protected Instance(double amount, EntityAttributeModifier.Operation operation) {
            this.modifier = new EntityAttributeModifier(uuid, name, amount, operation);
        }

        public void addTransientModifier(LivingEntity entity) {
            addTransientInternal(modifier, entity);
        }

        public void addPermanentModifier(LivingEntity entity) {
            addPermanentInternal(modifier, entity);
        }

        public MutableText translatable() {
            return translatableInternal(this.modifier.getValue(), this.modifier.getOperation(), false,-1);
        }

        public MutableText translatable(double baseAmount) {
            return translatableInternal(this.modifier.getValue(), this.modifier.getOperation(), true, baseAmount);
        }
    }
}