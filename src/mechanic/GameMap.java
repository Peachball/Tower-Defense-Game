
package mechanic;

import java.util.ArrayList;

import monsters.Monster;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
/*
 * My Ideas:
 * -Each GameElement has a boolean array variable that gets set to the map's boolean array in Update
 * -Each GameElement can access that information and do whatever they want with it
 * -This means that monsters can use it to pathfind
 * -Since this is controlled by the Map, the Map doesn't have to include projectiles into the grid
 * -Whenever a tower is placed, the tower game element's position gets snapped to the grid and the Map's boolean array gets updated, and so will each GameElement's boolean array
 * -This means that their pathfinding gets updated as well
 */
import org.newdawn.slick.geom.Shape;

import particlesystem.EmitterTypes;
import particlesystem.ParticleBase;
import particlesystem.ParticleEmitter;
import particlesystem.Trail;
import projectiles.Projectile;
import towers.Tower;
import ui.UI;

public class GameMap {
	/////////DEBUG/////
	boolean debug = true;
	boolean viewTowerTarget = true;
	boolean viewTowerRange = false;
	boolean viewGrid = false;
	boolean viewMonsterPath = true;
	boolean viewHealthBars = true;
	boolean viewScentGrid = false; //destroys fps
	boolean viewAttackCooldown = true;
	///////////////////
	UI ui;
	Point spawn;
	Point destination;
	Point mapTopLeft; //THE TOP LEFT POINT OF THE MAP
	float mWidth; //MAP WIDTH
	float mHeight; //MAP HEIGHT
	int gWidth; //GRID WIDTH
	int gHeight; //GRID HEIGHT
	Point mouseLoc;
	float frametime;
	boolean[][] pathGrid;
	double[][] scentGrid;
	Tower[][] towerGrid;
	ArrayList<GameElement> elementList = new ArrayList<GameElement>();
	ArrayList<GameElement> elementBuffer = new ArrayList<GameElement>();
	ArrayList<ParticleBase> particleList = new ArrayList<ParticleBase>();
	ArrayList<ParticleBase> particleBuffer = new ArrayList<ParticleBase>();
	
	ParticleEmitter pe;
	Point prevLoc;
	Trail t;
	
	public GameMap(int gridWidth, int gridHeight, float mapWidth, float mapHeight, Point mapTopLeft, Point spawn, Point destination){
		this.gWidth = gridWidth;
		this.gHeight = gridHeight;
		this.spawn = spawn;
		this.destination = destination;
		this.mapTopLeft = mapTopLeft;
		pathGrid = new boolean[gWidth][gHeight];
		scentGrid = new double[gWidth][gHeight];
		towerGrid = new Tower[gWidth][gHeight];
		this.mouseLoc = new Point();
		this.mWidth = mapWidth; 
		this.mHeight = mapHeight; 
		this.scentGrid = this.createScentGrid(pathGrid, destination);
		
		t = new Trail(this.mouseLoc, "res/trail_lightning.png", 0, 24, 0.2f, 16, true, 64);
		this.addTrail(t);
	}
	
	public GameMap(int height, int width){
		this(height, width, 800, 800, new Point(), new Point(), new Point(height - 1, width - 1));
	}
	
