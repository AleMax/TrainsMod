package alemax.trainsmod.networking;

import alemax.trainsmod.entities.EntityRailcar;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TrainSyncSpeedMessage implements IMessage {

	public TrainSyncSpeedMessage() {}
	
	private int entityID;
	private double speed;
	private double aimedSpeed;
	
	/*
	private ArrayList<TrackPoint> trackPoints;
	private ArrayList<Double> tpLenghts;
	private double currentTrackLength;
	*/
	
	public TrainSyncSpeedMessage(int entityID, double speed, double aimedSpeed) {
		this.entityID = entityID;
		this.speed = speed;
		this.aimedSpeed = aimedSpeed;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		buf.writeDouble(speed);
		buf.writeDouble(aimedSpeed);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		entityID = buf.readInt();
		speed = buf.readDouble();
		aimedSpeed = buf.readDouble();
	}
	public static class TrainSyncSpeedMessageHandler implements IMessageHandler<TrainSyncSpeedMessage, IMessage> {

		@Override
		public IMessage onMessage(TrainSyncSpeedMessage message, MessageContext ctx) {
			try {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					EntityRailcar entity = (EntityRailcar) Minecraft.getMinecraft().world.getEntityByID(message.entityID);
					if(entity != null)
						entity.syncSpeed(message.speed, message.aimedSpeed);
				});
				return null;
			} catch(Exception e) {
				System.out.println("EXCEPTION");
				return null;
			}
		}
		
	}
	
}
