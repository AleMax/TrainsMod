package alemax.trainsmod.init;

import alemax.trainsmod.TrainsMod;
import alemax.trainsmod.block.TMBlock;
import alemax.trainsmod.block.blockentity.BlockEntityTrackMarker;
import alemax.trainsmod.block.blockentity.BlockEntityTrackSuper;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TMBlockEntities {

    public static BlockEntityType<BlockEntityTrackMarker> BLOCK_ENTITY_TRACK_MARKER;
    public static BlockEntityType<BlockEntityTrackSuper> BLOCK_ENTITY_TRACK_SUPER;

    public static void registerAll() {
        BLOCK_ENTITY_TRACK_MARKER = Registry.register(Registry.BLOCK_ENTITY, new Identifier(TrainsMod.modid, "track_marker"), BlockEntityType.Builder.create(BlockEntityTrackMarker::new, TMBlocks.BLOCK_TRACK_MARKER).build(null));
        BLOCK_ENTITY_TRACK_SUPER = Registry.register(Registry.BLOCK_ENTITY, new Identifier(TrainsMod.modid, "track_super"), BlockEntityType.Builder.create(BlockEntityTrackSuper::new, TMBlocks.BLOCK_TRACK_SUPER).build(null));
    }


}
