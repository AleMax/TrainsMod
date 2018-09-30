package alemax.trainsmod.items;

import alemax.trainsmod.init.ModItems;
import alemax.trainsmod.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemTrainRotator extends Item {

	public ItemTrainRotator() {
		setRegistryName("item_train_rotator");
		setUnlocalizedName("item_train_rotator");
		setCreativeTab(CommonProxy.tab_trainsmod);
		
		ModItems.ITEMS.add(this);
	}
	
}
