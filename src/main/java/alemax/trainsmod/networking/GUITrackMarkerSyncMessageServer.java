package alemax.trainsmod.networking;

import java.util.List;

import com.google.common.base.Charsets;

import alemax.trainsmod.blocks.tileentities.TileEntityTrackMarker;
import alemax.trainsmod.entities.EntityRailcar;
import alemax.trainsmod.util.TrackType;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GUITrackMarkerSyncMessageServer implements IMessage {
	
	private int posX;
	private int posY;
	private int posZ;
	private String channel;
	private int channelLength;
	private TrackType trackType;
	
	public GUITrackMarkerSyncMessageServer() {}
	
    public GUITrackMarkerSyncMessageServer(String channel, TrackType trackType) {
		this.channel = channel;
		this.channelLength = channel.length();
		this.trackType = trackType;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(channelLength);
		for(int i = 0; i < channelLength; i++) {
			buf.writeChar(channel.charAt(i));
		}
		buf.writeByte((byte) trackType.ordinal());
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.channelLength = buf.readInt();
		this.channel = "";
		for(int i = 0; i < this.channelLength; i++) {
			this.channel += buf.readChar();
		}
		this.trackType = TrackType.values[buf.readByte()];
	}
	
	public static class GUITrackMarkerSyncMessageServerHandler implements IMessageHandler<GUITrackMarkerSyncMessageServer, IMessage> {

		@Override
		public IMessage onMessage(GUITrackMarkerSyncMessageServer message, MessageContext ctx) {
			try {
				
				EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
				WorldServer world = serverPlayer.getServerWorld();
				
				world.addScheduledTask(() -> {
					List<TileEntity> tileEntities = world.loadedTileEntityList;
					for(TileEntity te : tileEntities) {
						if(te instanceof TileEntityTrackMarker) {
							if(((TileEntityTrackMarker) te).getChannel().equalsIgnoreCase(message.channel)) {
								((TileEntityTrackMarker) te).setTrackType(message.trackType);
							}
						}
					}
			    	//TileEntity tileEntity = world.getTileEntity(new BlockPos(message.posX, message.posY, message.posZ));
			    	//if(tileEntity instanceof TileEntityTrackMarker) {
			    		//((TileEntityTrackMarker) tileEntity).set(message.channel, message.angle, message.height, message.trackType);
			    	//}
			    });
				
				return null;
			} catch(Exception e) {
				System.out.println("EXCEPTION");
				return null;
			}
		}
		
	}
	
}
