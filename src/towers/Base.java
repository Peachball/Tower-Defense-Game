package towers;

import mechanic.GameMap;

public class Base extends Tower {

	public Base(double x, double y, GameMap map, float attackRange, float baseAttackTime, float baseAttackSpeed,
			float attackDamage) {
		super(x, y, map, attackRange, baseAttackTime, baseAttackSpeed, attackDamage);
		this.setImage("res/fatbarrelturret.JPG");
	}
	
	public Base(double x, double y, GameMap map){
		this(x,y,map,0,0,0,0);
	}
	
	@Override
	public void update(){
		if(this.getHP() < 0){
			map.endGame(1);
		}
	}

}
