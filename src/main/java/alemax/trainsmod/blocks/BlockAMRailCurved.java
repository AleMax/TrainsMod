package alemax.trainsmod.blocks;

import javax.vecmath.Vector2d;

import alemax.trainsmod.blocks.properties.UnlistedPropertyByte;
import alemax.trainsmod.blocks.properties.UnlistedPropertyInteger;
import alemax.trainsmod.blocks.properties.UnlistedPropertyRailPoints;
import alemax.trainsmod.blocks.tileentities.TileEntityAMRailCurved;
import alemax.trainsmod.blocks.tileentities.TileEntityTrackMarker;
import alemax.trainsmod.init.ModBlocks;
import alemax.trainsmod.init.ModItems;
import alemax.trainsmod.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAMRailCurved extends Block {
	
	public static final AxisAlignedBB AM_RAIL_CURVED_AABB = new AxisAlignedBB(0, 0, 0, 1, 0.1875, 1);
	
	public static final UnlistedPropertyByte LENGTH = new UnlistedPropertyByte("length");
	public static final UnlistedPropertyInteger CURRENT_STEP = new UnlistedPropertyInteger("current_step");
	public static final UnlistedPropertyRailPoints POINTS = new UnlistedPropertyRailPoints("points");
	
	public BlockAMRailCurved() {
		super(Material.CIRCUITS);
		setUnlocalizedName("am_rail_curved");
		setRegistryName("am_rail_curved");
		setCreativeTab(CommonProxy.tab_trainsmod);
		//setDefaultState(state);
		
		ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] {LENGTH, CURRENT_STEP, POINTS};
		return new ExtendedBlockState(this, new IProperty[0], unlistedProperties);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntityAMRailCurved te = (TileEntityAMRailCurved) worldIn.getTileEntity(pos);
		//float radius = te.getRadius();
		long id = te.getId();
		for(int i = -5; i < 6; i++) {
			for(int j = -5; j < 6; j++) {
				int x = pos.getX() + i;
				int y = pos.getY();
				int z = pos.getZ() + j;
				BlockPos nPos = new BlockPos(x,y,z);
				TileEntity teN = worldIn.getTileEntity(nPos);
				if(teN instanceof TileEntityAMRailCurved) {
					if(((TileEntityAMRailCurved) teN).getId() == id) worldIn.setBlockState(nPos, Blocks.AIR.getDefaultState());
				}
			}
		}
		
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {	
		if(placer instanceof EntityPlayer) {
			byte comingFrom = 0;
			byte goingTo = 1;
			float radius = 20.5f;
			float rot = placer.getRotationYawHead() % 360;
			
			if(placer.getAdjustedHorizontalFacing() == EnumFacing.NORTH) {
				comingFrom = 0;
				if((rot > -180 && rot < -90) || (rot > 180 && rot < 270)) goingTo = 3;
				else goingTo = 1;
			} else if(placer.getAdjustedHorizontalFacing() == EnumFacing.EAST) {
				comingFrom = 1;
				if((rot > -180 && rot < -90) || (rot > 180 && rot < 270)) goingTo = 2;
				else goingTo = 0;
			} else if(placer.getAdjustedHorizontalFacing() == EnumFacing.SOUTH) {
				comingFrom = 2;
				if((rot > -90 && rot < 0 ) || (rot > 270 && rot < 360)) goingTo = 3;
				else goingTo = 1;
			} else {
				comingFrom = 3;
				if((rot < 90 && rot > 0 ) || (rot < -270 && rot > -360)) goingTo = 0;
				else goingTo = 2;
			}
			//radius = stack.getCount() - 0.5f;
			radius = 100 + stack.getCount() - 0.5f;
		
		
			TileEntityAMRailCurved te = (TileEntityAMRailCurved) worldIn.getTileEntity(pos);
			te.set(comingFrom, goingTo, radius);
			int steps = te.getSteps();
			int cycles = (int) Math.round((steps * 2 + 1) * 0.5 * 0.5);
			Vector2d[][] points = te.getAllPoints();
			int y = pos.getY();
			long id = te.getId();
			for(int i = 1; i < cycles; i++) {
				long x = 0;
				long z = 0;
				byte length = 2;
				
				if(cycles - i == 1 && steps % 2 != 0) {
					x = (long) (Math.floor((points[i * 2][6].x + points[i * 2 + 1][6].x) * 0.5) + pos.getX());
					z = (long) (Math.floor((points[i * 2][6].y + points[i * 2 + 1][6].y) * 0.5) + pos.getZ());
					length = 1;
				} else {
					x = (long) (Math.floor(points[i * 2 + 1][6].x) + pos.getX());
					z = (long) (Math.floor(points[i * 2 + 1][6].y) + pos.getZ());
				}
				worldIn.setBlockState(new BlockPos(x, y, z), ModBlocks.am_rail_curved.getDefaultState());
				TileEntityAMRailCurved teN = (TileEntityAMRailCurved) worldIn.getTileEntity(new BlockPos(x, y, z));
				Vector2d[][] newPoints = new Vector2d[steps + 1][7];
				int dx = (int) (pos.getX() - x);
				int dz = (int) (pos.getZ() - z);
				for(int j = 0; j < newPoints.length; j++) {
					for(int jj = 0; jj < 7;jj++) {
						newPoints[j][jj] = new Vector2d();
						newPoints[j][jj].x = points[j][jj].x + dx;
						newPoints[j][jj].y = points[j][jj].y + dz;
					}
				}
				teN.set(id, comingFrom, goingTo, radius, steps, i, length, newPoints);
			}
		}
		//TileEntityAMRailCurved te = (TileEntityAMRailCurved) worldIn.getTileEntity(pos);
		//te.setRadius(50.5f);
		//worldIn.setBl
		System.out.println(worldIn.isRemote + " FINISHED CREATING BLOCK");
	}
	
	@Override
	public IExtendedBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
	    IExtendedBlockState ext = (IExtendedBlockState) state;
	    TileEntityAMRailCurved te = (TileEntityAMRailCurved) world.getTileEntity(pos);
	    
	    if (te instanceof TileEntityAMRailCurved) {
	        ext = ext
	        		.withProperty(LENGTH, (te.getLength()))
	        		.withProperty(CURRENT_STEP, (te.getCurrentStep()))
	        		.withProperty(POINTS, (te.getPoints()));
	    }
	    return ext;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		//AxisAlignedBB = new AxisAlignedBB(pos);
		return AM_RAIL_CURVED_AABB;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityAMRailCurved();
		//return new TileEntityTrackMarking();
	}
	
	public void initModel() {
		
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
