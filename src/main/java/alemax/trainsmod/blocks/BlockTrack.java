package alemax.trainsmod.blocks;

import alemax.trainsmod.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockTrack extends Block {

	public BlockTrack(Material materialIn) {
		super(materialIn);
		setUnlocalizedName("block_tm_track");
		setRegistryName("block_tm_track");
		setCreativeTab(CommonProxy.tab_trainsmod);
	}
	
}
