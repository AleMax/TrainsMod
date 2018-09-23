package alemax.trainsmod.util;

import net.minecraft.util.math.BlockPos;

public class TrackPoint {
	
	public double x;
	public double y;
	public double z;
	public BlockPos block;
	public byte direction;
	
	public TrackPoint(double x, double y, double z, BlockPos block, byte direction) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.block = block;
		this.direction = direction;
	}	
	
}

