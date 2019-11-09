package alemax.trainsmod.block;

import alemax.trainsmod.TrainsMod;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public abstract class TMBlock extends Block {

    public final String name;
    public ItemGroup itemGroup;

    public TMBlock(Settings block$Settings_1, String name) {
        super(block$Settings_1);
        this.name = name;
    }

    public void register() {
        Identifier identifier = new Identifier(TrainsMod.modid, name);

        //Register the block
        Registry.register(Registry.BLOCK, identifier, this);

        //Register the ItemBlock
        if(itemGroup != null)
            Registry.register(Registry.ITEM, identifier, new BlockItem(this, new Item.Settings().group(itemGroup)));
        else
            Registry.register(Registry.ITEM, identifier, new BlockItem(this, new Item.Settings()));

    }

}
