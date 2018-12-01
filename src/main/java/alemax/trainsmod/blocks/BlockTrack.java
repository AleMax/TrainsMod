package alemax.trainsmod.blocks;

import alemax.trainsmod.blocks.properties.UnlistedPropertyBlockPos;
import alemax.trainsmod.blocks.properties.UnlistedPropertyVector3d;
import alemax.trainsmod.blocks.tileentities.TileEntityAMRailCurved;
import alemax.trainsmod.blocks.tileentities.TileEntityTrack;
import alemax.trainsmod.proxy.CommonProxy;
import alemax.trainsmod.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTrack extends Block {

	public static final UnlistedPropertyVector3d TRACK_POINTS = new UnlistedPropertyVector3d("track_points");
	
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
	
	@Override
	protected BlockStateContainer createBlockState() {
		IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] {TRACK_POINTS};
		return new ExtendedBlockState(this, new IProperty[0], unlistedProperties);
	}
	
	@Override
	public IExtendedBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
	    IExtendedBlockState ext = (IExtendedBlockState) state;
	    TileEntity te = world.getTileEntity(pos);
	    
	    
	    if (te instanceof TileEntityTrack) {
	    	TileEntity teSuper = world.getTileEntity(((TileEntityTrack) te).getSuperPos());
	    	if(teSuper instanceof TileEntityTrack) {
	    		if(((((TileEntityTrack) teSuper).getTrackData()).trackPoints) == null) {
	    			System.out.println("smh´th");
	    		}
	    		ext = ext.withProperty(TRACK_POINTS, (((TileEntityTrack) teSuper).getTrackData().trackPoints));
	    	}
	    }
	    return ext;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }
	
    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }
	
}
