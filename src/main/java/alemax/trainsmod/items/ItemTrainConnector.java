package alemax.trainsmod.items;

import alemax.trainsmod.init.ModItems;
import alemax.trainsmod.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemTrainConnector extends Item {

	public ItemTrainConnector() {
		setRegistryName("item_train_connector");
		setUnlocalizedName("item_train_connector");
		setCreativeTab(CommonProxy.tab_trainsmod);
		
		ModItems.ITEMS.add(this);
	}
	
}
