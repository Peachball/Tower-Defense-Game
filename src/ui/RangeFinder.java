package ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import mechanic.DamageType;
import mechanic.Point;

public class RangeFinder extends UIElement {
	static final float ROTATION_SPEED = -16f;
	Color edgeColor;
	public RangeFinder(UI ui, Point loc, float radius, DamageType damagetype, boolean isMaxLevel) {
		super(ui, loc);
		String imagePath = "res/ui/range32";
		boolean draw = true;
		switch(damagetype) {
		case IMPACT:
			imagePath += "impact";
			edgeColor = new Color(255, 255, 0);
			break;
		case PIERCE:
			imagePath += "pierce";
			edgeColor = new Color(255, 230, 20);
			break;
		case MAGIC:
			imagePath += "magic";
			edgeColor = new Color(0, 40, 255);
			break;
		case ABSOLUTE:
			imagePath += "absolute";
			edgeColor = new Color(255, 0, 0);
			break;
		default:
			draw = false;
			break;
		}
		if(isMaxLevel) {
			imagePath += "2";
		} else {
			imagePath += "1";
		}
		imagePath += ".png";
		if(draw) {
			this.setImage(imagePath);
		}
		this.setSize(radius/16);
	}
	public void updateRangeFinder(float radius, DamageType damagetype, boolean isMaxLevel) {
		String imagePath = "res/ui/range32";
		boolean draw = true;
		switch(damagetype) {
		case IMPACT:
			imagePath += "impact";
			edgeColor = new Color(255, 255, 0);
			break;
		case PIERCE:
			imagePath += "pierce";
			edgeColor = new Color(255, 230, 20);
			break;
		case MAGIC:
			imagePath += "magic";
			edgeColor = new Color(0, 40, 255);
			break;
		case ABSOLUTE:
			imagePath += "absolute";
			edgeColor = new Color(255, 0, 0);
			break;
		default:
			draw = false;
			break;
		}
		if(isMaxLevel) {
			imagePath += "2";
		} else {
			imagePath += "1";
		}
		imagePath += ".png";
		if(draw) {
			this.setImage(imagePath);
		}
		this.setSize(radius/16);
	}
	@Override
	public void update() {
		this.orientation += ROTATION_SPEED * this.getFrameTime();
	}
	@Override
	public void draw(Graphics g) {
		if(this.getImage() != null){
			Image endPic = this.getImage().getScaledCopy((float) getSize());
			endPic.rotate(-(float)orientation);
			float width = endPic.getWidth();
			float height = endPic.getHeight();
			g.drawImage(endPic, (float) this.getFinalLoc().getX() - width/2, (float) this.getFinalLoc().getY() - height/2);
			g.setColor(edgeColor);
			g.drawOval((float) this.getFinalLoc().getX() - width/2, (float) this.getFinalLoc().getY() - height/2, width, height);
			this.setWidth(width);
			this.setHeight(height);
		}
	}
}
