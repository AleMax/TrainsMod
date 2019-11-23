package alemax.trainsmod.networking;

import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarkerHandler;
import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import alemax.trainsmod.global.tracknetwork.*;
import alemax.trainsmod.util.TrackPointsUtil;
import alemax.trainsmod.util.TrackType;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.util.sat4j.core.Vec;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class PacketS2CSyncGlobalOnPlayerJoin extends TMPacket {


    public PacketS2CSyncGlobalOnPlayerJoin() {
        super("sync_global_on_player_join");
    }

    public void send(PlayerEntity player, TrackMarkerHandler markerOverworld, TrackNetwork networkOverworld) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        //TRACKMARKERS:
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
        int uniqueInt = 1;

        for(int i = 0; i < TrackNetworkInstances.OVERWORLD.trackPoints.size(); i++) {
            networkOverworld.trackPoints.get(i).setUniqueID(uniqueInt++);
        }

        buf.writeInt(networkOverworld.trackPoints.size());
        for(int i = 0; i < markerOverworld.trackMarkers.size(); i++) {
            TrackPoint point = networkOverworld.trackPoints.get(i);
            buf.writeDouble(point.getPos().x);
            buf.writeDouble(point.getPos().y);
            buf.writeDouble(point.getPos().z);
            buf.writeInt(point.getUniqueID());

            if(point instanceof TrackPointStandard) {
                buf.writeEnumConstant(TrackPointType.STANDARD);
                buf.writeInt(((TrackPointStandard) point).getPrevious().getUniqueID());
                buf.writeInt(((TrackPointStandard) point).getNext().getUniqueID());
            } else if(point instanceof TrackPointEnd) {
                buf.writeEnumConstant(TrackPointType.END);
                buf.writeInt(((TrackPointEnd) point).getPrevious().getUniqueID());
                if(((TrackPointStandard) point).getNext() != null)
                    buf.writeInt(((TrackPointEnd) point).getNext().getUniqueID());
                else
                    buf.writeInt(0);

            }

        }


    }

    @Override
    public void register() {
        ClientSidePacketRegistry.INSTANCE.register(this.identifier, (packetContext, buf) -> {
            //TRACKMARKERS:
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

            //TRACKNETWORK:
            count = buf.readInt();
            List<TrackPoint> points = new ArrayList<TrackPoint>();

            int readerIndex = buf.readerIndex();
            for(int i = 0; i < count; i++) {
                Vec3d pos = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
                int uniqueID = buf.readInt();

                TrackPointType type = buf.readEnumConstant(TrackPointType.class);

                if(type == TrackPointType.STANDARD) {
                    TrackPointStandard point = new TrackPointStandard(pos, uniqueID);
                    points.add(point);
                    buf.readerIndex(buf.readerIndex() + 8); //Skip previous and next
                    //buf.readInt(); buf.readInt(); //Skip previous and next
                } else if(type == TrackPointType.END) {
                    TrackPointEnd point = new TrackPointEnd(pos, uniqueID);
                    TrackNetworkInstances.OVERWORLD.addTrackPoint(point);
                    buf.readerIndex(buf.readerIndex() + 8); //Skip previous and next
                    //buf.readInt(); buf.readInt(); //Skip previous and next
                }
            }

            buf.readerIndex(readerIndex);
            for(int i = 0; i < count; i++) {
                buf.readerIndex(buf.readerIndex() + 28); //Skips pos and uniqueID
                TrackPointType type = buf.readEnumConstant(TrackPointType.class);

                if(type == TrackPointType.STANDARD) {
                    TrackPointStandard point = (TrackPointStandard) points.get(i);
                    int previous = buf.readInt();
                    int next = buf.readInt();

                    if(previous > 0) {
                        point.setPrevious(TrackPointsUtil.matchID(points, previous));
                    } else {
                        //TODO: Throw some Exception some day
                    }
                    if(next > 0) {
                        point.setNext(TrackPointsUtil.matchID(points, next));
                    } else {
                        //TODO: Throw some Exception some day
                    }
                } else if(type == TrackPointType.END) {
                    TrackPointEnd point = (TrackPointEnd) points.get(i);
                    int previous = buf.readInt();
                    int next = buf.readInt();

                    if(previous > 0) {
                        point.setPrevious(TrackPointsUtil.matchID(points, previous));
                    } else {
                        //TODO: Throw some Exception some day
                    }
                    if(next > 0) {
                        point.setNext((TrackPointEnd) TrackPointsUtil.matchID(points, next));
                    } else {
                        point.setNext(null);
                    }

                }

            }

            packetContext.getTaskQueue().execute(() -> {
                TrackMarkerInstances.OVERWORLD = new TrackMarkerHandler();
                TrackMarkerInstances.OVERWORLD.trackMarkers = markers;

                TrackNetworkInstances.OVERWORLD = new TrackNetwork();
                TrackNetworkInstances.OVERWORLD.trackPoints = points;
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
