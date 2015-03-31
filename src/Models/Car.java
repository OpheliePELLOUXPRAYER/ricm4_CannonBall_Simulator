package Models;

import java.awt.Point;


/**
 * A traduir en anglais
 * @author PELLOUX-PRAYER
 *
 */
public class Car{
	private int _id;
	private String _image;
	private Point _position;
	private int _vitesse = 90;
	private int _angle = 90;
	
	private static int regVitesse = 90;
	private static int regAngle = 90;
	
	public Car(){
		_image = "car.png";
		_position = new Point(0,0);
		_vitesse = 15;
		_angle = 0;
	}
	
	public Car(String image){
		_image = image;
		_position = new Point(0,0);
		_vitesse = 15;
		_angle = 0;
	}
	
	public Car(String image, Point point){
		_image = image;
		_position = point;
		_vitesse = 15;
		_angle = 0;
	}
	
	public Car(String image, Point point, int vitesse){
		_image = image;
		_position = point;
		_vitesse = vitesse;
		_angle = 0;
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

	public void avancer(){
		int de = 1; // a voir
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
	
	/**
	 * 
	 */
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

	public boolean contains(Point p) {
		if(p.distance(get_position()) <= 60/2){
			return true;
		}
		return false;
	}
}
