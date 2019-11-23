package alemax.trainsmod.mixin;

import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarkerHandler;
import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import alemax.trainsmod.global.tracknetwork.*;
import alemax.trainsmod.util.TrackType;
import com.mojang.datafixers.DataFixer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelProperties.class)
public class MixinLevelProperties {


    @Inject(at = @At("RETURN"), method = "<init>")
    private void onConstructed(CallbackInfo info) {
        if(TrackMarkerInstances.OVERWORLD == null) {
            TrackMarkerInstances.OVERWORLD = new TrackMarkerHandler();
            TrackNetworkInstances.OVERWORLD = new TrackNetwork();
        }


    }

    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/world/level/LevelInfo;Ljava/lang/String;)V")
    private void onConstructed(LevelInfo levelInfo_1, String string_1, CallbackInfo info) {
        if(TrackMarkerInstances.OVERWORLD == null) {
            TrackMarkerInstances.OVERWORLD = new TrackMarkerHandler();
            TrackNetworkInstances.OVERWORLD = new TrackNetwork();
        }

    }



    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/nbt/CompoundTag;Lcom/mojang/datafixers/DataFixer;ILnet/minecraft/nbt/CompoundTag;)V")
    private void onConstructed(CompoundTag compoundTag_1, DataFixer dataFixer_1, int int_1, CompoundTag compoundTag_2, CallbackInfo info) {
        TrackMarkerInstances.OVERWORLD = new TrackMarkerHandler();
        int count = compoundTag_1.getInt("trackMarkerCount");
        for(int i = 0; i < count; i++) {
            CompoundTag compoundTag = compoundTag_1.getCompound("trackMarker_" + i);
            BlockPos pos = new BlockPos(compoundTag.getInt("posX"), compoundTag.getInt("posY"), compoundTag.getInt("posZ"));
            TrackMarker marker = new TrackMarker(pos);
            marker.channel = compoundTag.getString("channel");
            marker.angle = compoundTag.getFloat("angle");
            marker.height = compoundTag.getByte("height");
            marker.trackType = TrackType.values[compoundTag.getByte("trackType")];
            TrackMarkerInstances.OVERWORLD.trackMarkers.add(marker);
        }

        TrackNetworkInstances.OVERWORLD = new TrackNetwork();
        count = compoundTag_1.getInt("trackPointCount");
        for(int i = 0; i < count; i++) {
            CompoundTag compoundTag = compoundTag_1.getCompound("trackPoint_" + i);
            Vec3d pos = new Vec3d(compoundTag.getDouble("posX"), compoundTag.getDouble("posY"), compoundTag.getDouble("posZ"));
            int uniqueID = compoundTag.getInt("id");
            String type = compoundTag.getString("type");

            if(type.equals("standard")) {
                TrackPointStandard point = new TrackPointStandard(pos, uniqueID);
                TrackNetworkInstances.OVERWORLD.addTrackPoint(point);
            } else if(type.equals("end")) {
                TrackPointEnd point = new TrackPointEnd(pos, uniqueID);
                TrackNetworkInstances.OVERWORLD.addTrackPoint(point);
            }
        }

        for(int i = 0; i < count; i++) {
            CompoundTag compoundTag = compoundTag_1.getCompound("trackPoint_" + i);
            String type = compoundTag.getString("type");

            if(type.equals("standard")) {
                TrackPointStandard point = (TrackPointStandard) TrackNetworkInstances.OVERWORLD.trackPoints.get(i);
                int previous = compoundTag.getInt("previous");
                int next = compoundTag.getInt("next");

                if(previous > 0) {
                    point.setPrevious(matchID(previous));
                } else {
                    //TODO: Throw some Exception some day
                }
                if(next > 0) {
                    point.setNext(matchID(next));
                } else {
                    //TODO: Throw some Exception some day
                }

            } else if(type.equals("end")) {
                TrackPointEnd point = (TrackPointEnd) TrackNetworkInstances.OVERWORLD.trackPoints.get(i);
                int previous = compoundTag.getInt("previous");
                int next = compoundTag.getInt("next");

                if(previous > 0) {
                    point.setPrevious(matchID(previous));
                } else {
                    //TODO: Throw some Exception some day
                }
                if(next > 0) {
                    point.setNext((TrackPointEnd) matchID(next));
                } else {
                    point.setNext(null);
                }
            }
        }

    }

    private TrackPoint matchID(int id) {
        for(int i = 0; i < TrackNetworkInstances.OVERWORLD.trackPoints.size(); i++) {
            if(TrackNetworkInstances.OVERWORLD.trackPoints.get(i).getUniqueID() == id)
                return TrackNetworkInstances.OVERWORLD.trackPoints.get(i);
        }
        return null;
    }

    @Inject(at = @At("RETURN"), method = "updateProperties")
    private void updateProperties(CompoundTag compoundTag_1, CompoundTag compoundTag_2, CallbackInfo info) {
        compoundTag_1.putInt("trackMarkerCount", TrackMarkerInstances.OVERWORLD.trackMarkers.size());
        for(int i = 0; i < TrackMarkerInstances.OVERWORLD.trackMarkers.size(); i++) {
            TrackMarker marker = TrackMarkerInstances.OVERWORLD.trackMarkers.get(i);
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putInt("posX", marker.getPos().getX());
            compoundTag.putInt("posY", marker.getPos().getY());
            compoundTag.putInt("posZ", marker.getPos().getZ());
            compoundTag.putString("channel", marker.channel);
            compoundTag.putFloat("angle", marker.angle);
            compoundTag.putByte("height", marker.height);
            compoundTag.putByte("trackType", (byte) marker.trackType.ordinal());
            compoundTag_1.put("trackMarker_" + i, compoundTag);
        }

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
                compoundTag.putInt("previous", ((TrackPointStandard) point).getPrevious().getUniqueID());
                if(((TrackPointStandard) point).getNext() != null)
                    compoundTag.putInt("next", ((TrackPointStandard) point).getNext().getUniqueID());
                else
                    compoundTag.putInt("next", 0);
            }

            compoundTag_1.put("trackPoint_" + i, compoundTag);
        }

    }


}
