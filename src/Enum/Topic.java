package Enum;

public enum Topic{
	TOPIC_STEER,
	TOPIC_THROT,
	TOPIC_ADD,
	TOPIC_DEL;
	
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
			default:
				return "no topic";
		}
	}
}


