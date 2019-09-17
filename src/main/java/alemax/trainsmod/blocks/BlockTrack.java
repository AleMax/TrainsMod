package alemax.trainsmod.blocks;

import java.util.ArrayList;
import java.util.Collections;

import javax.vecmath.Vector3d;

import alemax.trainsmod.blocks.properties.UnlistedPropertyBlockPos;
import alemax.trainsmod.blocks.properties.UnlistedPropertyVector3d;
import alemax.trainsmod.blocks.tileentities.TileEntityAMRailCurved;
import alemax.trainsmod.blocks.tileentities.TileEntityTrack;
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TileEntityTrack) {
			if(worldIn.isRemote) System.out.print("Client:\t");
			else System.out.print("Server:\t");
			BlockPos superPos = ((TileEntityTrack) te).getSuperPos();
			System.out.println(superPos.getX() + "\t" + superPos.getY() + "\t" + superPos.getZ());
		}
		
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public IExtendedBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
	    IExtendedBlockState ext = (IExtendedBlockState) state;
	    TileEntity te = world.getTileEntity(pos);
	    
	    if (te instanceof TileEntityTrack && te != null) {
	    	BlockPos superPos = ((TileEntityTrack) te).getSuperPos();
	    	TileEntity teSuper = null;
	    	if(superPos != null) {
	    		teSuper = world.getTileEntity(((TileEntityTrack) te).getSuperPos());
	    	}
	    	if(teSuper instanceof TileEntityTrack) {
	    		TrackData td = ((TileEntityTrack) teSuper).getTrackData();
	    		if(td == null) ext = ext.withProperty(TRACK_POINTS, new Vector3d[0]);
	    		else {
	    			ArrayList<Integer> trackPointsIndices = new ArrayList<>();
	    			for(int i = 0; i < td.trackPoints.length - 1; i++) {
	    	        	Vector3d middlePoint = new Vector3d((td.trackPoints[i].x + td.trackPoints[i + 1].x) / 2.0, (td.trackPoints[i].y + td.trackPoints[i + 1].y) / 2.0, (td.trackPoints[i].z + td.trackPoints[i + 1].z) / 2.0);
	    	        	if(i % 2 != 0) {
	    	        		if(middlePoint.x < 10) {
	    	        			//System.out.println(middlePoint.x + "\t" + middlePoint.y + "\t" + middlePoint.z + "\t");
	    	        		}
	    	        	}
	    	        	if(new BlockPos(middlePoint.x, middlePoint.y, middlePoint.z).equals(pos)) {
	    	        		if(i == 1) {
	    	        			//System.out.println("NOW");
	    	        		}
	    	        		int index = i;
	    	        		boolean add = true;
	    	        		for(int j = 0; j < trackPointsIndices.size(); j++) {
	    	        			if(trackPointsIndices.get(j).intValue() == index) add = false;
	    	        		}
	    	        		if(add) trackPointsIndices.add(index);
	    	        		index = i + 1;
	    	        		add = true;
	    	        		for(int j = 0; j < trackPointsIndices.size(); j++) {
	    	        			if(trackPointsIndices.get(j).intValue() == index) add = false;
	    	        		}
	    	        		if(add) trackPointsIndices.add(index);
	    	        	}
	    	        }
	    			Vector3d[] sendTrackPoints = new Vector3d[trackPointsIndices.size()];
	    			Collections.sort(trackPointsIndices);
	    			for(int i = 0; i < trackPointsIndices.size(); i++) {
	    				//System.out.println(trackPointsIndices.get(i));
	    				sendTrackPoints[i] = td.trackPoints[trackPointsIndices.get(i)];
	    				//sendTrackPoints[i].x -= superPos.getX();
	    				//sendTrackPoints[i].y -= superPos.getY();
	    				//sendTrackPoints[i].z -= superPos.getZ();
	    				sendTrackPoints[i].x -= pos.getX();
	    				sendTrackPoints[i].y -= pos.getY();
	    				sendTrackPoints[i].z -= pos.getZ();
	    			}
	    			//System.out.println("------------");
	    			ext = ext.withProperty(TRACK_POINTS, sendTrackPoints);
	    		}
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
