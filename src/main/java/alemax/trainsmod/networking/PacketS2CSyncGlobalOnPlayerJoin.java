package alemax.trainsmod.networking;

import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarkerHandler;
import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import alemax.trainsmod.global.tracknetwork.TrackNetwork;
import alemax.trainsmod.util.TrackType;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.util.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

public class PacketS2CSyncGlobalOnPlayerJoin extends TMPacket {


    public PacketS2CSyncGlobalOnPlayerJoin() {
        super("sync_global_on_player_join");
    }

    public void send(PlayerEntity player, TrackMarkerHandler markerOverworld, TrackNetwork networkOverworld) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        //TRACKMARKERS
        buf.writeInt(markerOverworld.trackMarkers.size());
        for(int i = 0; i < markerOverworld.trackMarkers.size(); i++) {
            TrackMarker marker = markerOverworld.trackMarkers.get(i);
            buf.writeBlockPos(marker.getPos());
            buf.writeString(marker.channel, TrackMarker.MAX_CHANNEL_LENGTH);
            buf.writeFloat(marker.angle);
            buf.writeByte(marker.height);
            //buf.writeByte(marker.trackType.ordinal());
            buf.writeEnumConstant(marker.trackType);
        }
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, new CustomPayloadS2CPacket(this.identifier, buf));

        //TRACKNETWORK:



    }

    @Override
    public void register() {
        ClientSidePacketRegistry.INSTANCE.register(this.identifier, (packetContext, buf) -> {
            int count = buf.readInt();
            List<TrackMarker> markers = new ArrayList<TrackMarker>();
            for(int i = 0; i < count; i++) {
                TrackMarker marker = new TrackMarker(buf.readBlockPos());
                marker.channel = buf.readString(TrackMarker.MAX_CHANNEL_LENGTH);
                marker.angle = buf.readFloat();
                marker.height = buf.readByte();
                //marker.trackType = TrackType.values[buf.readByte()];
                marker.trackType = buf.readEnumConstant(TrackType.class);
                markers.add(marker);
            }
            packetContext.getTaskQueue().execute(() -> {
                TrackMarkerInstances.OVERWORLD = new TrackMarkerHandler();
                TrackMarkerInstances.OVERWORLD.trackMarkers = markers;
            });

        });
    }

    //player.networkHandler.sendPacket(new CustomPayloadS2CPacket(ident, buf));
    //PlayerStream.around(world, pos, distance).forEach(x -> ((ServerPlayerEntity)x).networkHandler.sendPacket(new CustomPayloadS2CPacket(ident, buf)));
    //server.getPlayerManager().sendToAll(new CustomPayloadS2CPacket(ident, buf));
    //ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, new CustomPayloadS2CPacket(ident, buf));
    //ClientSidePacketRegistry.INSTANCE.sendToServer(new CustomPayloadC2SPacket(ident, buf));
    //MinecraftClient.getInstance().getNetworkHandler().getConnection().send(new CustomPayloadC2SPacket(ident, buf));
}
