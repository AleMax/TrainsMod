package alemax.trainsmod.block.blockentity.render;

import alemax.trainsmod.block.blockentity.BlockEntityTrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import org.lwjgl.opengl.GL11;

public class BERTrackMarker extends BlockEntityRenderer<BlockEntityTrackMarker> {
	@Override
	public void render(BlockEntityTrackMarker blockEntity, double x, double y, double z, float partialTicks, int destroyStage) {
		TrackMarker marker = TrackMarkerInstances.OVERWORLD.getTrackMarker(blockEntity.getPos());
		if (marker == null) return;
		
		GlStateManager.pushMatrix();
		
		GlStateManager.translated(x + 0.5, y + 1.01, z + 0.5);
		GlStateManager.rotatef(marker.angle, 0, 1, 0); // yes, GL takes degrees, not radians. yes, it's ridiculous.
		
		GlStateManager.bindTexture(0); // unbind current texture, since we're drawing without one
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBufferBuilder();
		
		builder.begin(GL11.GL_TRIANGLES, VertexFormats.POSITION);
		
		builder.vertex(+0.0, 0, +0.25).next();
		builder.vertex(-0.1, 0, -0.25).next();
		builder.vertex(+0.1, 0, -0.25).next();
		
		GlStateManager.color3f(0, 0, 0);
		tessellator.draw();
		
		GlStateManager.popMatrix();
	}
}
