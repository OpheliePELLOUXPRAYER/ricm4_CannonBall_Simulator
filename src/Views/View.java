package Views;

import java.awt.Point;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Animation;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

import Kernel.Controleur;
import Models.QRcode;

public class View extends BasicGame{

	private Animation[][] _animation_cars;
	private Animation[][] _animation_qrs;
	private Controleur _ctrl;
	private int _nbQRcode = 0;
	private int _nbCars = 0;
	
	public View(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	
	public View(String title, Controleur c) {
		super(title);
		_ctrl = c;
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		SpriteSheet sprite = new SpriteSheet(_ctrl.get_cars().get(0).get_image(), 42, 42);
		SpriteSheet sprite_rabbit = new SpriteSheet("rabbit.png", 42, 42);
		int tAnim = 350;	// temps d'affichage de chaque frame en ms
		_nbCars = _ctrl.get_cars().size();
		_animation_cars = new Animation[_nbCars][1];
		_animation_qrs = new Animation[1][2];
		for(int i = 0; i<_nbCars; i++){
			for(int j = 0; j<_animation_cars.length; j++)
			{
				_animation_cars[i][j] = new Animation();
				_animation_cars[i][j].addFrame(sprite.getSprite(0, j), tAnim);
				_animation_cars[i][j].addFrame(sprite.getSprite(1, j), tAnim);
			}
		}		
		
		for(int k = 0; k < _animation_qrs[0].length; k++){
			_animation_qrs[0][k] = new Animation();
			_animation_qrs[0][k].addFrame(sprite_rabbit.getSprite(0, k), tAnim);
			_animation_qrs[0][k].addFrame(sprite_rabbit.getSprite(1, k), tAnim);
			_animation_qrs[0][k].addFrame(sprite_rabbit.getSprite(2, k), tAnim);
		}
		
		if(Mouse.isClipMouseCoordinatesToWindow()){
			Mouse.setClipMouseCoordinatesToWindow(false);
			//System.setProperty("org.lwjgl.input.Mouse.allowNegativeMouseCoords", "true");
		}
	}	
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input input = gc.getInput();
		if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
		{
			_ctrl.get_qrs().add(new QRcode("rabbit.png", new Point((Mouse.getEventX()-21) , ((-1*Mouse.getEventY())-24))));
		}
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		g.setColor(Color.white);
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		
		_nbQRcode = _ctrl.get_qrs().size();
		for(int i=0;i<_nbCars;i++){
			g.drawAnimation(_animation_cars[i][0], _ctrl.get_cars().get(i).get_position().x,_ctrl.get_cars().get(i).get_position().y);
		}	
		for(int i=0;i<_nbQRcode;i++){
			g.drawAnimation(_animation_qrs[0][0], _ctrl.get_qrs().get(i).get_position().x, _ctrl.get_qrs().get(i).get_position().y);	
		}
	}
}
