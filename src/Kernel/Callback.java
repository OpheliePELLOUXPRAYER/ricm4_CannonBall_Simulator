package Kernel;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Callback implements MqttCallback {
	
	String _message;
	
	public Callback(String message){
		super();
		_message = message;
	} 

	public void connectionLost(Throwable throwable) {
		System.out.println("Connection lost!");
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}
	
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("-------------------------------------------------");
		System.out.println("| Topic:" + topic);
		System.out.println("| Message: " + new String(message.getPayload()));
		System.out.println("-------------------------------------------------"); 
		set_message(message);
	}
	
	public void set_message(MqttMessage message){
		_message = new String(message.getPayload());
	}

	public String get_message() {
		return _message;
	}
	
}