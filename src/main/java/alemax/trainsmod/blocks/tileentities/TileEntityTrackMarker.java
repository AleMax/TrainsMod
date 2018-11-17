package alemax.trainsmod.blocks.tileentities;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import alemax.trainsmod.TrainsMod;
import alemax.trainsmod.init.ModBlocks;
import alemax.trainsmod.util.TrackType;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

public class TileEntityTrackMarker extends TileEntity {
	
	private static double CIRCLE_INCREMENT_STEPS = 0.01;
	
	private String channel;
	private float angle;
	private byte height;
	private TrackType trackType;
	
	private float realAngle;
	private Ticket chunkLoaderTicket;
	
	public void setup(String channel) {
		this.channel = channel;
		this.angle = 0;
		this.height = 5;
		this.trackType = TrackType.CONCRETE;
		markDirty();
	}
	
	public void setTicket(Ticket chunkLoaderTicket) {
		this.chunkLoaderTicket = chunkLoaderTicket;
		ChunkPos pos = new ChunkPos(getPos());
		ForgeChunkManager.forceChunk(chunkLoaderTicket, new ChunkPos(getPos()));
	}
	
	public void unloadChunk() {
		if(chunkLoaderTicket != null) {
			ForgeChunkManager.unforceChunk(chunkLoaderTicket, new ChunkPos(getPos()));
			ForgeChunkManager.releaseTicket(chunkLoaderTicket);
		}
	}
	
	public Ticket getChunkLoaderTicket() {
		return chunkLoaderTicket;
	}
	
	public void set(String channel, float angle, byte height) {
		this.channel = channel;
		this.angle = angle;
		this.height = height;
		markDirty();
	}
	
	public void setTrackType(TrackType trackType) {
		this.trackType = trackType;
	}
	
	public void buildTrack() {
		List<TileEntity> tileEntities = world.loadedTileEntityList;
		List<TileEntity> foundTileEntities = new ArrayList<>();
		for(TileEntity te : tileEntities) {
			if(te instanceof TileEntityTrackMarker) {
				if(((TileEntityTrackMarker) te).channel.equalsIgnoreCase(this.channel)) {
					BlockPos tePos = ((TileEntityTrackMarker) te).getPos();
					if(tePos.getX() != getPos().getX() || tePos.getY() != getPos().getY() || tePos.getZ() != getPos().getZ()) {
						foundTileEntities.add(te);
					}
				}
			}
		}
		if(foundTileEntities.size() == 1) {
			TileEntity tileEntity = foundTileEntities.get(0);
			double distance = Math.sqrt(Math.pow(tileEntity.getPos().getX() - getPos().getX(), 2) + Math.pow(tileEntity.getPos().getZ() - getPos().getZ(), 2));
			if(distance > 1.9) {
				List<Vector2d> points = calculateTrack((TileEntityTrackMarker) tileEntity, distance);
				Vector3d[] points3d = getPointsWithHeight(points, (TileEntityTrackMarker) tileEntity);
				
				Vector3d[] nextPoints = getLeftRightPoints(points3d, 1);
				Vector3d[] next2Points = getLeftRightPoints(points3d, -1);
				
				//System.out.println(points.size());
				//System.out.println(nextPoints.size());
				
				for(int i = 0; i < points.size(); i++) {
					this.world.setBlockState(new BlockPos(points3d[i].x, points3d[i].y, points3d[i].z), ModBlocks.track.getDefaultState());
				}
				
			} else {
				System.out.println("TO CLOSE TO EACH OTHER");
				//error message
			}
		} else {
			System.out.println("NO 2 MARKERS");
			//error message
		}
	}
	
