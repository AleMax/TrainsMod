package alemax.trainsmod.blocks.models;

import alemax.trainsmod.util.Reference;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class BakedModelLoader implements ICustomModelLoader {

    public static final ModelAMRailCurved AM_RAIL_CURVED_MODEL = new ModelAMRailCurved();
    public static final ModelTrack TRACK_MODEL = new ModelTrack();

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
    	if(modelLocation.getResourceDomain().equals(Reference.MODID)) {
    		if("am_rail_curved".equals(modelLocation.getResourcePath())) return true;
    		if("track".equals(modelLocation.getResourcePath())) return true;
    	}
    	return false;
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
    	if(modelLocation.getResourceDomain().equals(Reference.MODID)) {
    		if("am_rail_curved".equals(modelLocation.getResourcePath())) return AM_RAIL_CURVED_MODEL;
    		if("track".equals(modelLocation.getResourcePath())) return TRACK_MODEL;
    	}
    	return null;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }
}