package towers;

import java.util.ArrayList;

import mechanic.DamageType;
import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;
import monsters.Monster;
import particlesystem.ParticleEmitter;
import particlesystem.EmitterTypes;
import projectiles.Laser;
import statuseffect.Aura;
import statuseffect.StatusFrost;
import statuseffect.TargetType;

public class FrostTower extends Tower{
	/*----
	 * TOWER: Frost Tower
	 * --
	 * ATTACK: No target, AOE
	 * TYPE: Magic
	 * MODIFIERS: Frost
	 * MAX LEVEL: 5
	 * 
	 * --DESCRIPTION--
	 * A generic slow tower that slows everything in an area around it
	 * 
	 * --FIXED ATTRIBUTES--
	 * Base Attack Time: 2
	 * Base Attack Speed: 100
	 * Attack Damage: 0
	 * Frost
	 * 	Magic damage
	 * Duration
	 * 
	 * --MODIFIED ATTRIBUTES--
	 * Level
	 * 	Attack Range
	 * 	Frost
	 * 		Speed modifier
	 * 		Damage per second
	 * 		Duration
	 */
	//ATTRIBUTES//
	public static final int MAX_LEVEL = 5;
	public static final float BASE_ATTACK_TIME = 3f;
	public static final int BASE_ATTACK_SPEED = 100;
	public static final DamageType DAMAGE_TYPE = DamageType.MAGIC;
	public static final float FROST_DURATION = 6;
	//LEVEL-BASED ATTRIBUTES//
	public static final float[] ATTACK_RANGE = {100, 120, 140, 160, 180};
	public static final float[] FROST_SPEED_MODIFIER = {0.68f, 0.64f, 0.60f, 0.56f, 0.52f};
	//public static final float[] FROST_SPEED_MODIFIER = {0.8f, 0.65f, 0.50f, 0.35f, 0.20f};
	public static final float[] FROST_DAMAGE_PER_SECOND = {15, 30, 45, 60, 75};
	public static final int[] COST_FOR_LEVEL = {125, 100, 125, 150, 175}; //First is cost for buying, rest is for upgrading
	//OTHER//
	public static final String ID = "frost";
	public static final String IMAGE_PATH = "res/towers/tower_frost.png";
	public static final String PROJECTILE_IMAGE_PATH = "res/blank.png";
	//Aura frostAura;
	//----------------//
	public FrostTower(Point loc, int level) {
		this(loc.getX(), loc.getY(), level);
	}
	public FrostTower(double x, double y) {
		this(x, y, 1);
	}
	public FrostTower(double x, double y, int level){
		super(x, y, 0, BASE_ATTACK_TIME, BASE_ATTACK_SPEED, 0, DAMAGE_TYPE);
		this.setNetWorth(COST_FOR_LEVEL[0]);
		this.setMaxLevel(MAX_LEVEL);
		this.setLevel(level);
		this.setAttackRange(ATTACK_RANGE[this.getLevel() - 1]);
		this.setImage(IMAGE_PATH);
		//this.frostAura = new Aura(this, "frostaura", this.getAttackRange(), TargetType.MONSTER, new StatusFrost(null, FROST_SPEED_MODIFIER[this.getLevel() - 1], FROST_DAMAGE_PER_SECOND[this.getLevel() - 1], FROST_DURATION, this.getLevel()));
		//this.addStatusEffect(frostAura);
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
		/*THINGS BELOW ARE COMPLETELY UNNECESSARY*/
		String i = "res/particle_genericBlue.png";
		//attack particle: expands outwards
		ParticleEmitter pe = new ParticleEmitter(this.getLoc(), EmitterTypes.POINT_RADIAL, i, true, /*point, emitter type, image path, alphaDecay*/
				1.0f, 1.0f, /*particle start scale*/
				FrostTower.ATTACK_RANGE[this.getLevel() - 1]/4, FrostTower.ATTACK_RANGE[this.getLevel() - 1]/4, /*particle end scale*/
				1.5f, /*drag*/
				0, 0, /*rotational velocity*/
				0.4f, 0.4f, /*min and max lifetime*/
				0, 0, /*min and max launch speed*/
				0, 1, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
				0, 0, 0, 0); /*keyvalues*/
		this.getMap().addParticleEmitter(pe);
		/*THINGS ABOVE ARE COMPLETELY UNNECESSARY*/
		
		this.updateElementsInRangeList();
		for(GameElement e: this.elementsInRange) {
			if(e instanceof Monster) {
				StatusFrost effect = new StatusFrost(e, FROST_SPEED_MODIFIER[this.getLevel() - 1], FROST_DAMAGE_PER_SECOND[this.getLevel() - 1], FROST_DURATION, this.getLevel());
				e.addStatusEffect(effect); //handles stacking within GameElement
			}
		}
	}
	
	@Override
	public void onLevelUp() {
		this.changeNetWorth(COST_FOR_LEVEL[this.getLevel() - 1]);
		this.setAttackRange(ATTACK_RANGE[this.getLevel() - 1]);
		//this.frostAura.updateAuraEffect(this.getAttackRange(), new StatusFrost(null, FROST_SPEED_MODIFIER[this.getLevel() - 1], FROST_DAMAGE_PER_SECOND[this.getLevel() - 1], FROST_DURATION, this.getLevel()));
	}
	
	/*
	@Override
	public void update(){
		for(GameElement e : map.getElements()){
			if(e instanceof Monster){
				map.addProjectile(new Laser(this, e));
				
			}
		}
	}
	*/
	
	/*
	@Override
	public void draw(Graphics g){
		g.drawImage(this.getImage().getScaledCopy((float) 0.5),(float)this.getX(), (float)this.getY());
	}*/
	
}
