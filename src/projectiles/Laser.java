package projectiles;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;

import towers.Tower;
import mechanic.DamageType;
import mechanic.GameElement;

public class Laser extends Projectile {

	float delay;
	
	public Laser(GameElement creator, GameElement monster, float damage){
		super(creator, monster, damage, DamageType.MAGIC, 0, "");
		this.delay = 0.2f;
		monster.doDamage(this.damage, this.getDamageType());
	}
	
	@Override
	public void update(){
		if(this.delay < 0){
			this.setRemove(true);
		}
		else{
			this.delay -= this.getFrameTime();
		}
	}
	
	@Override
	public void draw(Graphics g){
		if(delay < 0){
			return;
		}
		g.setColor(Color.yellow);
		g.drawLine((float)this.creator.getX(),(float)this.creator.getY(),(float)this.target.getX(),(float)this.target.getY());
	}
	
}
