package alemax.trainsmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class TrainsMod implements ModInitializer {

	public static final String modid = "trainsmod";
	public static final String modName = "TrainsMod";
	public static final String modVersion = "0.2.0";

	@Override
	public void onInitialize() {
		System.out.println("Starting TrainsMod");
	}

	//ItemGroups:
	public static final ItemGroup ITEM_GROUP_TRAINSMOD = FabricItemGroupBuilder.build(new Identifier("trainsmod", "general"), () -> new ItemStack(Blocks.POWERED_RAIL));


}
