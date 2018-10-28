package alemax.trainsmod.blocks;

import alemax.trainsmod.TrainsMod;
import alemax.trainsmod.blocks.tileentities.TileEntityTrackMarker;
import alemax.trainsmod.guis.GUITrackMarker;
import alemax.trainsmod.init.CreativeTabTrainsMod;
import alemax.trainsmod.init.GuiHandler;
import alemax.trainsmod.proxy.CommonProxy;
import alemax.trainsmod.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Type;

public class BlockTrackMarker extends Block {

	public BlockTrackMarker() {
		super(Material.ROCK);
		setUnlocalizedName(Reference.MODID + ".track_marker");
		setRegistryName("track_marker");
		setCreativeTab(CommonProxy.tab_trainsmod);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if(placer instanceof EntityPlayer) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity instanceof TileEntityTrackMarker) {
				TileEntityTrackMarker te = (TileEntityTrackMarker) tileEntity;
				te.setup(placer.getName());
			}
		}
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(tileEntity instanceof TileEntityTrackMarker) {
			TileEntityTrackMarker te = (TileEntityTrackMarker) tileEntity;
			te.unloadChunk();
		}
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityTrackMarker();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TileEntityTrackMarker) {
			if(worldIn.isRemote) {
				playerIn.openGui(TrainsMod.instance, GuiHandler.TRACK_MARKER, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}
	
	
	
}
