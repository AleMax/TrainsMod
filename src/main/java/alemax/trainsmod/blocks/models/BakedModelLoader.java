package alemax.trainsmod.blocks.models;

import alemax.trainsmod.util.Reference;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class BakedModelLoader implements ICustomModelLoader {

    public static final ModelAMRailCurved AM_RAIL_CURVED_MODEL = new ModelAMRailCurved();


    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourceDomain().equals(Reference.MODID) && "am_rail_curved".equals(modelLocation.getResourcePath());
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return AM_RAIL_CURVED_MODEL;
        
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }
}