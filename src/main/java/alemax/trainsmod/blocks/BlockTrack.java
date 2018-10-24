package alemax.trainsmod.blocks;

import alemax.trainsmod.blocks.tileentities.TileEntityTrackMarking;
import alemax.trainsmod.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTrack extends Block {

	public BlockTrack(Material materialIn) {
		super(materialIn);
		setUnlocalizedName("block_tm_track");
		setRegistryName("block_tm_track");
		setCreativeTab(CommonProxy.tab_trainsmod);
	}

	
}
