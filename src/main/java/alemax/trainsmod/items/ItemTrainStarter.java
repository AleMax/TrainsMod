package alemax.trainsmod.items;

import alemax.trainsmod.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemTrainStarter extends Item {

	public ItemTrainStarter() {
		setRegistryName("item_train_starter");
		setUnlocalizedName("item_train_starter");
		setCreativeTab(CreativeTabs.TRANSPORTATION);
		
		ModItems.ITEMS.add(this);
	}
	
}
