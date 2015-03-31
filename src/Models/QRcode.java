package Models;

import java.awt.Point;

/**
 * Modele du QRcode 
 * Projet Cannon Ball
 * @author PELLOUX-PRAYER
 * @version 2
 * @date 31/03/15
 */
public class QRcode{
	
	/**
	 * Valeur du QRcode
	 */
	private int _value;
	/**
	 * Position du QRcode
	 */
	private Point _position;
	
	/**
	 * Constructeur 
	 * @param value : la valeur du QRcode
	 * @param p : la position initial
	 */
	public QRcode(int value, Point p) {
		_value = value;
		_position = p;
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
	
	public int get_value() {
		return _value;
	}

	public void set_value(int value) {
		this._value = value;
	}

	public Point get_position() {
		return _position;
	}

	public void set_position(Point position) {
		this._position = position;
	}
}