	private Vector3d[] getPointsWithHeight(List<Vector2d> points2d, TileEntityTrackMarker otherTileEntity) {
		Vector3d[] points3d = new Vector3d[points2d.size()];
		double currentHeighth = this.pos.getY() + (this.height / 16.0) + 1;
		double finalHeighth = otherTileEntity.pos.getY() + (otherTileEntity.height / 16.0) + 1;
		double heightDifference = (finalHeighth - currentHeighth) / (points3d.length - 1);
		
		for(int i = 0; i < points3d.length; i++) {
			points3d[i] = new Vector3d(points2d.get(i).x, currentHeighth + heightDifference * i , points2d.get(i).y);
		}
		return points3d;
	}
	
	
	private List<Vector2d> calculateTrack(TileEntityTrackMarker otherTileEntity, double distance) {
		synchronizeAngle(otherTileEntity);
		otherTileEntity.synchronizeAngle(this);
		byte firstSide = checkSide(otherTileEntity, distance);
		byte secondSide = otherTileEntity.checkSide(this, distance);

		if(firstSide < 0 && secondSide < 0 || firstSide > 0 && secondSide > 0) {
			float radius = 0;
			float circleMiddleDistance = (float) distance;
			
			Vector2d firstSideVector = getLeftRightVector(firstSide);
			Vector2d secondSideVector = otherTileEntity.getLeftRightVector(secondSide);
			Vector2d firstMiddlePoint = new Vector2d(getPos().getX() + 0.5, getPos().getZ() + 0.5);
			Vector2d secondMiddlePoint = new Vector2d(otherTileEntity.getPos().getX() + 0.5, otherTileEntity.getPos().getZ() + 0.5);
			
			while(2 * radius < circleMiddleDistance) {
				radius += CIRCLE_INCREMENT_STEPS;
				firstMiddlePoint.add(firstSideVector);
				secondMiddlePoint.add(secondSideVector);
				circleMiddleDistance = (float) Math.sqrt(Math.pow(secondMiddlePoint.x - firstMiddlePoint.x, 2) + Math.pow(secondMiddlePoint.y - firstMiddlePoint.y, 2));
			}
			double firstAngle = getAngleBetween(firstMiddlePoint, secondMiddlePoint);
			double secondAngle = otherTileEntity.getAngleBetween(secondMiddlePoint, firstMiddlePoint);
			
			double firstLength = radius * firstAngle;
			double secondLength = radius * secondAngle;
			double addedLength = firstLength + secondLength;
			double scaleFactor = addedLength / Math.round(addedLength);
			double firstScaled = firstLength / scaleFactor - 0.5;
			double secondScaled = secondLength / scaleFactor - 0.5;
			int firstSteps = (int) Math.round(Math.floor(firstScaled));
			int secondSteps = (int) Math.round(Math.floor(secondScaled));
			
			if(Math.abs(firstScaled - Math.round(firstScaled)) < 0.001 && Math.abs(secondScaled - Math.round(secondScaled)) < 0.001) { //FIX IT (10.5, 10.5)
				firstSteps = (int) Math.round(firstScaled);
				secondSteps = (int) (Math.round(secondScaled) - 1);
			}
			
			double firstAngleToLastPoint = 0;
			double secondAngleToLastPoint = 0;
			if(firstSide > 0) {
				firstAngleToLastPoint = getAngleBetweenPoints(firstMiddlePoint, new Vector2d(getPos().getX() + 0.5, getPos().getZ() + 0.5)) + ((firstSteps + 0.5) * scaleFactor) / firstLength * Math.toDegrees(firstAngle);
				double x = getAngleBetweenPoints(firstMiddlePoint, new Vector2d(getPos().getX() + 0.5, getPos().getZ() + 0.5));
				double y = ((firstSteps + 0.5) * scaleFactor) / firstLength * Math.toDegrees(firstAngle);
				secondAngleToLastPoint = getAngleBetweenPoints(secondMiddlePoint, new Vector2d(otherTileEntity.getPos().getX() + 0.5, otherTileEntity.getPos().getZ() + 0.5)) + ((secondSteps + 0.5) * scaleFactor) / secondLength * Math.toDegrees(secondAngle);
				
			} else {
				firstAngleToLastPoint = getAngleBetweenPoints(firstMiddlePoint, new Vector2d(getPos().getX() + 0.5, getPos().getZ() + 0.5)) - ((firstSteps + 0.5) * scaleFactor) / firstLength * Math.toDegrees(firstAngle);
				secondAngleToLastPoint = getAngleBetweenPoints(secondMiddlePoint, new Vector2d(otherTileEntity.getPos().getX() + 0.5, otherTileEntity.getPos().getZ() + 0.5)) - ((secondSteps + 0.5) * scaleFactor) / secondLength * Math.toDegrees(secondAngle);
			}
			if(firstAngleToLastPoint < 0) firstAngleToLastPoint += 360;
			else if(firstAngleToLastPoint > 360) firstAngleToLastPoint -= 360;
			
			if(secondAngleToLastPoint < 0) secondAngleToLastPoint += 360;
			else if(secondAngleToLastPoint > 360) secondAngleToLastPoint -= 360;
			
			Vector2d firstLastPoint = new Vector2d(-Math.sin(Math.toRadians(firstAngleToLastPoint)) * radius, Math.cos(Math.toRadians(firstAngleToLastPoint)) * radius);
			Vector2d secondLastPoint = new Vector2d(-Math.sin(Math.toRadians(secondAngleToLastPoint)) * radius, Math.cos(Math.toRadians(secondAngleToLastPoint)) * radius);
			
			double firstAngleDifference = Math.toDegrees(firstLastPoint.angle(new Vector2d(pos.getX() + 0.5 - firstMiddlePoint.x, pos.getZ() + 0.5 - firstMiddlePoint.y)));
			double secondAngleDifference = Math.toDegrees(secondLastPoint.angle(new Vector2d(otherTileEntity.pos.getX() + 0.5 - secondMiddlePoint.x, otherTileEntity.pos.getZ() + 0.5 - secondMiddlePoint.y)));
			
			double fad = this.realAngle - getAngleBetweenPoints(new Vector2d(firstLastPoint.x + firstMiddlePoint.x, firstLastPoint.y + firstMiddlePoint.y), new Vector2d(secondLastPoint.x + secondMiddlePoint.x, secondLastPoint.y + secondMiddlePoint.y));
			fad = Math.abs(fad);
			if(fad > 180) fad = 360 - fad;
			
			double sad = otherTileEntity.realAngle -  getAngleBetweenPoints(new Vector2d(secondLastPoint.x + secondMiddlePoint.x, secondLastPoint.y + secondMiddlePoint.y), new Vector2d(firstLastPoint.x + firstMiddlePoint.x, firstLastPoint.y + firstMiddlePoint.y));
			sad = Math.abs(sad);
			if(sad > 180) sad = 360 - sad;
			
			ArrayList<Vector2d> firstPoints = new ArrayList<>();
			ArrayList<Vector2d> secondPoints = new ArrayList<>();
			
			Vector2d currentPoint = new Vector2d(-Math.sin(Math.toRadians(this.realAngle)) * 0.5 * scaleFactor, Math.cos(Math.toRadians(this.realAngle)) * 0.5 * scaleFactor);
			Vector2d aimedPoint = new Vector2d(firstMiddlePoint.x - (pos.getX() + 0.5) + firstLastPoint.x, firstMiddlePoint.y - (pos.getZ() + 0.5) + firstLastPoint.y);
			double currentAngle = this.realAngle;
			double angleSteps = fad / (firstSteps + 1);
			byte incrementFactor = 1;
			
			if(firstSide < 0) incrementFactor = -1;
			
			firstPoints.add(new Vector2d(currentPoint));
			for(int i = 0; i < firstSteps; i++) {
				currentAngle = currentAngle + (incrementFactor * angleSteps);
				currentPoint.x += (- Math.sin(Math.toRadians(currentAngle)));
				currentPoint.y += Math.cos(Math.toRadians(currentAngle));
				
				firstPoints.add(new Vector2d(currentPoint));
			}
			
			double xScale = aimedPoint.x / currentPoint.x;
			double yScale = aimedPoint.y / currentPoint.y;
			
			for(int i = 0; i < firstPoints.size(); i++) {
				firstPoints.get(i).x *= xScale;
				firstPoints.get(i).y *= yScale;
			}
			
			currentPoint = new Vector2d(-Math.sin(Math.toRadians(otherTileEntity.realAngle)) * 0.5 * scaleFactor, Math.cos(Math.toRadians(otherTileEntity.realAngle)) * 0.5 * scaleFactor);
			aimedPoint = new Vector2d(secondMiddlePoint.x - (otherTileEntity.pos.getX() + 0.5) + secondLastPoint.x, secondMiddlePoint.y - (otherTileEntity.pos.getZ() + 0.5) + secondLastPoint.y);
			currentAngle = otherTileEntity.realAngle;
			angleSteps = sad / (secondSteps + 1);
			
			secondPoints.add(new Vector2d(currentPoint));
			for(int i = 0; i < secondSteps; i++) {
				currentAngle = currentAngle + (incrementFactor * angleSteps);
				currentPoint.x += (- Math.sin(Math.toRadians(currentAngle)));
				currentPoint.y += Math.cos(Math.toRadians(currentAngle));
				
				secondPoints.add(new Vector2d(currentPoint));
			}
			
			xScale = aimedPoint.x / currentPoint.x;
			yScale = aimedPoint.y / currentPoint.y;
			
			for(int i = 0; i < secondPoints.size(); i++) {
				secondPoints.get(i).x *= xScale;
				secondPoints.get(i).y *= yScale;
			}
			
			ArrayList<Vector2d> finalPoints = new ArrayList<>();
			finalPoints.add(new Vector2d(this.pos.getX() + 0.5, this.pos.getZ() + 0.5));
			for(int i = 0; i < firstPoints.size(); i++) {
				finalPoints.add(new Vector2d(this.pos.getX() + 0.5 + firstPoints.get(i).x, this.pos.getZ() + 0.5 + firstPoints.get(i).y));
			}
			
			for(int i = secondPoints.size() - 1; i > -1; i--) {
				finalPoints.add(new Vector2d(otherTileEntity.pos.getX() + 0.5 + secondPoints.get(i).x, otherTileEntity.pos.getZ() + 0.5 + secondPoints.get(i).y));
			}
			finalPoints.add(new Vector2d(otherTileEntity.pos.getX() + 0.5, otherTileEntity.pos.getZ() + 0.5));
			
			return finalPoints;
		} else if(firstSide == 0 && secondSide == 0) {
			int steps = (int) Math.round(distance);
			Vector2d direction = new Vector2d(otherTileEntity.pos.getX() - this.pos.getX(), otherTileEntity.pos.getZ() - this.pos.getZ());
			direction.scale(1.0 / steps);
			ArrayList<Vector2d> finalPoints = new ArrayList<>();
			for(int i = 0; i < steps + 1; i++) {
				finalPoints.add(new Vector2d(this.pos.getX() + 0.5 + (direction.x * i), this.pos.getZ() + 0.5 + (direction.y * i)));
			}
			return finalPoints;
		} else {
			Vector2d intersect = null;
			double firstDistance = 0;
			double secondDistance = 0;
			try {
				intersect = getLineIntersection(new Vector2d(this.pos.getX() + 0.5, this.pos.getZ() + 0.5), new Vector2d(this.pos.getX() + 0.5 - Math.sin(Math.toRadians(this.realAngle)), this.pos.getZ() + 0.5 + Math.cos(Math.toRadians(this.realAngle))), new Vector2d(otherTileEntity.pos.getX() + 0.5, otherTileEntity.pos.getZ() + 0.5), new Vector2d(otherTileEntity.pos.getX() + 0.5 - Math.sin(Math.toRadians(otherTileEntity.realAngle)), otherTileEntity.pos.getZ() + 0.5 + Math.cos(Math.toRadians(otherTileEntity.realAngle))));
				firstDistance = Math.sqrt(Math.pow(intersect.x - (this.pos.getX() + 0.5), 2) + Math.pow(intersect.y - (this.pos.getZ() + 0.5), 2));
				secondDistance = Math.sqrt(Math.pow(intersect.x - (otherTileEntity.pos.getX() + 0.5), 2) + Math.pow(intersect.y - (otherTileEntity.pos.getZ() + 0.5), 2));
			} catch (Exception e) {
				
				return null;
			}
				
				
			if(secondDistance > firstDistance) {
				Vector2d sideVector = getLeftRightVector(firstSide);
				Vector2d otherLineVector = new Vector2d(Math.sin(Math.toRadians(otherTileEntity.realAngle)), -Math.cos(Math.toRadians(otherTileEntity.realAngle)));
				double alpha = 0;
				if(firstSide < 1) alpha = getCounterClockwiseAngle(otherLineVector, sideVector);
				else alpha = getCounterClockwiseAngle(sideVector, otherLineVector);
				alpha *= -1;
				if(alpha < 0) alpha += 360;
				
				double radius = Math.cos(Math.toRadians(alpha)) * firstDistance / (1 - Math.sin(Math.toRadians(alpha)));
				sideVector.normalize();
				Vector2d middlePoint = new Vector2d(this.pos.getX() + 0.5 + sideVector.x * radius, this.pos.getZ() + 0.5 + sideVector.y * radius);
				Vector2d perp = getPerpendicularPoint(new Vector2d(otherTileEntity.pos.getX() + 0.5, otherTileEntity.pos.getZ() + 0.5), new Vector2d(otherTileEntity.pos.getX() + 0.5 - Math.sin(Math.toRadians(otherTileEntity.realAngle)), otherTileEntity.pos.getZ() + 0.5 + Math.cos(Math.toRadians(otherTileEntity.realAngle))), middlePoint);
				
				System.out.println(perp.x + "\t" + perp.y);
				
				double angleDifference = this.realAngle - (otherTileEntity.realAngle + 180) % 360;
				angleDifference = Math.abs(angleDifference);
				if(angleDifference > 180) angleDifference = 360 - angleDifference;
				
				double circleLength = 2 * radius * Math.PI * angleDifference / 360.0;
				double scaleFactor = circleLength / Math.round(circleLength);
				double circleScaled = circleLength / scaleFactor - 0.5;
				int circleSteps = (int) Math.round(Math.floor(circleScaled));
				
				Vector2d currentPoint = new Vector2d(-Math.sin(Math.toRadians(this.realAngle)) * 0.5 * scaleFactor, Math.cos(Math.toRadians(this.realAngle)) * 0.5 * scaleFactor);
				Vector2d aimedPoint = new Vector2d(perp.x - (this.pos.getX() + 0.5), perp.y - (this.pos.getZ() + 0.5));
				double currentAngle = this.realAngle;
				double angleSteps = angleDifference / (circleSteps + 1);
				byte incrementFactor = 1;
				
				ArrayList<Vector2d> circlePoints = new ArrayList<Vector2d>();
				
				if(firstSide < 0) incrementFactor = -1;
				
				circlePoints.add(new Vector2d(currentPoint));
				for(int i = 0; i < circleSteps; i++) {
					currentAngle = currentAngle + (incrementFactor * angleSteps);
					currentPoint.x += (- Math.sin(Math.toRadians(currentAngle)));
					currentPoint.y += Math.cos(Math.toRadians(currentAngle));
					
					circlePoints.add(new Vector2d(currentPoint));
				}
				
				double xScale = aimedPoint.x / currentPoint.x;
				double yScale = aimedPoint.y / currentPoint.y;
				
				for(int i = 0; i < circlePoints.size(); i++) {
					circlePoints.get(i).x *= xScale;
					circlePoints.get(i).y *= yScale;
				}
				
				ArrayList<Vector2d> linePoints = new ArrayList<>();
				
				Vector2d lineDirection = new Vector2d(otherTileEntity.pos.getX() + 0.5 - perp.x, otherTileEntity.pos.getZ() + 0.5 - perp.y);
				double lineLength = lineDirection.length();
				int steps = (int) Math.round(lineLength);
				if(steps == 0) steps = 1;
				lineDirection.scale(1.0 / steps);
				for(int i = 1; i < steps + 1; i++) {
					linePoints.add(new Vector2d(perp.x + (lineDirection.x * i), perp.y + (lineDirection.y * i)));
				}
				
				ArrayList<Vector2d> finalPoints = new ArrayList<>();
				finalPoints.add(new Vector2d(this.pos.getX() + 0.5, this.pos.getZ() + 0.5));
				for(int i = 0; i < circlePoints.size(); i++) {
					finalPoints.add(new Vector2d(this.pos.getX() + 0.5 + circlePoints.get(i).x, this.pos.getZ() + 0.5 + circlePoints.get(i).y));
				}
				
				for(int i = 0; i < linePoints.size(); i++) {
					finalPoints.add(new Vector2d(linePoints.get(i).x,linePoints.get(i).y));
				}
				
				return finalPoints;	
			} else {
				Vector2d sideVector = otherTileEntity.getLeftRightVector(secondSide);
				Vector2d otherLineVector = new Vector2d(Math.sin(Math.toRadians(this.realAngle)), -Math.cos(Math.toRadians(this.realAngle)));
				double alpha = 0;
				if(firstSide < 1) alpha = getCounterClockwiseAngle(sideVector, otherLineVector);
				else alpha = getCounterClockwiseAngle(otherLineVector, sideVector);
				alpha *= -1;
				if(alpha < 0) alpha += 360;
				
				double radius = Math.cos(Math.toRadians(alpha)) * secondDistance / (1 - Math.sin(Math.toRadians(alpha))); //ich hoffe doch..
				sideVector.normalize();
				Vector2d middlePoint = new Vector2d(otherTileEntity.pos.getX() + 0.5 + sideVector.x * radius, otherTileEntity.pos.getZ() + 0.5 + sideVector.y * radius);
				Vector2d perp = getPerpendicularPoint(new Vector2d(this.pos.getX() + 0.5, this.pos.getZ() + 0.5), new Vector2d(this.pos.getX() + 0.5 - Math.sin(Math.toRadians(this.realAngle)), this.pos.getZ() + 0.5 + Math.cos(Math.toRadians(this.realAngle))), middlePoint);
				//System.out.println(radius);
				
				double angleDifference = otherTileEntity.realAngle - (this.realAngle + 180) % 360;
				angleDifference = Math.abs(angleDifference);
				if(angleDifference > 180) angleDifference = 360 - angleDifference;
				
				double circleLength = 2 * radius * Math.PI * angleDifference / 360.0;
				double scaleFactor = circleLength / Math.round(circleLength);
				double circleScaled = circleLength / scaleFactor - 0.5;
				int circleSteps = (int) Math.round(Math.floor(circleScaled));
				
				Vector2d currentPoint = new Vector2d(-Math.sin(Math.toRadians(otherTileEntity.realAngle)) * 0.5 * scaleFactor, Math.cos(Math.toRadians(otherTileEntity.realAngle)) * 0.5 * scaleFactor);
				Vector2d aimedPoint = new Vector2d(perp.x - (otherTileEntity.pos.getX() + 0.5), perp.y - (otherTileEntity.pos.getZ() + 0.5));
				double currentAngle = otherTileEntity.realAngle;
				double angleSteps = angleDifference / (circleSteps + 1);
				byte incrementFactor = 1;
				
				ArrayList<Vector2d> circlePoints = new ArrayList<Vector2d>();
				
				if(secondSide < 0) incrementFactor = -1;
				
				circlePoints.add(new Vector2d(currentPoint));
				for(int i = 0; i < circleSteps; i++) {
					currentAngle = currentAngle + (incrementFactor * angleSteps);
					currentPoint.x += (- Math.sin(Math.toRadians(currentAngle)));
					currentPoint.y += Math.cos(Math.toRadians(currentAngle));
					
					circlePoints.add(new Vector2d(currentPoint));
				}
				
				double xScale = aimedPoint.x / currentPoint.x;
				double yScale = aimedPoint.y / currentPoint.y;
				
				for(int i = 0; i < circlePoints.size(); i++) {
					circlePoints.get(i).x *= xScale;
					circlePoints.get(i).y *= yScale;
				}
				
				ArrayList<Vector2d> linePoints = new ArrayList<>();
				
				Vector2d lineDirection = new Vector2d(this.pos.getX() + 0.5 - perp.x, this.pos.getZ() + 0.5 - perp.y);
				double lineLength = lineDirection.length();
				int steps = (int) Math.round(lineLength);
				if(steps == 0) steps = 1;
				lineDirection.scale(1.0 / steps);
				for(int i = 1; i < steps + 1; i++) {
					linePoints.add(new Vector2d(perp.x + (lineDirection.x * i), perp.y + (lineDirection.y * i)));
				}
				
				ArrayList<Vector2d> finalPoints = new ArrayList<>();
				finalPoints.add(new Vector2d(otherTileEntity.pos.getX() + 0.5, otherTileEntity.pos.getZ() + 0.5));
				for(int i = 0; i < circlePoints.size(); i++) {
					finalPoints.add(new Vector2d(otherTileEntity.pos.getX() + 0.5 + circlePoints.get(i).x, otherTileEntity.pos.getZ() + 0.5 + circlePoints.get(i).y));
				}
				
				for(int i = 0; i < linePoints.size(); i++) {
					finalPoints.add(new Vector2d(linePoints.get(i).x, linePoints.get(i).y));
				}
				
				ArrayList<Vector2d> flippedFinalPoints = new ArrayList<>();
				
				for(int i = finalPoints.size() - 1; i > -1; i--) {
					flippedFinalPoints.add(finalPoints.get(i));
				}
				
				return flippedFinalPoints;	
			}
			
		}
		
		
	}
	
