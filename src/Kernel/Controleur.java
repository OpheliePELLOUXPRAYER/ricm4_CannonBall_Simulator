package Kernel;

import java.awt.Point;
import java.awt.image.SampleModel;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import Enum.Mode;
import Enum.Topic;
import Models.Car;
import Models.QRcode;
import Views.View;

public class Controleur {
	private ArrayList<Car> _cars;
	private ArrayList<QRcode> _qrs;
	private Timer t;
	private Mode _mode;

	private MqttClient client;
	
	private Callback callback;

	public Controleur(Mode mode, Topic[] topic, String broker, String clientId) {
		int qos = 0;
		MemoryPersistence persistence = new MemoryPersistence();
		callback = new Callback("Idle");

		t = new Timer();

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				ordonnanceur();
			}
		};
		t.schedule(task, 1000);

		try {
			client = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);

			System.out.println("Connecting to broker: " + broker);
			client.connect(connOpts);
			System.out.println("Connected");
			client.setCallback(callback);

			int lenght = topic.length;
			for (int i = 0; i < lenght; i++) {
				Topic tmp = topic[i];
				client.subscribe(tmp.toString(tmp), qos);
			}

			// ----------------------------------------------------------------//
			_cars = new ArrayList<Car>();
			_cars.add(new Car("car.png", new Point(230, 450), 0));
			_qrs = new ArrayList<QRcode>();

			if (mode == Mode.RABBIT) {
				// _qrs.add(new QRcode("rabbit.png", new Point(230,400)));
			}

			AppGameContainer container;
			try {
				container = new AppGameContainer(new View("Simulator", this));
				container.setDisplayMode(500, 500, false);
				container.setShowFPS(false);
				// container.setAlwaysRender(true);
				container.start();
			} catch (SlickException e) {
				e.printStackTrace();
			}

			// ----------------------------------------------------------------//
			client.disconnect();
			System.out.println("Disconnected");

			System.exit(0);
		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
	}

	public ArrayList<Car> get_cars() {
		return _cars;
	}

	public void set_cars(ArrayList<Car> _cars) {
		this._cars = _cars;
	}

	public ArrayList<QRcode> get_qrs() {
		return _qrs;
	}

	public void set_qrs(ArrayList<QRcode> _qrs) {
		this._qrs = _qrs;
	}

	public Mode get_mode() {
		return _mode;
	}

	public void set_mode(Mode _mode) {
		this._mode = _mode;
	}

	/**
	 * Ordonnanceur : Fonction controlant l'execution des actions du jeu
	 */
	public void readData() {
		String message;
		if ((message = callback.get_message()) != "Idle") {
			_cars.get(0).set_vitesse(message);
		}
		while (!callback.isMessageArrived())
			;
		if ((message = callback.get_message()) != "Idle") {
			_cars.get(0).set_direction(message);
		}
	}

	public void ordonnanceur() {
		TimerTask task = new TimerTask() {
			public void run() {
				if (callback.isMessageArrived()) {
					readData();
				}
				/*
				 * int pos = _qrs.get(0).get_position().x -
				 * _cars.get(0).get_angle(); int vit =
				 * _qrs.get(0).get_position().y - _cars.get(0).get_vitesse();
				 * _qrs.get(0).set_position(new Point(pos, vit));
				 */
				_cars.get(0).avancer(_cars.get(0).get_angle(),
						_cars.get(0).get_vitesse());
			}
		};
		t.scheduleAtFixedRate(task, 0, 1000);
	}

	public int getQrByPosition(int i, int j) {
		Point p = new Point(i, j);
		for (int k = 0; k < _qrs.size(); k++) {
			if (_qrs.get(k).contains(p)) {
				return k;
			}
		}
		return -1;
	}

	public int getCarByPosition(int i, int j) {
		Point p = new Point(i, j);
		for (int k = 0; k < _cars.size(); k++) {
			if (_cars.get(k).contains(p)) {
				return k;
			}
		}
		return -1;
	}

	public void addQR(String image,  int i, int j) {
		int k = this.get_qrs().size();
		try {
			/* envoie k, i et j : "k:i,j" */
			String tmp = String.valueOf(k) + ":" + String.valueOf(i) + "," + String.valueOf(j); 
	        MqttMessage coord = new MqttMessage(tmp.getBytes());
			client.publish(Topic.toString(Topic.TOPIC_ADD), coord);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		get_qrs().add(new QRcode(image, new Point(i, j)));
	}

	public void removeQR(int k) {
		Point point = get_qrs().get(k).get_position();
		try {
			String tmp = String.valueOf(k) + ":" + String.valueOf(point.x) + "," + String.valueOf(point.y); 
	        MqttMessage coord = new MqttMessage(tmp.getBytes());
			client.publish(Topic.toString(Topic.TOPIC_DEL), coord);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		get_qrs().remove(k);
	}

	public void moveQR(int _idQRmove, int i, int j) {
		get_qrs().get(_idQRmove).set_position(new Point(i,j));
		try {
			String tmp = String.valueOf(_idQRmove) + ":" + String.valueOf(i) + "," + String.valueOf(j); 
	        MqttMessage coord = new MqttMessage(tmp.getBytes());
			client.publish(Topic.toString(Topic.TOPIC_MOV), coord);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
