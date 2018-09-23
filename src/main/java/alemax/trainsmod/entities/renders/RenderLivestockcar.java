package alemax.trainsmod.entities.renders;

import alemax.trainsmod.entities.EntityFreightcar;
import alemax.trainsmod.entities.EntityLivestockcar;
import alemax.trainsmod.entities.models.ModelFreightcar;
import alemax.trainsmod.entities.models.ModelLivestockcar;
import alemax.trainsmod.entities.models.ModelRailcar;
import alemax.trainsmod.util.Reference;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderLivestockcar extends Render<EntityLivestockcar> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entities/livestockcar.png");
	ModelRailcar model = new ModelRailcar("livestockcar.obj");
	
    public static final Factory FACTORY = new Factory();
	
	public RenderLivestockcar(RenderManager rendermanagerIn) {
		super(rendermanagerIn);	
		
	}
		
	public void doRender(EntityLivestockcar entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
		
		
	    GlStateManager.pushMatrix();
	    float yaw  = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
	    double xDif = entity.posX - x;
	    double yDif = entity.posY - y;
	    double zDif = entity.posZ - z;
	    x = entity.getTrainPosX() - xDif;
	    y = entity.getTrainPosY() - yDif;
	    z = entity.getTrainPosZ() - zDif;
	    this.setupTranslation(x, y, z);
	    this.setupRotation(entity, yaw);
	    this.bindEntityTexture(entity);
	    
	    this.model.render();
	
	    GlStateManager.popMatrix();
	    
	    entity.lastPartialTick = partialTicks;
	    super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    public void setupRotation(EntityLivestockcar p_188311_1_, float p_188311_2_)
    {
        GlStateManager.rotate(180 - p_188311_2_, 0.0F, 1.0F, 0.0F);
    }

    public void setupTranslation(double p_188309_1_, double p_188309_3_, double p_188309_5_)
    {
        GlStateManager.translate((float)p_188309_1_, (float)p_188309_3_ + 0.375F, (float)p_188309_5_);
    }



	@Override
	protected ResourceLocation getEntityTexture(EntityLivestockcar entity) {
		return this.TEXTURE;
	}
	
	
	public static class Factory implements IRenderFactory<EntityLivestockcar> {

		@Override
		public Render<? super EntityLivestockcar> createRenderFor(RenderManager manager) {
			return new RenderLivestockcar(manager);
		}

    }

}
