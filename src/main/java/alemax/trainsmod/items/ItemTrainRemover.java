package alemax.trainsmod.items;

import alemax.trainsmod.init.ModItems;
import alemax.trainsmod.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemTrainRemover extends Item {

	public ItemTrainRemover() {
		setRegistryName("item_train_remover");
		setUnlocalizedName("item_train_remover");
		setCreativeTab(CommonProxy.tab_trainsmod);
		
		ModItems.ITEMS.add(this);
	}
	
}
