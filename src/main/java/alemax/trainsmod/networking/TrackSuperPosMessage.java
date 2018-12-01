package alemax.trainsmod.networking;

import java.util.List;

import com.google.common.base.Charsets;

import alemax.trainsmod.blocks.tileentities.TileEntityTrack;
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

public class TrackSuperPosMessage implements IMessage {
	
	private BlockPos pos;
	private BlockPos superPos;
	
	public TrackSuperPosMessage() {}
	
    public TrackSuperPosMessage(BlockPos pos, BlockPos superPos) {
		this.pos = pos;
		this.superPos = superPos;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(superPos.getX());
		buf.writeInt(superPos.getY());
		buf.writeInt(superPos.getZ());
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.superPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	public static class TrackSuperPosMessageHandler implements IMessageHandler<TrackSuperPosMessage, IMessage> {

		@Override
		public IMessage onMessage(TrackSuperPosMessage message, MessageContext ctx) {
			try {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					WorldClient world = Minecraft.getMinecraft().world;
					TileEntity te = world.getTileEntity(message.pos);
					if(te instanceof TileEntityTrack) {
						((TileEntityTrack) te).setSuperPos(message.superPos);
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
