
package projectiles;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import towers.Tower;

import org.newdawn.slick.Image;

import mechanic.DamageType;
import mechanic.GameElement;
import mechanic.Point;

public class Projectile extends GameElement {
	GameElement creator;
	GameElement target;
	float damage;
	Point prevLoc;
	
	public Projectile(GameElement creator, GameElement target, float damage, DamageType type, float speed, String path) {
		this.creator = creator;
		this.changeLoc(Point.clone(creator.getLoc()));
		this.prevLoc = Point.clone(this.getLoc());
		this.target = target;
		this.damage = damage;
		this.setDamageType(type);
		this.setSpeed(speed);
		this.setImage(path);
	}
	public Projectile(GameElement creator, GameElement target, float damage, DamageType type, float speed, Image image) { //SAME THING BUT WITH IMAGE
		this.creator = creator;
		this.changeLoc(Point.clone(creator.getLoc()));
		this.prevLoc = Point.clone(this.getLoc());
		this.target = target;
		this.damage = damage;
		this.setDamageType(type);
		this.setSpeed(speed);
		this.setImage(image);
	}
	
	@Override
	public void update() {
		this.onProjectileUpdate();
		this.prevLoc = Point.clone(this.getLoc());
		this.moveTowardsPoint(this.target.getLoc());
		if(this.target != null && this.creator != null) {
			if(this.isReasonableDistanceAwayFromColliding(this.target)) {
				boolean isKillingBlow = this.target.doDamage(this.damage, this.getDamageType());
				if(creator instanceof Tower) {
					((Tower) this.creator).onAttackLanded(this.target);
					if(isKillingBlow) {
						((Tower) this.creator).onAttackKillTarget(this.target);
					}
				}
				this.setRemove(true);
				this.onProjectileHit();
			}
		}
	}
	public void onProjectileUpdate() {
		
	}
	/*
	 * Called when the projectile lands
	 */
	public void onProjectileHit() {
		/*
		String i = "res/particle_genericYellow.png";
		ParticleEmitter pe = new ParticleEmitter(this.getLoc(), EmitterTypes.POINT_DIRECTION, i, false, //point, emitter type, image path, alphaDecay
				0.8f, 0.8f, //particle start scale
				0, 0, //particle end scale
				2.5f, //drag
				0, 0, //rotational velocity
				0.3f, 0.5f, //min and max lifetime
				300, 400, //min and max launch speed
				0, 2, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)
				(float)Point.getDirectionDeg(this.getLoc(), this.prevLoc), 20, 0, 0); //keyvalues
		this.getMap().addParticleEmitter(pe);
		*/
	}
}
