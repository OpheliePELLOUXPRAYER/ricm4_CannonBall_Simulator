package Enum;

/**
 * Enum Topic 
 * Projet Cannon Ball
 * @author PELLOUX-PRAYER
 * @version 2
 * @date 31/03/15
 */
public enum Topic{
	TOPIC_STEER,	// vitesse
	TOPIC_THROT,	// angle
	TOPIC_ADD, 		// ajout d'un QRcode
	TOPIC_DEL, 		// supression d'un QRcode
	TOPIC_MOV; 		// deplacement d'un QRcode
	
	public static String toString(Topic t){
		switch(t){
			case TOPIC_STEER:
				return "simulator/steering";
			case TOPIC_THROT : 
				return "simulator/throttle";
			case TOPIC_ADD : 
				return "simulator/addqrcode";
			case TOPIC_DEL : 
				return "simulator/delqrcode";
			case TOPIC_MOV : 
				return "simulator/movqrcode";
			default:
				return "no topic";
		}
	}
}


