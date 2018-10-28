package alemax.trainsmod.blocks.tileentities;

import alemax.trainsmod.TrainsMod;
import alemax.trainsmod.util.TrackType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

public class TileEntityTrackMarker extends TileEntity {
	
	private String channel;
	private float angle;
	private byte height;
	private TrackType trackType;
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
		System.out.println("FORCE_CHUNK");
	}
	
	public void unloadChunk() {
		if(chunkLoaderTicket != null) {
			ForgeChunkManager.unforceChunk(chunkLoaderTicket, new ChunkPos(getPos()));
			ForgeChunkManager.releaseTicket(chunkLoaderTicket);
			System.out.println("UNFORCE_CHUNK");
		}
	}
	
	public Ticket getChunkLoaderTicket() {
		return chunkLoaderTicket;
	}
	
	public void set(String channel, float angle, byte height, TrackType trackType) {
		this.channel = channel;
		this.angle = angle;
		this.height = height;
		this.trackType = trackType;
		markDirty();
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
