package alemax.trainsmod.init;

import alemax.trainsmod.util.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabTrainsMod extends CreativeTabs {

	public CreativeTabTrainsMod() {
		super(Reference.MODID);
		
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.item_br143);
	}
	
}
