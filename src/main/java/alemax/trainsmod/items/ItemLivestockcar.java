package alemax.trainsmod.items;

import java.util.List;

import alemax.trainsmod.entities.EntityFreightcar;
import alemax.trainsmod.entities.EntityLivestockcar;
import alemax.trainsmod.entities.EntityRailcar;
import alemax.trainsmod.init.ModBlocks;
import alemax.trainsmod.init.ModItems;
import alemax.trainsmod.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import trackapi.lib.ITrack;
import trackapi.lib.ITrackBlock;

public class ItemLivestockcar extends Item {
	
	public ItemLivestockcar() {
		setRegistryName("item_livestockcar");
		setUnlocalizedName("item_livestockcar");
		setCreativeTab(CommonProxy.tab_trainsmod);
		
		ModItems.ITEMS.add(this);
	}
		
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote) {
			if((worldIn.getBlockState(pos).getBlock() instanceof ITrackBlock || worldIn.getTileEntity(pos) instanceof ITrack) && !(worldIn.getBlockState(pos).getBlock().getRegistryName().equals(ModBlocks.am_rail_curved.getRegistryName()))) {
				double posX = pos.getX() + 0.5;
				double posY = pos.getY() + 0.125;
				double posZ = pos.getZ() + 0.5;
				EntityLivestockcar car = new EntityLivestockcar(worldIn, pos);
				AxisAlignedBB subwayBox = car.getBoundBox().offset(car.posX, car.posY, car.posZ);
				List<EntityRailcar> trains = worldIn.getEntitiesWithinAABB(EntityRailcar.class, new AxisAlignedBB(-30 + subwayBox.minX, -10 + subwayBox.minY, -30 + subwayBox.minZ, 30 + subwayBox.maxX, 10 + subwayBox.maxY, 30 + subwayBox.maxZ)); 
				boolean intersect = false;
				for(EntityRailcar e : trains) {
					if(e.getEntityBoundingBox().intersects(subwayBox)) {
						intersect = true;
						break;
					}
				}
				if(!intersect) {
					worldIn.spawnEntity(car);
				}
			}
		}
			
		
		return EnumActionResult.SUCCESS;
	}
	
}
