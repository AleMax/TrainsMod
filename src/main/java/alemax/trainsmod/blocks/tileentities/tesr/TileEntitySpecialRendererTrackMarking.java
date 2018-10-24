package alemax.trainsmod.blocks.tileentities.tesr;

import alemax.trainsmod.blocks.tileentities.TileEntityTrackMarking;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.client.model.animation.FastTESR;

public class TileEntitySpecialRendererTrackMarking extends FastTESR<TileEntityTrackMarking> {

	@Override
	public void renderTileEntityFast(TileEntityTrackMarking te, double x, double y, double z, float partialTicks,
			int destroyStage, float partial, BufferBuilder buffer) {
		
		buffer.pos(0, 1, 0).color(1, 1, 1, 1).endVertex();
		buffer.pos(0, 1, 1).color(1, 1, 1, 1).endVertex();
		buffer.pos(1, 1, 0).color(1, 1, 1, 1).endVertex();
		buffer.pos(1, 1, 1).color(1, 1, 1, 1).endVertex();
		
		System.out.println("Rendered");
	}
	
	
	
}
