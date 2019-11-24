package alemax.trainsmod.block.blockentity.render;

import alemax.trainsmod.TrainsMod;
import alemax.trainsmod.block.blockentity.BlockEntityTrackSuper;
import alemax.trainsmod.global.tracknetwork.*;
import alemax.trainsmod.util.TrackPointsUtils;
import alemax.trainsmod.util.obj.OBJ;
import alemax.trainsmod.util.obj.OBJHandler;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;

import static org.lwjgl.opengl.GL11.*;

public class BERTrackSuper extends BlockEntityRenderer<BlockEntityTrackSuper> {

	public static OBJ model;
	public static Identifier track_texture;

	public static float x = 0;
	public static float y = 0;

	@Override
	public void render(BlockEntityTrackSuper blockEntity, double x, double y, double z, float partialTicks, int destroyStage) {

		if(blockEntity.trackDisplayList == 0) {
			if(!createDisplayList(blockEntity))
				return;
		}

		GlStateManager.pushMatrix();

		GlStateManager.translated(x, y, z);

		GlStateManager.bindTexture(0); // unbind current texture, since we're drawing without one

		bindTexture(track_texture);

		GlStateManager.callList(blockEntity.trackDisplayList);

		bindTexture(new Identifier("textures/block/gravel.png"));

		GlStateManager.callList(blockEntity.railbedDisplayList);

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
				Vec3d[] pointsPos = new Vec3d[points.size()];
				Vec3d[][] leftPoints = new Vec3d[37][points.size()];
				Vec3d[][] rightPoints = new Vec3d[37][points.size()];

				for(int i = 0; i < points.size(); i++) {
					pointsPos[i] = points.get(i).getPos();
				}

				for(int i = 0; i < 37; i++) {
					leftPoints[i] = TrackPointsUtils.getLeftRightPoints(pointsPos, -(44 + i) / 32f);
					rightPoints[i] = TrackPointsUtils.getLeftRightPoints(pointsPos,  (44 + i) / 32f);
				}

				blockEntity.railbedDisplayList = GlStateManager.genLists(1);
				GlStateManager.newList(blockEntity.railbedDisplayList, GL11.GL_COMPILE);

				int beX = blockEntity.getPos().getX();
				int beY = blockEntity.getPos().getY();
				int beZ = blockEntity.getPos().getZ();

				for(int i = 1; i < pointsPos.length; i++) {

					double lowerY = points.get(i - 1).getPos().y;
					if(points.get(i).getPos().y < lowerY) lowerY = points.get(i).getPos().y;
					lowerY -= Math.floor(lowerY);
					int lowerYInt = (int) Math.round(lowerY * 32);



					int startingHeight = lowerYInt - 9;
					double endHeight = 0;
					if(startingHeight < 5) endHeight = -32;
					int index = 0;
					float x;
					float z;

					//System.out.println(startingHeight);

					GL11.glBegin(GL_QUADS);
					/*
					GL11.glNormal3f(0, 1, 0);
					//GL11.glTexCoord2f(0,0);
					GL11.glVertex3f(-2,-1,2);

					GL11.glNormal3f(0, 1, 0);
					//GL11.glTexCoord2f(0,0);
					GL11.glVertex3f(2,-1,2);

					GL11.glNormal3f(0, 1, 0);
					//GL11.glTexCoord2f(0,0);
					GL11.glVertex3f(2,-1,-2);

					GL11.glNormal3f(0, 1, 0);
					//GL11.glTexCoord2f(0,0);
					GL11.glVertex3f(-2,-1,-2);

					*/



					for(; startingHeight > endHeight; startingHeight--) {
						//System.out.println(startingHeight + "\t" + endHeight);
						GL11.glColor4f(1,1,1,1);


						//TOP
						x = (float) leftPoints[index][i].x - beX;
						z = (float) leftPoints[index][i].z - beZ;
						y = (float) rightPoints[index][i].y - index / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, z);
						GL11.glVertex3f(x, y, z);

						x = (float) leftPoints[index][i - 1].x - beX;
						z = (float) leftPoints[index][i - 1].z - beZ;
						y = (float) rightPoints[index][i - 1].y - index / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, z);
						GL11.glVertex3f(x, y, z);

						x = (float) rightPoints[index][i - 1].x - beX;
						z = (float) rightPoints[index][i - 1].z - beZ;
						y = (float) rightPoints[index][i - 1].y - index / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, z);
						GL11.glVertex3f(x, y, z);

