package Kernel;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPublishSample {

    public static void main(String[] args) {
    	/*
    	String defaultSpeed	= "90";
    	String defaultAngle	= "90";
    	int defaultTime		= -1;
    	*/

        String topic        = "simulator/throttle"; // propulsion
        String topic2       = "simulator/steering"; // direction
        String speed      	= "94";
        String angle 		= "91";
        int time			= 5000;
        int qos             = 2;
        String broker       = "tcp://localhost:1883";
        String clientId     = "pub";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);            
            System.out.println("Connected");

            System.out.println("Publishing speed: "+speed);
            MqttMessage message = new MqttMessage(speed.getBytes());
            System.out.println("Publishing angle: "+angle);
            MqttMessage message2 = new MqttMessage(angle.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            message2.setQos(qos);
            sampleClient.publish(topic2, message2);
            
            Thread.sleep(time);
            
            speed = "91";
            angle = "85";
            
            
            System.out.println("Publishing speed: "+speed);
            message = new MqttMessage(speed.getBytes());
            System.out.println("Publishing angle: "+angle);
            message2 = new MqttMessage(angle.getBytes());
            System.out.println("Publishing time: "+time);
            sampleClient.publish(topic, message);
            message2.setQos(qos);
            sampleClient.publish(topic2, message2);
            
            System.out.println("Message published");
            sampleClient.disconnect();
            System.out.println("Disconnected");
            
            System.exit(0);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
