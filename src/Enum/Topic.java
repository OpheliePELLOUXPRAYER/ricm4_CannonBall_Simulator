package Enum;

public enum Topic{
	TOPIC_STEER,
	TOPIC_THROT;
	
	public String toString(Topic t){
		switch(t){
			case TOPIC_STEER:
				return "metrics/steering";
			case TOPIC_THROT : 
				return "metrics/throttle";
			default:
				return "no topic";
		}
	}
}


