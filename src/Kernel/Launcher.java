package Kernel;

import Enum.Mode;
import Enum.Topic;

public class Launcher {

	public static void main(String[] args) {
			new Controleur(Mode.RABBIT, Topic.TOPIC_STEER, "tcp://localhost:1883", "Simulator");
	}
}