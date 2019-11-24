package alemax.trainsmod.block.blockentity.render;

import alemax.trainsmod.TrainsMod;
import alemax.trainsmod.block.blockentity.BlockEntityTrackMarker;
import alemax.trainsmod.block.blockentity.BlockEntityTrackSuper;
import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import alemax.trainsmod.global.tracknetwork.*;
import alemax.trainsmod.init.TMBlocks;
import alemax.trainsmod.util.Texture;
import alemax.trainsmod.util.TrackPointsUtils;
import alemax.trainsmod.util.obj.OBJ;
import alemax.trainsmod.util.obj.OBJHandler;
import alemax.trainsmod.util.obj.OBJLoader;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;

public class BERTrackSuper extends BlockEntityRenderer<BlockEntityTrackSuper> {

	public static OBJ model;

	@Override
	public void render(BlockEntityTrackSuper blockEntity, double x, double y, double z, float partialTicks, int destroyStage) {

		if(blockEntity.displayList == 0) {
			if(!createDisplayList(blockEntity))
				return;
		}
		//TextureUtil
		GlStateManager.pushMatrix();

		GlStateManager.translated(x, y, z);

		GlStateManager.bindTexture(0); // unbind current texture, since we're drawing without one

		MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier(TrainsMod.modid, "textures/block/track_concrete.png"));

		GlStateManager.callList(blockEntity.displayList);

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
				for(int i = 1; i < points.size(); i++) {
					Vec3d middlePos = new Vec3d(
							(points.get(i - 1).getPos().x + points.get(i).getPos().x) / 2.0 - blockEntity.getPos().getX(),
							(points.get(i - 1).getPos().y + points.get(i).getPos().y) / 2.0 - blockEntity.getPos().getY() - 0.3125,
							(points.get(i - 1).getPos().z + points.get(i).getPos().z) / 2.0 - blockEntity.getPos().getZ());
					Vec3d twoPoints = new Vec3d(points.get(i).getPos().x - points.get(i - 1).getPos().x, points.get(i).getPos().y - points.get(i - 1).getPos().y, points.get(i).getPos().z - points.get(i - 1).getPos().z);
					double angle = Math.toDegrees(Math.atan(twoPoints.x / twoPoints.z));
					double heightAngle = Math.toDegrees(Math.atan(twoPoints.y / Math.sqrt(Math.pow(twoPoints.x, 2) + Math.pow(twoPoints.z, 2))));

					GlStateManager.translated(middlePos.x, middlePos.y,middlePos.z);
					GlStateManager.rotated(angle, 0, 1, 0);
					GlStateManager.rotated(-heightAngle, 1,0,0);
					OBJHandler.INSTANCE.render(model);
					GlStateManager.rotated(heightAngle, 1,0,0);
					GlStateManager.rotated(-angle, 0, 1, 0);
					GlStateManager.translated(-middlePos.x, -middlePos.y,-middlePos.z);

					/*
					GlStateManager.begin(GL11.GL_LINES);
					GL11.glColor4f(1,0,0,1);
					GL11.glVertex3d(points.get(i - 1).getPos().x - blockEntity.getPos().getX(), points.get(i - 1).getPos().y - blockEntity.getPos().getY(), points.get(i - 1).getPos().z - blockEntity.getPos().getZ());
					GL11.glColor4f(1,0,0,1);
					GL11.glVertex3d(points.get(i).getPos().x - blockEntity.getPos().getX(), points.get(i).getPos().y - blockEntity.getPos().getY(), points.get(i).getPos().z - blockEntity.getPos().getZ());
					GlStateManager.end();
					
					 */
				}
				GlStateManager.endList();
			}
		}


		return false;
	}

}
