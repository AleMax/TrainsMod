package alemax.trainsmod.util;

import net.minecraft.util.math.AxisAlignedBB;

public class AMMaths {
	
	
	
	public static AxisAlignedBB rotateY(AxisAlignedBB box, double rot) {
		double minX = box.minX;
		double minZ = box.minZ;
		double maxX = box.maxX;
		double maxZ = box.maxZ;
		//System.out.println("rot");
		for(int i = 0; i < rot; i++) {
			double nMinZ = minX;
			double nMaxX = -minZ;
			double nMaxZ = maxX;
			double nMinX = -maxZ;
			minZ = nMinZ;
			maxX = nMaxX;
			maxZ = nMaxZ;
			minX = nMinX;
		}
		return new AxisAlignedBB(minX, box.minY, minZ, maxX, box.maxY, maxZ);
	}
	
}
