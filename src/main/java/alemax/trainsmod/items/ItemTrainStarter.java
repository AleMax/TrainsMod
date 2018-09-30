package alemax.trainsmod.items;

import alemax.trainsmod.init.ModItems;
import alemax.trainsmod.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemTrainStarter extends Item {

	public ItemTrainStarter() {
		setRegistryName("item_train_starter");
		setUnlocalizedName("item_train_starter");
		setCreativeTab(CommonProxy.tab_trainsmod);
		
		ModItems.ITEMS.add(this);
	}
	
}
