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

import Enum.Topic;
import Models.Car;
import Models.QRcode;
import Views.View;

/**
 * Controleur du simulateur 
 * Projet Cannon Ball
 * @author PELLOUX-PRAYER
 * @version 2
 * @date 31/03/15
 */
public class Controleur {
	/**
	 * Liste des voitures 
	 * pour le moment il n'y a qu'une seul voiture de gere 
	 * mis dans le cas d'un convois il faudra pouvoir g�rer plus d'une voiture
	 */
	private ArrayList<Car> _cars;
	
	/**
	 * Liste des QRcodes 
	 * Le simulateur gere un certain nombre de QRcode 
	 */
	private ArrayList<QRcode> _qrs;
	/**
	 * le timer de l'ordonnanceur 
	 * au cas d'un d�placement de plusieurs voitures c'est lui qui g�rera leurs d�placement 
	 * via un tourniquet par exemple
	 */
	private Timer t;

	/**
	 * Client de mqtt
	 */
	private MqttClient client;
	/**
	 * Quality of service 
	 * http://www.eclipse.org/paho/files/mqttdoc/Cclient/qos.html 
	 */
	int qos = 0; 
	
	/**
	 * Callback
	 * voir fichier callback.java pour plus d'information
	 */
	private Callback callback;

	/**
	 * Constructeur du Controleur 
	 * @param topic : les topic auquel le clien mosquitto subscrit 
	 * @param broker : l'adresse sur lequel le broker est lanc� 
	 * @param clientId : l'identifiant du client
	 */
	public Controleur(Topic[] topic, String broker, String clientId) {
		MemoryPersistence persistence = new MemoryPersistence(); // initialisation de la persistence
		callback = new Callback("Idle"); // creation du callback en initialisant le message courant � "Idle"

		// creation du timer
		t = new Timer();

		// lancement de l'ordonnanceur. 
		// il tourne toute les secondes sur lui m�me
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				ordonnanceur();
			}
		};
		t.schedule(task, 500);
		
		try {
			client = new MqttClient(broker, clientId, persistence); // initialisation du client sur l'adresse du broker avec l'id clientId et avec la persistence cr�er plus haut 
			MqttConnectOptions connOpts = new MqttConnectOptions(); // initilatilsation des options de connection
			connOpts.setCleanSession(true);

			System.out.println("Connecting to broker: " + broker);
			client.connect(connOpts);
			System.out.println("Connected");
			client.setCallback(callback); // met le callback personel pour le retour d'information du client 

			int lenght = topic.length;
			for (int i = 0; i < lenght; i++) {
				Topic tmp = topic[i];
				client.subscribe(Topic.toString(tmp), qos);// subscribe a chaque topic donne en parametre du constructeur 
			}

			// Partie concernant la mise en place des donn�es du simulateur (les listes et la vue)
			// ----------------------------------------------------------------//
			_cars = new ArrayList<Car>();
			// ajoute une voiture � la liste de voiture en lui donnant une position centr� en bas de la fenetre
			// et avec une image par defaut
			_cars.add(new Car("./images/car.png", new Point(230, 450)));
			// la liste est vite au lancement du programme
			_qrs = new ArrayList<QRcode>();

			// Partie concernant la vue
			AppGameContainer container;
			try {
				container = new AppGameContainer(new View("Simulator", this));
				container.setDisplayMode(500, 500, false);
				container.setShowFPS(false);
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
	
	/**
	 * ReadData
	 * si un message a �t� re�u et signal� par le CallBack 
	 * on recupere le message et s'il est diffr�rent de "Idle" on modifie les donn�e en cons�quence
	 */
	public void readData() {
		String message;
		if ((message = callback.get_message()) != "Idle") {
			_cars.get(0).set_vitesse(message); // on modifie la vitesse 
		}
		while (!callback.isMessageArrived()); // on attend de resevoir l'information du l'angle
		if ((message = callback.get_message()) != "Idle") {
			_cars.get(0).set_direction(message); // on modifie l'angle
		}
	}
	
	/**
	 * Ordonnanceur
	 */
	public void ordonnanceur() {
		TimerTask task = new TimerTask() {
			public void run() {
				if (callback.isMessageArrived()) {
					readData();
				}
				System.out.println("alive");
				_cars.get(0).avancer(_cars.get(0).get_angle(),_cars.get(0).get_vitesse());
				Point p = _cars.get(0).get_position();
				coordVoit(p.x, p.y);
			}
		};
		t.scheduleAtFixedRate(task, 0, 500);
	}

	public void coordVoit(int i, int j) {
		try {
			String tmp = String.valueOf(i) + ":" + String.valueOf(j); 
	        MqttMessage coord = new MqttMessage(tmp.getBytes());
	        coord.setQos(2);
			client.publish(Topic.toString(Topic.TOPIC_CAR), coord);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * getQrByPosition
	 * @param i : coordonn�e x de la position
	 * @param j : coordonn�e y de la position
	 * @return -1 si la fonction n'a pas trouv� de QRcode � la position donn�e 
	 * k indice de la liste : si la fonction � trouv� un QRcode proche de cette position (proche � 30 pixels)
	 */
	public int getQrByPosition(int i, int j) {
		Point p = new Point(i, j);
		for (int k = 0; k < _qrs.size(); k++) {
			if (_qrs.get(k).contains(p)) {
				return k;
			}
		}
		return -1;
	}

	/**
	 * getQrByPosition
	 * @param i : coordonn�e x de la position
	 * @param j : coordonn�e y de la position
	 * @return -1 si la fonction n'a pas trouv� de Car � la position donn�e 
	 * k indice de la liste : si la fonction � trouv� un Car proche de cette position (proche � 30 pixels)
	 */
	public int getCarByPosition(int i, int j) {
		Point p = new Point(i, j);
		for (int k = 0; k < _cars.size(); k++) {
			if (_cars.get(k).contains(p)) {
				return k;
			}
		}
		return -1;
	}

	/**
	 * Ajoute un QRcode � la position donn�e avec la valeur du QRcode donn�e
	 * @param value : valeur du QRcode
	 * @param i : x de la position
	 * @param j : y de la position
	 */
	public void addQR(int value,  int i, int j) {
		// Partie concernant l'envoi d'un formation au serveur mosquitto (plublish)
		//------------------------------------------------//
		int k = this.get_qrs().size();
		try {
			/* envoie k, i et j : "k:value:i,j" */
			String tmp = String.valueOf(k) + ":" + String.valueOf(value) + ":" + String.valueOf(i) + ":" + String.valueOf(j); 
	        MqttMessage coord = new MqttMessage(tmp.getBytes());
	        coord.setQos(2);
			client.publish(Topic.toString(Topic.TOPIC_ADD), coord);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
		//------------------------------------------------//
		// ajout du QRcode dans la liste 
		get_qrs().add(new QRcode(value, new Point(i, j)));
	}

	public void removeQR(int k) {
		// Partie concernant l'envoi d'un formation au serveur mosquitto (plublish)
		//------------------------------------------------//
		Point point = get_qrs().get(k).get_position();
		int value = get_qrs().get(k).get_value();
		try {
			String tmp = String.valueOf(k) + ":" + String.valueOf(value) + ":" + String.valueOf(point.x) + ":" + String.valueOf(point.y); 
	        MqttMessage coord = new MqttMessage(tmp.getBytes());
	        coord.setQos(2);
			client.publish(Topic.toString(Topic.TOPIC_DEL), coord);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
		//------------------------------------------------//
		// supression du QRcode de la liste
		get_qrs().remove(k);
	}

	public void moveQR(int k, int i, int j) {
		// Partie concernant l'envoi d'un formation au serveur mosquitto (plublish)
		//------------------------------------------------//
		int value = get_qrs().get(k).get_value();
		try {
			String tmp = String.valueOf(k) + ":" + String.valueOf(value) + ":" + String.valueOf(i) + ":" + String.valueOf(j); 
	        MqttMessage coord = new MqttMessage(tmp.getBytes());
	        coord.setQos(2);
			client.publish(Topic.toString(Topic.TOPIC_MOV), coord);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
		//------------------------------------------------//
		// changement de la position du QRcode
		get_qrs().get(k).set_position(new Point(i,j));
	}
}
