package net.beelabs.sos.common.init;

import net.beelabs.sos.common.SweetsOSpirits;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class SoSSensors {
    //public static final SensorType<SoulmellowAttackablesSensor> SOULMELLOW_ATTACKABLES = register("soulmellow_attackables", SoulmellowAttackablesSensor::new);


    private static <U extends Sensor<?>> SensorType<U> register(String id, Supplier<U> factory) {
        return Registry.register(Registries.SENSOR_TYPE, new Identifier(id), new SensorType<U>(factory));
    }

    public static void registerSensors(){
        SweetsOSpirits.LOGGER.info("Registering Brain Sensors for " + SweetsOSpirits.MOD_ID);
    }
}
