package alemax.trainsmod.init;

import java.util.ArrayList;
import java.util.List;

import alemax.trainsmod.blocks.BlockAMRail;
import alemax.trainsmod.blocks.BlockAMRailCurved;
import alemax.trainsmod.blocks.BlockTrackMarking;
import net.minecraft.block.Block;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final BlockAMRail AM_RAIL = new BlockAMRail();
	public static final BlockAMRailCurved AM_RAIL_CURVED = new BlockAMRailCurved();
	public static final BlockTrackMarking TRACK_MARKING = new BlockTrackMarking();
	
	@SideOnly(Side.CLIENT)
	public static void initModels() {
		AM_RAIL.initModel();
		AM_RAIL_CURVED.initModel();
		
	}
	
}
