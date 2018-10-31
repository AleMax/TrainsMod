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
		synchronizeAngle(otherTileEntity, distance);
		otherTileEntity.synchronizeAngle(this, distance);
		byte side = checkSide(otherTileEntity, distance);
		System.out.println(side);
	}
	
	private byte checkSide(TileEntityTrackMarker otherTileEntity, double distance) {
		//Setup all points
		Vector2d linePoint1 = new Vector2d(getPos().getX(), getPos().getZ());
		Vector2d line1_2 = new Vector2d(-distance * Math.sin(Math.toRadians(this.realAngle)), distance * Math.cos(Math.toRadians(this.realAngle)));
		Vector2d linePoint2 = new Vector2d(linePoint1);
		linePoint2.add(line1_2);
		Vector2d leftPoint = new Vector2d(linePoint1.x + line1_2.y, linePoint1.y - line1_2.x);
		Vector2d point = new Vector2d(otherTileEntity.getPos().getX(), otherTileEntity.getPos().getZ());
		
		//Calculate the real stuff
		double sidePoint = (point.x - linePoint1.x) * (linePoint2.y - linePoint1.y) - (point.y - linePoint1.y) * (linePoint2.x - linePoint1.x); //Formula to compute the side of a point apparently
		double sideLeft = (leftPoint.x - linePoint1.x) * (linePoint2.y - linePoint1.y) - (leftPoint.y - linePoint1.y) * (linePoint2.x - linePoint1.x);
		
		if(sidePoint < -0.001 && sideLeft < 0) return -1;
		if(sidePoint > 0.001 && sideLeft < 0) return 1;
		if(sidePoint < -0.001 && sideLeft > 0) return 1;
		if(sidePoint > 0.001 && sideLeft > 0) return -1;
		return 0;
	}
	
	private void synchronizeAngle(TileEntityTrackMarker otherTileEntity, double distance) {
		float angle = (float) Math.toDegrees(Math.acos((otherTileEntity.getPos().getZ() - getPos().getZ()) / distance));
		if(Math.abs(this.angle - angle) > 90) {
			this.realAngle = (this.angle + 180) % 360;
		} else {
			this.realAngle = this.angle;
		}
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
