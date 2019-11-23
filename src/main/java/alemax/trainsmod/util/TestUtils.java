package alemax.trainsmod.util;

import alemax.trainsmod.global.tracknetwork.TrackNetworkInstances;
import alemax.trainsmod.global.tracknetwork.TrackPoint;
import alemax.trainsmod.global.tracknetwork.TrackPointEnd;
import alemax.trainsmod.global.tracknetwork.TrackPointStandard;
import net.minecraft.nbt.CompoundTag;

public class TestUtils {

    public static void saveTrackNetwork(CompoundTag compoundTag_1) {
        int uniqueInt = 1;

        for(int i = 0; i < TrackNetworkInstances.OVERWORLD.trackPoints.size(); i++) {
            TrackNetworkInstances.OVERWORLD.trackPoints.get(i).setUniqueID(uniqueInt++);
        }

        compoundTag_1.putInt("trackPointCount", TrackNetworkInstances.OVERWORLD.trackPoints.size());
        for(int i = 0; i < TrackNetworkInstances.OVERWORLD.trackPoints.size(); i++) {
            TrackPoint point = TrackNetworkInstances.OVERWORLD.trackPoints.get(i);
            CompoundTag compoundTag = new CompoundTag();

            compoundTag.putDouble("posX", point.getPos().x);
            compoundTag.putDouble("posY", point.getPos().y);
            compoundTag.putDouble("posZ", point.getPos().z);
            compoundTag.putInt("id", point.getUniqueID());

            if(point instanceof TrackPointStandard) {
                compoundTag.putString("type", "standard");
                compoundTag.putInt("previous", ((TrackPointStandard) point).getPrevious().getUniqueID());
                compoundTag.putInt("next", ((TrackPointStandard) point).getNext().getUniqueID());
            } else if(point instanceof TrackPointEnd) {
                compoundTag.putString("type", "end");
                compoundTag.putInt("previous", ((TrackPointEnd) point).getPrevious().getUniqueID());
                if(((TrackPointEnd) point).getNext() != null)
                    compoundTag.putInt("next", ((TrackPointEnd) point).getNext().getUniqueID());
                else
                    compoundTag.putInt("next", 0);
            }

            compoundTag_1.put("trackPoint_" + i, compoundTag);
        }
    }

}