	public void update() {
		//Updates everything
		for(int i = 0; i < elementList.size(); i++) {
			elementList.get(i).passFrameTime(frametime);
			elementList.get(i).update(); 
			elementList.get(i).updateStatusEffects();
			elementList.get(i).updateRegardsToParent();
			if(elementList.get(i).getRemove()){
				elementList.remove(i);
			}
		}
		for(int i = 0; i < particleList.size(); i++) {
			particleList.get(i).passFrameTime(frametime);
			particleList.get(i).move();
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
		for(int i = 0; i < elementList.size(); i++) { //draw elements
			GameElement temp = elementList.get(i);
			temp.draw(g);
			//BELOW IS FOR DEBUGGING PURPOSES/////////
			if(debug) {
				if(viewMonsterPath) {
					if(temp instanceof Monster) {
						viewPathFinding((Monster) temp, g); //view the path that the monster takes
					}
				}
				if(temp instanceof Tower) {
					if(((Tower) temp).targetIsTargetable()) {
						//draws a circle around the turret's target
						g.setColor(Color.red);
						if(viewTowerTarget) {
							g.drawOval((float)((Tower) temp).getTarget().getLoc().getX() - ((Tower) temp).getTarget().getCollisionRadius(), (float)((Tower) temp).getTarget().getLoc().getY() - ((Tower) temp).getTarget().getCollisionRadius(), ((Tower) temp).getTarget().getCollisionRadius()*2, ((Tower) temp).getTarget().getCollisionRadius()*2);
						}
					} else {
						g.setColor(Color.gray);
					}
					if(viewTowerRange) {
						//attack range
						g.drawOval((float)temp.getLoc().getX() - ((Tower) temp).getAttackRange(), (float)temp.getLoc().getY() - ((Tower) temp).getAttackRange(), ((Tower) temp).getAttackRange() * 2, ((Tower) temp).getAttackRange() * 2);
					}
					if(viewAttackCooldown) {
						int width = 30;
						int height = 4;
						int yOffset = -16;
						if(((Tower)temp).attackCooldown <= 0) {
							switch(temp.getDamageType()) {
							case IMPACT:
								g.setColor(new Color(255,255,100));
								break;
							case PIERCE:
								g.setColor(new Color(255,180,120));
								break;
							case MAGIC:
								g.setColor(new Color(150,150,255));
								break;
							case ABSOLUTE:
								g.setColor(new Color(255,150,150));
								break;
							default:
								break;
							}
						} else {
							switch(temp.getDamageType()) {
							case IMPACT:
								g.setColor(new Color(100,100,0));
								break;
							case PIERCE:
								g.setColor(new Color(110,60,0));
								break;
							case MAGIC:
								g.setColor(new Color(0,0,110));
								break;
							case ABSOLUTE:
								g.setColor(new Color(110,0,0));
								break;
							default:
								break;
							}
						}
						Shape rect = new Rectangle((float)temp.getLoc().getX() - width/2, (float)temp.getLoc().getY() - (yOffset + height/2), (float)width - ((Tower)temp).attackCooldown * width, (float)height);
						g.fill(rect);
					}
				}
				if(viewHealthBars) {
					//health bars
					if(temp instanceof Monster) {
						int width = 100;
						int height = 10;
						int yOffset = 50;
						g.setColor(Color.red);
						Shape rect = new Rectangle((float)temp.getLoc().getX() - width/2, (float)temp.getLoc().getY() - (yOffset + height/2), (float)(temp.getHP()/temp.getMaxHP() * width), (float)height);
						g.fill(rect);
					}
				}
				if(viewScentGrid) {
					this.viewScentGrid(g);
				}
			}
			//ABOVE IS FOR DEBUGGING PURPOSES//////////////
		}
		for(int i = 0; i < particleList.size(); i++) //draw particles
		{
			particleList.get(i).draw(g);
		}
		for(int i = 0; i < elementList.size(); i++) { //draw the elements' status icons
			elementList.get(i).drawStatusIcons(g);
		}
		if(viewGrid) {
			this.drawGridHighlight(g, this.mouseLoc);
		}
	}
	//////////////////////////////////
	//PATHING/////////////////////////
	//////////////////////////////////
	public void updatePathing() {
		this.scentGrid = createScentGrid(this.pathGrid, this.destination);
		for(GameElement g : this.elementList) {
			if(g instanceof Monster) {
				((Monster) g).updatePath(destination);
			}
		}
	}
	public void updatePathing(double[][] scentGrid) {
		this.scentGrid = scentGrid;
		for(GameElement g : this.elementList) {
			if(g instanceof Monster) {
				((Monster) g).updatePath(destination);
			}
		}
	}
	//part of bs pathfinding algorithm
	public double[][] createScentGrid(boolean[][] pathGrid, Point target) {
		double[][] scentGrid = new double[pathGrid.length][pathGrid[0].length];
		/*
		for(int x = 0; x < pathGrid.length; x++) {
			for(int y = 0; y < pathGrid[0].length; y++) {
				scentGrid[x][y] = 0;
			}
		}
		*/
		for(int i = 0; i < pathGrid.length * pathGrid[0].length; i++) { //how many times to iterate
			double[][] scentGridBuffer = new double[pathGrid.length][pathGrid[0].length];
			for(int x = 0; x < pathGrid.length; x++) {
				for(int y = 0; y < pathGrid[0].length; y++) {
					scentGridBuffer[x][y] = 0;
				}
			}
			for(int x = 0; x < pathGrid.length; x++) {
				for(int y = 0; y < pathGrid[0].length; y++) {
					if(pathGrid[x][y]) {
						scentGridBuffer[x][y] = 0;
					} else {
						double top, left, bottom, right;
						if(y == 0) {
							top = 0;
						} else if (pathGrid[x][y-1]) {
							top = 0;
						} else {
							top = scentGrid[x][y-1];
						}
						
						if(y == pathGrid[0].length - 1) {
							bottom = 0;
						} else if (pathGrid[x][y+1]) {
							bottom = 0;
						} else {
							bottom = scentGrid[x][y+1];
						}
						
						if(x == 0) {
							left = 0;
						} else if (pathGrid[x-1][y])  {
							left = 0;
						} else {
							left = scentGrid[x-1][y];
						}
						
						if(x == pathGrid.length - 1) {
							right = 0;
						} else if(pathGrid[x+1][y]) {
							right = 0;
						} else {
							right = scentGrid[x+1][y];
						}
						scentGridBuffer[x][y] = (top + left + bottom + right) / 4;
					}
				}
			}
			for(int x = 0; x < pathGrid.length; x++) {
				for(int y = 0; y < pathGrid[0].length; y++) {
					scentGrid[x][y] = scentGridBuffer[x][y];
				}
			}
			scentGrid[(int) target.getX()][(int) target.getY()] = 1;
		}
		return scentGrid;
	}
	//purely a debug function
	public void viewPathFinding(Monster m, Graphics g) {
		for(int index = 0; index < m.getPath().size() - 1; index++) {
			g.setColor(Color.yellow);
			g.drawLine((float)this.gridToPosition(m.getPath().get(index)).getX(), (float)this.gridToPosition(m.getPath().get(index)).getY(), (float)this.gridToPosition(m.getPath().get(index + 1)).getX(), (float)this.gridToPosition(m.getPath().get(index + 1)).getY());
		}
	}
	//yet another debug function, very expensive
	public void viewScentGrid(Graphics g) {
		for(int x = 0; x < this.pathGrid.length; x++) {
			for(int y = 0; y < this.pathGrid[0].length; y++) {
				Point loc = gridToPosition(new Point(x,y));
				float boxWidth = 0.9f * this.mWidth / this.gWidth;
				float boxHeight = 0.9f *this.mHeight / this.gHeight;
				g.setColor(new Color((int)(Math.pow(this.scentGrid[x][y], 0.125) * 255), (int)(Math.pow(this.scentGrid[x][y], 0.5) * 511), (int)(this.scentGrid[x][y] * 1023), 255));
				//g.setColor(new Color((int)(this.scentGrid[x][y] * 2555), (int)(this.scentGrid[x][y] * 25555), (int)(this.scentGrid[x][y] * 255555), 255));
				//g.setColor(new Color((int)(-Math.log(this.scentGrid[x][y]) % 2) * 255 , 0, 0, 255));
				g.drawRect((float)loc.getX() - boxWidth / 2, (float)loc.getY() - boxHeight / 2, boxWidth, boxHeight);
			}
		}
	}
	public void passPathGrid(boolean[][] pathGrid) {
		if(pathGrid.length != this.pathGrid.length || pathGrid[0].length != this.pathGrid[0].length) {
			System.out.println("Mismatch in array lengths in passPathGrid function!"); //TODO: debug message
		} else {
			for(int x = 0; x < this.pathGrid.length; x++) {
				for(int y = 0; y < this.pathGrid[0].length; y++) {
					this.pathGrid[x][y] = pathGrid[x][y];
				}
			}
		}
		this.updatePathing();
	}
	//END OF PATHING////////////////////
	
	//draws highlights on the map about which grid box the specified point is in
	public void drawGridHighlight(Graphics g, Point p) {
		Point loc = gridToPosition(positionToGrid(p));
		float boxWidth = this.mWidth / this.gWidth;
		float boxHeight = this.mHeight / this.gHeight;
		g.setColor(Color.cyan);
		//if is occupied
		if(pathGrid[(int) positionToGrid(p).x][(int) positionToGrid(p).y] == true)
			g.setColor(Color.magenta);
		g.drawRect((float)loc.getX() - boxWidth / 2, (float)loc.getY() - boxHeight / 2, boxWidth, boxHeight);
	}
	public Point getMapDimensions() {
		return new Point(this.mWidth, this.mHeight);
	}
	public Point getPathDimensions() {
		return new Point(this.gWidth, this.gHeight);
	}
	public Point getMapTopLeft() {
		return this.mapTopLeft;
	}
	
	//Takes a position on the map and converts it into grid coordinates
	public Point positionToGrid(Point loc) {
		//Squishes it down and floors the value
		int gridposX = (int)(this.gWidth * (loc.getX() - this.mapTopLeft.getX()) / this.mWidth);
		int gridposY = (int)(this.gHeight * (loc.getY() - this.mapTopLeft.getY()) / this.mHeight);
		//If for whatever reason the position is out of bounds, it will set it to the closest one
		if(gridposX > this.gWidth - 1)
			gridposX = this.gWidth - 1;
		if(gridposX < 0)
			gridposX = 0;
		if(gridposY > this.gHeight - 1)
			gridposY = this.gHeight - 1;
		if(gridposY < 0)
			gridposY = 0;
		//Return the grid position as a point
		return new Point(gridposX, gridposY);
	}
	
	//Takes a position on the grid and converts it into map coordinates
	public Point gridToPosition(Point loc) {
		float x = (float) (loc.getX() + 0.5) * this.mWidth / this.gWidth;
		float y = (float) (loc.getY() + 0.5) * this.mHeight / this.gHeight;
		x += this.mapTopLeft.getX();
		y += this.mapTopLeft.getY();
		return new Point(x, y);
	}
	
	//Places a tower, returns whether or not it's successful
	public boolean placeTower(Tower theTower) { //Will snap the tower to the grid and also change the boolean pathfinding array so that that square is blocked
		Point gridPos = positionToGrid(theTower.getLoc()); //gridPos is the grid coordinates of the tower
		boolean[][] testPath = new boolean[this.pathGrid.length][this.pathGrid[0].length]; //instantiates a test pathGrid for testing if the new tower blocks all paths
		for(int x = 0; x < this.pathGrid.length; x++) {
			for(int y = 0; y < this.pathGrid[0].length; y++) {
				testPath[x][y] = this.pathGrid[x][y]; //sets testPath to pathGrid
			}
		}
		testPath[(int)gridPos.getX()][(int)gridPos.getY()] = true; //hypothetically puts a tower down
		double[][] testScent = this.createScentGrid(testPath, this.destination); //tests scent on the hypothetical situation
		boolean isBlocked = testScent[(int)this.spawn.getX()][(int)this.spawn.getY()] == 0; //tests if this hypothetical situation results in an error in pathfinding
		if(pathGrid[(int) gridPos.getX()][(int) gridPos.getY()] == false && isBlocked == false) { //if there is nothing there and it doesn't block the path
			Point loc = gridToPosition(gridPos);
			theTower.getLoc().changeX(loc.getX());
			theTower.getLoc().changeY(loc.getY());
			theTower.setMap(this);
			elementBuffer.add(theTower);
			pathGrid[(int) gridPos.getX()][(int) gridPos.getY()] = true;
			towerGrid[(int) gridPos.getX()][(int) gridPos.getY()] = theTower;
			this.updatePathing();
			return true;
		}
		else
		{
			System.out.println("Attempted to place tower at " + gridPos.getX() + ", " + gridPos.getY() + " that would obstruct path!"); //TODO: debug message
			return false;
		}
	}
	public boolean testIfBlocksPath(Point gridPos) {
		boolean[][] testPath = new boolean[this.pathGrid.length][this.pathGrid[0].length]; //instantiates a test pathGrid for testing if the new tower blocks all paths
		for(int x = 0; x < this.pathGrid.length; x++) {
			for(int y = 0; y < this.pathGrid[0].length; y++) {
				testPath[x][y] = this.pathGrid[x][y]; //sets testPath to pathGrid
			}
		}
		testPath[(int)gridPos.getX()][(int)gridPos.getY()] = true; //hypothetically puts a tower down
		double[][] testScent = this.createScentGrid(testPath, this.destination); //tests scent on the hypothetical situation
		boolean isBlocked = testScent[(int)this.spawn.getX()][(int)this.spawn.getY()] == 0; //tests if this hypothetical situation results in an error in pathfinding
		return isBlocked;
	}
	//returns the tower at the grid position
	public Tower getTowerAtGridPos(Point loc) {
		return towerGrid[(int)loc.x][(int)loc.y];
	}
	public boolean isTowerAtGridPos(Point loc) {
		if(towerGrid[(int)loc.x][(int)loc.y] != null) {
			return true;
		} else {
			return false;
		}
	}
	//Handles all the nuances when it comes to removing a tower SHOULD BE USED EVERY TIME A TOWER IS REMOVED
	public void removeTowerAtGridPos(Point loc) {
		pathGrid[(int) loc.x][(int) loc.y] = false;
		Tower t = getTowerAtGridPos(loc);
		t.setRemove(true);
		towerGrid[(int)loc.x][(int)loc.y] = null;
		this.updatePathing();
	}
	
	//Returns an arraylist of game elements in the area, if input is 0 then it will return only the element at that position
	public ArrayList<GameElement> findElementsInArea (Point loc, float radius) {
		ArrayList<GameElement> elements = new ArrayList<GameElement>();
		for (GameElement e : this.getElements()) {
			if(Point.getDistance(e.getLoc(), loc) <= radius) {
				elements.add(e);
			}
		}
		return elements;
	}
	public ArrayList<Monster> findMonstersInArea (Point loc, float radius) {
		ArrayList<Monster> monsters = new ArrayList<Monster>();
		for (GameElement e : this.getElements()) {
			if(Point.getDistance(e.getLoc(), loc) <= radius && e instanceof Monster) {
				monsters.add((Monster) e);
			}
		}
		return monsters;
	}
	public ArrayList<Tower> findTowersInArea (Point loc, float radius) {
		ArrayList<Tower> towers = new ArrayList<Tower>();
		for (GameElement e : this.getElements()) {
			if(Point.getDistance(e.getLoc(), loc) <= radius && e instanceof Tower) {
				towers.add((Tower) e);
			}
		}
		return towers;
	}

	public void spawnCreep(Monster creep) {
		creep.setMap(this);
		creep.updatePath(this.destination);
		elementBuffer.add(creep);
	}
	
	public ArrayList<GameElement> getElements(){
		return elementList;
	}
	
	public void addProjectile(Projectile p){
		p.setMap(this);
		elementBuffer.add(p);
	}
	public void addParticle(ParticleBase p){
		particleBuffer.add(p);
	}
	public void addParticleEmitter(ParticleEmitter e) {
		e.setMap(this);
		elementBuffer.add(e);
	}
	public void addTrail(Trail t) {
		t.setMap(this);
		elementBuffer.add(t);
	}
	public void passFrameTime(float d){
		this.frametime = d;
	}
	public void passMousePosition(int x, int y) {
		this.mouseLoc.changeX(x);
		this.mouseLoc.changeY(y);
	}
	public Point getMousePosition() {
		return this.mouseLoc;
	}
	public boolean[][] getPathGrid() {
		return this.pathGrid;
	}
	public double[][] getScentGrid() {
		return this.scentGrid;
	}
	public Point getSpawn() {
		return this.spawn;
	}
	public Point getDestination() {
		return this.destination;
	}
	public void setUI(UI ui) {
		this.ui = ui;
	}
	public UI getUI() {
		return this.ui;
	}
}
