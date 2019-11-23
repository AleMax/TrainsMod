package alemax.trainsmod.init;

import alemax.trainsmod.block.BlockTrack;
import alemax.trainsmod.block.BlockTrackMarker;
import alemax.trainsmod.block.BlockTrackSuper;


public class TMBlocks {

    public static BlockTrackMarker BLOCK_TRACK_MARKER;
    public static BlockTrack BLOCK_TRACK;
    public static BlockTrackSuper BLOCK_TRACK_SUPER;

    public static void registerAll() {
        BLOCK_TRACK_MARKER = new BlockTrackMarker();
        BLOCK_TRACK = new BlockTrack();
        BLOCK_TRACK_SUPER = new BlockTrackSuper();

        BLOCK_TRACK_MARKER.register();
        BLOCK_TRACK.register();
        BLOCK_TRACK_SUPER.register();
    }

}
