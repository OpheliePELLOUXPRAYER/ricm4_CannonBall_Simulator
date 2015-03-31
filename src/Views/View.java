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
	private Animation[] _animation_qrs;
	private Controleur _ctrl;
	private int _nbQRcode = 0;
	private int _nbCars = 0;
	private int _move = 0;
	private int _idQRmove = -1;
	private int _currentValueOfQR = 0;
	
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
		//SpriteSheet sprite_rabbit = new SpriteSheet("./images/rabbit.png", 42, 42);
		int tAnim = 350;	// temps d'affichage de chaque frame en ms
		_nbCars = _ctrl.get_cars().size();
		_animation_cars = new Animation[_nbCars][4];
		_animation_qrs = new Animation[4];
		for(int i = 0; i<_nbCars; i++){
			for(int j = 0; j<_animation_cars[i].length; j++)
			{
				_animation_cars[i][j] = new Animation();
				_animation_cars[i][j].addFrame(sprite.getSprite(0, j), tAnim);
				_animation_cars[i][j].addFrame(sprite.getSprite(1, j), tAnim);
			}
		}	
		
		for(int k = 0; k < 4; k++){
			_animation_qrs[k] = new Animation();
			_animation_qrs[k].addFrame(new SpriteSheet("./images/"+k+".png", 42, 42), tAnim);
		}
		/*for(int k = 0; k < _animation_qrs[0].length; k++){
			_animation_qrs[0][k] = new Animation();
			_animation_qrs[0][k].addFrame(sprite_rabbit.getSprite(0, k), tAnim);
			_animation_qrs[0][k].addFrame(sprite_rabbit.getSprite(1, k), tAnim);
			_animation_qrs[0][k].addFrame(sprite_rabbit.getSprite(2, k), tAnim);
		}
		*/
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
			int i = (Mouse.getEventX()-21);
			int j = ((-1*Mouse.getEventY())-21);
			if(_ctrl.getQrByPosition(i, j) == -1 && _ctrl.getCarByPosition(i, j) == -1){
				_ctrl.addQR(_currentValueOfQR, i, j);
			}
		}
		if(input.isMousePressed(Input.MOUSE_MIDDLE_BUTTON))
		{
			int i = (Mouse.getEventX()-21);
			int j = ((-1*Mouse.getEventY())-21);
			int k = 0;
			if((k = _ctrl.getQrByPosition(i, j)) > -1){
				_ctrl.removeQR(k);
			}		
		}
		if(input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)){
			int i;
			int j;
			int k = 0;
			switch(_move){
			case 0:
				i = (Mouse.getEventX()-21);
				j = ((-1*Mouse.getEventY())-21);
				if((k = _ctrl.getQrByPosition(i, j)) > -1){
					_ctrl.get_qrs().get(k).set_position(new Point(-1,-1));
					_idQRmove = k;
					_move = 1;
				}
				break;
			case 1 :
				i = (Mouse.getEventX()-21);
				j = ((-1*Mouse.getEventY())-21);
				_ctrl.get_qrs().get(_idQRmove).set_position(new Point(-1,-1));
				if((k = _ctrl.getQrByPosition(i, j)) == -1 && _ctrl.getCarByPosition(i, j) == -1){
					_ctrl.moveQR(_idQRmove, i, j);
					_move = 0;
					_idQRmove = -1;
				}
			}
		}
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		g.setColor(Color.white);
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		
		_nbQRcode = _ctrl.get_qrs().size();
		for(int i=0;i<_nbCars;i++){
			//System.out.println("Angle : " + _ctrl.get_cars().get(i).get_angle());
			_animation_cars[i][0].getCurrentFrame().setRotation((-2*_ctrl.get_cars().get(i).get_angle()));
			//System.out.println("Rotation : " + _animation_cars[i][0].getCurrentFrame().getRotation());
			g.drawAnimation(_animation_cars[i][0], _ctrl.get_cars().get(i).get_position().x,_ctrl.get_cars().get(i).get_position().y);
		}	
		
		if(_nbQRcode > 0 && _move == 1){
			int x = (Mouse.getEventX()-21);
			int y = ((-1*Mouse.getEventY())-21);
			_ctrl.get_qrs().get(_idQRmove).set_position(new Point(x,y));
		}
		
		for(int i=0;i<_nbQRcode;i++){
			g.drawAnimation(_animation_qrs[_ctrl.get_qrs().get(i).get_value()], _ctrl.get_qrs().get(i).get_position().x, _ctrl.get_qrs().get(i).get_position().y);		
		}
	}
	
	public void keyPressed(int key, char c){
		switch(key){
		case Input.KEY_A:
			_currentValueOfQR = 0;
			break;
		case Input.KEY_Z:
			_currentValueOfQR = 1;
			break;
		case Input.KEY_E:
			_currentValueOfQR = 2;
			break;
		case Input.KEY_R:
			_currentValueOfQR = 3;
			break;
		}
	}
	
}
