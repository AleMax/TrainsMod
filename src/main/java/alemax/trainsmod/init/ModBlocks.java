package alemax.trainsmod.init;

import java.util.ArrayList;
import java.util.List;

import alemax.trainsmod.blocks.BlockAMRail;
import alemax.trainsmod.blocks.BlockAMRailCurved;
import alemax.trainsmod.blocks.BlockTrackMarker;
import alemax.trainsmod.util.Reference;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {


	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	@ObjectHolder("trainsmod:am_rail")
	public static final BlockAMRail am_rail = null;
	@ObjectHolder("trainsmod:am_rail_curved")
	public static final BlockAMRailCurved am_rail_curved = null;
	@ObjectHolder("trainsmod:track_marker")
	public static final BlockTrackMarker track_marker = null;
	
	
}
