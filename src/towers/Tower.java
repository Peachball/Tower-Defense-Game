
package towers;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import particlesystem.ParticleEmitter;
import particlesystem.EmitterTypes;
import projectiles.Laser;
import projectiles.Projectile;
import mechanic.DamageType;
import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;
import monsters.Monster;

public class Tower extends GameElement {
	
	ArrayList<GameElement> elementsInRange = new ArrayList<GameElement>();
	
	public static final float SEARCH_INTERVAL = 0.2f; //how often it searches for a new target
	public static final float MIN_ATTACK_SPEED = 50; //minimum attack speed
	private int netWorth;
	public static final float SELL_MULTIPLIER = 0.75f;
	private float searchTimer;
	
	private float baseAttackDamage;
	private float finalAttackDamage;
	
	private float projectileSpeed;
	private Image projectileImage;
	
	private float attackRange;		//in pixels
	private float baseAttackTime;	//in seconds
	private float baseAttackSpeed;		//in percent, the tower's base attack speed (usually 100)
	private float finalAttackSpeed;		//in percent, after all the bonuses from buffs
	private float attackInterval;	//for internal use, in seconds, calculated each time attack speed changes
	public float attackCooldown;	//for internal use, decremented each frame and checks if cooldown < 0
	private GameElement attackTarget;		//its target
	/*
	 * Explanation of base attack time and attack speed:
	 * 
	 * Each tower has a certain base attack time in seconds. 
	 * If the attack speed is at 100 (which is the default), then the tower will attack once every (base attack time) seconds.
	 * If the attack speed is at 200 (which is gained through upgrades or auras or whatever), then the tower will attack once every (base attack time / 2) seconds, or twice as often.
	 * Basically the formula can be thought like this:
	 * Time between attack = (base attack time)/(attack speed / 100)
	 * and:
	 * DPS = (attack damage) / ((base attack time)/(attack speed / 100))
	 * or just
	 * DPS = (attack damage) * (attack speed / 100) / (base attack time)
	 * 
	 * This way we gain an easy way to stack attack speed bonuses while also gaining leverage over their properties (i.e. a cannon that attacks once every 20 seconds won't suddenly become OP if we give it attack speed since we can just set its base attack time really high)
	 * 
	 * NOTES:
	 * Minimum attack speed is 50
	 */
	public Tower(double x, double y, float attackRange, float baseAttackTime, float baseAttackSpeed, float attackDamage, DamageType type){
		super();
		this.changeLoc(new Point(x,y));
		this.attackRange = attackRange;
		this.baseAttackTime = baseAttackTime;
		this.baseAttackSpeed = baseAttackSpeed;
		this.attackCooldown = 0;
		this.baseAttackDamage = attackDamage;
		this.searchTimer = 0;
		this.setDamageType(type);
		this.netWorth = 0;
	}
	
	/*
	 * Updates the tower's elements in range list
	 * 
	 * For use in towers that do something in an area around them (like a slow tower)
	 */
	public void updateElementsInRangeList(){
		elementsInRange.clear();
		for(GameElement e : this.getMap().getElements()) {
			if((float)Point.getDistance(e.getLoc(), this.getLoc()) <= this.attackRange) {
				elementsInRange.add(e);
			}
		}
	}
	
	/*
	 * Finds a monster in range and sets that as its target
	 * 
	 * Should be called only when its old target dies or runs out of range
	 * 
	 * Also it will target the enemy that's the first on the list, meaning that theoretically it will target the front enemy
	 */
	public void acquireTarget() {
		this.attackTarget = null;
		for(GameElement e : this.getMap().getElements()) {
			if((float)Point.getDistance(e.getLoc(), this.getLoc()) <= this.attackRange && e instanceof Monster) {
				this.attackTarget = e;
				//System.out.println("TARGET FOUND B0SS");
				break;
			}
		}
	}
	
	/*
	 * Checks if its target can no longer be targeted (if it's ded or it's out of range)
	 */
	public boolean targetIsTargetable(){
		if(this.attackTarget == null || this.getMap().getElements().contains(this.attackTarget) == false || Point.getDistance(this.attackTarget.getLoc(), this.getLoc()) > this.attackRange) {
			return false;
		} else {
			return true;
		}
	}
	
