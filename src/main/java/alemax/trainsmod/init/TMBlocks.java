package alemax.trainsmod.init;

import alemax.trainsmod.block.BlockTrackMarker;


public class TMBlocks {

    public static BlockTrackMarker BLOCK_TRACK_MARKER;


    public static void registerAll() {
        BLOCK_TRACK_MARKER = new BlockTrackMarker();

        BLOCK_TRACK_MARKER.register();
    }

}
