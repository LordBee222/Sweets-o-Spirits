package net.beelabs.sos.common.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

import java.util.Random;
import java.util.UUID;
import java.util.random.RandomGenerator;

import static net.minecraft.item.ItemStack.MODIFIER_FORMAT;

public class RangedRandomAttributeModifierHolder extends AbstractAttributeModifierHolder {
    public final double defaultMinAmount;
    public final double defaultMaxAmount;
    public final EntityAttributeModifier.Operation defaultOperation;
    protected final Instance defaultInstance;

    public RangedRandomAttributeModifierHolder(EntityAttribute attribute, UUID uuid, String name, double defaultMinAmount, double defaultMaxAmount, EntityAttributeModifier.Operation defaultOperation) {
        super(attribute, uuid, name);
        this.defaultMinAmount = defaultMinAmount;
        this.defaultMaxAmount = defaultMaxAmount;
        this.defaultOperation = defaultOperation;
        this.defaultInstance = new Instance(defaultMinAmount, defaultMaxAmount, defaultOperation);
    }

    public Instance get() {
        return defaultInstance;
    }

    public Instance getWithMultiplier(double multiplier) {
        return getWithMultipliers(multiplier, multiplier);
    }

    public Instance getWithMultipliers(double minMultiplier, double maxMultiplier) {
        return new Instance(defaultMinAmount * minMultiplier, defaultMaxAmount * maxMultiplier, defaultOperation);
    }

    public Instance getWithSummand(double summand) {
        return getWithSummands(summand, summand);
    }

    public Instance getWithSummands(double minSummand, double maxSummand) {
        return new Instance(defaultMinAmount + minSummand, defaultMaxAmount + maxSummand, defaultOperation);
    }

    public Instance getWithNewAmounts(double newMinAmount, double newMaxAmount) {
        return new Instance(newMinAmount, newMaxAmount, defaultOperation);
    }

    public Instance getWithNewAmountAndOperation(double newMinAmount, double newMaxAmount, EntityAttributeModifier.Operation operation) {
        return new Instance(newMinAmount, newMaxAmount, operation);
    }

    public class Instance extends AbstractAttributeModifierHolder.Instance {
        public final double minAmount;
        public final double maxAmount;
        public final EntityAttributeModifier.Operation operation;
        protected RandomGenerator random;


        protected Instance(double minAmount, double maxAmount, EntityAttributeModifier.Operation operation) {
            this.minAmount = minAmount;
            this.maxAmount = maxAmount;
            this.operation = operation;
        }

        public EntityAttributeModifier modifier() {
            return modifier(randomAmount());
        }

        public EntityAttributeModifier modifier(double amount) {
            return new EntityAttributeModifier(uuid, name, capToRange(amount, minAmount, maxAmount), operation);
        }

        public void addTransientModifier(LivingEntity entity) {
            addTransientInternal(modifier(), entity);
        }

        public void addPermanentModifier(LivingEntity entity) {
            addPermanentInternal(modifier(), entity);
        }

        public MutableText translatable() {
            return translatableInternal(maxAmount, operation, false, -1);
        }

        public MutableText translatable(double baseAmount) {
            return translatableInternal(maxAmount, operation, true, baseAmount);
        }

        @Override
        protected MutableText translatableInternal(double maxAmount, EntityAttributeModifier.Operation operation, boolean displaysBase, double baseValue) {
            MutableText result = super.translatableInternal(maxAmount, operation, displaysBase, baseValue);
            if (result.getContent() instanceof TranslatableTextContent contents) {
                String newKey = contents.getKey().replace("modifier", "piglinproliferation.ranged_modifier");
                Object[] oldArgs = contents.getArgs();
                Object[] newArgs = new Object[contents.getArgs().length + 1];
                newArgs[0] = MODIFIER_FORMAT.format(formattedAmount(minAmount, operation, displaysBase, baseValue));
                System.arraycopy(oldArgs, 0, newArgs, 1, oldArgs.length);
                result = Text.translatable(newKey, newArgs).fillStyle(result.getStyle());
            }
            return result;
        }

        public double randomAmount() {
            return random().nextDouble(minAmount, maxAmount);
        }

        public int randomIntAmount() {
            return random().nextInt(Math.round((float) minAmount), Math.round((float) maxAmount) + 1);
        }

        protected RandomGenerator random() {
            if (random == null)
                random = new Random();
            return random;
        }
    }

    public static int capToRange(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public static double capToRange(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }
}