package alemax.trainsmod.blocks.tileentities.tesr;

import java.util.ArrayList;
import java.util.Collections;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import alemax.trainsmod.blocks.tileentities.TileEntityTrack;
import alemax.trainsmod.blocks.tileentities.TileEntityTrackMarker;
import alemax.trainsmod.util.TrackData;
import net.minecraft.block.BlockColored;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.animation.FastTESR;

public class FastTESRTrack extends FastTESR<TileEntityTrack> {

	@Override
	public void renderTileEntityFast(TileEntityTrack te, double x, double y, double z, float partialTicks,
			int destroyStage, float partial, BufferBuilder buffer) {
		
		BlockPos pos = te.getPos();
		BlockPos superPos = te.getSuperPos();
		TileEntity superTE = null;
		
		if(superPos != null)
			superTE = te.getWorld().getTileEntity(te.getSuperPos());
		
		if(superTE != null && superTE instanceof TileEntityTrack) {
			TrackData trackData = ((TileEntityTrack) superTE).getTrackData();
    		
			if(trackData == null) return;
			
    		ArrayList<Integer> trackPointsIndices = new ArrayList<>();
    		for(int i = 0; i < trackData.trackPoints.length - 1; i++) {
    	        Vector3d middlePoint = new Vector3d((trackData.trackPoints[i].x + trackData.trackPoints[i + 1].x) / 2.0, (trackData.trackPoints[i].y + trackData.trackPoints[i + 1].y) / 2.0, (trackData.trackPoints[i].z + trackData.trackPoints[i + 1].z) / 2.0);
    	        BlockPos middlePos = new BlockPos(middlePoint.x, middlePoint.y, middlePoint.z);
    	        
    	        if(middlePos.equals(pos)) {
    	        	int index = i;
    	        	boolean add = true;
    	        	for(int j = 0; j < trackPointsIndices.size(); j++) {
    	        		if(trackPointsIndices.get(j).intValue() == index) add = false;
    	        	}
    	        	if(add) trackPointsIndices.add(index);
    	        	index = i + 1;
    	        	add = true;
    	        	for(int j = 0; j < trackPointsIndices.size(); j++) {
    	        		if(trackPointsIndices.get(j).intValue() == index) add = false;
    	        	}
    	        	if(add) trackPointsIndices.add(index);
    	        }
    	    }
    		Vector3d[] trackPoints = new Vector3d[trackPointsIndices.size()];
    		Collections.sort(trackPointsIndices);
    		for(int i = 0; i < trackPointsIndices.size(); i++) {
    			trackPoints[i] = trackData.trackPoints[trackPointsIndices.get(i)];
    			trackPoints[i].x -= pos.getX();
    			trackPoints[i].y -= pos.getY();
    			trackPoints[i].z -= pos.getZ();
    		}
    		
    		
    		
    	    Vector3d[] leftPoints = getLeftRightPoints(trackPoints, -1.25);
    	    Vector3d[] rightPoints = getLeftRightPoints(trackPoints, 1.25);
    	    
    	    buffer.setTranslation(x,y,z);
    	    
    	    int lmCombined = getWorld().getCombinedLight(te.getPos().up(), 0);
            int lmA = lmCombined >> 16 & 65535;
            int lmB = lmCombined & 65535;
    	    
            BlockModelShapes bm = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes();
    	    TextureAtlasSprite redTexture =  bm.getTexture(Blocks.GRAVEL.getDefaultState());
    	    
    	    for(int i = 0; i < trackPoints.length - 1; i++) {
    	    	buffer.pos(leftPoints[i + 1].x, leftPoints[i + 1].y, leftPoints[i + 1].z).color(1f,1f,1f,1f).tex(redTexture.getMinU(), redTexture.getMinV()).lightmap(lmA,lmB).endVertex();
    	    	buffer.pos(leftPoints[i].x, leftPoints[i].y, leftPoints[i].z).color(1f,1f,1f,1f).tex(redTexture.getMinU(), redTexture.getMinV()).lightmap(lmA,lmB).endVertex();
    	    	buffer.pos(rightPoints[i].x, rightPoints[i].y, rightPoints[i].z).color(1f,1f,1f,1f).tex(redTexture.getMinU(), redTexture.getMinV()).lightmap(lmA,lmB).endVertex();
    	    	buffer.pos(rightPoints[i + 1].x, rightPoints[i + 1].y, rightPoints[i + 1].z).color(1f,1f,1f,1f).tex(redTexture.getMinU(), redTexture.getMinV()).lightmap(lmA,lmB).endVertex();
    	    }
    		
		}
		
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