	/*
	 * Makes the tower attack, if there is a target
	 */
	public void attack() {
		if(this.targetIsTargetable() == false && this.searchTimer <= 0) { //if target is outside of range or it ded
			this.acquireTarget();
			this.searchTimer = SEARCH_INTERVAL;
		} else {
			this.searchTimer -= this.getFrameTime();
		}
		if(this.attackTarget != null && this.getMap().getElements().contains(this.attackTarget)) { //if there is a target
			while(this.attackCooldown <= 0) {
				this.onAttackTarget();
				this.attackCooldown += 1; //If the attack is successful, it will set the cooldown as a ratio
			}
			this.searchTimer = 0;
		} else {
			this.attackCooldown = 0;
		}
		//if there is no target, then nothing happens
	}
	/*
	 * OVERRIDABLE FUNCTIONS***********************************************
	 * What to do when the tower executes an attack
	 */
	public void onAttackTarget() {
		this.turnToTarget();
		this.getMap().addProjectile(new Projectile(this, this.attackTarget, this.finalAttackDamage, this.getDamageType(), this.projectileSpeed, this.projectileImage));
	}
	/*
	 * Called by projectiles whenever a projectile lands
	 */
	public void onAttackLanded(GameElement target) {
		
	}	
	/*
	 * Called by a projectile when it kills a target
	 */
	public void onAttackKillTarget(GameElement target) {
		
	}
	/*
	 * Called every frame
	 */
	public void onUpdate() {
		
	}
	
	public void updateAttackSpeed() {
		this.finalAttackSpeed = this.baseAttackSpeed + this.finalAttackSpeedModifier;
		if(this.finalAttackSpeed < MIN_ATTACK_SPEED) {
			this.finalAttackSpeed = MIN_ATTACK_SPEED;
		}
		this.attackInterval = this.baseAttackTime * 100 / this.finalAttackSpeed;
	}
	public void updateAttackDamage() {
		this.finalAttackDamage = this.baseAttackDamage * this.finalAttackDamageModifier;
		if(this.finalAttackDamage < 0) {
			this.finalAttackDamage = 0;
		}
	}
	public void turnToTarget() {
		if(this.attackTarget != null && this.attackTarget.getRemove() == false) {
			float deg = (float) Point.getDirectionDeg(this.getLoc(), this.attackTarget.getLoc());
			this.changeOrientation(deg);
		}
	}
	
	/*
	 * All towers have access to the acquire target, attack, etc. functions
	 * They can override them as they will depending on the tower
	 * (e.g. for a slow tower, it will override the attack function with its own code of applying a slow debuff)
	 */
	
	
	/*
	 * This is the basic loop that each tower should go through
	 * 
	 * 1. If it's time to attack, then it will update its attack speed and try to attack a target, else it will decrement its attack cooldown
	 * 2. Go to step 1
	 */
	
	@Override
	public void update() {
		this.onTowerUpdate();
		this.updateAttackSpeed();
		this.attackCooldown -= this.getFrameTime()/this.attackInterval; //decrease the cooldown by a ratio, so that when attack speed changes on the fly, it accounts for it
		if(this.attackCooldown < 0) {
			this.updateAttackDamage();
			this.attack();
		}
	}
	public void onTowerUpdate() {
		
	}
	@Override
	public boolean changeHP(double hp) {
		return false;
	}
	
	public float getAttackRange(){
		return this.attackRange;
	}
	public void setAttackRange(float range) {
		this.attackRange = range;
	}
	public float getBaseAttackSpeed() {
		return this.baseAttackSpeed;
	}
	public void setBaseAttackSpeed(float as) {
		this.baseAttackSpeed = as;
	}
	public float getBaseAttackDamage() {
		return this.baseAttackDamage;
	}
	public void setBaseAttackDamage(float damage) {
		if(damage > 0) {
			this.baseAttackDamage = damage;
		} else {
			this.baseAttackDamage = 0;
		}
	}
	public float getAttackSpeed(){
		return this.finalAttackSpeed;
	}
	public float getBaseAttackTime(){
		return this.baseAttackTime;
	}
	public GameElement getTarget(){
		return this.attackTarget;
	}
	public void setProjectileSpeed(float speed) {
		this.projectileSpeed = speed;
	}
	public void setProjectileImage(String path){
		try {
			this.projectileImage = new Image(path);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setProjectileImage(Image image) {
		this.projectileImage = image;
	}
	public float getFinalAttackDamage() {
		return this.finalAttackDamage;
	}
	public float getProjectileSpeed() {
		return this.projectileSpeed;
	}
	public Image getProjectileImage() {
		return this.projectileImage;
	}
	public String getTowerID() {
		return "";
	}
	public int getCostForNextLevel() {
		return 0;
	}
	public float getAttackRangeForNextLevel() {
		return 0;
	}
	public int getNetWorth() {
		return this.netWorth;
	}
	public void changeNetWorth(int money) {
		this.netWorth += money;
	}
	public void setNetWorth(int money){
		this.netWorth = money;
	}
	public int getSellPrice() {
		return (int)(this.netWorth * SELL_MULTIPLIER);
	}
}
