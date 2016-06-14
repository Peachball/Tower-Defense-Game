package slickGameTesting;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame {
	Map<String, Image> images = new HashMap<String, Image>();
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Game("ayy lmao"));
		app.setDisplayMode(1200, 600, false);
		app.start();
	}

	public Game(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		Image i = new Image("res/lenny face.png");
		g.drawImage(i, 0.0f, 0.0f);
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		// TODO Auto-generated method stub
		Image i = new Image("res/lenny face.png");
		images.put("lenny face", i);
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException {
		// TODO Auto-generated method stub
		
	}
	
}
