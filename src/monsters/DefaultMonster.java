package monsters;


import particlesystem.ParticleEmitter;
import particlesystem.EmitterTypes;
import mechanic.DamageType;
import mechanic.GameMap;
import mechanic.Point;

public class DefaultMonster extends Monster{
	public static String imagePath = "res/idgaf.png";
	public static int MAXHP = 500;
	public static int MOVESPEED = 100;
	public static int COLLISION_RADIUS = 20;
	private static final int BOUNTY = 10;
	private static final int LIFE_COST = 1;
	private static double IMPACT_RESISTANCE = 30;
	private static double PIERCE_RESISTANCE = 5;
	private static double MAGIC_RESISTANCE = 25;
	private static double[] resistances = {IMPACT_RESISTANCE, PIERCE_RESISTANCE, MAGIC_RESISTANCE, 0};
	
	public DefaultMonster(double x, double y, int bonusHP, int bonusBounty){
		super(BOUNTY + bonusBounty, LIFE_COST);
		changeLoc(new Point(x,y));
		this.setImage(imagePath);
		this.changeMaxHP(MAXHP + bonusHP);
		this.changeHP(MAXHP + bonusHP);
		this.setSpeed(MOVESPEED);
		this.setCollisionRadius(COLLISION_RADIUS);
		this.setBaseResistances(resistances);
		/*
		this.setBaseResistance(DamageType.IMPACT, IMPACT_RESISTANCE);
		this.setBaseResistance(DamageType.PIERCE, PIERCE_RESISTANCE);
		this.setBaseResistance(DamageType.MAGIC, MAGIC_RESISTANCE);
		*/
	}
	public DefaultMonster(double x, double y) {
		this(x, y, 0, 0);
	}
	@Override
	public void onMonsterDeath() {
		String i = "res/particle_explosion.png";
		ParticleEmitter pe = new ParticleEmitter(this.getLoc(), EmitterTypes.POINT_RADIAL, i, true, /*point, emitter type, image path, alphaDecay*/
				0.0f, 0.5f, /*particle start scale*/
				0.5f, 1.0f, /*particle end scale*/
				2.5f, /*drag*/
				-300, 300, /*rotational velocity*/
				0.5f, 1, /*min and max lifetime*/
				100, 300, /*min and max launch speed*/
				0, 4, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
				0, 0, 0, 0); /*keyvalues*/
		this.getMap().addParticleEmitter(pe);
	}
}
