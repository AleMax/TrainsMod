package alemax.trainsmod.init;

import alemax.trainsmod.block.BlockTrackMarker;


public class TMBlocks {

    public static final BlockTrackMarker BLOCK_TRACK_MARKER = new BlockTrackMarker();


    public static void registerAll() {
        BLOCK_TRACK_MARKER.register();
    }

}
