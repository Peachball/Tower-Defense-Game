package ui.towerselectUI;

import org.newdawn.slick.Color;

import mechanic.DamageType;
import mechanic.Point;
import towers.StandardTower;
import towers.FrostTower;
import towers.ShockTower;
import towers.Tower;
import ui.GridSelect;
import ui.Text;
import ui.TextFormat;
import ui.UI;
import ui.UIBox;
import ui.UIBoxOrigin;
import ui.UIElement;
/*
 * DESCRIPTION
 * 
 * a thing that you click to place a tower
 */

/*
 * TYPES:
 * default
 * frost
 */
public class TowerSelectTower extends UIElement {
	GridSelect gridSelector;
	boolean mouseIsInside;
	String type; //TOWER TYPE
	int cost;
	Text tooltipText;
	UIBox uibox;
	GhostTower ghostTower;
	DamageType damageType;
	UIElement redXMoney;
	boolean canBuy;
	static final Color COLOR_HOVERED_OVER = new Color(255, 255, 255);
	static final Color COLOR_NOT_HOVERED_OVER = new Color(210, 200, 220);
	static final float OPACITY_CAN_BUY = 1.0f;
	static final float OPACITY_CAN_NOT_BUY = 0.3f;
	static final int LEFT_MARGIN = 4;
	static final int TOP_MARGIN = 4;
	public TowerSelectTower(UI ui, Point loc, String type) {
		super(ui, loc);
		String tooltip = "";
		float range = 0;
		mouseIsInside = false;
		this.type = type;
		switch(type) {
		case StandardTower.ID:
			this.setImage(StandardTower.IMAGE_PATH);
			cost = StandardTower.COST_FOR_LEVEL[0];
			range = StandardTower.ATTACK_RANGE[0];
			damageType = StandardTower.DAMAGE_TYPE;
			tooltip = StandardTower.ID.toUpperCase() + " TOWER"
					+ "|"
					+ "|a regular tower that shoots bullets"
					+ "|"
					+ "|DAMAGE TYPE: " + StandardTower.DAMAGE_TYPE.getName()
					+ "|DAMAGE: " + (int)StandardTower.ATTACK_DAMAGE[0]
					+ "|RANGE: " + (int)StandardTower.ATTACK_RANGE[0]
					+ "|BASE ATTACK TIME: " + StandardTower.BASE_ATTACK_TIME
					+ "|ATTACK SPEED: " + (int)StandardTower.ATTACK_SPEED[0]
					+ "|"
					+ "|COST: " + cost;
			break;
		case FrostTower.ID:
			this.setImage(FrostTower.IMAGE_PATH);
			cost = FrostTower.COST_FOR_LEVEL[0];
			range = FrostTower.ATTACK_RANGE[0];
			damageType = FrostTower.DAMAGE_TYPE;
			tooltip = FrostTower.ID.toUpperCase() + " TOWER"
					+ "|"
					+ "|a frosty tower that slows everything around it"
					+ "|"
					+ "|DAMAGE TYPE: " + FrostTower.DAMAGE_TYPE.getName()
					+ "|SLOW: " + (int)(100 - FrostTower.FROST_SPEED_MODIFIER[0] * 100) + "%"
					+ "|DAMAGE PER SECOND: " + (int)FrostTower.FROST_DAMAGE_PER_SECOND[0]
					+ "|DURATION: " + FrostTower.FROST_DURATION
					+ "|RANGE: " + (int)FrostTower.ATTACK_RANGE[0]
					+ "|BASE ATTACK TIME: " + FrostTower.BASE_ATTACK_TIME
					+ "|ATTACK SPEED: " + (int)FrostTower.BASE_ATTACK_SPEED
					+ "|"
					+ "|COST: " + cost;
			break;
		case ShockTower.ID:
			this.setImage(ShockTower.IMAGE_PATH);
			cost = ShockTower.COST_FOR_LEVEL[0];
			range = ShockTower.ATTACK_RANGE;
			damageType = ShockTower.DAMAGE_TYPE;
			tooltip = ShockTower.ID.toUpperCase() + " TOWER"
					+ "|"
					+ "|a tower that hits multiple enemies and increasing the damage they take from IMPACT damage sources"
					+ "|"
					+ "|DAMAGE TYPE: " + ShockTower.DAMAGE_TYPE.getName()
					+ "|DAMAGE: " + (int)ShockTower.ATTACK_DAMAGE[0]
					+ "|DAMAGE REDUCTION PER BOUNCE: " + (int)(100 - ShockTower.DAMAGE_MULTIPLIER_PER_BOUNCE * 100) + "%"
					+ "|NUMBER OF TARGETS: " + ShockTower.NUMBER_OF_TARGETS[0]
					+ "|DEBUFF DAMAGE INCREASE: " + (int)-ShockTower.IMPACT_RESISTANCE_MODIFIER[0] + "%"
					+ "|DEBUFF DURATION: " + (int)ShockTower.DEBUFF_DURATION
					+ "|RANGE: " + (int)ShockTower.ATTACK_RANGE
					+ "|BASE ATTACK TIME: " + ShockTower.BASE_ATTACK_TIME
					+ "|ATTACK SPEED: " + (int)ShockTower.BASE_ATTACK_SPEED
					+ "|"
					+ "|COST: " + cost;
			break;
		default:
			break;
		}
		tooltipText = new Text(this.getUI(), new Point(), 275, 8, 12, 11, 20, Color.white, tooltip, TextFormat.LEFT_JUSTIFIED);
		tooltipText.setUseOutline(true);
		this.addChild(tooltipText);
		uibox = new UIBox(this.getUI(), new Point(), 200, 200, true, UIBoxOrigin.BOTTOM_LEFT);
		this.addChild(uibox);
		ghostTower = new GhostTower(ui, new Point(), this.getImage(), range, damageType, false);
		this.addChild(ghostTower);
		
		redXMoney = new UIElement(this.getUI(), this.getLoc());
		redXMoney.setImage("res/xmoney.png");
		redXMoney.setIsFront(true);
		redXMoney.setParent(this);
		//this.getUI().addUIElement(text);
	}
	public void passGridSelector(GridSelect sel) {
		this.gridSelector = sel;
		ghostTower.changeLoc(Point.subtract(this.gridSelector.getFinalLoc(), this.getFinalLoc()));
	}
	@Override
	public void onClick() {
		if(this.pointIsInHitbox(this.getUI().getMouseLoc())) {
			this.onClickedOn();
		}
	}
	@Override
	public void update() {
		canBuy = this.getUI().getMoney() >= cost;
		if(this.pointIsInHitbox(this.getUI().getMouseLoc())) {
			mouseIsInside = true;
		} else {
			mouseIsInside = false;
		}
		uibox.setHeight(tooltipText.getHeight() + TOP_MARGIN * 2);
		uibox.setWidth(tooltipText.getWidth() + LEFT_MARGIN);
		if(mouseIsInside) {
			this.setColor(COLOR_HOVERED_OVER);
			this.gridSelector.isHoveringOverTowerSelect = true;
			if(!this.gridSelector.isSelectingTower) {
				if(tooltipText.getRemove()) {
					//tooltipText.setRemove(false); already done in UI
					//uibox.setRemove(false);
					this.getUI().addUIElement(uibox);
					this.getUI().addUIElement(tooltipText);
					this.getUI().addUIElement(ghostTower);
				}
				uibox.changeLoc(Point.subtract(this.getUI().getMouseLoc(), this.getFinalLoc()));
				uibox.fitOnScreen();
				tooltipText.changeLoc(Point.add(uibox.getTopLeftLoc(), new Point(LEFT_MARGIN, TOP_MARGIN)));
				ghostTower.changeLoc(Point.subtract(this.gridSelector.getFinalLoc(), this.getFinalLoc()));
				if(!canBuy) {
					if(redXMoney.getRemove()) {
						this.getUI().addUIElement(redXMoney);
					}
				}
				redXMoney.changeLoc(Point.subtract(this.gridSelector.getFinalLoc(), this.getFinalLoc()));
			} else {
				for(UIElement e : this.getChildren()) {
					e.setRemove(true);
				}
			}
		} else {
			this.setColor(COLOR_NOT_HOVERED_OVER);
			for(UIElement e : this.getChildren()) {
				e.setRemove(true);
			}
		}
		if(canBuy){
			this.setAlpha(OPACITY_CAN_BUY);
		} else {
			this.setAlpha(OPACITY_CAN_NOT_BUY);
			this.setColor(COLOR_NOT_HOVERED_OVER);
		}
	}
	private void onClickedOn() {
		this.gridSelector.updateStatusOfMap();
		if(gridSelector.canPlaceTower && canBuy) {
			boolean placed = this.placeTower();
			if(placed) {
				this.getUI().setMoney(this.getUI().getMoney() - cost);
				this.gridSelector.updateStatusOfMap();
			}
		}
	}
	private boolean placeTower() {
		Point loc = gridSelector.getFinalLoc();
		switch(type) {
		case StandardTower.ID:
			Tower defaultTower = new StandardTower(loc.getX(), loc.getY(), 1);
			return this.getMap().placeTower(defaultTower);
		case FrostTower.ID:
			Tower frostTower = new FrostTower(loc.getX(), loc.getY(), 1);
			return this.getMap().placeTower(frostTower);
		case ShockTower.ID:
			Tower shockTower = new ShockTower(loc.getX(), loc.getY(), 1);
			return this.getMap().placeTower(shockTower);
		default:
			return false;
		}
	}
}
