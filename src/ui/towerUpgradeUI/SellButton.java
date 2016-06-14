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
public class SellButton extends UIElement {
	public boolean mouseIsInside;
	static final Color COLOR_HOVERED_OVER = new Color(255, 255, 255);
	static final Color COLOR_NOT_HOVERED_OVER = new Color(210, 200, 220);
	Text text;
	int sellprice;
	public SellButton(UI ui, Point loc) {
		super(ui, loc);
		this.setImage("res/ui/sell.png");
		this.setSize(1.6);
		this.setColor(COLOR_NOT_HOVERED_OVER);
		this.text = new Text(ui, new Point(-80, -5), 160, 8, 12, 10, 12, new Color(120, 10, 10), "sell text", TextFormat.CENTER_JUSTIFIED);
		this.addChild(text);
	}
	public void setSellPrice(int price) {
		this.sellprice = price;
		this.text.setText("sell:" + price);
	}
	@Override
	public void update() {
		this.mouseIsInside = this.pointIsInHitbox(this.getUI().getMouseLoc());
		if(mouseIsInside){
			this.setColor(COLOR_HOVERED_OVER);
		} else {
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
		this.getParent().passMessage("sellButtonClicked");
	}
}
