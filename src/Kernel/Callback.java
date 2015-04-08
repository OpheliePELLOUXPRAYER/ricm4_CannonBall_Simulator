package Kernel;

import java.util.concurrent.Semaphore;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;


/**
 * Callback
 * Projet Cannon Ball
 * Cette classe permet d'informer le client de la r�ception d'un message 
 * @author PELLOUX-PRAYER
 * @version 2
 * @date 31/03/15
 */
public class Callback implements MqttCallback {

	/**
	 * Dernier message re�u
	 */
	String _message;
	
	/**
	 * Dernier Topic re�u
	 */
	String _topic;
	
	/**
	 * Boolean informant sur l'arriv� d'un message 
	 */
	static boolean _messageArrived = false;
	
	/**
	 * boolean laissant passer le premier message 
	 * puis restant � vrai pour les suivant 
	 * permant de mettre en place un ordre fifo
	 */
	boolean nfirst = false;
	/**
	 * semaphore permettant de gerer l'interbloquage 
	 */
	static Semaphore sem = new Semaphore(0);

	/**
	 * Constructeur du callback
	 * @param message : la valeur par defaut du message courant
	 */
	public Callback(String message) {
		super();
		_message = message;
	}

	public void connectionLost(Throwable throwable) {
		System.out.println("Connection lost!");
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub

	}

	/**
	 * Informe sur l'arriv� d'un message nouveau message
	 * @return
	 */
	public boolean isMessageArrived() {
		return _messageArrived;
	}
	
	/**
	 * Traite l'arriv� d'un message 
	 */
	public synchronized void messageArrived(String topic, MqttMessage message)
			throws Exception {
		if (nfirst) {
			sem.acquire();
		} else {
			nfirst = true;
		}
		/*
		System.out.println("-------------------------------------------------");
		System.out.println("| Topic:" + topic);
		System.out.println("| Message: " + new String(message.getPayload()));
		System.out.println("-------------------------------------------------");
		*/
		System.out.println(topic + " : " + message);
		set_message(message); // modifie le message courant
		set_topic(topic); // modifie le topic courant
		_messageArrived = true; // indique qu'il y a nouveau message
	}

	public void set_topic(String topic) {
		_topic = topic;
	}

	public String get_topic() {
		return _topic;
	}
	
	public void set_message(MqttMessage message) {
		_message = new String(message.getPayload());
	}

	public String get_message() {
		sem.release(); // libr�re le prochain message
		_messageArrived = false; // indique que le callback est pr�t � recevoir un nouveau message 
		return _message;
	}

}