	private Vector2d getPerpendicularPoint(Vector2d linePoint1, Vector2d linePoint2, Vector2d point) {
		
		
		double k = ((linePoint2.y - linePoint1.y) * (point.x - linePoint1.x) - (linePoint2.x - linePoint1.x) * (point.y - linePoint1.y)) / (Math.pow(linePoint2.y - linePoint1.y, 2) + Math.pow(linePoint2.x - linePoint1.x, 2));
		double x = point.x - k * (linePoint2.y - linePoint1.y);
		double y = point.y + k * (linePoint2.x - linePoint1.x);
		
		
		/*
		double k =  ((point.x - linePoint1.x) * (linePoint2.x - linePoint1.x) + (point.y - linePoint1.y) * (linePoint2.y - linePoint1.y)) / (Math.pow(linePoint2.x - linePoint1.x, 2) + Math.pow(linePoint2.y - linePoint1.y, 2));
		double x = linePoint1.x - k * (linePoint2.x - linePoint1.x);
		double y = linePoint1.y + k * (linePoint2.y - linePoint1.y);
	*/

		return new Vector2d(x, y);
	}
	
	private double getCounterClockwiseAngle(Vector2d firstVector, Vector2d secondVector) {
		double dot = firstVector.x * secondVector.x + firstVector.y * secondVector.y;     
		double det = firstVector.x * secondVector.y - firstVector.y * secondVector.x; 
		return Math.toDegrees(Math.atan2(det, dot));
	}
	
