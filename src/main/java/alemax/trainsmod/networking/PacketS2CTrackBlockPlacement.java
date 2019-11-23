package alemax.trainsmod.networking;

import alemax.trainsmod.block.TMBlock;
import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import alemax.trainsmod.init.TMBlocks;
import alemax.trainsmod.util.TrackType;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class PacketS2CTrackBlockPlacement extends TMPacket {


    public PacketS2CTrackBlockPlacement() {
        super("track_block_placement");
    }

    public void send(MinecraftServer server, ArrayList<BlockPos> trackPositions) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeInt(trackPositions.size());
        for(int i = 0; i < trackPositions.size(); i++) {
            buf.writeBlockPos(trackPositions.get(i));
        }

        server.getPlayerManager().sendToAll(new CustomPayloadS2CPacket(this.identifier, buf));
    }

    @Override
    public void register() {
        ClientSidePacketRegistry.INSTANCE.register(this.identifier, (packetContext, buf) -> {
            int size = buf.readInt();
            ArrayList<BlockPos> trackPositions = new ArrayList<BlockPos>();
            for(int i = 0; i < size; i++) {
                trackPositions.add(buf.readBlockPos());
            }
            packetContext.getTaskQueue().execute(() -> {
                World world = packetContext.getPlayer().world;
                for(int i = 0; i < size; i++) {
                    world.setBlockState(trackPositions.get(i), TMBlocks.BLOCK_TRACK.getDefaultState());
                }

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
