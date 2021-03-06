
package mechanic;

import java.util.ArrayList;

import monsters.Monster;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
/*
 * My Ideas:
 * -Each GameElement has a boolean array variable that gets set to the map's boolean array in Update
 * -Each GameElement can access that information and do whatever they want with it
 * -This means that monsters can use it to pathfind
 * -Since this is controlled by the Map, the Map doesn't have to include projectiles into the grid
 * -Whenever a tower is placed, the tower game element's position gets snapped to the grid and the Map's boolean array gets updated, and so will each GameElement's boolean array
 * -This means that their pathfinding gets updated as well
 */



import particlesystem.ParticleBase;
import particlesystem.ParticleEmitter;
import projectiles.Projectile;
import towers.Tower;

public class GameMap {
	
	int spawnX;
	int spawnY;
	int gWidth; //GRID WIDTH
	int gHeight; //GRID HEIGHT
	float frametime;
	boolean[][] pathGrid;
	ArrayList<GameElement> elementList = new ArrayList<GameElement>();
	ArrayList<GameElement> elementBuffer = new ArrayList<GameElement>();
	ArrayList<ParticleBase> particleList = new ArrayList<ParticleBase>();
	ArrayList<ParticleBase> particleBuffer = new ArrayList<ParticleBase>();
	
	public GameMap(int height, int width, int spawnX, int spawnY){
		gWidth = width;
		gHeight = height;
		this.spawnX = spawnX;
		this.spawnY = spawnY;
		pathGrid = new boolean[gWidth][gHeight];
	}
	
	public GameMap(int height, int width){
		this(height, width, 0, 0);
	}
	
	public void update() {
		//Updates everything (positions)
		for(int i = 0; i < elementList.size(); i++) {
			elementList.get(i).update(); 
			elementList.get(i).passFrameTime(frametime);
			if(elementList.get(i).getRemove()){
				elementList.remove(i);
			}
			//todo: set each game element's boolean list to the map's
		}
		for(int i = 0; i < particleList.size(); i++) {
			particleList.get(i).move();
			particleList.get(i).passFrameTime(frametime);
			if(particleList.get(i).getRemove()){
				particleList.remove(i);
			}
		}
		elementList.addAll(elementBuffer);
		elementBuffer.clear();
		particleList.addAll(particleBuffer);
		particleBuffer.clear();
	}
	public void draw(Graphics g) {
		for(int i = 0; i < elementList.size(); i++) {
			GameElement temp = elementList.get(i);
			temp.draw(g);
			//BELOW IS FOR DEBUGGING PURPOSES
			if(temp instanceof Tower) {
				if(((Tower) temp).targetIsTargetable()) {
					g.setColor(Color.red);
					g.drawOval((float)((Tower) temp).getTarget().getLoc().getX() - 50, (float)((Tower) temp).getTarget().getLoc().getY() - 50, 100, 100);
				} else {
					g.setColor(Color.white);
				}
				g.drawOval((float)temp.getLoc().getX() - ((Tower) temp).getAttackRange(), (float)temp.getLoc().getY() - ((Tower) temp).getAttackRange(), ((Tower) temp).getAttackRange() * 2, ((Tower) temp).getAttackRange() * 2);
			}
			//ABOVE IS FOR DEBUGGING PURPOSES
		}
		for(int i = 0; i < particleList.size(); i++)
		{
			particleList.get(i).draw(g);
		}
	}
	
	public void placeTower(Tower theTower) { //Will snap the tower to the grid and also change the boolean pathfinding array so that that square is blocked
		Tower tempTower = theTower;  //TODO: Convert loc position to grid position
		elementBuffer.add(theTower);
		//pathGrid[(int) tempTower.getLoc().x][(int) tempTower.getLoc().y] = true;
	}
	
	public void spawnCreep(Monster creep) {
		elementBuffer.add(creep);
	}
	
	public ArrayList<GameElement> getElements(){
		return elementList;
	}
	
	public void addProjectile(Projectile p){
		elementBuffer.add(p);
	}
	public void addParticle(ParticleBase p){
		particleBuffer.add(p);
	}
	public void addParticleEmitter(ParticleEmitter e) {
		elementBuffer.add(e);
	}
	public void passFrameTime(float d){
		this.frametime = d;
	}
}
