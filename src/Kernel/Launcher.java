package Kernel;

import Enum.Mode;
import Enum.Topic;

public class Launcher {

	public static void main(String[] args) {
		Topic[] topic = {Topic.TOPIC_STEER, Topic.TOPIC_THROT};
		new Controleur(Mode.RABBIT, topic,
				"tcp://localhost:1883", "Simulator");
	}
}