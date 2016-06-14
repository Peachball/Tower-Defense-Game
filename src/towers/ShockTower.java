package towers;

import java.util.ArrayList;

import mechanic.DamageType;
import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;
import monsters.Monster;
import particlesystem.ParticleEmitter;
import particlesystem.EmitterTypes;
import projectiles.BouncingProjectile;
import projectiles.Laser;
import projectiles.Projectile;
import projectiles.ShockProjectile;

public class ShockTower extends Tower{
	/*----
	 * TOWER: Shock Tower
	 * --
	 * ATTACK: Single target
	 * TYPE: Magic
	 * MODIFIERS: Shocked
	 * MAX LEVEL: 4
	 * 
	 * --DESCRIPTION--
	 * A tower that hits multiple targets, dealing damage and reducing their impact resistance
	 * 
	 * --FIXED ATTRIBUTES--
	 * Base Attack Time: 2
	 * Projectile Speed: 3750
	 * Attack Range: 140
	 * Bounce radius: 200
	 * Damage multiplier per bounce: 0.8
	 * Debuff duration: 4
	 * 
	 * --MODIFIED ATTRIBUTES--
	 * Level
	 * 	Attack Damage
	 * 	Cost
	 * 	Targets bounced to
	 * 	Shock
	 * 		Impact resistance reduction
	 */
	//ATTRIBUTES//
	public static final int MAX_LEVEL = 4;
	public static final float BASE_ATTACK_TIME = 2.5f;
	public static final float BASE_ATTACK_SPEED = 100;
	public static final DamageType DAMAGE_TYPE = DamageType.MAGIC;
	public static final int PROJECTILE_SPEED = 3750;
	public static final float ATTACK_RANGE = 140;
	public static final float BOUNCE_RADIUS = 200;
	public static final float DAMAGE_MULTIPLIER_PER_BOUNCE = 0.70f;
	public static final float DEBUFF_DURATION = 4;
	//LEVEL-BASED ATTRIBUTES//
	public static final float[] ATTACK_DAMAGE = {90, 100, 110, 120};
	public static final int[] COST_FOR_LEVEL = {75, 50, 75, 100}; //First is cost for buying, rest is for upgrading
	public static final int[] NUMBER_OF_TARGETS = {3, 4, 5, 6};
	public static final float[] IMPACT_RESISTANCE_MODIFIER = {-5, -15, -25, -35};
	//OTHER//
	public static final String ID = "shock";
	public static final String IMAGE_PATH = "res/towers/tower_shock.png";
	public static final String PROJECTILE_IMAGE_PATH = "res/blank.png";
	public ShockTower(double x, double y) {
		this(x, y, 1);
	}
	public ShockTower(double x, double y, float attackRange, float baseAttackTime, float baseAttackSpeed, float damage){
		super(x, y, attackRange, baseAttackTime, baseAttackSpeed, damage, DAMAGE_TYPE);
		this.setProjectileSpeed(PROJECTILE_SPEED);
		this.setProjectileImage(PROJECTILE_IMAGE_PATH);
		this.setImage(IMAGE_PATH);
	}
	public ShockTower(double x, double y, int level) {
		super(x, y, ATTACK_RANGE, BASE_ATTACK_TIME, BASE_ATTACK_SPEED, ATTACK_DAMAGE[level - 1], DAMAGE_TYPE);
		this.setNetWorth(COST_FOR_LEVEL[0]);
		this.setMaxLevel(MAX_LEVEL);
		this.setLevel(level);
		this.setProjectileSpeed(PROJECTILE_SPEED);
		this.setProjectileImage(PROJECTILE_IMAGE_PATH);
		this.setImage(IMAGE_PATH);
	}
	@Override
	public String getTowerID() { //MUST KEEP
		return ID;
	}
	@Override
	public int getCostForNextLevel() { //MUST KEEP
		if(this.getLevel() != this.getMaxLevel()) {
			return COST_FOR_LEVEL[this.getLevel()];
		} else {
			return 0;
		}
	}
	@Override
	public float getAttackRangeForNextLevel() { //MUST KEEP
		if(this.getLevel() != this.getMaxLevel()) {
			return ATTACK_RANGE;
		} else {
			return 0;
		}
	}
	
	@Override
	public void onAttackTarget() {
		this.turnToTarget();
		this.getMap().addProjectile(new ShockProjectile(this, this.getTarget(), this.getFinalAttackDamage(), this.getDamageType(), this.getProjectileSpeed(), NUMBER_OF_TARGETS[this.getLevel() - 1] - 1, DAMAGE_MULTIPLIER_PER_BOUNCE, BOUNCE_RADIUS, IMPACT_RESISTANCE_MODIFIER[this.getLevel() - 1], DEBUFF_DURATION, this.getLevel()));
		//this.getMap().addProjectile(new BouncingProjectile(this, this.getTarget(), this.getFinalAttackDamage(), this.getDamageType(), this.getProjectileSpeed(), this.getProjectileImagePath(), this.getLoc(), 10, 0.9f, 200, true));
		/*THINGS BELOW ARE COMPLETELY UNNECESSARY*/
		/*
		String i = "res/particle_genericRed.png";
		ParticleEmitter pe = new ParticleEmitter(this.getLoc(), EmitterTypes.POINT_DIRECTION, i, false, //point, emitter type, image path, alphaDecay
				1.0f, 1.0f, //particle start scale
				0, 0, //particle end scale
				20.5f, //drag
				0, 0, //rotational velocity
				0.3f, 0.4f, //min and max lifetime
				100, 2200, //min and max launch speed
				0, 2, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)
				(float)Point.getDirectionDeg(this.getTarget().getLoc(), this.getLoc()), 20, 0, 0); //keyvalues
		this.getMap().addParticleEmitter(pe);
		*/
		/*THINGS ABOVE ARE COMPLETELY UNNECESSARY*/
	}
	public void onLevelUp() {
		this.changeNetWorth(COST_FOR_LEVEL[this.getLevel() - 1]);
		//this.setAttackRange(ATTACK_RANGE[this.getLevel() - 1]);
		//this.setBaseAttackSpeed(ATTACK_SPEED[this.getLevel() - 1]);
		this.setBaseAttackDamage(ATTACK_DAMAGE[this.getLevel() - 1]);
	}
	@Override
	public void onTowerUpdate() {
		this.turnToTarget();
	}
	
	
}
