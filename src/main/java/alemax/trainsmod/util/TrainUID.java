package alemax.trainsmod.util;

import java.util.ArrayList;
import java.util.Random;

public class TrainUID {
	
	private static ArrayList<Long> trainUIDs = new ArrayList<>();
	private static Random rdm = new Random();
	
	public static void addID(Long arg0) {
		trainUIDs.add(arg0);
	}
	
	public static long generateID() {
		boolean found = false;
		long id;
		do {
			id = rdm.nextLong();
			for(int i = 0; i < trainUIDs.size(); i++) {
				if(id == trainUIDs.get(i)) {
					found = true;
					break;
				}
						
			}
		} while(found);
		trainUIDs.add(id);
		return id;
	}
	
	public static void removeID(long id) {
		trainUIDs.remove(id);
	}
	
	
}
