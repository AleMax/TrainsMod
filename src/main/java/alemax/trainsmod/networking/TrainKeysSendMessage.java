package alemax.trainsmod.networking;

import alemax.trainsmod.entities.EntityRailcar;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TrainKeysSendMessage implements IMessage {

	public TrainKeysSendMessage() {}
	
	private int playerEntityID;
	private int trainID;
	private byte key;
	
	public TrainKeysSendMessage(int playerEntityID, int trainID, byte key) {
		this.playerEntityID = playerEntityID;
		this.trainID = trainID;
		this.key = key;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(playerEntityID);
		buf.writeInt(trainID);
		buf.writeByte(key);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		playerEntityID = buf.readInt();
		trainID = buf.readInt();
		key = buf.readByte();
	}
	
	public static class TrainKeysSendMessageHandler implements IMessageHandler<TrainKeysSendMessage, IMessage> {

		@Override
		public IMessage onMessage(TrainKeysSendMessage message, MessageContext ctx) {
			try {
				
				EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
				WorldServer world = serverPlayer.getServerWorld();
				
				world.addScheduledTask(() -> {
			    	Entity train = world.getEntityByID(message.trainID);
			    	if(train != null && train instanceof EntityRailcar) {
			    		((EntityRailcar) train).userInput(message.playerEntityID, message.key);
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
