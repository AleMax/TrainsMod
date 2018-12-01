package alemax.trainsmod.blocks.tileentities;

import javax.vecmath.Vector3d;

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
	
	public BlockPos getSuperPos() {
		return superPos;
	}
	
	public TrackData getTrackData() {
		return trackData;
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
			compound.setInteger("trackDataPointsLength", this.trackData.trackPoints.length);
			for(int i = 0; i < this.trackData.trackPoints.length; i++) {
				compound.setDouble("trackDataPoints" + i + "x", this.trackData.trackPoints[i].x);
				compound.setDouble("trackDataPoints" + i + "y", this.trackData.trackPoints[i].y);
				compound.setDouble("trackDataPoints" + i + "z", this.trackData.trackPoints[i].z);
			}
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
			int length = compound.getInteger("trackDataPointsLength");
			this.trackData = new TrackData();
			this.trackData.trackPoints = new Vector3d[length];
			for(int i = 0; i < length; i++) {
				this.trackData.trackPoints[i] = new Vector3d(compound.getDouble("trackDataPoints" + i + "x"), compound.getDouble("trackDataPoints" + i + "y"), compound.getDouble("trackDataPoints" + i + "z"));
			}
		} else {
			this.trackData = null;
		}
	}
	
}
