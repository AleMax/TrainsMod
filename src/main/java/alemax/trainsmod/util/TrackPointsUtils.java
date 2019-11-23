package alemax.trainsmod.util;

import alemax.trainsmod.global.tracknetwork.TrackNetworkInstances;
import alemax.trainsmod.global.tracknetwork.TrackPoint;

import java.util.List;

public class TrackPointsUtils {

    public static TrackPoint matchID(List<TrackPoint> list, int id) {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getUniqueID() == id)
                return list.get(i);
        }
        return null;
    }

}
