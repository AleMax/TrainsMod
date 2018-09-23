package alemax.trainsmod.networking;

import alemax.trainsmod.entities.EntityRailcar;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TrainSyncMessage implements IMessage {

	public TrainSyncMessage() {}
	
	private int entityID;
	private double posX;
	private double posY;
	private double posZ;
	private double rearPosX;
	private double rearPosY;
	private double rearPosZ;
	private double speed;
	private byte direction;
	private byte movingDirection;
	private float rotationYaw;
	private float rotationPitch;
	private float trainRotYaw;
	private float trainRotPitch;
	private boolean onRail;
	private BlockPos railBlock;
	
	public TrainSyncMessage(int entityID, double trainPosX, double trainPosY, double trainPosZ, double rearPosX, double rearPosY, double rearPosZ, double speed, byte direction, byte movingDirection, float rotationYaw, float rotationPitch, float trainRotYaw, float trainRotPitch, boolean onRail, BlockPos railBlock) {
		this.posX = trainPosX;
		this.posY = trainPosY;
		this.posZ = trainPosZ;
		this.rearPosX = rearPosX;
		this.rearPosY = rearPosY;
		this.rearPosZ = rearPosZ;
		this.entityID = entityID;
		this.speed = speed;
		this.direction = direction;
		this.movingDirection = movingDirection;
		this.rotationYaw = rotationYaw;
		this.rotationPitch = rotationPitch;
		this.trainRotYaw = trainRotYaw;
		this.trainRotPitch = trainRotPitch;
		this.onRail = onRail;
		this.railBlock = railBlock;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
		buf.writeDouble(rearPosX);
		buf.writeDouble(rearPosY);
		buf.writeDouble(rearPosZ);
		buf.writeDouble(speed);
		buf.writeByte(direction);
		buf.writeByte(movingDirection);
		buf.writeFloat(rotationYaw);
		buf.writeFloat(rotationPitch);
		buf.writeFloat(trainRotYaw);
		buf.writeFloat(trainRotPitch);
		buf.writeBoolean(onRail);
		buf.writeInt(railBlock.getX());
		buf.writeInt(railBlock.getY());
		buf.writeInt(railBlock.getZ());
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		entityID = buf.readInt();
		posX = buf.readDouble();
		posY = buf.readDouble();
		posZ = buf.readDouble();
		rearPosX = buf.readDouble();
		rearPosY = buf.readDouble();
		rearPosZ = buf.readDouble();
		speed = buf.readDouble();
		direction = buf.readByte();
		movingDirection = buf.readByte();
		rotationYaw = buf.readFloat();
		rotationPitch = buf.readFloat();
		trainRotYaw = buf.readFloat();
		trainRotPitch = buf.readFloat();
		onRail = buf.readBoolean();
		railBlock = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}
	public static class TrainSyncMessageHandler implements IMessageHandler<TrainSyncMessage, IMessage> {

		@Override
		public IMessage onMessage(TrainSyncMessage message, MessageContext ctx) {
			try {
				boolean ret = false;

				ret = true;
				Minecraft.getMinecraft().addScheduledTask(() -> {
					EntityRailcar entity = (EntityRailcar) Minecraft.getMinecraft().world.getEntityByID(message.entityID);
					if(entity != null)
						entity.sync(message.posX, message.posY, message.posZ, message.rearPosX, message.rearPosY, message.rearPosZ, message.speed, message.direction, message.movingDirection, message.rotationYaw, message.rotationPitch, message.trainRotYaw, message.trainRotPitch, message.onRail, message.railBlock);
				});
			
				return null;
			} catch(Exception e) {
				System.out.println("EXCEPTION");
				return null;
			}
		}
		
	}
	
}
