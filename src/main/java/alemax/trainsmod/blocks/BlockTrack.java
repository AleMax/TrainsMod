package alemax.trainsmod.blocks;

import alemax.trainsmod.blocks.tileentities.TileEntityTrack;
import alemax.trainsmod.proxy.CommonProxy;
import alemax.trainsmod.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTrack extends Block {

	public BlockTrack() {
		super(Material.IRON);
		setUnlocalizedName(Reference.MODID + ".track");
		setRegistryName("track");
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityTrack();
	}
	
}
