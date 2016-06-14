package projectiles;

import mechanic.DamageType;
import mechanic.GameElement;
import particlesystem.Trail;
import statuseffect.StatusShock;

public class ShockProjectile extends BouncingProjectile {
	float impactResistanceModifier;
	float debuffDuration;
	int level;
	public ShockProjectile(GameElement creator, GameElement target, float damage, DamageType type, float speed, 
			int numBounces, float damageMultiplierPerBounce, float bounceRadius, 
			float impactResistanceModifier, float debuffDuration, int level){
		super(creator, target, damage, type, speed, "res/blank.png", creator.getLoc(), numBounces, damageMultiplierPerBounce, bounceRadius, true);
		this.impactResistanceModifier = impactResistanceModifier;
		this.debuffDuration = debuffDuration;
		this.level = level;
	}
	@Override
	public void onSetMap() {
		//TRAIL
		Trail t = new Trail(this, "res/trail_lightning.png", 48, 0, 0.3f, 64, false, 128);
		this.getMap().addTrail(t);
	}
	@Override
	public void onBouncingProjectileHit() {
		StatusShock modifier = new StatusShock(this.target, this.debuffDuration, this.impactResistanceModifier, this.level);
		this.target.addStatusEffect(modifier);
	}
}
