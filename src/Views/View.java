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

/**
 * Classe gérant la vue du simulateur  
 * Projet Cannon Ball
 * @author PELLOUX-PRAYER
 * @version 2
 * @date 31/03/15
 */
public class View extends BasicGame{

	/**
	 * Tableau d'animation pour les voitures
	 */
	private Animation[][] _animation_cars;
	/**
	 * Tableau d'animation pour les QRcode
	 * Pour le moment tableau à une dimension car une seul image est utilisé par type de QRcode
	 * Mais il nous devions mettre en place un déplacement automatique d'un QRcode il faudra ajouter une deuxième dimension 
	 */
	private Animation[] _animation_qrs;
	/**
	 * Le controleur
	 */
	private Controleur _ctrl;
	/**
	 * Nombre de QRcode
	 */
	private int _nbQRcode = 0;
	/**
	 * Nombre de voiture
	 */
	private int _nbCars = 0;
	/**
	 * inique le nombre de clique pour la gestion du move
	 */
	private int _move = 0;
	/**
	 * Id courant du QRcode en mouvement
	 * -1 s'il n'y à pas de QRcode en mouvement
	 */
	private int _idQRmove = -1;
	/**
	 * Valeur courante du QRcode 
	 * Il sera ajouté lors du clique un QRcode de cette valeur 
	 * Cette valeur peut être changé garce à certaine touche du clavier 
	 */
	private int _currentValueOfQR = 0;
	
	/**
	 * Constructeur 
	 * @param title de la vue
	 * @param c : constructeur de la vue
	 */
	public View(String title, Controleur c) {
		super(title);
		_ctrl = c;
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		SpriteSheet sprite = new SpriteSheet(_ctrl.get_cars().get(0).get_image(), 42, 42);
		int tAnim = 350;	// temps d'affichage de chaque frame en ms
		_nbCars = _ctrl.get_cars().size();
		_animation_cars = new Animation[_nbCars][4];
		_animation_qrs = new Animation[4];
		for(int i = 0; i<_nbCars; i++){
			for(int j = 0; j<_animation_cars[i].length; j++)
			{
				_animation_cars[i][j] = new Animation(); // creation de l'animation 
				_animation_cars[i][j].addFrame(sprite.getSprite(0, j), tAnim); // ajout des frame intermedaire 
				_animation_cars[i][j].addFrame(sprite.getSprite(1, j), tAnim); // 2 pour la voiture mais cela peut être plus 
			}
		}	
		
		for(int k = 0; k < 4; k++){
			_animation_qrs[k] = new Animation();
			_animation_qrs[k].addFrame(new SpriteSheet("./images/"+k+".png", 42, 42), tAnim);
		}
		
		if(Mouse.isClipMouseCoordinatesToWindow()){
			// permet de prendre en compte le déplacement de la sourie
			Mouse.setClipMouseCoordinatesToWindow(false);
		}
	}	
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input input = gc.getInput(); // récupération de l'input du GameContainer
		if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) // si le bouton de gauche à été pressé
		{
			int x = (Mouse.getEventX()-21);  // on récupère la position de x lors de l'evenement et on soustrer la taille de l'image par 2 (ici on a une image de 42 pixels)
			int y = ((-1*Mouse.getEventY())-21); // on récupère la position de y lors de l'evenement que l'on multiplie par -1 pour passer en valeur positive et on soustrer la taille de l'image par 2 (ici on a une image de 42 pixels)
			if(_ctrl.getQrByPosition(x, y) == -1 && _ctrl.getCarByPosition(x, y) == -1){ // on regarde si l'image est dans un emplacement vide 
				_ctrl.addQR(_currentValueOfQR, x, y);
			}
		}
		if(input.isMousePressed(Input.MOUSE_MIDDLE_BUTTON))
		{
			if(_move == 0){
				int x = (Mouse.getEventX()-21);
				int y = ((-1*Mouse.getEventY())-21);
				int k = 0;
				if((k = _ctrl.getQrByPosition(x, y)) > -1){ // on regarde si y a bien un QRcode à supprimer
					_ctrl.removeQR(k);
				}	
			}
		}
		if(input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)){
			int x;
			int y;
			int k = 0;
			switch(_move){
			case 0:
				x = (Mouse.getEventX()-21);
				y = ((-1*Mouse.getEventY())-21);
				if((k = _ctrl.getQrByPosition(x, y)) > -1){
					_idQRmove = k; // on memorise l'id du QRcode déplacé
					_move = 1; // on indique qu'il y a un clique 
				}
				break;
			case 1 :
				x = (Mouse.getEventX()-21);
				y = ((-1*Mouse.getEventY())-21);
				_ctrl.get_qrs().get(_idQRmove).set_position(new Point(-1,-1)); // on met la position du QRcode à -1 -1 pour qu'il puisse être posé
				if((k = _ctrl.getQrByPosition(x, y)) == -1 && _ctrl.getCarByPosition(x, y) == -1){
					_ctrl.moveQR(_idQRmove, x, y);// on place le QRcode et on indique qu'il y a eu un move
					_move = 0; // on reviens à 0 pour le nombre de clique 
					_idQRmove = -1; // on indique qu'il n'y à plus de QRcode en mouvement
				}
			}
		}
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.setColor(Color.white); // on change la couleur 
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight()); // et on place un fond blanc
		
		_nbQRcode = _ctrl.get_qrs().size();
		for(int i=0;i<_nbCars;i++){
			//System.out.println("Angle : " + _ctrl.get_cars().get(i).get_angle());
			_animation_cars[i][0].getCurrentFrame().setRotation((-2*_ctrl.get_cars().get(i).get_angle()));
			//System.out.println("Rotation : " + _animation_cars[i][0].getCurrentFrame().getRotation());
			g.drawAnimation(_animation_cars[i][0], _ctrl.get_cars().get(i).get_position().x,_ctrl.get_cars().get(i).get_position().y);
		}	
		
		// On regarde si un QRcode est en déplacement
		if(_move == 1){
			int x = (Mouse.getEventX()-21);
			int y = ((-1*Mouse.getEventY())-21);
			//_ctrl.get_qrs().get(_idQRmove).set_position(new Point(x,y));
			// on change la position du QRcode concerné par le move pour qu'il suive la sourie
			_ctrl.moveQR(_idQRmove, x, y);
		}
		
		for(int i=0;i<_nbQRcode;i++){
			g.drawAnimation(_animation_qrs[_ctrl.get_qrs().get(i).get_value()], _ctrl.get_qrs().get(i).get_position().x, _ctrl.get_qrs().get(i).get_position().y);		
		}
	}
	
	/**
	 * permet de changer la valeur courante de QRcode
	 */
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
