package alemax.trainsmod.init;

import alemax.trainsmod.block.blockentity.BlockEntityTrackMarker;
import alemax.trainsmod.block.blockentity.BlockEntityTrackSuper;
import alemax.trainsmod.block.blockentity.render.BERTrackMarker;
import alemax.trainsmod.block.blockentity.render.BERTrackSuper;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

public class TMBlockEntityRenderer {

    public static void registerAll() {

        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityTrackMarker.class, new BERTrackMarker());
        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityTrackSuper.class, new BERTrackSuper());


    }


}
