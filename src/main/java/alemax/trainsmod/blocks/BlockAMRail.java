package alemax.trainsmod.blocks;

import alemax.trainsmod.blocks.tileentities.TileEntityAMRail;
import alemax.trainsmod.blocks.tileentities.TileEntityTrackMarker;
import alemax.trainsmod.init.ModBlocks;
import alemax.trainsmod.init.ModItems;
import alemax.trainsmod.proxy.CommonProxy;
import alemax.trainsmod.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import trackapi.lib.ITrackBlock;

@net.minecraftforge.fml.common.Optional.Interface(iface = "trackapi.lib.ITrackBlock", modid = "trackapi")
public class BlockAMRail extends Block implements ITrackBlock {
	
	public static final AxisAlignedBB AM_RAIL_AABB_NS = new AxisAlignedBB(-0.625, 0, 0, 1.625, 0.1875, 1);
	public static final AxisAlignedBB AM_RAIL_AABB_EW = new AxisAlignedBB(0, 0, -0.625, 1, 0.1875, 1.625);
	public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public BlockAMRail() {
		super(Material.CIRCUITS);
		setUnlocalizedName("am_rail");
		setRegistryName("am_rail");
		setCreativeTab(CommonProxy.tab_trainsmod);
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.EAST));
		ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if(placer instanceof EntityPlayer) {
			worldIn.setBlockState(pos, this.getDefaultState().withProperty(FACING, placer.getAdjustedHorizontalFacing()));
			TileEntityAMRail te = (TileEntityAMRail) worldIn.getTileEntity(pos);
			te.set(placer.getAdjustedHorizontalFacing());
		}
		
		//te.set(state.getValue(FACING));
	}
	
	@Override
	public Vec3d getNextPosition(World world, BlockPos pos, Vec3d currentPosition, Vec3d motion) {
		float speed = (float) Math.sqrt(Math.pow(motion.x, 2) + Math.pow(motion.y, 2) + Math.pow(motion.z, 2));
		//if(world.getBlockState(pos).getValue(FACING).equals(EnumFacing.EAST)) System.out.println("east");
		EnumFacing facing = world.getBlockState(pos).getValue(FACING);
		
		//System.out.println("Lol");
		
		if(facing.equals(EnumFacing.NORTH) || facing.equals(EnumFacing.SOUTH)) {
			if(motion.z > 0) {
				double posZ = currentPosition.z + speed;
				if(posZ > (pos.getZ() + 1.1)) posZ = pos.getZ() + 1.1;
				return new Vec3d(pos.getX() + 0.5, pos.getY() + 0.125, posZ);
			} else {
				double posZ = currentPosition.z - speed;
				if(posZ < (pos.getZ() - 0.1)) posZ = pos.getZ() - 0.1;
				return new Vec3d(pos.getX() + 0.5, pos.getY() + 0.125, posZ);
			}
		} else {
			if(motion.x > 0) {
				double posX = currentPosition.x + speed;
				if(posX > (pos.getX() + 1.1)) posX = pos.getX() + 1.1;
				return new Vec3d(posX, pos.getY() + 0.125, pos.getZ() + 0.5);
			} else {
				double posX = currentPosition.x - speed;
				if(posX < (pos.getX()  - 0.1)) posX = pos.getX()  - 0.1;
				return new Vec3d(posX, pos.getY() + 0.125, pos.getZ() + 0.5);
			}
		}
	}

	@Override
	public double getTrackGauge(World arg0, BlockPos arg1) {
		return 1.5;
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		if(state.getValue(FACING).equals(EnumFacing.NORTH)) return 0;
		if(state.getValue(FACING).equals(EnumFacing.EAST)) return 1;
		if(state.getValue(FACING).equals(EnumFacing.SOUTH)) return 2;
		if(state.getValue(FACING).equals(EnumFacing.WEST)) return 3;
		return 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if(meta == 0) return this.getDefaultState().withProperty(FACING, EnumFacing.NORTH);
		if(meta == 1) return this.getDefaultState().withProperty(FACING, EnumFacing.EAST);
		if(meta == 2) return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
		if(meta == 3) return this.getDefaultState().withProperty(FACING, EnumFacing.WEST);
		return this.getDefaultState().withProperty(FACING, EnumFacing.NORTH);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if(state.getValue(FACING).equals(EnumFacing.NORTH) || state.getValue(FACING).equals(EnumFacing.SOUTH)) return AM_RAIL_AABB_NS;
		else return AM_RAIL_AABB_EW;
	}
	
	public void initModel() {
		
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		TileEntityAMRail te = new TileEntityAMRail();
		te.set(state.getValue(FACING));
		return te;
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
