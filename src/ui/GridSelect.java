package ui;

import org.newdawn.slick.Graphics;

import mechanic.Point;
import towers.Tower;
import ui.towerUpgradeUI.TowerUpgradeMenu;
/*
 * DESCRIPTION
 * 
 * the little box highlighter thing, to highlight towers or blank spaces
 */
public class GridSelect extends UIElement {
	Point gridLoc;
	Tower selectedTower;
	UIElement redX;
	TowerUpgradeMenu upgradeMenu;
	RangeFinder towerRangeFinder;
	public boolean isSelectingTower;
	public boolean canPlaceTower;
	public boolean isHoveringOverTowerSelect;
	
	public GridSelect(UI ui) {
		super(ui);
		//IMAGE IS 32x32
		this.selectedTower = null;
		this.setImage("res/ui/gridSelect.png");
		isHoveringOverTowerSelect = false;
		isSelectingTower = false;
		canPlaceTower = true;
		redX = new UIElement(this.getUI(), this.getLoc());
		redX.setImage("res/x.png");
		redX.setIsFront(true);
		redX.setParent(this);
		upgradeMenu = new TowerUpgradeMenu(this.getUI(), new Point(), this);
		this.addChild(upgradeMenu);
	}
	@Override
	public void onSetMap() {
		this.setSize((this.getMap().getMapDimensions().getX() / this.getMap().getPathDimensions().getX()) / 32); //ASSUMING GRID IS SQUARE
		this.setGridLoc(new Point());
	}
	@Override
	public void update() {
		/*
		Point gridLoc = this.getMap().positionToGrid(this.loc);
		if(this.getMap().isTowerAtGridPos(gridLoc)) {
			this.selectedTower = this.getMap().getTowerAtGridPos(gridLoc);
			this.isSelectingTower = true;
		} else {
			this.selectedTower = null;
			this.isSelectingTower = false;
		}
		if(this.getMap().getPathGrid()[(int)gridLoc.getX()][(int)gridLoc.getY()]) {
			this.canPlaceTower = false;
		} else {
			this.canPlaceTower = true;
		}
		*/
		if(canPlaceTower == false && isHoveringOverTowerSelect) {
			if(redX.getRemove()) {
				this.getUI().addUIElement(redX);		
				redX.changeOrigin(this.getFinalLoc());
			}
		} else {
			redX.setRemove(true);
		}
		isHoveringOverTowerSelect = false;
	}
	public void setGridLoc(Point loc) {
		this.gridLoc = this.getMap().positionToGrid(this.getMap().gridToPosition(loc));
		this.loc = this.getMap().gridToPosition(this.gridLoc);
		this.updateStatusOfMap();
	}
	public void updateStatusOfMap() {
		if(this.getMap().isTowerAtGridPos(this.gridLoc)) {
			this.selectedTower = this.getMap().getTowerAtGridPos(this.gridLoc);
			this.isSelectingTower = true;
		} else {
			this.selectedTower = null;
			this.isSelectingTower = false;
		}
		if(this.getMap().getPathGrid()[(int)this.gridLoc.getX()][(int)this.gridLoc.getY()] || this.getMap().testIfBlocksPath(this.gridLoc)) {
			this.canPlaceTower = false;
		} else {
			this.canPlaceTower = true;
		}
		if(isSelectingTower) {
			if(upgradeMenu.getRemove()) {
				this.getUI().addUIElement(this.upgradeMenu);
			}
			upgradeMenu.updateRelationships();
			upgradeMenu.updateText();
		} else {
			this.upgradeMenu.setRemove(true);
		}
	}
	@Override
	public void passMessage(String message) {
		Point moveVector = new Point();
		switch(message) {
		case "up":
			moveVector = new Point(0, -1);
			break;
		case "down":
			moveVector = new Point(0, 1);
			break;
		case "left":
			moveVector = new Point(-1, 0);
			break;
		case "right":
			moveVector = new Point(1, 0);
			break;
		case "upgradeButtonClicked":
			//placing of tower is in TowerUpgradeMenu
			this.setGridLoc(this.getMap().positionToGrid(this.upgradeMenu.getFinalLoc()));
			break;
		case "sellButtonClicked":
			//money is given back in TowerUpgradeMenu
			this.getMap().removeTowerAtGridPos(this.gridLoc);
			break;
		default:
			break;
		}
		this.setGridLoc(Point.add(this.gridLoc, moveVector));
	}
	public Tower getSelectedTower() {
		return this.selectedTower;
	}
	
	@Override
	public void onClick() {
		if(this.getUI().getMouseLoc().getX() >= this.getMap().getMapTopLeft().getX()
				&& this.getUI().getMouseLoc().getY() >= this.getMap().getMapTopLeft().getY()
				&& this.getUI().getMouseLoc().getX() <= this.getMap().getMapTopLeft().getX() + this.getMap().getMapDimensions().getX()
				&& this.getUI().getMouseLoc().getY() <= this.getMap().getMapTopLeft().getY() + this.getMap().getMapDimensions().getY()
				&& !(this.upgradeMenu.getUIBox().pointIsInHitbox(this.getUI().getMouseLoc()) && !this.upgradeMenu.getRemove())) {
		this.setGridLoc(this.getMap().positionToGrid(this.getUI().mouseLoc));
		}
	}
}
