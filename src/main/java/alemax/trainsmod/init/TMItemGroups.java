package alemax.trainsmod.init;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class TMItemGroups {

    public static ItemGroup ITEM_GROUP_TRAINSMOD;

    public static void registerAll() {
        ITEM_GROUP_TRAINSMOD = FabricItemGroupBuilder.build(new Identifier("trainsmod", "general"), () -> new ItemStack(Blocks.POWERED_RAIL));
    }

}
