package alemax.trainsmod.blocks.tileentities;

import alemax.trainsmod.util.TrackData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityTrack extends TileEntity {
	
	private BlockPos superPos;
	private TrackData trackData;
	
	public void setSuperPos(BlockPos superPos) {
		this.superPos = superPos;
	}
	
}
