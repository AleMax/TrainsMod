package alemax.trainsmod.init;

import alemax.trainsmod.block.blockentity.BlockEntityTrackMarker;
import alemax.trainsmod.block.blockentity.render.BERTrackMarker;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

public class TMBlockEntityRenderer {

    public static void registerAll() {

        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityTrackMarker.class, new BERTrackMarker());



    }


}
