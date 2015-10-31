package states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import mechanic.Game;
import mechanic.GameInfo;

public class StateEnd extends BasicGameState {
	Image loseIcon;
	Image winIcon;

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		loseIcon = new Image("res/bsloseicon.png");
		winIcon = new Image("res/bswinicon.png");
	}

	@Override
	public void render(GameContainer container, StateBasedGame arg1, Graphics g) throws SlickException {
		switch(GameInfo.won){
		case 1:
			g.drawImage(winIcon, 0, 0);
			break;
		case 2:
			g.drawImage(loseIcon, 0, 0);
			break;
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame state, int arg2) throws SlickException {
		Input input = container.getInput();
		if(input.isMousePressed(input.MOUSE_LEFT_BUTTON)){
			state.enterState(Game.STATE_MENU);
		}
	}

	@Override
	public int getID() {
		return Game.STATE_END;
	}

}
