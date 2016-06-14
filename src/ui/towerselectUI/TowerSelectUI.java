package ui.towerselectUI;

import mechanic.Point;
import towers.FrostTower;
import towers.ShockTower;
import towers.StandardTower;
import ui.GridSelect;
import ui.UI;
import ui.UIElement;

/*
 * DESCRIPTION
 * 
 * the little thing at the bottom where you can select a tower
 * DIMENSIONS: 800x100
 */
public class TowerSelectUI extends UIElement {
	GridSelect gridSelector;
	TowerSelectTower standard, frost, shock;
	public TowerSelectUI(UI ui, GridSelect gridSelector) {
		super(ui,new Point());
		this.setImage("res/ui/TowerSelectUI.png");
		this.gridSelector = gridSelector;
		standard = new TowerSelectTower(this.getUI(), new Point(-350, -25), StandardTower.ID);
		standard.setParent(this);
		this.getUI().addUIElement(standard);
		frost = new TowerSelectTower(this.getUI(), new Point(-300, -25), FrostTower.ID);
		frost.setParent(this);
		this.getUI().addUIElement(frost);
		shock = new TowerSelectTower(this.getUI(), new Point(-250, -25), ShockTower.ID);
		shock.setParent(this);
		this.getUI().addUIElement(shock);
	}
	@Override
	public void onSetMap() {
		this.changeLoc(new Point(400, 750));
	}
	public void passGridSelector(GridSelect sel) {
		this.gridSelector = sel;
	}
	@Override
	public void onAddedToUI() {
		for(UIElement u : this.getChildren()) {
			this.getUI().bringToFront(u);
			if(u instanceof TowerSelectTower) {
				((TowerSelectTower) u).passGridSelector(this.gridSelector);
			}
		}
	}
}
