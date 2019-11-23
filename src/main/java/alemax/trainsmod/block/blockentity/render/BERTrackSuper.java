package alemax.trainsmod.block.blockentity.render;

import alemax.trainsmod.block.blockentity.BlockEntityTrackMarker;
import alemax.trainsmod.block.blockentity.BlockEntityTrackSuper;
import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import alemax.trainsmod.global.tracknetwork.*;
import alemax.trainsmod.util.TrackPointsUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;

public class BERTrackSuper extends BlockEntityRenderer<BlockEntityTrackSuper> {

	@Override
	public void render(BlockEntityTrackSuper blockEntity, double x, double y, double z, float partialTicks, int destroyStage) {

		if(blockEntity.displayList == 0) {
			if(!createDisplayList(blockEntity))
				return;
		}

		GlStateManager.pushMatrix();

		GlStateManager.translated(x, y, z);

		GlStateManager.bindTexture(0); // unbind current texture, since we're drawing without one

		GlStateManager.callList(blockEntity.displayList);

		/*
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBufferBuilder();

		builder.begin(GL11.GL_TRIANGLES, VertexFormats.POSITION);
		
		builder.vertex(+0.0, 0, +0.35).next();
		builder.vertex(+0.05, 0, -0.0).next();
		builder.vertex(-0.05, 0, -0.0).next();

		builder.vertex(-0.05, 0, -0.0).next();
		builder.vertex(+0.05, 0, -0.0).next();
		builder.vertex(+0.0, 0, -0.35).next();

		GlStateManager.color3f(0, 0, 0);
		tessellator.draw();
		*/

		GlStateManager.popMatrix();
	}

	public boolean createDisplayList(BlockEntityTrackSuper blockEntity) {
		TrackPoint nearestTP = TrackNetworkInstances.OVERWORLD.getNearestTrackPoint(new Vec3d(blockEntity.getPos().getX() + 0.5, blockEntity.getPos().getY() + 0.5, blockEntity.getPos().getZ() + 0.5));
		if(nearestTP != null) {
			double distance = Math.sqrt(Math.pow(blockEntity.getPos().getX() + 0.5 - nearestTP.getPos().x, 2) + Math.pow(blockEntity.getPos().getY() + 0.5 - nearestTP.getPos().y, 2) + Math.pow(blockEntity.getPos().getZ() + 0.5 - nearestTP.getPos().z, 2));
			if(distance < 1.5) {
				ArrayList<TrackPoint> points = new ArrayList<TrackPoint>();
				points.add(nearestTP);
				if(nearestTP instanceof TrackPointStandard) {
					TrackPoint prev = nearestTP;
					TrackPoint next = ((TrackPointStandard) nearestTP).getNext();
					while(next instanceof TrackPointStandard) {
						points.add(next);
						TrackPoint newNext = next.getNextTrackPoint(prev);
						prev = next;
						next = newNext;
					}
					points.add(next);
					Collections.reverse(points);

					prev = nearestTP;
					next = ((TrackPointStandard) nearestTP).getPrevious();
					while(next instanceof TrackPointStandard) {
						points.add(next);
						TrackPoint newNext = next.getNextTrackPoint(prev);
						prev = next;
						next = newNext;
					}
					points.add(next);

				} else if (nearestTP instanceof TrackPointEnd){
					TrackPoint prev = nearestTP;
					TrackPoint next = ((TrackPointEnd) nearestTP).getPrevious();
					while(next instanceof TrackPointStandard) {
						points.add(next);
						TrackPoint newNext = next.getNextTrackPoint(prev);
						prev = next;
						next = newNext;
					}
					points.add(next);
				}

				blockEntity.displayList = GlStateManager.genLists(1);
				GlStateManager.newList(blockEntity.displayList, GL11.GL_COMPILE);
				GlStateManager.begin(GL11.GL_LINES);
				for(int i = 1; i < points.size(); i++) {
					System.out.println(points.get(i).getPos().x + "\t" + points.get(i).getPos().y + "\t" + points.get(i).getPos().z);
					GL11.glColor4f(1,0,0,1);
					GL11.glVertex3d(points.get(i - 1).getPos().x - blockEntity.getPos().getX(), points.get(i - 1).getPos().y - blockEntity.getPos().getY(), points.get(i - 1).getPos().z - blockEntity.getPos().getZ());
					GL11.glColor4f(1,0,0,1);
					GL11.glVertex3d(points.get(i).getPos().x - blockEntity.getPos().getX(), points.get(i).getPos().y - blockEntity.getPos().getY(), points.get(i).getPos().z - blockEntity.getPos().getZ());
				}
				GlStateManager.end();
				GlStateManager.endList();
			}
		}


		return false;
	}

}
