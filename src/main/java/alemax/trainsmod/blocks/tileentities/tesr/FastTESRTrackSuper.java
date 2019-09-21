package alemax.trainsmod.blocks.tileentities.tesr;

import java.util.ArrayList;
import java.util.Collections;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import org.lwjgl.util.vector.Vector3f;

import alemax.trainsmod.blocks.tileentities.TileEntityTrackSuper;
import alemax.trainsmod.util.TrackData;
import alemax.trainsmod.util.objloader.Obj.Face;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.animation.FastTESR;

public class FastTESRTrackSuper extends FastTESR<TileEntityTrackSuper>{

	@Override
	public void renderTileEntityFast(TileEntityTrackSuper te, double x, double y, double z, float partialTicks,
			int destroyStage, float partial, BufferBuilder buffer) {
		
		BlockPos pos = te.getPos();
		
		TrackData trackData = te.getTrackData();
    		
		if(trackData == null || trackData.trackPoints == null) return;
			
    	Vector3d[] transTrackPoints = new Vector3d[trackData.trackPoints.length];
    	Vector3d[] middlePoints = new Vector3d[transTrackPoints.length - 1];
    	
    	for(int i = 0; i < transTrackPoints.length; i++) {
    		transTrackPoints[i] = trackData.trackPoints[i];
    		//transTrackPoints[i].x -= pos.getX();
    		//transTrackPoints[i].y -= pos.getY();
    		//transTrackPoints[i].z -= pos.getZ();
    	}
    	
    	for(int i = 0; i < middlePoints.length; i++) {
    		middlePoints[i] = new Vector3d(
    				(transTrackPoints[i].x + transTrackPoints[i+1].x) / 2 - pos.getX(), 
    				(transTrackPoints[i].y + transTrackPoints[i+1].y) / 2 - pos.getY(), 
    				(transTrackPoints[i].z + transTrackPoints[i+1].z) / 2 - pos.getZ());
    	}
    		
    		
    		
    	//Vector3d[] leftPoints = getLeftRightPoints(transTrackPoints, -1.25);
    	//Vector3d[] rightPoints = getLeftRightPoints(transTrackPoints, 1.25);
    	
    	//TODO: Checken, ob man die trans hier und oben beide einfach weglassen kann
    	buffer.setTranslation(x,y,z);
    	    
    	//int lmCombined = getWorld().getCombinedLight(te.getPos().up(), 0);
        //int lmA = lmCombined >> 16 & 65535;
        //int lmB = lmCombined & 65535;
    	    
        BlockModelShapes bm = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes();
    	TextureAtlasSprite redTexture =  bm.getTexture(Blocks.GRAVEL.getDefaultState());
    	
    	for(int i = 0; i < middlePoints.length; i++) {
    		//buffer.setTranslation(middlePoints[i].x, middlePoints[i].y, middlePoints[i].z);
    		
    		int lmCombined = getWorld().getCombinedLight(new BlockPos(middlePoints[i].x + pos.getX(), middlePoints[i].y + pos.getY(), middlePoints[i].z + pos.getZ()), 0);
	        int lmA = lmCombined >> 16 & 65535;
	        int lmB = lmCombined & 65535;
    		
    		for(Face face : te.MODEL.getFaces()) {
    			
    			
    			
    			if(face.getVertices().length == 4) {
    				
	    			Vector3f[] vertices = {
	    				new Vector3f(te.MODEL.getVertices().get(face.getVertices()[0] - 1)),
	    			    new Vector3f(te.MODEL.getVertices().get(face.getVertices()[1] - 1)),
	    				new Vector3f(te.MODEL.getVertices().get(face.getVertices()[2] - 1)),
	    				new Vector3f(te.MODEL.getVertices().get(face.getVertices()[3] - 1))
	                };
	    			
	    			
	    			
	    			buffer.pos(vertices[0].x + middlePoints[i].x, vertices[0].y + middlePoints[i].y, vertices[0].z + middlePoints[i].z).color(1f,1f,1f,1f).tex(redTexture.getMinU(), redTexture.getMinV()).lightmap(lmA,lmB).endVertex();
	    			buffer.pos(vertices[1].x + middlePoints[i].x, vertices[1].y + middlePoints[i].y, vertices[1].z + middlePoints[i].z).color(1f,1f,1f,1f).tex(redTexture.getMinU(), redTexture.getMinV()).lightmap(lmA,lmB).endVertex();
	    			buffer.pos(vertices[2].x + middlePoints[i].x, vertices[2].y + middlePoints[i].y, vertices[2].z + middlePoints[i].z).color(1f,1f,1f,1f).tex(redTexture.getMinU(), redTexture.getMinV()).lightmap(lmA,lmB).endVertex();
	    			buffer.pos(vertices[3].x + middlePoints[i].x, vertices[3].y + middlePoints[i].y, vertices[3].z + middlePoints[i].z).color(1f,1f,1f,1f).tex(redTexture.getMinU(), redTexture.getMinV()).lightmap(lmA,lmB).endVertex();
    				
	    			
    			} else {
	    			Vector3f[] vertices = {
	    				new Vector3f(te.MODEL.getVertices().get(face.getVertices()[0] - 1)),
	    				new Vector3f(te.MODEL.getVertices().get(face.getVertices()[1] - 1)),
		    			new Vector3f(te.MODEL.getVertices().get(face.getVertices()[2] - 1))
		            };
	    			
	    				
	    				
		    		buffer.pos(vertices[0].x + middlePoints[i].x, vertices[0].y + middlePoints[i].y, vertices[0].z + middlePoints[i].z).color(1f,1f,1f,1f).tex(redTexture.getMinU(), redTexture.getMinV()).lightmap(lmA,lmB).endVertex();
		    		buffer.pos((vertices[0].x + vertices[1].x) / 2 + middlePoints[i].x,(vertices[0].y + vertices[1].y) / 2 + middlePoints[i].y,(vertices[0].z + vertices[1].z) / 2 + middlePoints[i].z).color(1f,1f,1f,1f).tex(redTexture.getMinU(), redTexture.getMinV()).lightmap(lmA,lmB).endVertex();
		    		buffer.pos(vertices[1].x + middlePoints[i].x, vertices[1].y + middlePoints[i].y, vertices[1].z + middlePoints[i].z).color(1f,1f,1f,1f).tex(redTexture.getMinU(), redTexture.getMinV()).lightmap(lmA,lmB).endVertex();
		    		buffer.pos(vertices[2].x + middlePoints[i].x, vertices[2].y + middlePoints[i].y, vertices[2].z + middlePoints[i].z).color(1f,1f,1f,1f).tex(redTexture.getMinU(), redTexture.getMinV()).lightmap(lmA,lmB).endVertex();
		    			
    			}
    		}
    	}
    	
    	/*
    	for(int i = 0; i < transTrackPoints.length - 1; i++) {
    	    buffer.pos(leftPoints[i + 1].x, leftPoints[i + 1].y, leftPoints[i + 1].z).color(1f,1f,1f,1f).tex(redTexture.getMinU(), redTexture.getMinV()).lightmap(lmA,lmB).endVertex();
    	    buffer.pos(leftPoints[i].x, leftPoints[i].y, leftPoints[i].z).color(1f,1f,1f,1f).tex(redTexture.getMinU(), redTexture.getMinV()).lightmap(lmA,lmB).endVertex();
    	    buffer.pos(rightPoints[i].x, rightPoints[i].y, rightPoints[i].z).color(1f,1f,1f,1f).tex(redTexture.getMinU(), redTexture.getMinV()).lightmap(lmA,lmB).endVertex();
    	    buffer.pos(rightPoints[i + 1].x, rightPoints[i + 1].y, rightPoints[i + 1].z).color(1f,1f,1f,1f).tex(redTexture.getMinU(), redTexture.getMinV()).lightmap(lmA,lmB).endVertex();
    	}
    	*/
		
	}
	
