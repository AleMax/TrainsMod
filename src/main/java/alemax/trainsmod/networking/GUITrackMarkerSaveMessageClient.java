package alemax.trainsmod.networking;

import com.google.common.base.Charsets;

import alemax.trainsmod.blocks.tileentities.TileEntityTrackMarker;
import alemax.trainsmod.entities.EntityRailcar;
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

public class GUITrackMarkerSaveMessageClient implements IMessage {
	
	private String channel;
	private int channelLength;
	private float angle;
	private byte height;
	private TrackType trackType;
	private int posX;
	private int posY;
	private int posZ;
	
	public GUITrackMarkerSaveMessageClient() {}
	
    public GUITrackMarkerSaveMessageClient(String channel, float angle, byte height, TrackType trackType, int posX, int posY, int posZ) {
		this.channel = channel;
		this.channelLength = channel.length();
		this.angle = angle;
		this.height = height;
		this.trackType = trackType;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(channelLength);
		for(int i = 0; i < channelLength; i++) {
			buf.writeChar(channel.charAt(i));
		}
		buf.writeFloat(angle);
		buf.writeByte(height);
		buf.writeByte((byte) trackType.ordinal());
		buf.writeInt(posX);
		buf.writeInt(posY);
		buf.writeInt(posZ);
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.channelLength = buf.readInt();
		this.channel = "";
		for(int i = 0; i < this.channelLength; i++) {
			this.channel += buf.readChar();
		}
		this.angle = buf.readFloat();
		this.height = buf.readByte();
		this.trackType = TrackType.values[buf.readByte()];
		this.posX = buf.readInt();
		this.posY = buf.readInt();
		this.posZ = buf.readInt();
	}

	public static class GUITrackMarkerSaveMessageClientHandler implements IMessageHandler<GUITrackMarkerSaveMessageClient, IMessage> {

		@Override
		public IMessage onMessage(GUITrackMarkerSaveMessageClient message, MessageContext ctx) {
			try {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					WorldClient world = Minecraft.getMinecraft().world;
				    TileEntity tileEntity = world.getTileEntity(new BlockPos(message.posX, message.posY, message.posZ));
				    if(tileEntity instanceof TileEntityTrackMarker) {
				    	((TileEntityTrackMarker) tileEntity).set(message.channel, message.angle, message.height, message.trackType);
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
