package Models;

import java.awt.Point;

/**
 * A traduire en anglais
 * @author PELLOUX-PRAYER
 *
 */
public class QRcode{
	
	private String _image;
	private int _id;
	private int _distance;
	private Point _position;
	
	public QRcode(String image, Point p) {
		_image = image;
		_position = p;
	}

	public String get_image() {
		return _image;
	}

	public void set_image(String image) {
		this._image = image;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int get_distance() {
		return _distance;
	}

	public void set_distance(int _distance) {
		this._distance = _distance;
	}

	public Point get_position() {
		return _position;
	}

	public void set_position(Point position) {
		this._position = position;
	}
	
	
}
