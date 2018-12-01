package alemax.trainsmod.networking;

import java.util.ArrayList;
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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TrackBuildMessage implements IMessage {
	
	private int posX;
	private int posY;
	private int posZ;
	
	public TrackBuildMessage() {}
	
    public TrackBuildMessage(int posX, int posY, int posZ) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(posX);
		buf.writeInt(posY);
		buf.writeInt(posZ);
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.posX = buf.readInt();
		this.posY = buf.readInt();
		this.posZ = buf.readInt();
	}
	
	public static class TrackBuildMessageHandler implements IMessageHandler<TrackBuildMessage, IMessage> {

		@Override
		public IMessage onMessage(TrackBuildMessage message, MessageContext ctx) {
			try {
				
				EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
				WorldServer world = serverPlayer.getServerWorld();
				
				world.addScheduledTask(() -> {
					TileEntity tileEntity = world.getTileEntity(new BlockPos(message.posX, message.posY, message.posZ));
					if(tileEntity instanceof TileEntityTrackMarker) {
						((TileEntityTrackMarker) tileEntity).buildTrack();
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
