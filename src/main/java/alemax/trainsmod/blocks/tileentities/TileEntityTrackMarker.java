package alemax.trainsmod.blocks.tileentities;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2d;

import alemax.trainsmod.TrainsMod;
import alemax.trainsmod.util.TrackType;
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
				calculateTrack((TileEntityTrackMarker) tileEntity, distance);
			} else {
				System.out.println("TO CLOSE TO EACH OTHER");
				//error message
			}
		} else {
			System.out.println("NO 2 MARKERS");
			//error message
		}
	}
	
	private void calculateTrack(TileEntityTrackMarker otherTileEntity, double distance) {
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
			
			for(int i = 0; i < firstPoints.size(); i++) {
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
			
			for(int i = 0; i < finalPoints.size(); i++) {
				System.out.println(finalPoints.get(i).x + "\t" + finalPoints.get(i).y);
			}
			
			System.out.println("smth lol FJIAH");
			/*
			System.out.println(firstAngleDifference);
			System.out.println(fad);
			System.out.println(sad);
			System.out.println((firstLastPoint.x + firstMiddlePoint.x) + "\t" + (firstLastPoint.y + firstMiddlePoint.y));
			System.out.println((secondLastPoint.x + secondMiddlePoint.x) + "\t" + (secondLastPoint.y + secondMiddlePoint.y));
			*/
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
