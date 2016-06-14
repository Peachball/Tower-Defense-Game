package ui;

import org.newdawn.slick.Color;

import mechanic.Point;
/*
 * DESCRIPTION
 * 
 * The text that displays the money
 */
public class LivesText extends Text {
	static final Color HEALTHY_COLOR = new Color(255, 255, 255);
	static final float HEALTHY_DAMAGED_THRESHOLD = 0.8f;
	static final Color DAMAGED_COLOR = new Color(255, 255, 140);
	static final float DAMAGED_DYING_THRESHOLD = 0.3f;
	static final Color DYING_COLOR = new Color(255, 140, 140);
	public LivesText(UI ui, Point loc, int boxWidth) {
		super(ui, loc, boxWidth, 6, 12, 7, 14, new Color(255, 255, 255), "lives: ", TextFormat.RIGHT_JUSTIFIED);
	}
	@Override
	public void update() {
		this.setText("lives: " + this.getUI().getLives());
		float ratio = (float)(this.getUI().getLives())/(float)(this.getUI().getMaxLives());
		if(ratio > HEALTHY_DAMAGED_THRESHOLD) {
			this.setColor(HEALTHY_COLOR);
		} else if(ratio > DAMAGED_DYING_THRESHOLD) {
			this.setColor(DAMAGED_COLOR);
		} else {
			this.setColor(DYING_COLOR);
		}
	}
}
