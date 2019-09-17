package alemax.trainsmod.blocks;

import alemax.trainsmod.blocks.tileentities.TileEntityTrackSuper;
import alemax.trainsmod.util.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTrackSuper extends BlockTrack {

	public BlockTrackSuper() {
		super();
		setUnlocalizedName(Reference.MODID + ".track_super");
		setRegistryName("track_super");
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityTrackSuper();
	}

}
