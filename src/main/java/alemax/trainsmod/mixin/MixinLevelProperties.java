package alemax.trainsmod.mixin;

import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarkerHandler;
import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import alemax.trainsmod.util.TrackType;
import com.mojang.datafixers.DataFixer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(LevelProperties.class)
public class MixinLevelProperties {

    /*
    @Inject(at = @At("RETURN"), method = "<init>")
    private void onConstructed(CallbackInfo info) {
        TrackMarkerInstances.SERVER_HANDLER = new TrackMarkerHandler(0);
        System.out.println("Created new Handler");
    }
     */



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
    }


}
