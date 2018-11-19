package alemax.trainsmod.blocks.tileentities;

import alemax.trainsmod.util.TrackData;
import alemax.trainsmod.util.TrackType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityTrack extends TileEntity {
	
	private BlockPos superPos;
	private TrackData trackData;
	
	public void setSuperPos(BlockPos superPos) {
		this.superPos = superPos;
	}
	
	public void setTrackData(TrackData trackData) {
		this.trackData = trackData;
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
		if(superPos != null) {
			compound.setInteger("superX", superPos.getX());
			compound.setInteger("superY", superPos.getY());
			compound.setInteger("superZ", superPos.getZ());
		} else {
			compound.setInteger("superX", 0);
			compound.setInteger("superY", 0);
			compound.setInteger("superZ", 0);
		}
		if(this.trackData != null) {
			compound.setBoolean("hasTrackData", true);
			//Add TrackData
		} else {
			compound.setBoolean("hasTrackData", false);
		}
		
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.superPos = new BlockPos(compound.getInteger("superX"), compound.getInteger("superY"), compound.getInteger("superZ"));
		boolean hasTrackData = compound.getBoolean("hasTrackData");
		if(hasTrackData) {
			
		}
	}
	
}
