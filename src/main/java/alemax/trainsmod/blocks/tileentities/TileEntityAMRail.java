package alemax.trainsmod.blocks.tileentities;

import alemax.trainsmod.util.TrackPoint;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import trackapi.lib.ITrack;

public class TileEntityAMRail extends TileEntity {
	
	TrackPoint firstPoint;
	TrackPoint secondPoint;
	boolean north_south;
	
	public void set(EnumFacing facing) {
		if(facing.equals(EnumFacing.NORTH) || facing.equals(EnumFacing.SOUTH)) {
			firstPoint = new TrackPoint(0.5, 0.125, 0, pos, (byte) 2);
			secondPoint = new TrackPoint(0.5, 0.125, 1, pos, (byte) 0);
			north_south = true;
		} else {
			firstPoint = new TrackPoint(0, 0.125, 0.5, pos, (byte) 1);
			secondPoint = new TrackPoint(1, 0.125, 0.5, pos, (byte) 3);
			north_south = false;
		}
		markDirty();
	}
	

	//@Override
	public Vec3d getNextPosition(Vec3d position, Vec3d motion) {
		float speed = (float) Math.sqrt(Math.pow(motion.x, 2) + Math.pow(motion.y, 2) + Math.pow(motion.z, 2));
		if(this.north_south) {
			if(motion.z > 0) {
				double posZ = position.z + speed;
				if(posZ > (secondPoint.z + getPos().getZ()) + 0.1) posZ = (secondPoint.z + getPos().getZ()  + 0.1);
				return new Vec3d(position.x, position.y, posZ);
			} else {
				double posZ = position.z - speed;
				if(posZ < (firstPoint.z + getPos().getZ()) - 0.1) posZ = (firstPoint.z + getPos().getZ() - 0.1);
				return new Vec3d(position.x, position.y, posZ);
			}
		} else {
			if(motion.x > 0) {
				double posX = position.x + speed;
				if(posX > (secondPoint.x + getPos().getX()) + 0.1) posX = (secondPoint.x + getPos().getX() + 0.1);
				return new Vec3d(posX, position.y, position.z);
			} else {
				double posX = position.x - speed;
				if(posX < (firstPoint.x + getPos().getX()) - 0.1) posX = (firstPoint.x + getPos().getX() - 0.1);
				return new Vec3d(posX, position.y, position.z);
			}
		}
	}

	//@Override
	public double getTrackGauge() {
		return 1.5;
	}
	
	@Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }
	
	public TrackPoint getTrackPoint(byte i) {
		if(i == 0) return getFirstPoint();
		return getSecondPoint();
	}
	
	public TrackPoint getFirstPoint() {
		return new TrackPoint(firstPoint.x + pos.getX(), firstPoint.y + pos.getY(), firstPoint.z + pos.getZ(), pos, firstPoint.direction);
	}
	
	public TrackPoint getSecondPoint() {
		return new TrackPoint(secondPoint.x + pos.getX(), secondPoint.y + pos.getY(), secondPoint.z + pos.getZ(), pos, secondPoint.direction);
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
		compound.setDouble("dx1", firstPoint.x);
		compound.setDouble("dy1", firstPoint.y);
		compound.setDouble("dz1", firstPoint.z);
		compound.setInteger("ix1", firstPoint.block.getX());
		compound.setInteger("iy1", firstPoint.block.getY());
		compound.setInteger("iz1", firstPoint.block.getZ());
		compound.setByte("direction1", firstPoint.direction);
		
		compound.setDouble("dx2", secondPoint.x);
		compound.setDouble("dy2", secondPoint.y);
		compound.setDouble("dz2", secondPoint.z);
		compound.setInteger("ix2", secondPoint.block.getX());
		compound.setInteger("iy2", secondPoint.block.getY());
		compound.setInteger("iz2", secondPoint.block.getZ());
		compound.setByte("direction2", secondPoint.direction);
		compound.setBoolean("north_south", north_south);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		double dx1 = compound.getDouble("dx1");
		double dy1 = compound.getDouble("dy1");
		double dz1 = compound.getDouble("dz1");
		int ix1 = compound.getInteger("ix1");
		int iy1 = compound.getInteger("iy1");
		int iz1 = compound.getInteger("iz1");
		this.firstPoint = new TrackPoint(dx1, dy1, dz1, new BlockPos(ix1, iy1, iz1), compound.getByte("direction1"));
		
		double dx2 = compound.getDouble("dx2");
		double dy2 = compound.getDouble("dy2");
		double dz2 = compound.getDouble("dz2");
		int ix2 = compound.getInteger("ix2");
		int iy2 = compound.getInteger("iy2");
		int iz2 = compound.getInteger("iz2");
		this.secondPoint = new TrackPoint(dx2, dy2, dz2, new BlockPos(ix2, iy2, iz2), compound.getByte("direction2"));
		this.north_south = compound.getBoolean("north_south");
		super.readFromNBT(compound);
	}


	
}
