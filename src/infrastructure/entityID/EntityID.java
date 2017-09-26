package infrastructure.entityID;

import java.util.UUID;

public class EntityID {
	private String ID;

	public EntityID(){
		this.ID=UUID.randomUUID().toString();
	}
	
	public  String getID() {
		return ID;
	}
	
	
}
