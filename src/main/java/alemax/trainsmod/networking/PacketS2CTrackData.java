package alemax.trainsmod.networking;

import alemax.trainsmod.global.tracknetwork.TrackNetworkInstances;
import alemax.trainsmod.global.tracknetwork.TrackPoint;
import alemax.trainsmod.global.tracknetwork.TrackPointEnd;
import alemax.trainsmod.global.tracknetwork.TrackPointStandard;
import alemax.trainsmod.init.TMBlocks;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.loader.util.sat4j.core.Vec;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;

public class PacketS2CTrackData extends TMPacket {


    public PacketS2CTrackData() {
        super("track_data");
    }

    public void send(MinecraftServer server, BlockPos mainPos, Vec3d[] points) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeBlockPos(mainPos);
        buf.writeInt(points.length);
        for(int i = 0; i < points.length; i++) {
            buf.writeDouble(points[i].x);
            buf.writeDouble(points[i].y);
            buf.writeDouble(points[i].z);
        }

        server.getPlayerManager().sendToAll(new CustomPayloadS2CPacket(this.identifier, buf));
    }

    @Override
    public void register() {
        ClientSidePacketRegistry.INSTANCE.register(this.identifier, (packetContext, buf) -> {
            BlockPos mainPos = buf.readBlockPos();
            int size = buf.readInt();
            Vec3d[] points = new Vec3d[size];
            for(int i = 0; i < points.length; i++) {
                points[i] = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
            }

            packetContext.getTaskQueue().execute(() -> {
                World world = packetContext.getPlayer().world;

                TrackPoint[] trackPoints = new TrackPoint[points.length];
                trackPoints[0] = new TrackPointEnd(points[0]);
                for(int i = 1; i < (trackPoints.length - 1); i++) {
                    trackPoints[i] = new TrackPointStandard(points[i]);
                }
                trackPoints[trackPoints.length - 1] = new TrackPointEnd(points[trackPoints.length - 1]);

                ((TrackPointEnd) trackPoints[0]).setPrevious(trackPoints[1]);
                for(int i = 1; i < (trackPoints.length - 1); i++) {
                    ((TrackPointStandard) trackPoints[i]).setPrevious(trackPoints[i - 1]);
                    ((TrackPointStandard) trackPoints[i]).setNext(trackPoints[i + 1]);
                }
                ((TrackPointEnd) trackPoints[trackPoints.length - 1]).setPrevious(trackPoints[trackPoints.length - 2]);

                for(int i = 0; i < trackPoints.length; i++) {
                    TrackNetworkInstances.OVERWORLD.addTrackPoint(trackPoints[i]);
                }

                world.setBlockState(mainPos, TMBlocks.BLOCK_TRACK_SUPER.getDefaultState());


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
