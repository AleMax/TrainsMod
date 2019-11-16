package alemax.trainsmod.networking;

import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import alemax.trainsmod.util.TrackType;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class PacketS2CSaveGUITrackMarker extends TMPacket {


    public PacketS2CSaveGUITrackMarker() {
        super("save_gui_track_marker_to_client");
    }

    public void send(MinecraftServer server, BlockPos pos, String channel, float angle, byte height, TrackType type) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeBlockPos(pos);
        buf.writeString(channel, TrackMarker.MAX_CHANNEL_LENGTH);
        buf.writeFloat(angle);
        buf.writeByte(height);
        //buf.writeByte(type.ordinal());
        buf.writeEnumConstant(type);

        server.getPlayerManager().sendToAll(new CustomPayloadS2CPacket(this.identifier, buf));
    }

    @Override
    public void register() {
        ClientSidePacketRegistry.INSTANCE.register(this.identifier, (packetContext, buf) -> {
            BlockPos pos = buf.readBlockPos();
            String channel = buf.readString(TrackMarker.MAX_CHANNEL_LENGTH);
            float angle = buf.readFloat();
            byte height = buf.readByte();
            //TrackType type = TrackType.values[buf.readByte()];
            TrackType type = buf.readEnumConstant(TrackType.class);
            packetContext.getTaskQueue().execute(() -> {
                TrackMarker marker = TrackMarkerInstances.OVERWORLD.getTrackMarker(pos);
                marker.channel = channel;
                marker.angle = angle;
                marker.height = height;
                marker.trackType = type;

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