	private Vector2d getLineIntersection(Vector2d pointA, Vector2d pointB, Vector2d pointC, Vector2d pointD) { 
        double a1 = pointB.y - pointA.y; 
        double b1 = pointA.x - pointB.x; 
        double c1 = a1*(pointA.x) + b1*(pointA.y); 
       
        double a2 = pointD.y - pointC.y; 
        double b2 = pointC.x - pointD.x; 
        double c2 = a2*(pointC.x)+ b2*(pointC.y); 
       
        double determinant = a1*b2 - a2*b1; 
       
        if (determinant == 0) return null;
        else { 
            double x = (b2*c1 - b1*c2)/determinant; 
            double y = (a1*c2 - a2*c1)/determinant; 
            return new Vector2d(x, y); 
        } 
    } 
	
	private double getAngleBetweenPoints(Vector2d firstPoint, Vector2d secondPoint) {
		Vector2d direction = new Vector2d(secondPoint.x - firstPoint.x, secondPoint.y - firstPoint.y);
		double angle = Math.toDegrees(direction.angle(new Vector2d(0,1)));
		double angleTo90 = (float) Math.toDegrees(direction.angle(new Vector2d(-1, 0)));
		
		if(angleTo90 > 90) angle = 360 - angle;
		return angle;
	}
	
