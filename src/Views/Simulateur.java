package Views;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import Kernel.Controleur;

public class Simulateur extends BasicGameState{
	
	private Animation[][] _animation;
	private Controleur _ctrl;
	
	public Simulateur(Controleur c) {
		_ctrl = c;
	}

	public void init(GameContainer gc, StateBasedGame sg)
			throws SlickException {
		SpriteSheet sprite = new SpriteSheet(_ctrl.get_cars().get(0).get_image(), 42, 42);
		SpriteSheet sprite_rabbit = new SpriteSheet(_ctrl.get_qrs().get(0).get_image(), 42, 42);
		int tAnim = 350;	// temps d'affichage de chaque frame en ms
		_animation = new Animation[2][5];		// 1 animation pour chaque direction, donc 4 par perso
		for(int j = 0; j<_animation.length; j++)
		{
			_animation[0][j] = new Animation();
			_animation[0][j].addFrame(sprite.getSprite(0, j), tAnim);
			_animation[0][j].addFrame(sprite.getSprite(1, j), tAnim);
			_animation[1][j] = new Animation();
			_animation[1][j].addFrame(sprite_rabbit.getSprite(0, j), tAnim);
			_animation[1][j].addFrame(sprite_rabbit.getSprite(1, j), tAnim);
			_animation[1][j].addFrame(sprite_rabbit.getSprite(2, j), tAnim);
		}
	}

	public void render(GameContainer gc, StateBasedGame sg, Graphics gr)
			throws SlickException {
		// TODO Auto-generated method stub
		gr.setColor(Color.white); // FOND BLEU FONCE
		gr.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		gr.drawAnimation(_animation[0][0], _ctrl.get_cars().get(0).get_position().x,_ctrl.get_cars().get(0).get_position().y);
		gr.drawAnimation(_animation[1][0], _ctrl.get_qrs().get(0).get_position().x, _ctrl.get_qrs().get(0).get_position().y);
	}

	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		// TODO Auto-generated method stub		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}


}
