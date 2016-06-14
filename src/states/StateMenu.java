
package states;


import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import ui.StartButton;
import ui.Text;
import ui.TextFormat;
import ui.UI;
import ui.UIBox;
import ui.UIBoxOrigin;
import mechanic.Game;
import mechanic.Point;

public class StateMenu extends BasicGameState implements ComponentListener{
	
	private MouseOverArea start;
	private Image startButton;
	private Image gameTitle;
	private StateBasedGame game;
	private UI ui;
	private StartButton button;
	Text text;
	String string;
	
	public StateMenu(){

	}
	public void init(GameContainer container, StateBasedGame arg1){
		// TODO Auto-generated method stub
		try{
		startButton = new Image("res/StartButton.png");
		}
		catch (SlickException e){
			System.out.println("Didn't find startbutton???");
		}
		ui = new UI();
		button = new StartButton(ui, new Point(400, 400));
		string = "ARE YOU READY TO PLAY THE GAME???";
		text = new Text(ui, new Point(200, 500), 400, 4, 10, 10, 10, Color.white, string, TextFormat.CENTER_JUSTIFIED);
		text.setUseOutline(true);
		ui.addUIElement(button);
		ui.addUIElement(text);
	}
	

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
    	//start = new MouseOverArea(container,startButton,512, 364, 180,90, this);

    	this.game  = game;
    }

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		//start.render(container, g);
		g.drawRect(200, 500, 400, 300);
		ui.draw(g);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		// TODO Auto-generated method stub
		ui.passFrameTime((float)arg2 / 1000);
		ui.update();
		if(button.isPressed) {
			game.enterState(Game.STATE_GAME);
		}
		text.setScale((float)this.ui.getMouseLoc().getX()/100);
	}
	@Override
	public void mousePressed(int button, int x, int y) {
		ui.onClick();
		string += " i'll bash your head in i swear on me mum";
		text.setText(string);
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		ui.passMouseLoc(new Point(newx, newy));
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return Game.STATE_MENU;
	}


	@Override
	public void componentActivated(AbstractComponent com) {
		if (com == start ) {
			game.enterState(Game.STATE_GAME);
			
		}
	}

}