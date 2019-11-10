package alemax.trainsmod.networking;

import alemax.trainsmod.global.trackmarker.TrackMarkerHandler;
import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class PacketS2CSyncGlobalOnPlayerJoin extends TMPacket {


    public PacketS2CSyncGlobalOnPlayerJoin() {
        super("sync_global_on_player_join");
    }

    public void send(int count, ServerPlayerEntity player) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        //buf.writeInt(count);
        player.networkHandler.sendPacket(new CustomPayloadS2CPacket(this.identifier, buf));
    }

    @Override
    public void register() {
        ClientSidePacketRegistry.INSTANCE.register(this.identifier, (packetContext, packetByteBuf) -> {
            System.out.println("SEND");
            int count = packetByteBuf.readInt();
            packetContext.getTaskQueue().execute(() -> {
                //TrackMarkerInstances.OVERWORLD = new TrackMarkerHandler(count);
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
