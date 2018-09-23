package alemax.trainsmod.util;

import java.util.ArrayList;
import java.util.Collections;

import alemax.trainsmod.entities.EntityRailcar;

public class Train {
	
	public ArrayList<EntityRailcar> railcars = new ArrayList<>();
	
	public void revertMovingDirection() {
		if(railcars.size() > 0) {
			railcars.get(0).isLeadingTrain = false;
			for(EntityRailcar car : railcars) {
				car.revertMovingDirection();
			}
			Collections.reverse(railcars);
			railcars.get(0).isLeadingTrain = true;
		}
		
	}
	
	public void removeRailcar(EntityRailcar railcar) {
		
		railcars.remove(railcar);
		if(railcars.size() > 0) {
			calcAllPositions();
		}
		
	}
	
	public void calcAllPositions() {
		if(railcars.size() > 0) {
			for(int i = 1; i < railcars.size(); i++) {
				railcars.get(i).calcPositionInTrain();
			}
		}
	}
	
}