	private byte checkSide(TileEntityTrackMarker otherTileEntity, double distance) {
		//Setup all points
		Vector2d linePoint1 = new Vector2d(getPos().getX() + 0.5, getPos().getZ() + 0.5);
		Vector2d line1_2 = new Vector2d(-distance * Math.sin(Math.toRadians(this.realAngle)), distance * Math.cos(Math.toRadians(this.realAngle)));
		Vector2d linePoint2 = new Vector2d(linePoint1);
		linePoint2.add(line1_2);
		Vector2d leftPoint = new Vector2d(linePoint1.x + line1_2.y, linePoint1.y - line1_2.x);
		Vector2d point = new Vector2d(otherTileEntity.getPos().getX() + 0.5, otherTileEntity.getPos().getZ() + 0.5);
		
		//Calculate the real stuff
		double sidePoint = (point.x - linePoint1.x) * (linePoint2.y - linePoint1.y) - (point.y - linePoint1.y) * (linePoint2.x - linePoint1.x); //Formula to compute the side of a point apparently
		double sideLeft = (leftPoint.x - linePoint1.x) * (linePoint2.y - linePoint1.y) - (leftPoint.y - linePoint1.y) * (linePoint2.x - linePoint1.x);
		
		if(sidePoint < -0.001 && sideLeft < 0) return -1;
		if(sidePoint > 0.001 && sideLeft < 0) return 1;
		if(sidePoint < -0.001 && sideLeft > 0) return 1;
		if(sidePoint > 0.001 && sideLeft > 0) return -1;
		return 0;
	}
	
