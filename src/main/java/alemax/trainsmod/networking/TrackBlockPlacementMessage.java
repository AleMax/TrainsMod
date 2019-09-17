package alemax.trainsmod.networking;

import java.util.ArrayList;

import alemax.trainsmod.blocks.tileentities.TileEntityTrack;
import alemax.trainsmod.init.ModBlocks;
import alemax.trainsmod.util.TrackData;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TrackBlockPlacementMessage implements IMessage {

	ArrayList<BlockPos> trackPositions;
	int size;
	
	public TrackBlockPlacementMessage() {}
	
    public TrackBlockPlacementMessage(ArrayList<BlockPos> trackPositions) {
		this.trackPositions = trackPositions;
		this.size = trackPositions.size();
	}
	

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(size);
		for(int i = 0; i < size; i++) {
			buf.writeInt(trackPositions.get(i).getX());
			buf.writeInt(trackPositions.get(i).getY());
			buf.writeInt(trackPositions.get(i).getZ());
		}
		
	}
    
	@Override
	public void fromBytes(ByteBuf buf) {
		size = buf.readInt();
		trackPositions = new ArrayList<BlockPos>();
		for(int i = 0; i < size; i++) {
			trackPositions.add(new BlockPos(buf.readInt(), buf.readInt(),buf.readInt()));
		}
	}
	
	public static class TrackBlockPlacementMessageHandler implements IMessageHandler<TrackBlockPlacementMessage, IMessage> {

		@Override
		public IMessage onMessage(TrackBlockPlacementMessage message, MessageContext ctx) {
			try {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					WorldClient world = Minecraft.getMinecraft().world;
					for(int i = 0; i < message.size; i++) {
						world.setBlockState(message.trackPositions.get(i), ModBlocks.track.getDefaultState());
					}
					
			    });
				return null;
			} catch(Exception e) {
				System.out.println("EXCEPTION ON BLOCK PLACING (CLIENT)");
				return null;
			}
		}
		
	}


}
