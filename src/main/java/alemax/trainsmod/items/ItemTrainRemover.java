package alemax.trainsmod.items;

import alemax.trainsmod.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemTrainRemover extends Item {

	public ItemTrainRemover() {
		setRegistryName("item_train_remover");
		setUnlocalizedName("item_train_remover");
		setCreativeTab(CreativeTabs.TRANSPORTATION);
		
		ModItems.ITEMS.add(this);
	}
	
}
