package alemax.trainsmod.blocks.tileentities.tesr;

import alemax.trainsmod.blocks.tileentities.TileEntityTrackMarking;
import net.minecraft.block.BlockColored;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.animation.AnimationTESR;
import net.minecraftforge.client.model.animation.FastTESR;

public class FastTESRTrackMarking extends FastTESR<TileEntityTrackMarking> {

	@Override
	public void renderTileEntityFast(TileEntityTrackMarking te, double x, double y, double z, float partialTicks,
			int destroyStage, float partial, BufferBuilder buffer) {

        BlockModelShapes bm = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes();
        TextureAtlasSprite redTexture =  bm.getTexture(Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED));
        
        int lmCombined = getWorld().getCombinedLight(te.getPos().up(), 0);
        int lmA = lmCombined >> 16 & 65535;
        int lmB = lmCombined & 65535;
        
        Vec3d middle = new Vec3d(0.5, 0, 0.5);
        
        double angle = Math.toRadians(te.getAngle());
        double radius = 0.4;
        double thickness = 0.05;
        
        Vec3d directionVec = new Vec3d(Math.sin(angle) * radius, 0, -Math.cos(angle) * radius);
        Vec3d diagVec = directionVec.crossProduct(new Vec3d(0, 1, 0)).normalize().scale(thickness);
        
        buffer.setTranslation(x,y,z);
        
        buffer.pos(middle.x + directionVec.x - diagVec.x, 1.001, middle.z + directionVec.z - diagVec.z).color(1f,1f,1f,1f).tex(redTexture.getMinU(), redTexture.getMinV()).lightmap(lmA,lmB).endVertex();
        buffer.pos(middle.x + directionVec.x + diagVec.x, 1.001, middle.z + directionVec.z + diagVec.z).color(1f,1f,1f,1f).tex(redTexture.getMaxU(), redTexture.getMinV()).lightmap(lmA,lmB).endVertex();
        buffer.pos(middle.x - directionVec.x + diagVec.x, 1.001, middle.z - directionVec.z + diagVec.z).color(1f,1f,1f,1f).tex(redTexture.getMaxU(), redTexture.getMaxV()).lightmap(lmA,lmB).endVertex();
        buffer.pos(middle.x - directionVec.x - diagVec.x, 1.001, middle.z - directionVec.z - diagVec.z).color(1f,1f,1f,1f).tex(redTexture.getMinU(), redTexture.getMaxV()).lightmap(lmA,lmB).endVertex();

		
	}
	
	
	
}