	private void synchronizeAngle(TileEntityTrackMarker otherTileEntity) {
		Vector2d teVector = new Vector2d(-Math.sin(Math.toRadians(this.angle)), Math.cos(Math.toRadians(this.angle)));
		Vector2d otherTeVector = new Vector2d(otherTileEntity.pos.getX() - pos.getX(), otherTileEntity.pos.getZ() - pos.getZ());
		
		if(Math.toDegrees(teVector.angle(otherTeVector)) < 90) {
			this.realAngle = this.angle;
		} else {
			this.realAngle = (this.angle + 180) % 360;
		}
		
	}
	
	private Vector2d getLeftRightVector(byte direction) {
		if(direction == 0) return new Vector2d();
		Vector2d line1_2 = new Vector2d(-Math.sin(Math.toRadians(this.realAngle)), Math.cos(Math.toRadians(this.realAngle)));
		Vector2d directionVec = new Vector2d(line1_2.y, -line1_2.x);
		directionVec.normalize();
		directionVec.scale(CIRCLE_INCREMENT_STEPS);
		if(direction == 1) directionVec.scale(-1);
		return directionVec;
	}
	
	private double getAngleBetween(Vector2d thisMiddlePoint, Vector2d otherMiddlePoint) {
		Vector2d middleLine = new Vector2d(otherMiddlePoint.x - thisMiddlePoint.x, otherMiddlePoint.y - thisMiddlePoint.y);
		Vector2d lineToTE = new Vector2d(this.getPos().getX() + 0.5 - thisMiddlePoint.x, this.getPos().getZ() + 0.5 - thisMiddlePoint.y);
		return middleLine.angle(lineToTE);
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
	
	@Override
	public void onLoad() {
		if(!this.world.isRemote) {
			setTicket(ForgeChunkManager.requestTicket(TrainsMod.instance, this.world, Type.NORMAL));
		}
	}
	
	
	
	public String getChannel() {
		return channel;
	}
	
	public float getAngle() {
		return angle;
	}
	
	public int getHeight() {
		return height;
	}
	
	public TrackType getTrackType() {
		return trackType;
	}
	
	@Override
	public boolean hasFastRenderer() {
		return true;
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
	    writeToNBT(nbtTagCompound);
	    return nbtTagCompound;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if(channel == null) channel = "";
		compound.setString("channel", channel);
		compound.setFloat("angle", angle);
		compound.setByte("height", height);
		compound.setByte("trackType", (byte) trackType.ordinal());
		
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.channel = compound.getString("channel");
		this.angle = compound.getFloat("angle");
		this.height = compound.getByte("height");
		byte trackTypeIndex = compound.getByte("trackType");
		if(trackTypeIndex < 0 || trackTypeIndex > TrackType.values.length - 1) trackTypeIndex = 0;
		this.trackType = TrackType.values[trackTypeIndex];
		
	}
	
	
	
	
}
