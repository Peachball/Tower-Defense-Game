package ui.towerUpgradeUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import mechanic.DamageType;
import mechanic.Point;
import towers.StandardTower;
import towers.Tower;
import towers.FrostTower;
import towers.ShockTower;
import ui.GridSelect;
import ui.RangeFinder;
import ui.Text;
import ui.TextFormat;
import ui.UI;
import ui.UIBox;
import ui.UIBoxOrigin;
import ui.UIElement;
/*
 * DESCRIPTION
 * 
 * the upgrade menu when the gridselector is hovering over a tower
 */
public class TowerUpgradeMenu extends UIElement {
	static final int BOX_WIDTH = 325;
	static final int RIGHT_COLUMN_WIDTH = 80;
	static final int BOX_OFFSET = 20;
	static final int EDGE_MARGIN = 4;
	static final int BUTTON_MARGIN = 6;
	static final Color NORMAL_TEXT_COLOR = Color.white;
	static final Color UPGRADE_TEXT_COLOR = new Color(255, 255, 220);
	static final float BOX_ALPHA = 0.75f;
	GridSelect gridSelector;
	String type;
	UIBox uibox;
	String leftTooltip;
	String rightTooltip;
	String rightUpgradedTooltip;
	Text leftText;
	Text rightText;
	Tower selectedTower;
	boolean isMaxLevel;
	int upgradeCost;
	UpgradeButton upgradeButton;
	SellButton sellButton;
	RangeFinder rangeFinder;
	RangeFinder upgradedRangeFinder;
	public TowerUpgradeMenu(UI ui, Point loc, GridSelect gridSelector) {
		super(ui, loc);
		this.gridSelector = gridSelector;
		/*
		switch(type) {
		case StandardTower.ID:
			leftT = "STANDARD TOWER LEVEL "
				+ "|"
				+ "|DAMAGE TYPE: "
				+ "|DAMAGE: "
				+ "|RANGE: "
				+ "|ATTACK SPEED: "
				+ "|BASE ATTACK TIME: ";
			rightT
		}
		*/
		uibox = new UIBox(this.getUI(), new Point(0, -BOX_OFFSET), 200, 200, true, UIBoxOrigin.BOTTOM_CENTER);
		uibox.setAlpha(BOX_ALPHA);
		uibox.setParent(this);
		rangeFinder = new RangeFinder(ui, new Point(), 0, DamageType.IMPACT, isMaxLevel);
		rangeFinder.setParent(this);
		upgradedRangeFinder = new RangeFinder(ui, new Point(), 0, DamageType.IMPACT, isMaxLevel);
		upgradedRangeFinder.setParent(this);
		leftText = new Text(this.getUI(), new Point(), BOX_WIDTH - RIGHT_COLUMN_WIDTH, 8, 12, 11, 22, Color.white, "", TextFormat.LEFT_JUSTIFIED);
		leftText.setUseOutline(true);
		leftText.setParent(this);
		rightText = new Text(this.getUI(), new Point(), BOX_WIDTH, 8, 12, 9, 22, new Color(240, 240, 240), "", TextFormat.RIGHT_JUSTIFIED);
		rightText.setUseOutline(true);
		//rightText.setOutlineColor(Color.white);
		rightText.setParent(this);
		upgradeButton = new UpgradeButton(this.getUI(), new Point());
		upgradeButton.setParent(this);
		sellButton = new SellButton(this.getUI(), new Point());
		sellButton.setParent(this);
	}
	public void updateText() {
		this.selectedTower = gridSelector.getSelectedTower();
		this.type = this.selectedTower.getTowerID();
		this.clearText();
		int level = this.selectedTower.getLevel();
		
		this.leftTooltip = this.type.toUpperCase() + " TOWER"
						+ "|LEVEL: " + level
						+ "|";
		this.rightTooltip = "||current stats:";
		this.rightUpgradedTooltip = "||level " + (level + 1) + " stats:";
		
		isMaxLevel = level == this.selectedTower.getMaxLevel();
		if(!isMaxLevel) {
			this.upgradeCost = this.selectedTower.getCostForNextLevel();
		}
		//ATTRIBUTES////////////////////////////////////////////////////////////
		switch(this.type) {
		case StandardTower.ID:
			addAttribute("- DAMAGE", StandardTower.ATTACK_DAMAGE, level, isMaxLevel);
			addAttribute("- RANGE", StandardTower.ATTACK_RANGE, level, isMaxLevel);
			addAttribute("- ATTACK SPEED", StandardTower.ATTACK_SPEED, level, isMaxLevel);
			addAttribute("- BASE ATTACK TIME", StandardTower.BASE_ATTACK_TIME, level, isMaxLevel);
			break;
		case FrostTower.ID:
			addAttributePercentDifferenceInverted("- SLOW", FrostTower.FROST_SPEED_MODIFIER, level, isMaxLevel);
			addAttribute("- DAMAGE PER SECOND", FrostTower.FROST_DAMAGE_PER_SECOND, level, isMaxLevel);
			addAttribute("- DURATION", FrostTower.FROST_DURATION, level, isMaxLevel);
			addAttribute("- RANGE", FrostTower.ATTACK_RANGE, level, isMaxLevel);
			addAttribute("- ATTACK SPEED", FrostTower.BASE_ATTACK_SPEED, level, isMaxLevel);
			addAttribute("- BASE ATTACK TIME", FrostTower.BASE_ATTACK_TIME, level, isMaxLevel);
			break;
		case ShockTower.ID:
			addAttribute("- DAMAGE", ShockTower.ATTACK_DAMAGE, level, isMaxLevel);
			addAttribute("- RANGE", ShockTower.ATTACK_RANGE, level, isMaxLevel);
			addAttribute("- ATTACK SPEED", ShockTower.BASE_ATTACK_SPEED, level, isMaxLevel);
			addAttribute("- BASE ATTACK TIME", ShockTower.BASE_ATTACK_TIME, level, isMaxLevel);
			addAttribute("- NUMBER OF TARGETS", ShockTower.NUMBER_OF_TARGETS, level, isMaxLevel);
			addAttributePercentDifferenceInverted("- DAMAGE REUDCTION PER BOUNCE", ShockTower.DAMAGE_MULTIPLIER_PER_BOUNCE, level, isMaxLevel);
			addAttribute("- DEBUFF DURATION", ShockTower.DEBUFF_DURATION, level, isMaxLevel);
			addAttributeWithPercentSignInverted("- DEBUFF IMPACT DAMAGE INCREASE", ShockTower.IMPACT_RESISTANCE_MODIFIER, level, isMaxLevel);
			break;
		}
		////////////////////////////////////////////////////////////////////////
		leftText.setText(this.leftTooltip);
		rightText.setText(this.rightTooltip);
		leftText.updateNumOfLines();
		
		for(UIElement u : this.getChildren()) {
			u.updateRelationships();
		}
		
		this.uibox.setOriginMethod(UIBoxOrigin.BOTTOM_CENTER);
		this.uibox.setWidth(BOX_WIDTH + EDGE_MARGIN * 2);
		this.uibox.setHeight(leftText.getHeight() + this.upgradeButton.getHeight() + this.sellButton.getHeight() + EDGE_MARGIN * 2 + BUTTON_MARGIN * 4);
		this.uibox.changeLoc(new Point(0, -BOX_OFFSET));
		if(this.uibox.getTopLeftLoc().getY() + this.uibox.getOrigin().getY() < 0) {
			this.uibox.setOriginMethod(UIBoxOrigin.TOP_CENTER);
			this.uibox.changeLoc(new Point(0, BOX_OFFSET));
		}
		this.uibox.fitOnScreen();
		leftText.changeLoc(Point.add(uibox.getTopLeftLoc(), new Point(EDGE_MARGIN, EDGE_MARGIN)));
		rightText.changeLoc(Point.add(uibox.getTopLeftLoc(), new Point(EDGE_MARGIN, EDGE_MARGIN)));
		upgradeButton.changeLoc(Point.add(uibox.getBottomRightLoc(), new Point(-BUTTON_MARGIN - upgradeButton.getWidth()/2, -BUTTON_MARGIN * 3 - upgradeButton.getHeight()/2 - sellButton.getHeight())));
		upgradeButton.setCost(this.upgradeCost);
		sellButton.changeLoc(Point.add(uibox.getBottomRightLoc(), new Point(-BUTTON_MARGIN - sellButton.getWidth()/2, -BUTTON_MARGIN - sellButton.getHeight()/2)));
		sellButton.setSellPrice(this.selectedTower.getSellPrice());
		rangeFinder.updateRangeFinder(this.selectedTower.getAttackRange(), this.selectedTower.getDamageType(), isMaxLevel);
		upgradedRangeFinder.updateRangeFinder(this.selectedTower.getAttackRangeForNextLevel(), this.selectedTower.getDamageType(), this.selectedTower.getLevel() + 1 == this.selectedTower.getMaxLevel());
	}
	@Override
	public void update() {
		if(!this.isMaxLevel) {
			if(this.upgradeButton.getRemove() && !this.getRemove()) {
				this.getUI().addUIElement(this.upgradeButton);
			}
		} else {
			this.upgradeButton.setRemove(true);
		}
		if(!this.isMaxLevel && this.upgradeButton.mouseIsInside /*&& this.getUI().getMoney() >= this.upgradeCost*/) {
			this.rightText.setText(rightUpgradedTooltip);
			this.rightText.setColor(UPGRADE_TEXT_COLOR);
			if(this.upgradedRangeFinder.getRemove()) {
				this.getUI().addUIElement(upgradedRangeFinder);
			}
		} else {
			this.rightText.setText(rightTooltip);
			this.rightText.setColor(NORMAL_TEXT_COLOR);
			upgradedRangeFinder.setRemove(true);
		}
	}
	@Override
	public void passMessage(String message){
		switch(message) {
		case "upgradeButtonClicked":
			if(this.getUI().getMoney() >= this.upgradeCost) {
				this.selectedTower.levelUp();
				this.getUI().changeMoney(-this.upgradeCost);
			}
			this.getParent().passMessage("upgradeButtonClicked");
			break;
		case "sellButtonClicked":
			this.getUI().changeMoney(this.selectedTower.getSellPrice());
			this.getParent().passMessage("sellButtonClicked");
		default:
			break;
		}
	}
	public void passGridSelector(GridSelect sel) {
		this.gridSelector = sel;
	}
	public void addAttribute(String attributeName, float value[], int level, boolean isMaxLevel) {
		if(attributeName.length() > this.leftText.getLettersPerLine()) {
			rightTooltip += "|";
			rightUpgradedTooltip += "|";
		}
		this.leftTooltip += "|" + attributeName + ":";
		if(attributeName.length() == this.leftText.getLettersPerLine()) { //FORMATTING
			leftTooltip += "|";
		}
		this.rightTooltip += "|" + value[level - 1];
		if(!isMaxLevel) {
			float change = (value[level] - value[level - 1]);
			String modifier = "";
			if(change != 0) {
				if(change > 0) {
					modifier = "+";
				}
				this.rightUpgradedTooltip += "|" + "(" + modifier + change + ") " + value[level];
			} else {
				this.rightUpgradedTooltip += "|" + value[level - 1];
			}
		}
	}
	public void addAttributeWithPercentSign(String attributeName, float value[], int level, boolean isMaxLevel) {
		if(attributeName.length() > this.leftText.getLettersPerLine()) {
			rightTooltip += "|";
			rightUpgradedTooltip += "|";
		}
		this.leftTooltip += "|" + attributeName + ":";
		if(attributeName.length() == this.leftText.getLettersPerLine()) { //FORMATTING
			leftTooltip += "|";
		}
		this.rightTooltip += "|" + value[level - 1] + "%";
		if(!isMaxLevel) {
			float change = (value[level] - value[level - 1]);
			String modifier = "";
			if(change != 0) {
				if(change > 0) {
					modifier = "+";
				}
				this.rightUpgradedTooltip += "|" + "(" + modifier + change + ") " + value[level] + "%";
			} else {
				this.rightUpgradedTooltip += "|" + value[level - 1] + "%";
			}
		}
	}
	public void addAttributeWithPercentSign(String attributeName, float value, int level, boolean isMaxLevel) {
		if(attributeName.length() >= this.leftText.getLettersPerLine()) {
			rightTooltip += "|";
			rightUpgradedTooltip += "|";
		}
		this.leftTooltip += "|" + attributeName + ":";
		if(attributeName.length() == this.leftText.getLettersPerLine()) { //FORMATTING
			leftTooltip += "|";
		}
		this.rightTooltip += "|" + value + "%";
		this.rightUpgradedTooltip += "|" + value + "%";
	}
	public void addAttributeWithPercentSignInverted(String attributeName, float value[], int level, boolean isMaxLevel) {
		if(attributeName.length() >= this.leftText.getLettersPerLine()) {
			rightTooltip += "|";
			rightUpgradedTooltip += "|";
		}
		this.leftTooltip += "|" + attributeName + ":";
		if(attributeName.length() == this.leftText.getLettersPerLine()) { //FORMATTING
			leftTooltip += "|";
		}
		this.rightTooltip += "|" + -value[level - 1] + "%";
		if(!isMaxLevel) {
			float change = (value[level] - value[level - 1]);
			String modifier = "+";
			if(change != 0) {
				if(change > 0) {
					modifier = "";
				}
				this.rightUpgradedTooltip += "|" + "(" + modifier + -change + ") " + -value[level] + "%";
			} else {
				this.rightUpgradedTooltip += "|" + -value[level - 1] + "%";
			}
		}
	}
	public void addAttributeWithPercentSignInverted(String attributeName, float value, int level, boolean isMaxLevel) {
		if(attributeName.length() >= this.leftText.getLettersPerLine()) {
			rightTooltip += "|";
			rightUpgradedTooltip += "|";
		}
		this.leftTooltip += "|" + attributeName + ":";
		if(attributeName.length() == this.leftText.getLettersPerLine()) { //FORMATTING
			leftTooltip += "|";
		}
		this.rightTooltip += "|" + -value + "%";
		this.rightUpgradedTooltip += "|" + -value + "%";
	}
	public void addAttributePercentDifference(String attributeName, float value[], int level, boolean isMaxLevel) {
		if(attributeName.length() >= this.leftText.getLettersPerLine()) {
			rightTooltip += "|";
			rightUpgradedTooltip += "|";
		}
		this.leftTooltip += "|" + attributeName + ":";
		if(attributeName.length() == this.leftText.getLettersPerLine()) { //FORMATTING
			leftTooltip += "|";
		}
		this.rightTooltip += "|" + (roundToDecimalPoint(100 * (double)value[level - 1], 1) - 100) + "%";
		if(!isMaxLevel) {
			double change = (value[level] - value[level - 1]);
			String modifier = "";
			if(change != 0) {
				if(change > 0) {
					modifier = "+";
				}
				this.rightUpgradedTooltip += "|" + "(" + modifier + roundToDecimalPoint(100 * (double)change, 1) + "%) " + (roundToDecimalPoint(100 * (double)value[level], 1) - 100) + "%";
			} else {
				this.rightUpgradedTooltip += "|" + (roundToDecimalPoint(100 * (double)value[level - 1], 1) - 100) + "%";
			}
		}
	}
	public void addAttributePercentDifference(String attributeName, float value, int level, boolean isMaxLevel) {
		if(attributeName.length() >= this.leftText.getLettersPerLine()) {
			rightTooltip += "|";
			rightUpgradedTooltip += "|";
		}
		this.leftTooltip += "|" + attributeName + ":";
		if(attributeName.length() == this.leftText.getLettersPerLine()) { //FORMATTING
			leftTooltip += "|";
		}
		this.rightTooltip += "|" + (roundToDecimalPoint(100 * (double)value, 1) - 100) + "%";
		this.rightUpgradedTooltip += "|" + (roundToDecimalPoint(100 * (double)value, 1) - 100) + "%";
	}
	public void addAttributePercentDifferenceInverted(String attributeName, float value[], int level, boolean isMaxLevel) {
		if(attributeName.length() >= this.leftText.getLettersPerLine()) {
			rightTooltip += "|";
			rightUpgradedTooltip += "|";
		}
		this.leftTooltip += "|" + attributeName + ":";
		if(attributeName.length() == this.leftText.getLettersPerLine()) { //FORMATTING
			leftTooltip += "|";
		}
		this.rightTooltip += "|" + -(roundToDecimalPoint(100 * (double)value[level - 1], 1) - 100) + "%";
		if(!isMaxLevel) {
			double change = (value[level] - value[level - 1]);
			String modifier = "";
			if(change != 0) {
				if(change < 0) {
					modifier = "+";
				}
				this.rightUpgradedTooltip += "|" + "(" + modifier + -roundToDecimalPoint(100 * (double)change, 1) + "%) " + -(roundToDecimalPoint(100 * (double)value[level], 1) - 100) + "%";
			} else {
				this.rightUpgradedTooltip += "|" + -(roundToDecimalPoint(100 * (double)value[level - 1], 1) - 100) + "%";
			}
		}
	}
	public void addAttributePercentDifferenceInverted(String attributeName, float value, int level, boolean isMaxLevel) {
		if(attributeName.length() >= this.leftText.getLettersPerLine()) {
			rightTooltip += "|";
			rightUpgradedTooltip += "|";
		}
		this.leftTooltip += "|" + attributeName + ":";
		if(attributeName.length() == this.leftText.getLettersPerLine()) { //FORMATTING
			leftTooltip += "|";
		}
		this.rightTooltip += "|" + -(roundToDecimalPoint(100 * (double)value, 1) - 100) + "%";
		this.rightUpgradedTooltip += "|" + -(roundToDecimalPoint(100 * (double)value, 1) - 100) + "%";
	}
	public void addAttribute(String attributeName, float value, int level, boolean isMaxLevel) {
		if(attributeName.length() >= this.leftText.getLettersPerLine()) {
			rightTooltip += "|";
			rightUpgradedTooltip += "|";
		}
		this.leftTooltip += "|" + attributeName + ":";
		if(attributeName.length() == this.leftText.getLettersPerLine()) { //FORMATTING
			leftTooltip += "|";
		}
		this.rightTooltip += "|" + value;
		this.rightUpgradedTooltip += "|" + value;
	}
	public void addAttribute(String attributeName, int value[], int level, boolean isMaxLevel) {
		if(attributeName.length() >= this.leftText.getLettersPerLine()) {
			rightTooltip += "|";
			rightUpgradedTooltip += "|";
		}
		this.leftTooltip += "|" + attributeName + ":";
		if(attributeName.length() == this.leftText.getLettersPerLine()) { //FORMATTING
			leftTooltip += "|";
		}
		this.rightTooltip += "|" + value[level - 1];
		if(!isMaxLevel) {
			int change = (value[level] - value[level - 1]);
			String modifier = "";
			if(change != 0) {
				if(change > 0) {
					modifier = "+";
				}
				this.rightUpgradedTooltip += "|" + "(" + modifier + change + ") " + value[level];
			} else {
				this.rightUpgradedTooltip += "|" + value[level - 1];
			}
		}
	}
	public void addAttribute(String attributeName, int value, int level, boolean isMaxLevel) {
		if(attributeName.length() >= this.leftText.getLettersPerLine()) {
			rightTooltip += "|";
			rightUpgradedTooltip += "|";
		}
		this.leftTooltip += "|" + attributeName + ":";
		if(attributeName.length() == this.leftText.getLettersPerLine()) { //FORMATTING
			leftTooltip += "|";
		}
		this.rightTooltip += "|" + value;
		this.rightUpgradedTooltip += "|" + value;
	}
	public void clearText() {
		this.leftTooltip = "";
		this.rightTooltip = "";
		this.rightUpgradedTooltip = "";
	}
	public UIBox getUIBox() {
		return this.uibox;
	}
	public static double roundToDecimalPoint(double input, int decimalPoints) {
		double magnitude = Math.pow(10, decimalPoints);
		double in = input * magnitude;
		return Math.round(in)/magnitude;
	}
}
