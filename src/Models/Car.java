package Models;

import java.awt.Point;

/**
 * Modele du car 
 * Projet Cannon Ball
 * @author PELLOUX-PRAYER
 * @version 2
 * @date 31/03/15
 */
public class Car{
	/**
	 * l'id n'est pas utile pour le moment 
	 * il pourra par la suite identifier de façon unique les voitures
	 */
	private int _id;
	/**
	 * image associé à la voiture
	 */
	private String _image;
	/**
	 * Position de la voiture
	 */
	private Point _position;
	/**
	 * vitesse de la voiture 
	 */
	private int _vitesse = 0;
	/**
	 * angle de déplacement de la voiture
	 */
	private int _angle = 0;
	
	/**
	 * valeur de converstion de la vitesse
	 * 90 dans le programme C correspond à une vitesse de 0
	 */
	private static int regVitesse = 90;
	/**
	 * valeur de converstion de l'angle
	 * 90 dans le programme C correspond à un angle de 0
	 */
	private static int regAngle = 90;
	
	public Car(String image, Point point){
		_image = image;
		_position = point;
	}
	
	public Car(String image, Point point, int vitesse, int angle){
		_image = image;
		_position = point;
		_vitesse = vitesse;
		_angle = angle;
	}
	
	/**
	 * 
	 * @param p : le point à vérifier
	 * @return vrai si le point donnée en parametre est à une distance inférieur à 30 pixel 
	 */
	public boolean contains(Point p) {
		if(p.distance(get_position()) <= 60/2){
			return true;
		}
		return false;
	}
	
	public void avancer(){
		int de = 1;
		avancer(de, 0);
	}
	
	public void avancer(int angle, int de){
		set_position(new Point(_position.x - angle, _position.y - de));
	}
	
	public void reculer(){
		int de = 1; 
		avancer(-de, 0); 
	}
	
	public void reculer(int de, int angle){
		avancer(-de, angle);
	}
	
	public void tourner(String angle){
		this._angle = new Integer(angle)-regAngle;	
	}
	
	public void accelere(int de){
		set_vitesse(_vitesse + de);
	}
	
	public void ralentie(int de){
		set_vitesse(_vitesse - de);
	}
	
	public void stop(){
		while(_vitesse>0){
			_vitesse--;
		}
		set_vitesse(0);
	}

	public void set_direction(String string) {
		this._angle = new Integer(string)-regAngle;
		
	}
	
	public int get_id() {
		return _id;
	}

	public void set_id(int id) {
		this._id = id;
	}

	public String get_image() {
		return _image;
	}

	public void set_image(String image) {
		this._image = image;
	}

	public Point get_position() {
		return _position;
	}

	public void set_position(Point point) {
		this._position = point;
	}

	public int get_vitesse() {
		return _vitesse;
	}

	public void set_vitesse(String vitesse) {
		this._vitesse = new Integer(vitesse)-regVitesse;
	}
	
	public void set_vitesse(int vitesse) {
		this._vitesse = vitesse;
	}

	public int get_angle() {
		return _angle;
	}

	public void set_angle(int angle) {
		this._angle = angle;
	}
}
