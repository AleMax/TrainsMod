package alemax.trainsmod.items;

import alemax.trainsmod.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemTrainRotator extends Item {

	public ItemTrainRotator() {
		setRegistryName("item_train_rotator");
		setUnlocalizedName("item_train_rotator");
		setCreativeTab(CreativeTabs.TRANSPORTATION);
		
		ModItems.ITEMS.add(this);
	}
	
}
