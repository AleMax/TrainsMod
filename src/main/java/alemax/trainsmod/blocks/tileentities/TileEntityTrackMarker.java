package alemax.trainsmod.blocks.tileentities;

import alemax.trainsmod.util.TrackType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTrackMarker extends TileEntity {
	
	private String channel;
	private int angle;
	private int height;
	private TrackType trackType;
	
	public void setup(String channel) {
		this.channel = channel;
		this.angle = 0;
		this.height = 5;
		this.trackType = TrackType.CONCRETE;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public int getAngle() {
		return angle;
	}
	
	public int getHeight() {
		return height;
	}
	
	public TrackType getTrackType() {
		return trackType;
	}
	
	@Override
	public boolean hasFastRenderer() {
		return true;
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
	    writeToNBT(nbtTagCompound);
	    return nbtTagCompound;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("channel", channel);
		compound.setInteger("angle", angle);
		compound.setInteger("height", height);
		compound.setByte("trackType", (byte) trackType.ordinal());
		
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.channel = compound.getString("channel");
		this.angle = compound.getInteger("angle");
		this.height = compound.getInteger("height");
		byte trackTypeIndex = compound.getByte("trackType");
		if(trackTypeIndex < 0 || trackTypeIndex > TrackType.values.length - 1) trackTypeIndex = 0;
		this.trackType = TrackType.values[trackTypeIndex];
	}
	
	
	
	
}
