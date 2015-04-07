package Kernel;

import Enum.Topic;

/**
 * Lancer du simulateur
 * Projet Cannon Ball
 * @author PELLOUX-PRAYER
 * @version 2
 * @date 31/03/15
 */
public class Launcher {

	// Il risque d'avoir des problemes avec la librerie lwjGL
	// le programme a �tait programm�e en utilisant la version 2.9.2
	// il ce trouve que cette version n'ai peut �tre pas totalement compatible avec les nouvelles versions de windows
	// dans tout les cas il faut, pour r�gl� le programme de native :
	// ajouter dans les natives du jar le lien vers les natives d�sir� (windows sur windows, linux sur linux ..)
	// pour ce faire aller dans Propri�t� du projet 
	// java build path 
	// d�rouler l'onglet concernant lwjGL et modifi� la native location
	public static void main(String[] args) {
		Topic[] topic = {
					Topic.TOPIC_STEER, 
					Topic.TOPIC_THROT
				};
		// Creation du controleur du simulateur
		//new Controleur(topic, "tcp://mmammar.ddns.net:1884", "Simulator");
		new Controleur(topic, "tcp://192.168.43.105:1884", "Simulator");
		//new Controleur(topic, "tcp://5.196.18.82:80", "Simulator");
	}
}
