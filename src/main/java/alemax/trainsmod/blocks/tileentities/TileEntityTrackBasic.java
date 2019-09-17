package alemax.trainsmod.blocks.tileentities;

import javax.vecmath.Vector3d;

import alemax.trainsmod.util.TrackData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

//The TileEntity for the normal Track Blocks
public class TileEntityTrackBasic extends TileEntity {

	private BlockPos superPos;
	
	public void setSuperPos(BlockPos superPos) {
		this.superPos = superPos;		
	}
	
	public BlockPos getSuperPos() {
		return this.superPos;
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
		
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.superPos = new BlockPos(compound.getInteger("superX"), compound.getInteger("superY"), compound.getInteger("superZ"));
	}
}