						x = (float) rightPoints[index][i].x - beX;
						z = (float) rightPoints[index][i].z - beZ;
						y = (float) rightPoints[index][i].y - index / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, z);
						GL11.glVertex3f(x, y, z);

						//FRONT
						x = (float) leftPoints[index][i - 1].x - beX;
						z = (float) leftPoints[index][i - 1].z - beZ;
						y = (float) leftPoints[index][i - 1].y - (index + 1) / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, y);
						GL11.glVertex3f(x, y, z);

						x = (float) rightPoints[index][i - 1].x - beX;
						z = (float) rightPoints[index][i - 1].z - beZ;
						y = (float) rightPoints[index][i - 1].y - (index + 1) / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, y);
						GL11.glVertex3f(x, y, z);

						x = (float) rightPoints[index][i - 1].x - beX;
						z = (float) rightPoints[index][i - 1].z - beZ;
						y = (float) rightPoints[index][i - 1].y - index / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, y);
						GL11.glVertex3f(x, y, z);

						x = (float) leftPoints[index][i - 1].x - beX;
						z = (float) leftPoints[index][i - 1].z - beZ;
						y = (float) leftPoints[index][i - 1].y - index / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, y);
						GL11.glVertex3f(x, y, z);


						//BACK
						x = (float) leftPoints[index][i].x - beX;
						z = (float) leftPoints[index][i].z - beZ;
						y = (float) leftPoints[index][i].y - index / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, y);
						GL11.glVertex3f(x, y, z);

						x = (float) rightPoints[index][i].x - beX;
						z = (float) rightPoints[index][i].z - beZ;
						y = (float) rightPoints[index][i].y - index / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, y);
						GL11.glVertex3f(x, y, z);

						x = (float) rightPoints[index][i].x - beX;
						z = (float) rightPoints[index][i].z - beZ;
						y = (float) rightPoints[index][i].y - (index + 1) / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, y);
						GL11.glVertex3f(x, y, z);

						x = (float) leftPoints[index][i].x - beX;
						z = (float) leftPoints[index][i].z - beZ;
						y = (float) leftPoints[index][i].y - (index + 1) / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, y);
						GL11.glVertex3f(x, y, z);

						//LEFT
						x = (float) leftPoints[index][i - 1].x - beX;
						z = (float) leftPoints[index][i - 1].z - beZ;
						y = (float) leftPoints[index][i - 1].y - index / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, z);
						GL11.glVertex3f(x, y, z);

						x = (float) leftPoints[index][i].x - beX;
						z = (float) leftPoints[index][i].z - beZ;
						y = (float) leftPoints[index][i].y - index / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, z);
						GL11.glVertex3f(x, y, z);

						x = (float) leftPoints[index][i].x - beX;
						z = (float) leftPoints[index][i].z - beZ;
						y = (float) leftPoints[index][i].y - (index + 1) / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, z);
						GL11.glVertex3f(x, y, z);

						x = (float) leftPoints[index][i - 1].x - beX;
						z = (float) leftPoints[index][i - 1].z - beZ;
						y = (float) leftPoints[index][i - 1].y - (index + 1) / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, z);
						GL11.glVertex3f(x, y, z);

						//RIGHT
						x = (float) rightPoints[index][i - 1].x - beX;
						z = (float) rightPoints[index][i - 1].z - beZ;
						y = (float) rightPoints[index][i - 1].y - (index + 1) / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, z);
						GL11.glVertex3f(x, y, z);

						x = (float) rightPoints[index][i].x - beX;
						z = (float) rightPoints[index][i].z - beZ;
						y = (float) rightPoints[index][i].y - (index + 1) / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, z);
						GL11.glVertex3f(x, y, z);

						x = (float) rightPoints[index][i].x - beX;
						z = (float) rightPoints[index][i].z - beZ;
						y = (float) rightPoints[index][i].y - index / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, z);
						GL11.glVertex3f(x, y, z);

						x = (float) rightPoints[index][i - 1].x - beX;
						z = (float) rightPoints[index][i - 1].z - beZ;
						y = (float) rightPoints[index][i - 1].y - index / 32.0f - 9f / 32 - beY;
						GL11.glNormal3f(0, 1, 0);
						GL11.glTexCoord2f(x, z);
						GL11.glVertex3f(x, y, z);


						index++;
						if(index > 50) break;
					}


					GL11.glEnd();


				}

				GlStateManager.endList();

				blockEntity.trackDisplayList = GlStateManager.genLists(1);
				GlStateManager.newList(blockEntity.trackDisplayList, GL11.GL_COMPILE);
				for(int i = 1; i < points.size(); i++) {
					Vec3d middlePos = new Vec3d(
							(points.get(i - 1).getPos().x + points.get(i).getPos().x) / 2.0 - blockEntity.getPos().getX(),
							(points.get(i - 1).getPos().y + points.get(i).getPos().y) / 2.0 - blockEntity.getPos().getY() - (7.0 / 16), //TODO: Change the 7 / 16 if someday we will change change top pos of track
							(points.get(i - 1).getPos().z + points.get(i).getPos().z) / 2.0 - blockEntity.getPos().getZ());
					Vec3d twoPoints = new Vec3d(points.get(i - 1).getPos().x - points.get(i).getPos().x, points.get(i).getPos().y - points.get(i - 1).getPos().y, points.get(i).getPos().z - points.get(i - 1).getPos().z);
					double angle = Math.toDegrees(Math.atan2(twoPoints.x, twoPoints.z));
					double heightAngle = Math.toDegrees(Math.atan(Math.abs(twoPoints.y)));
					Vec3d rotVec = twoPoints.crossProduct(new Vec3d(0,1,0));

					//System.out.println(heightAngle + "\t" + angle + "\t" + -twoPoints.x + "\t" + twoPoints.z);

					GlStateManager.translated(middlePos.x, middlePos.y,middlePos.z);
					//GlStateManager.rotated(30, 0, 1, 0);
					//GlStateManager.rotated(45, 1,0,0);

					GL11.glRotatef((float) -angle,0,1,0);

					GL11.glRotatef((float)heightAngle,1,0,0);

					OBJHandler.INSTANCE.render(model);

					GL11.glRotatef((float)-heightAngle,1,0,0);

					GL11.glRotatef((float) angle,0,1,0);

					GlStateManager.translated(-middlePos.x, -middlePos.y,-middlePos.z);
				}
				GlStateManager.endList();

			}

		}


		return false;
	}

	@Override
	public boolean method_3563(BlockEntityTrackSuper blockEntity_1) {
		return true;
	}
}
