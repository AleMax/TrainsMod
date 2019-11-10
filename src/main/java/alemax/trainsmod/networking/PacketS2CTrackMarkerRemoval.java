package alemax.trainsmod.networking;

import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import alemax.trainsmod.util.TrackType;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class PacketS2CTrackMarkerRemoval extends TMPacket {


    public PacketS2CTrackMarkerRemoval() {
        super("track_marker_removal");
    }

    public void send(MinecraftServer server, BlockPos pos) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(pos);

        server.getPlayerManager().sendToAll(new CustomPayloadS2CPacket(this.identifier, buf));
    }

    @Override
    public void register() {
        ClientSidePacketRegistry.INSTANCE.register(this.identifier, (packetContext, buf) -> {
            BlockPos pos = buf.readBlockPos();
            packetContext.getTaskQueue().execute(() -> {
                TrackMarkerInstances.OVERWORLD.removeTrackMarker(pos);
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
