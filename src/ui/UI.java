package ui;

import java.util.ArrayList;

import mechanic.GameMap;
import mechanic.Point;
import ui.towerUpgradeUI.UpgradeButton;

import org.newdawn.slick.Graphics;

public class UI {
	GameMap map;
	Point mouseLoc;
	float frametime;
	int money;
	int lives;
	int maxLives;
	ArrayList<UIElement> elementList = new ArrayList<UIElement>();
	ArrayList<Integer> elementListFrontIndexes = new ArrayList<Integer>();
	ArrayList<UIElement> elementListAddBuffer = new ArrayList<UIElement>();
	ArrayList<UIElement> elementListRemoveBuffer = new ArrayList<UIElement>();
	public UI(GameMap map) {
		if(map != null) {
			this.map = map;
		}
		this.mouseLoc = new Point();
	}
	public UI() {
		this(null);
	}
	public void update() {
		for(UIElement u : this.elementList) {
			u.passFrameTime(this.frametime);
			u.update();
			u.updateRelationships();
			if(u.getRemove()) {
				this.elementListRemoveBuffer.add(u);
			}
			if(u.getRemoveNextFrame()) {
				u.setRemove(true);
			}
		}
		for(UIElement u : this.elementList) {
			u.updateRelationships();
		}
		this.elementList.addAll(this.elementListAddBuffer);
		this.elementList.removeAll(this.elementListRemoveBuffer);
		this.elementListAddBuffer.clear();
		this.elementListRemoveBuffer.clear();
	}
	public void draw(Graphics g) {
		for(UIElement u : this.elementList) {
			if(!u.isFront()) {
				u.draw(g);
			} else {
				this.elementListFrontIndexes.add(this.elementList.indexOf(u));
			}
		}
		for(int i : this.elementListFrontIndexes) {
			this.elementList.get(i).draw(g);
		}
		this.elementListFrontIndexes.clear();
	}
	public void addUIElement(UIElement u) {
		if(this.map != null) {
			u.setMap(this.map);
		}
		if(!this.elementList.contains(u) && !this.elementListAddBuffer.contains(u)) {
			this.elementListAddBuffer.add(u); //to ensure no repeats
		}
		u.setRemove(false);
		u.passFrameTime(this.frametime);
		u.updateRelationships();
		for(UIElement v : u.getChildren()) {
			this.addUIElement(v);
		}
		u.onAddedToUI();
		u.update();
		u.updateRelationships();
	}
	public void addUIElementNoChildren(UIElement u) {
		if(this.map != null) {
			u.setMap(this.map);
		}
		if(!this.elementList.contains(u) && !this.elementListAddBuffer.contains(u)) {
			this.elementListAddBuffer.add(u); //to ensure no repeats
		}
		u.setRemove(false);
		u.passFrameTime(this.frametime);
		u.updateRelationships();
		for(UIElement v : u.getChildren()) {
			v.setRemove(true);
		}
		u.onAddedToUI();
		u.update();
		u.updateRelationships();
	}
	public void setMap(GameMap map) {
		this.map = map;
	}
	public GameMap getMap() {
		return this.map;
	}
	/*
	 * DON'T USE THIS, COMPLETELY SKETCH
	 */
	public void bringToFront(UIElement u) {
		if(this.elementList.contains(u)) {
			elementList.remove(u);
			elementList.add(u);
		} else if (this.elementListAddBuffer.contains(u)) {
			elementListAddBuffer.remove(u);
			elementListAddBuffer.add(u);
		}
	}
	public void passMouseLoc(Point loc) {
		this.mouseLoc = loc;
	}
	public Point getMouseLoc() {
		return this.mouseLoc;
	}
	public void onClick() {
		for(UIElement u : this.elementList) {
			u.onClick();
		}
	}
	public void passFrameTime(float frametime) {
		this.frametime = frametime;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public void changeMoney(int money) {
		this.money += money;
	}
	public int getMoney() {
		return this.money;
	}
	public void setLives(int lives) {
		if(this.lives > this.maxLives) {
			this.lives = this.maxLives;
		} else if (this.lives < 0) {
			this.lives = 0;
		} else {
			this.lives = lives;
		}
	}
	public void changeLives(int lives) {
		if(this.lives + lives > this.maxLives) {
			this.lives = this.maxLives;
		} else if (this.lives + lives < 0) {
			this.lives = 0;
		} else {
			this.lives += lives;
		}
	}
	public int getLives() {
		return this.lives;
	}
	public void setMaxLives(int lives) {
		this.maxLives = lives;
	}
	public int getMaxLives() {
		return this.maxLives;
	}
}
