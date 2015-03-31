package Models;

import java.awt.Point;

/**
 * 
 * @author PELLOUX-PRAYER
 *
 */
public class QRcode{
	
	private int _value;
	private Point _position;
	
	public QRcode(int value, Point p) {
		_value = value;
		_position = p;
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

	public boolean contains(Point p) {
		if(p.distance(get_position()) <= 60/2){
			return true;
		}
		return false;
	}
}
