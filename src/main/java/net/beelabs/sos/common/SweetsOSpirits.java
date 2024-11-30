package net.beelabs.sos.common;

import net.beelabs.sos.common.entity.SoulmallowEntity;
import net.beelabs.sos.common.entity.WhispEntity;
import net.beelabs.sos.common.event.*;
import net.beelabs.sos.common.init.*;
import net.beelabs.sos.common.item.MarshmallowOnAStickItem;
import net.beelabs.sos.common.item.PigsteelShieldItem;
import net.beelabs.sos.common.keybind.UnleashedDashKeybind;
import net.beelabs.sos.common.networking.UnleashedDashPacket;
import net.beelabs.sos.common.particle.EatGappleParticle;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class SweetsOSpirits implements ModInitializer {
	public static final String MOD_ID = "sos";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static DefaultParticleType EAT_GAPPLE;


	@Override
	public void onInitialize() {

		// Init Item Stuffs
		SoSItems.registerItems();
		SoSItemGroup.registerItemGroups();

		// Init Brain Stuffs
		SoSSensors.registerSensors();
		SoSMemoryModules.registerMemoryModules();
		FabricDefaultAttributeRegistry.register(SoSEntities.SOULMALLOW, SoulmallowEntity.createAttributes());



		// Init Event Stuffs
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new LivingEntityAllowDamageCallback());
		ServerLivingEntityEvents.ALLOW_DEATH.register(new LivingEntityAllowDeathCallback());

		SoSStatusEffects.registerEffects();
		UnleashedDashPacket.registerReceivers();
		SoSSoundEvents.registerSounds();

		MarshmallowOnAStickItem.registerCookingTickEvent();

		SoSAttributes.registerAttribute();

		EAT_GAPPLE = Registry.register(Registries.PARTICLE_TYPE, SweetsOSpirits.id("eat_gapple"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(EAT_GAPPLE, EatGappleParticle.DefaultFactory::new);

		PigsteelShieldItem.registerPacketReceiver();
		PigsteelShieldItem.registerServerTickEvent();

		MultiplyMovementSpeedEvent.EVENT.register(new RocketJumpAirMobilityEvent());

	}


	public static Identifier id(String string) {
		return new Identifier(MOD_ID, string);
	}

	public static SoulmallowEntity findOwnedSoulmallow(LivingEntity owner) {
		if (owner.getWorld() instanceof ServerWorld world) {
			for (SoulmallowEntity soulmallow : world.getEntitiesByClass(SoulmallowEntity.class, owner.getBoundingBox().expand(10), soulmallow -> soulmallow.getOwner() == owner)) {
				if (soulmallow.isAlive()) return soulmallow;
			}
		}
		return null;
	}


	public static void onDeathBarter(SoulmallowEntity soulmallow, LivingEntity owner, ServerWorld serverWorld) {
		soulmallow.getWorld().playSound(null, owner.getX(), owner.getY(), owner.getZ(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.NEUTRAL, 10f, 2F);

		for (int i = 0; i < 200; i++) {
			serverWorld.spawnParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, owner.getX(), owner.getRandomBodyY(), owner.getZ(), 1, 0.2, 0.2, 0.2, 0.1);
			serverWorld.spawnParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, soulmallow.getX(), soulmallow.getRandomBodyY(), soulmallow.getZ(), 1, 0.2, 0.2, 0.2, 0.1);

		}
	}

	public static void takeRedirectedDamage(DamageSource damageSource, float amount, Entity causer, SoulmallowEntity soulmallow){
		if (causer instanceof LivingEntity) {
			float i = 0;
			int id = causer.getId();
			if (soulmallow.getRedirectedDamageCausers().get(id) != null) i = soulmallow.getRedirectedDamageCausers().get(id);
			soulmallow.getRedirectedDamageCausers().put(id, i + amount);
		}
		soulmallow.damage(damageSource, amount);
	}

	public static float getPercentToAttack(int targetId, SoulmallowEntity soulmallow) {
		return Optional.ofNullable(soulmallow.getRedirectedDamageCausers().get(targetId))
				.map(damage ->
						damage >= 6 && damage <= 10 ? 0.1f :
								damage >= 11 && damage <= 15 ? 0.4f :
										damage >= 16 && damage <= 20 ? 0.7f :
												damage >= 21 ? 1f : 0.0f
				).orElse(0.0f);
	}

	public static void createWithOwner(ServerWorld server, ItemEntity item){
		SoulmallowEntity soulmallow = SoSEntities.SOULMALLOW.spawn(server, item.getBlockPos(), SpawnReason.CONVERSION);
		if (soulmallow != null) soulmallow.setOwner((PlayerEntity) item.getOwner());
	}

	public static void onRedirectDamage(SoulmallowEntity soulmallow, DamageSource source, float amount, LivingEntity owner, ServerWorld serverWorld){
		SweetsOSpirits.takeRedirectedDamage(source, amount, source.getAttacker(), soulmallow);
		if (source.getAttacker() != null){
			SweetsOSpirits.redirectDamageToAttacker(source, amount, source.getAttacker().getId(), soulmallow);
			if (source.getAttacker() instanceof LivingEntity livingEntity) {
				WhispEntity whisp = new WhispEntity(serverWorld, livingEntity, soulmallow);
				serverWorld.spawnEntity(whisp);
				LOGGER.info("SPAWNED");
			}
		}
		soulmallow.getWorld().playSound(null, owner.getX(), owner.getY(), owner.getZ(), SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.NEUTRAL, 10f, 1F);
		for (int i = 0; i < 50; i++) {
			serverWorld.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, owner.getX(), owner.getRandomBodyY(), owner.getZ(), 1, 0.2, 0.2, 0.2, 0.1);
			serverWorld.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, soulmallow.getX(), soulmallow.getRandomBodyY(), soulmallow.getZ(), 1, 0.2, 0.2, 0.2, 0.1);

		}
		soulmallow.getBrain().remember(SoSMemoryModules.REDIRECTED_DAMAGE_TAKEN, soulmallow.getBrain().getOptionalMemory(SoSMemoryModules.REDIRECTED_DAMAGE_TAKEN).orElse(0f) + amount);
		soulmallow.getBrain().remember(SoSMemoryModules.REDIRECT_DAMAGE_ON_COOLDOWN, true, 100);
	}

	public static void redirectDamageToAttacker(DamageSource damageSource, float amount, int targetId, SoulmallowEntity soulmallow){
		Entity target = soulmallow.getWorld().getEntityById(targetId);
		float damageToDeal = amount * SweetsOSpirits.getPercentToAttack(targetId, soulmallow);
		if (target instanceof LivingEntity && target.getWorld() instanceof ServerWorld serverWorld){
			if (damageToDeal != 0) target.damage(damageSource, damageToDeal);
			for (int i = 0; i < 50; i++) serverWorld.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, target.getX(), target.getRandomBodyY(), target.getZ(), 1, 0.2, 0.2, 0.2, 0.1);
		}
	}
}