	private Vector3d[] getLeftRightPoints(Vector3d[] points, double direction) {
		Vector3d[] newPoints = new Vector3d[points.length];
		if(points.length > 0) {
			Vector2d[] currentPoints = {new Vector2d(2 * points[0].x - points[1].x, 2 * points[0].z - points[1].z), new Vector2d(points[0].x, points[0].z) , new Vector2d(points[1].x, points[1].z)};
			Vector2d point = getLeftRightPoint(currentPoints, direction);
			newPoints[0] = new Vector3d(point.x, points[0].y, point.y);
		}
		for(int i = 1; i < points.length - 1; i++) {
			Vector2d[] currentPoints = {new Vector2d(points[i - 1].x, points[i - 1].z), new Vector2d(points[i].x, points[i].z), new Vector2d(points[i + 1].x, points[i + 1].z)};
			Vector2d point = getLeftRightPoint(currentPoints, direction);
			newPoints[i] = new Vector3d(point.x, points[i].y, point.y);
		}
		if(points.length > 1) {
			Vector2d[] currentPoints = {new Vector2d(points[points.length - 2].x, points[points.length - 2].z), new Vector2d(points[points.length - 1].x, points[points.length - 1].z), new Vector2d(2 * points[points.length - 1].x - points[points.length - 2].x, 2 * points[points.length - 1].z - points[points.length - 2].z)};
			Vector2d point = getLeftRightPoint(currentPoints, direction);
			newPoints[points.length - 1] = new Vector3d(point.x, points[points.length - 1].y, point.y);
		}
		return newPoints;
	}
	
	private Vector2d getLeftRightPoint(Vector2d[] point, double direction) {
		if(point.length == 3) {
			Vector2d ab = new Vector2d(point[0].x - point[1].x, point[0].y - point[1].y);
			Vector2d bc = new Vector2d(point[2].x - point[1].x, point[2].y - point[1].y);
			double angleDifference = getCounterClockwiseAngle(ab, bc);
			double angle = getAngleBetweenPoints(point[1], point[0]);
			double newAngle = angle - (angleDifference / 2.0);
			return new Vector2d(point[1].x - Math.sin(Math.toRadians(newAngle)) * direction, point[1].y + Math.cos(Math.toRadians(newAngle)) * direction);
		}
		return null;
	}
	
	private double getCounterClockwiseAngle(Vector2d firstVector, Vector2d secondVector) {
		double dot = firstVector.x * secondVector.x + firstVector.y * secondVector.y;     
		double det = firstVector.x * secondVector.y - firstVector.y * secondVector.x;
		double angle = Math.toDegrees(Math.atan2(det, dot));
		if(angle < 0) angle += 360;
		return angle;
	}
	
	private double getAngleBetweenPoints(Vector2d firstPoint, Vector2d secondPoint) {
		Vector2d direction = new Vector2d(secondPoint.x - firstPoint.x, secondPoint.y - firstPoint.y);
		double angle = Math.toDegrees(direction.angle(new Vector2d(0,1)));
		double angleTo90 = (float) Math.toDegrees(direction.angle(new Vector2d(-1, 0)));
		
		if(angleTo90 > 90) angle = 360 - angle;
		return angle;
	}

}
