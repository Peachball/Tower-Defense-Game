
package projectiles;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import particlesystem.Trail;
import towers.Tower;

import java.util.ArrayList;

import org.newdawn.slick.Image;

import mechanic.DamageType;
import mechanic.GameElement;
import mechanic.Point;
import monsters.Monster;

public class BouncingProjectile extends Projectile {
	int numBounces;
	float damageMultiplierPerBounce;
	float bounceRadius;
	boolean bounceToNearestTargetNoRepeat;
	ArrayList<Monster> monstersBouncedTo;
	public BouncingProjectile(GameElement creator, GameElement target, float damage, DamageType type, float speed, String path,
			Point startPos, int numBounces, float damageMultiplierPerBounce, float bounceRadius,
			boolean bounceToNearestTargetNoRepeat) {
		super(creator, target, damage, type, speed, path);
		this.changeLoc(Point.clone(startPos));
		this.prevLoc = Point.clone(startPos);
		this.numBounces = numBounces;
		this.damageMultiplierPerBounce = damageMultiplierPerBounce;
		this.bounceRadius = bounceRadius;
		this.bounceToNearestTargetNoRepeat = bounceToNearestTargetNoRepeat;
		this.monstersBouncedTo = new ArrayList<Monster>();
	}
	public BouncingProjectile(GameElement creator, GameElement target, float damage, DamageType type, float speed, Image image, 
			Point startPos, int numBounces, float damageMultiplierPerBounce, float bounceRadius,
			boolean bounceToNearestTargetNoRepeat) { // SAME CONSTRUCTOR BUT WITH IMAGE
		super(creator, target, damage, type, speed, image);
		this.changeLoc(Point.clone(startPos));
		this.prevLoc = Point.clone(startPos);
		this.numBounces = numBounces;
		this.damageMultiplierPerBounce = damageMultiplierPerBounce;
		this.bounceRadius = bounceRadius;
		this.bounceToNearestTargetNoRepeat = bounceToNearestTargetNoRepeat;
		this.monstersBouncedTo = new ArrayList<Monster>();
	}
	public void setMonstersBouncedTo(ArrayList<Monster> monstersBouncedTo) {
		this.monstersBouncedTo = (ArrayList<Monster>) monstersBouncedTo.clone();
	}
	/*
	 * Called when the projectile lands
	 */
	@Override
	public void onProjectileHit() {
		this.onBouncingProjectileHit();
		this.setLoc(target.getLoc());
		if(this.numBounces > 0) {
			this.setRemove(false);
			ArrayList<Monster> monsterList = this.getMap().findMonstersInArea(this.getLoc(), bounceRadius);
			if(monsterList.size() > 1) { //IF THERE ARE OTHER TARGETS TO BOUNCE TO
				Monster randomTarget = null;
				if(!this.bounceToNearestTargetNoRepeat) {
					int randomIndex = (int) (Math.random() * monsterList.size());
					if (randomIndex == monsterList.size()) { //sometimes math.random may produce a perfect int of 1, which will not get casted down
						randomIndex = monsterList.size() - 1;
					}
					randomTarget = monsterList.get(randomIndex); //find the target
					if(randomTarget.equals(this.target)) { //if it's going to bounce to the same target (bounce to itself)
						if(randomIndex == 0) { //choose another index
							randomIndex++;
						} else {
							randomIndex--;
						}
						randomTarget = monsterList.get(randomIndex);
					}
				} else {
					float smallestDistance = bounceRadius;
					for(Monster m : monsterList) {
						boolean hasBouncedTo = false;
						for(Monster bm : this.monstersBouncedTo) { //check to make sure it hasn't been bounced to
							if(m.equals(bm)) {
								hasBouncedTo = true;
								//break;
							}
						}
						if(!hasBouncedTo && !m.equals(this.target) && Point.getDistance(m.getLoc(), this.getLoc()) <= smallestDistance) { //if it is closer and it isn't itself, it will override the existing one
							randomTarget = m;
							smallestDistance = (float) Point.getDistance(m.getLoc(), this.getLoc());
						}
					}
				}
				if(randomTarget != null) {
					if(this.bounceToNearestTargetNoRepeat) {
						this.monstersBouncedTo.add((Monster) this.target);
						//p.setMonstersBouncedTo(this.monstersBouncedTo);
						//this.monstersBouncedTo.clear();
					}
					this.target = randomTarget;
					this.damage *= damageMultiplierPerBounce;
					this.numBounces = this.numBounces - 1;
					//System.out.println("Number of bounces is " + numBounces);
					//BouncingProjectile p = new BouncingProjectile(this.creator, randomTarget, this.damage * damageMultiplierPerBounce, this.getDamageType(), (float)this.getSpeed(), this.getImage(), this.getLoc(), this.numBounces - 1, this.damageMultiplierPerBounce, bounceRadius, this.bounceToNearestTargetNoRepeat);
					//this.getMap().addProjectile(p);
				} else {
					//System.out.println("MY TARGET IS NULL");
					this.setRemove(true);
					monstersBouncedTo.clear();
				}
			} else {
				monstersBouncedTo.clear();
				this.setRemove(true);
			}
		} else {
			monstersBouncedTo.clear();
			this.setRemove(true);
		}
	}
	public void onBouncingProjectileHit() {
		
	}
}
