package alemax.trainsmod.networking;

import java.util.List;

import javax.vecmath.Vector3d;

import com.google.common.base.Charsets;

import alemax.trainsmod.blocks.tileentities.TileEntityTrack;
import alemax.trainsmod.blocks.tileentities.TileEntityTrackMarker;
import alemax.trainsmod.entities.EntityRailcar;
import alemax.trainsmod.util.TrackData;
import alemax.trainsmod.util.TrackType;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TrackDataMessage implements IMessage {
	
	private BlockPos pos;
	private TrackData data;
	
	public TrackDataMessage() {}
	
    public TrackDataMessage(BlockPos pos, TrackData data) {
		this.pos = pos;
		this.data = data;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(data.trackPoints.length); 
		for(int i = 0; i < data.trackPoints.length; i++) {
			buf.writeDouble(data.trackPoints[i].x);
			buf.writeDouble(data.trackPoints[i].y);
			buf.writeDouble(data.trackPoints[i].z);
		}
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		int length = buf.readInt();
		this.data = new TrackData();
		this.data.trackPoints = new Vector3d[length];
		for(int i = 0; i < length; i++) {
			this.data.trackPoints[i] = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		}
	}

	public static class TrackDataMessageHandler implements IMessageHandler<TrackDataMessage, IMessage> {

		@Override
		public IMessage onMessage(TrackDataMessage message, MessageContext ctx) {
			try {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					WorldClient world = Minecraft.getMinecraft().world;
					TileEntity te = world.getTileEntity(message.pos);
					if(te instanceof TileEntityTrack) {
						((TileEntityTrack) te).setTrackData(message.data);
						System.out.println("SYNCED TRACK DATA");
					}
			    });
				return null;
			} catch(Exception e) {
				System.out.println("EXCEPTION");
				return null;
			}
		}
		
	}
	
	
}
