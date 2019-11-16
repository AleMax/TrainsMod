package alemax.trainsmod.networking;

import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarkerHandler;
import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import alemax.trainsmod.gui.GUITrackMarker;
import alemax.trainsmod.init.TMPackets;
import alemax.trainsmod.util.TrackType;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class PacketC2SSaveGUITrackMarker extends TMPacket {


    public PacketC2SSaveGUITrackMarker() {
        super("save_gui_track_marker_to_server");
    }

    public void send(BlockPos pos, String channel, float angle, byte height, TrackType type) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeBlockPos(pos);
        buf.writeString(channel, TrackMarker.MAX_CHANNEL_LENGTH);
        buf.writeFloat(angle);
        buf.writeByte(height);
        //buf.writeByte(type.ordinal());
        buf.writeEnumConstant(type);

        ClientSidePacketRegistry.INSTANCE.sendToServer(new CustomPayloadC2SPacket(this.identifier, buf));
    }

    @Override
    public void register() {
        ServerSidePacketRegistry.INSTANCE.register(this.identifier, (packetContext, buf) -> {
            BlockPos pos = buf.readBlockPos();
            String channel = buf.readString(TrackMarker.MAX_CHANNEL_LENGTH);
            float angle = buf.readFloat();
            byte height = buf.readByte();
            //TrackType type = TrackType.values[buf.readByte()];
            TrackType type = buf.readEnumConstant(TrackType.class);

            MinecraftServer server = packetContext.getPlayer().getServer();

            packetContext.getTaskQueue().execute(() -> {
                TrackMarker marker = TrackMarkerInstances.OVERWORLD.getTrackMarker(pos);
                marker.channel = channel;
                marker.angle = angle;
                marker.height = height;
                marker.trackType = type;

                TMPackets.packetS2CSaveGUITrackMarker.send(server, pos, channel, angle, height, type);
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
