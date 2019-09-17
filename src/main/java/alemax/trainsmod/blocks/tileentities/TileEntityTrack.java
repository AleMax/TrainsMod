package alemax.trainsmod.blocks.tileentities;

import javax.vecmath.Vector3d;


import alemax.trainsmod.util.TrackData;
import alemax.trainsmod.util.TrackPoint;
import alemax.trainsmod.util.TrackType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class TileEntityTrack extends TileEntity implements ITickable {
	
	private BlockPos superPos;
	private TrackData trackData;
	
	public void setSuperPos(BlockPos superPos) {
		this.superPos = superPos;
		
	}
	
	public void setTrackData(TrackData trackData) {
		//Bitterer hotfix weil zu faul:
		
		if(trackData != null && trackData.trackPoints != null && trackData.trackPoints.length > 3 && trackData.trackPoints[3].x < 5) {
			System.out.println("HALLO WAS SOLL DIE SCHei0e");
		}
		if(this.world.isRemote) {
			System.out.println("HALLO WAS SOLL DIE SCHei0e");
		}
		
		this.trackData = new TrackData();
		this.trackData.trackPoints = new Vector3d[trackData.trackPoints.length];
		for(int i = 0; i < trackData.trackPoints.length; i++) {
			Vector3d currentPoint = trackData.trackPoints[i];
			this.trackData.trackPoints[i] = new Vector3d(currentPoint.x, currentPoint.y, currentPoint.z);
		}
		
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return false;
	}
	
	public BlockPos getSuperPos() {
		return this.superPos;
	}
	
	@Override
	public void update() {
		if(this.trackData != null && this.trackData.trackPoints != null && this.trackData.trackPoints.length > 3) {
			if(this.world.isRemote) {
				System.out.println("TILEENTITY: " + this.trackData.trackPoints[3].x);
			}
			
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
