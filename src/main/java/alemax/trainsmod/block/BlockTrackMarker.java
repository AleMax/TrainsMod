package alemax.trainsmod.block;

import alemax.trainsmod.init.TMItemGroups;
import net.minecraft.block.Material;

public class BlockTrackMarker extends TMBlock {

    public BlockTrackMarker() {
        super(Settings.of(Material.STONE));
        this.name = "block_track_marker";
        this.itemGroup = TMItemGroups.ITEM_GROUP_TRAINSMOD;
    }


}
