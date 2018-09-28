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

public class TrainClientKeysSendMessage implements IMessage {

	public TrainClientKeysSendMessage() {}
	
	private int playerEntityID;
	private int trainID;
	private byte key;
	
	public TrainClientKeysSendMessage(int playerEntityID, int trainID, byte key) {
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
	
	public static class TrainClientKeysSendMessageHandler implements IMessageHandler<TrainClientKeysSendMessage, IMessage> {

		@Override
		public IMessage onMessage(TrainClientKeysSendMessage message, MessageContext ctx) {
			try {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					WorldClient world = Minecraft.getMinecraft().world;
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
