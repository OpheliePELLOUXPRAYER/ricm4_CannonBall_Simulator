package Kernel;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
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

	private Callback callback;

	public Controleur(Mode mode, Topic topic, Topic topic2, String broker, String clientId) {
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
			MqttClient client = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);

			System.out.println("Connecting to broker: " + broker);
			client.connect(connOpts);
			System.out.println("Connected");
			client.setCallback(callback);

			client.subscribe(topic.toString(topic), qos);
			client.subscribe(topic.toString(topic2), qos);

			// ----------------------------------------------------------------//
			_cars = new ArrayList<Car>();
			_cars.add(new Car("car.png", new Point(230, 450), 0));
			_qrs = new ArrayList<QRcode>();
			
			if(mode == Mode.RABBIT){
				//_qrs.add(new QRcode("rabbit.png", new Point(230,400)));
			}

			AppGameContainer container;
			try {
				container = new AppGameContainer(new View("Simulator", this));
				container.setDisplayMode(500, 500, false);
				container.setShowFPS(false);
				//container.setAlwaysRender(true);
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
		while (!callback.isMessageArrived());
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
				/*int pos = _qrs.get(0).get_position().x - _cars.get(0).get_angle();
				int vit = _qrs.get(0).get_position().y - _cars.get(0).get_vitesse();
				_qrs.get(0).set_position(new Point(pos, vit));*/
				_cars.get(0).avancer(_cars.get(0).get_angle(),_cars.get(0).get_vitesse());
			}
		};
		t.scheduleAtFixedRate(task, 0, 1000);
	}
}
