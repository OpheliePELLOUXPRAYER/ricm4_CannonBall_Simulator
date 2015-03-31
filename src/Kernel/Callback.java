package Kernel;

import java.util.concurrent.Semaphore;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Callback implements MqttCallback {

	String _message;
	String _topic;
	static boolean _messageArrived = false;
	boolean nfirst = false;
	static Semaphore sem = new Semaphore(0);

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

	public boolean isMessageArrived() {
		return _messageArrived;
	}

	public synchronized void messageArrived(String topic, MqttMessage message)
			throws Exception {
		if (nfirst) {
			sem.acquire();
		} else {
			nfirst = true;
		}
		System.out.println("-------------------------------------------------");
		System.out.println("| Topic:" + topic);
		System.out.println("| Message: " + new String(message.getPayload()));
		System.out.println("-------------------------------------------------");
		set_message(message);
		set_topic(topic);
		_messageArrived = true;
		System.out.println(message);
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
		sem.release();
		_messageArrived = false;
		return _message;
	}

}