package ui.towerUpgradeUI;

import org.newdawn.slick.Color;

import mechanic.Point;
import ui.Text;
import ui.TextFormat;
import ui.UI;
import ui.UIElement;
/*
 * DESCRIPTION
 * 
 * the button you press for upgrading the tower
 */
public class UpgradeButton extends UIElement {
	static final Color COLOR_HOVERED_OVER = new Color(255, 255, 255);
	static final Color COLOR_NOT_HOVERED_OVER = new Color(210, 200, 220);
	static final float OPACITY_CAN_UPGRADE = 1.0f;
	static final float OPACITY_CAN_NOT_UPGRADE = 0.3f;
	public boolean mouseIsInside;
	Text text;
	int cost;
	public UpgradeButton(UI ui, Point loc) {
		super(ui, loc);
		this.setImage("res/ui/upgrade.png");
		this.setSize(1.6);
		this.setColor(COLOR_NOT_HOVERED_OVER);
		this.text = new Text(ui, new Point(-80, -5), 160, 8, 12, 10, 12, new Color(80, 80, 0), "upgrade text", TextFormat.CENTER_JUSTIFIED);
		this.addChild(text);
	}
	public void setCost(int cost) {
		this.cost = cost;
		this.text.setText("upgrade:" + cost);
	}
	@Override
	public void update() {
		this.mouseIsInside = this.pointIsInHitbox(this.getUI().getMouseLoc());
		if(this.cost <= this.getUI().getMoney()) {
			if(mouseIsInside){
				this.setColor(COLOR_HOVERED_OVER);
			} else {
				this.setColor(COLOR_NOT_HOVERED_OVER);
			}
			this.setAlpha(OPACITY_CAN_UPGRADE);
		} else {
			this.setAlpha(OPACITY_CAN_NOT_UPGRADE);
			this.setColor(COLOR_NOT_HOVERED_OVER);
		}
	}
	@Override
	public void onClick() {
		if(this.pointIsInHitbox(this.getUI().getMouseLoc())) {
			this.onClickedOn();
		}
	}
	private void onClickedOn() {
		this.getParent().passMessage("upgradeButtonClicked");
	}
}
