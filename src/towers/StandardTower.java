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

public class StandardTower extends Tower{
	/*----
	 * TOWER: Standard Tower
	 * --
	 * ATTACK: Single target
	 * TYPE: Impact
	 * MODIFIERS: 
	 * MAX LEVEL: 10
	 * 
	 * --DESCRIPTION--
	 * A tower that just shoots things
	 * 
	 * --FIXED ATTRIBUTES--
	 * Base Attack Time: 1
	 * Projectile Speed: 350
	 * 
	 * --MODIFIED ATTRIBUTES--
	 * Level
	 * 	Attack Range
	 * 	Attack Damage
	 * 	Base Attack Speed
	 * 	Cost
	 */
	//ATTRIBUTES//
	public static final int MAX_LEVEL = 10;
	public static final float BASE_ATTACK_TIME = 1f;
	public static final DamageType DAMAGE_TYPE = DamageType.IMPACT;
	public static final int PROJECTILE_SPEED = 350;
	//LEVEL-BASED ATTRIBUTES//
	public static final float[] ATTACK_RANGE = {100, 110, 120, 130, 140, 150, 160, 170, 180, 190};
	public static final float[] ATTACK_DAMAGE = {25, 35, 45, 55, 65, 75, 85, 95, 105, 115};
	public static final float[] ATTACK_SPEED = {100, 115, 130, 145, 160, 175, 190, 205, 220, 235};
	public static final int[] COST_FOR_LEVEL = {25, 35, 40, 45, 50, 55, 60, 65, 70, 75}; //First is cost for buying, rest is for upgrading
	//OTHER//
	public static final String ID = "standard";
	public static final String IMAGE_PATH = "res/towers/tower_standard.png";
	public static final String PROJECTILE_IMAGE_PATH = "res/particle_genericYellow.png";
	//----------------//
	public StandardTower(double x, double y) {
		this(x, y, 1);
	}
	public StandardTower(double x, double y, float attackRange, float baseAttackTime, float baseAttackSpeed, float damage){
		super(x, y, attackRange, baseAttackTime, baseAttackSpeed, damage, DAMAGE_TYPE);
		this.setProjectileSpeed(PROJECTILE_SPEED);
		this.setProjectileImage(PROJECTILE_IMAGE_PATH);
		this.setImage(IMAGE_PATH);
	}
	public StandardTower(double x, double y, int level) {
		super(x, y, ATTACK_RANGE[level - 1], BASE_ATTACK_TIME, ATTACK_SPEED[level - 1], ATTACK_DAMAGE[level - 1], DAMAGE_TYPE);
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
			return ATTACK_RANGE[this.getLevel()];
		} else {
			return 0;
		}
	}
	
	@Override
	public void onAttackTarget() {
		this.turnToTarget();
		this.getMap().addProjectile(new Projectile(this, this.getTarget(), this.getFinalAttackDamage(), this.getDamageType(), this.getProjectileSpeed(), this.getProjectileImage()));
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
		this.setAttackRange(ATTACK_RANGE[this.getLevel() - 1]);
		this.setBaseAttackSpeed(ATTACK_SPEED[this.getLevel() - 1]);
		this.setBaseAttackDamage(ATTACK_DAMAGE[this.getLevel() - 1]);
	}
	@Override
	public void onTowerUpdate() {
		this.turnToTarget();
	}
	
	
}
