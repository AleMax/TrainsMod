package alemax.trainsmod.blocks;

import alemax.trainsmod.blocks.tileentities.TileEntityTrackMarking;
import alemax.trainsmod.init.ModBlocks;
import alemax.trainsmod.init.ModItems;
import alemax.trainsmod.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTrackMarking extends Block {

	public BlockTrackMarking() {
		super(Material.CIRCUITS);
		setUnlocalizedName("block_tm_track_marking");
		setRegistryName("block_tm_track_marking");
		setCreativeTab(CommonProxy.tab_trainsmod);
		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public boolean hasTileEntity() {
		System.out.println(true);
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		System.out.println(false);
		return new TileEntityTrackMarking();
	}
	
}
