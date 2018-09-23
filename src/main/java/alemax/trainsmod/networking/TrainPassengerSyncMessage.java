package alemax.trainsmod.networking;

import alemax.trainsmod.entities.EntityRailcar;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TrainPassengerSyncMessage implements IMessage {
	
	public TrainPassengerSyncMessage() {}
	
	private int entityID;
	private int seatSlot;
	private boolean noEntity;
	private int seatEntityID;

	public TrainPassengerSyncMessage(int entityID, int seatSlot, boolean noEntity, int seatEntityID) {
		this.entityID = entityID;
		this.seatSlot = seatSlot;
		this.noEntity = noEntity;
		this.seatEntityID = seatEntityID;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		buf.writeInt(seatSlot);
		buf.writeBoolean(noEntity);
		if(!noEntity) buf.writeInt(seatEntityID);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		entityID = buf.readInt();
		seatSlot = buf.readInt();
		noEntity = buf.readBoolean();
		if(!noEntity) seatEntityID = buf.readInt();
	}
	
	public static class TrainPassengerSyncMessageHandler implements IMessageHandler<TrainPassengerSyncMessage, IMessage> {

		@Override
		public IMessage onMessage(TrainPassengerSyncMessage message, MessageContext ctx) {
			try {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					WorldClient world = Minecraft.getMinecraft().world;
					EntityRailcar entity = (EntityRailcar) world.getEntityByID(message.entityID);
					if(entity != null) {
						Entity seatedEntity = null;
						if(!message.noEntity) seatedEntity = world.getEntityByID(message.seatEntityID);
						entity.syncPassenger(message.seatSlot, seatedEntity);
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
