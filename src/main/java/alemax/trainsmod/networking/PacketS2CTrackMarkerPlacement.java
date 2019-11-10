package alemax.trainsmod.networking;

import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import alemax.trainsmod.util.TrackType;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class PacketS2CTrackMarkerPlacement extends TMPacket {


    public PacketS2CTrackMarkerPlacement() {
        super("track_marker_placement");
    }

    public void send(MinecraftServer server, TrackMarker marker) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(marker.getPos());
        buf.writeString(marker.channel);
        buf.writeFloat(marker.angle);
        buf.writeByte(marker.height);
        buf.writeByte(marker.trackType.ordinal());

        server.getPlayerManager().sendToAll(new CustomPayloadS2CPacket(this.identifier, buf));
    }

    @Override
    public void register() {
        ClientSidePacketRegistry.INSTANCE.register(this.identifier, (packetContext, buf) -> {
            BlockPos pos = buf.readBlockPos();
            String channel = buf.readString();
            float angle = buf.readFloat();
            byte height = buf.readByte();
            TrackType type = TrackType.values[buf.readByte()];
            packetContext.getTaskQueue().execute(() -> {
                TrackMarkerInstances.OVERWORLD.removeTrackMarker(pos);
                TrackMarker trackMarker = new TrackMarker(pos);
                trackMarker.channel = channel;
                trackMarker.angle = angle;
                trackMarker.height = height;
                trackMarker.trackType = type;
                TrackMarkerInstances.OVERWORLD.addTrackMarker(trackMarker);
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
