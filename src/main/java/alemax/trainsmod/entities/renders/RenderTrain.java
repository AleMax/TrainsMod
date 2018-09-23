package alemax.trainsmod.entities.renders;

import java.util.List;

import alemax.trainsmod.entities.EntityRailcar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class RenderTrain {

	public static RenderManager renderManager;
	
	private DynamicTexture lightmapTexture;
	private ResourceLocation locationLightMap;
	Minecraft mc;
	
	public RenderTrain() {
	}
	
	@SubscribeEvent
	public void onRenderLast(RenderWorldLastEvent event) {
		try {
			mc = Minecraft.getMinecraft();
			WorldClient world = mc.world;
			renderManager = mc.getRenderManager();
			renderManager.options = null;
			this.lightmapTexture = new DynamicTexture(16, 16);
	        this.locationLightMap = mc.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
			
			if(world != null) {
				GlStateManager.enableDepth();
		        GlStateManager.enableAlpha();
		        GlStateManager.alphaFunc(516, 0.5F);
				
		        GlStateManager.shadeModel(7424);
		        GlStateManager.alphaFunc(516, 0.1F);
		
		        GlStateManager.matrixMode(5888);
		        GlStateManager.pushMatrix();
		        RenderHelper.enableStandardItemLighting();
		        net.minecraftforge.client.ForgeHooksClient.setRenderPass(0);
		        renderEntities(event.getPartialTicks());
		        net.minecraftforge.client.ForgeHooksClient.setRenderPass(0);
		        RenderHelper.disableStandardItemLighting();
		        this.disableLightmap();
		        
		
		        GlStateManager.matrixMode(5888);
		        GlStateManager.popMatrix();
		        
			}
		} catch(NullPointerException e) {
			
		}
	}
	
	private void renderEntities(float partialTicks) {
		enableLightmap();
		
        
        renderManager.cacheActiveRenderInfo(mc.world, mc.fontRenderer, mc.getRenderViewEntity(), mc.pointedEntity, mc.gameSettings, partialTicks);
        Entity entity = mc.getRenderViewEntity();
        double d3 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
        double d4 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
        double d5 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
        renderManager.setRenderPosition(d3, d4, d5);
        
        mc.entityRenderer.enableLightmap();
        
        List<Entity> entities = mc.world.loadedEntityList;
        
        for(Entity entityIn : entities) {
        	if(entityIn instanceof EntityRailcar) {
        		if(((EntityRailcar) entityIn).lastPartialTick != partialTicks) {
                    renderManager.renderEntityStatic(entityIn, partialTicks, false);
        		}  
        	}
        }
        
        mc.entityRenderer.disableLightmap();
	}
	
	public void enableLightmap()
    {
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        float f = 0.00390625F;
        GlStateManager.scale(0.00390625F, 0.00390625F, 0.00390625F);
        GlStateManager.translate(8.0F, 8.0F, 8.0F);
        GlStateManager.matrixMode(5888);
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.locationLightMap);
        GlStateManager.glTexParameteri(3553, 10241, 9729);
        GlStateManager.glTexParameteri(3553, 10240, 9729);
        GlStateManager.glTexParameteri(3553, 10242, 10496);
        GlStateManager.glTexParameteri(3553, 10243, 10496);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
	
	public void disableLightmap()
    {
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
	
	private void renderEntities() {
		
	}
	
	
	public static RenderManager getRenderManager() {
		return renderManager;
	}
	
	
}
