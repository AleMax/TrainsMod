package alemax.trainsmod.blocks;

import java.util.ArrayList;
import java.util.Collections;

import javax.vecmath.Vector3d;

import alemax.trainsmod.blocks.properties.UnlistedPropertyBlockPos;
import alemax.trainsmod.blocks.properties.UnlistedPropertyVector3d;
import alemax.trainsmod.blocks.tileentities.TileEntityAMRailCurved;
import alemax.trainsmod.proxy.CommonProxy;
import alemax.trainsmod.util.Reference;
import alemax.trainsmod.util.TrackData;
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

//Abstract block class for both track types
public abstract class BlockTrack extends Block {
	
	//Everything in here is a common attribute of both track blocks (i guess)
	
	public BlockTrack() {
		super(Material.IRON);
	}

	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public abstract TileEntity createTileEntity(World world, IBlockState state);
	
	
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
