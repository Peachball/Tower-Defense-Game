package ui;

import org.newdawn.slick.Color;

import mechanic.Point;
/*
 * DESCRIPTION
 * 
 * The text that displays the money
 */
public class MoneyText extends Text {
	public MoneyText(UI ui, Point loc, int boxWidth) {
		super(ui, loc, boxWidth, 6, 12, 7, 14, Color.white, "money: ", TextFormat.RIGHT_JUSTIFIED);
	}
	@Override
	public void update() {
		this.setText("money: " + this.getUI().getMoney());
	}
}
