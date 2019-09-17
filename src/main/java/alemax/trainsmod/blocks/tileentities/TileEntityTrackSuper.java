package alemax.trainsmod.blocks.tileentities;

import javax.vecmath.Vector3d;

import alemax.trainsmod.util.TrackData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

//weird name, ik
//The inherited class so i just give a fastTESR to the super duper important track blocks, not the boring ones
public class TileEntityTrackSuper extends TileEntity {
	
	private TrackData trackData;
	
	public void setTrackData(TrackData trackData) {
		//TODO: Probieren ob es auch ohne diesen Workaround, also nur mit this.trackData = trackData geht
		
		this.trackData = new TrackData();
		this.trackData.trackPoints = new Vector3d[trackData.trackPoints.length];
		for(int i = 0; i < trackData.trackPoints.length; i++) {
			Vector3d currentPoint = trackData.trackPoints[i];
			this.trackData.trackPoints[i] = new Vector3d(currentPoint.x, currentPoint.y, currentPoint.z);
		}	
	}
	
	public TrackData getTrackData() {
		if(trackData != null && trackData.trackPoints != null) {
			TrackData td = new TrackData();
			td.trackPoints = new Vector3d[this.trackData.trackPoints.length];
			for(int i = 0; i < this.trackData.trackPoints.length; i++) {
				td.trackPoints[i] = new Vector3d(this.trackData.trackPoints[i].x, this.trackData.trackPoints[i].y,this.trackData.trackPoints[i].z);
			}
			return td;
		}
		return null;
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
		boolean hasTrackData = compound.getBoolean("hasTrackData");
		if(hasTrackData) {
			int length = compound.getInteger("trackDataPointsLength");
			this.trackData = new TrackData();
			this.trackData.trackPoints = new Vector3d[length];
			for(int i = 0; i < length; i++) {
				this.trackData.trackPoints[i] = new Vector3d(compound.getDouble("trackDataPoints" + i + "x"), compound.getDouble("trackDataPoints" + i + "y"), compound.getDouble("trackDataPoints" + i + "z"));
			}
		}
	}

	
}
