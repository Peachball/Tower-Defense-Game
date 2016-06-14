
package states;

import mechanic.Game;
import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;
import monsters.DefaultMonster;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import particlesystem.ParticleEmitter;
import particlesystem.EmitterTypes;
import towers.StandardTower;
import towers.FrostTower;
import towers.ShockTower;
import towers.Tower;
import ui.GridSelect;
import ui.LivesText;
import ui.MoneyText;
import ui.StartButton;
import ui.Text;
import ui.TextFormat;
import ui.UI;
import ui.UIElement;
import ui.towerselectUI.TowerSelectUI;

public class StateGame extends BasicGameState{
	static final int STARTING_LIVES = 50;
	static final int STARTING_MONEY = 5000;
	float delay = 0; //UNECCESARY VARIABLE, FOUND IN UPDATE
	float timescale = 1;
	double systemTime; //USED FOR FINDING FPS, USED IN UPDATE
	int monstersSpawned = 0;
	GameMap map;
	Image backgroundImage;
	UI ui;
	GridSelect gridSelector;
	TowerSelectUI towerSelector;
	public StateGame(){
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame arg1) throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame arg1){
		container.setClearEachFrame(true);
		this.setBackgroundImage("res/lenny face.png");
		map = new GameMap(20, 15, //dimensions of pathing grid
				800, 600, //dimensions of map
				new Point(0, 100), //top left corner of map (map coordinates)
				new Point (0, 5), new Point(18, 7)); //start and end points of pathing (grid coordinates)
		/*
		map.placeTower(new DefaultTower(200, 400, map, 300, 1, 100, 150)); //MAGIC NUMBER ALERT
		map.placeTower(new DefaultTower(400, 400, map, 300, 1, 250, 125));
		map.placeTower(new DefaultTower(600, 400, map, 300, 1, 450, 100));
		*/
		map.spawnCreep(new DefaultMonster(0, 0));
		
		Tower theTower = new ShockTower(400, 400, 1);
		map.placeTower(theTower);
		ui = new UI(map);
		map.setUI(ui);
		ui.setMaxLives(STARTING_LIVES);
		ui.setLives(STARTING_LIVES);
		ui.setMoney(STARTING_MONEY);
		gridSelector = new GridSelect(ui);
		ui.addUIElementNoChildren(gridSelector);
		towerSelector = new TowerSelectUI(ui, gridSelector);
		ui.addUIElement(towerSelector);
		MoneyText moneyText = new MoneyText(ui, new Point(575, 780), 200);
		ui.addUIElement(moneyText);
		LivesText livesText = new LivesText(ui, new Point(575, 760), 200);
		ui.addUIElement(livesText);
	}

	@Override
	public void render(GameContainer container, StateBasedGame arg1, Graphics g) throws SlickException {
		//g.clear();
		backgroundImage.drawWarped(600, 200, 700, 600, 20, 800, 40, 40); //start with topright, then clockwise
		map.draw(g);
		ui.draw(g);
		//map.drawGridHighlight(g, map.getMousePosition());
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		float frametime = (float) ((System.nanoTime() - systemTime) / 1000000000) * timescale;
		delay-= frametime;
		map.passFrameTime(frametime); //calculates difference in time per frame , and magic number is there since 1 second is 10^9 nanoseconds
		ui.passFrameTime(frametime);
		systemTime = System.nanoTime();
		map.update();
		ui.update();

		if(delay <= 0)
		{
			map.spawnCreep(new DefaultMonster(map.gridToPosition(map.getSpawn()).getX(), map.gridToPosition(map.getSpawn()).getY(), 12 * monstersSpawned, (int)monstersSpawned/5)); // MAGIC NUMBER
			delay = 1.2f;
			monstersSpawned++;
		}
	}
	@Override
	public void mousePressed(int button, int x, int y) {
		//PLACE A TOWER
		/*
		Tower theTower = new DefaultTower(x, y, 175, 1, 100, 50);
		//Tower theTower = new FrostTower(x, y, 1);
		if(map.isTowerAtGridPos(map.positionToGrid(new Point(x,y)))) {
			Tower existingTower = map.getTowerAtGridPos(map.positionToGrid(new Point(x,y)));
			if(existingTower.getLevel() == existingTower.getMaxLevel()) {
				map.removeTowerAtGridPos(map.positionToGrid(new Point(x,y)));
			} else {
				existingTower.levelUp();
			}
		} else {
			map.placeTower(theTower);
		}
		*/
		/*
		if(this.map.isTowerAtGridPos(this.map.positionToGrid(new Point(x, y)))) {
			map.removeTowerAtGridPos(this.map.positionToGrid(new Point(x, y)));
		}
		*/
		ui.onClick();
	}
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		timescale = (float)newx/400;
		map.passMousePosition(newx, newy);
		ui.passMouseLoc(new Point(newx, newy));
	}
	@Override
	public void keyPressed(int key, char c) {
		String message = "";
		switch(key) {
		case Input.KEY_UP:
			message = "up";
			break;
		case Input.KEY_DOWN:
			message = "down";
			break;
		case Input.KEY_LEFT:
			message = "left";
			break;
		case Input.KEY_RIGHT:
			message = "right";
			break;
		default:
			break;
		}
		gridSelector.passMessage(message);
	}

	@Override
	public int getID() {
		return Game.STATE_GAME;
	}
	
	public void passPathGrid(boolean[][] pathGrid) {
		this.map.passPathGrid(pathGrid);
	}
	public void setBackgroundImage(String image) {
		try {
			this.backgroundImage = new Image(image);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}