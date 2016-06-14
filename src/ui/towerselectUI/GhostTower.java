package ui.towerselectUI;

import org.newdawn.slick.Image;

import mechanic.DamageType;
import mechanic.Point;
import ui.RangeFinder;
import ui.UI;
import ui.UIElement;
/*
 * DESCRIPTION
 * 
 * a thing that shows up when you hover over a tower selector
 */
public class GhostTower extends UIElement {
	static final float OPACITY = 0.4f;
	RangeFinder rangeFinder;
	public GhostTower(UI ui, Point loc, Image image, float range, DamageType damagetype, boolean isMaxLevel) {
		super(ui, loc);
		this.setImage(image);
		this.setAlpha(OPACITY);
		rangeFinder = new RangeFinder(ui, new Point(), range, damagetype, isMaxLevel);
		this.addChild(rangeFinder);
	}
}
