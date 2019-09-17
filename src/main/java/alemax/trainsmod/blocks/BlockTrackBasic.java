package alemax.trainsmod.blocks;

import alemax.trainsmod.blocks.tileentities.TileEntityTrackBasic;
import alemax.trainsmod.util.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

//Class for the basic track block
public class BlockTrackBasic extends BlockTrack {

	public BlockTrackBasic() {
		super();
		setUnlocalizedName(Reference.MODID + ".track_basic");
		setRegistryName("track_basic");
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityTrackBasic();
	}
	
}
