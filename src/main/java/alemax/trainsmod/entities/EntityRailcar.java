package alemax.trainsmod.entities;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import alemax.trainsmod.TrainsMod;
import alemax.trainsmod.blocks.BlockAMRail;
import alemax.trainsmod.blocks.tileentities.TileEntityAMRail;
import alemax.trainsmod.blocks.tileentities.TileEntityAMRailCurved;
import alemax.trainsmod.init.ModBlocks;
import alemax.trainsmod.init.ModItems;
import alemax.trainsmod.networking.PacketHandler;
import alemax.trainsmod.networking.TrainPassengerSyncMessage;
import alemax.trainsmod.networking.TrainSyncMessage;
import alemax.trainsmod.networking.TrainSyncSpeedMessage;
import alemax.trainsmod.proxy.ClientProxy;
import alemax.trainsmod.util.AMMaths;
import alemax.trainsmod.util.ModKeys;
import alemax.trainsmod.util.Seat;
import alemax.trainsmod.util.TrackPoint;
import alemax.trainsmod.util.Train;
import alemax.trainsmod.util.TrainUID;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import trackapi.lib.ITrack;
import trackapi.lib.ITrackBlock;

public abstract class EntityRailcar extends Entity {
	
	public static final int SYNC_TIME = 20;
	public static final int SPEED_SYNC_TIME = 3;
	
	public long trainUniqueID;
	public Train train;
	
	AxisAlignedBB boundBox;
	AxisAlignedBB posBoundBox;
	int syncCooldown;
	int speedSyncCooldown;
	
	private double frontTrainMotionX;
	private double frontTrainMotionZ;
	
	private double rearPosX;
	private double rearPosY;
	private double rearPosZ;
	
	public float trainRotYaw;
	public float trainRotPitch;
	
	protected boolean clientCreated;
	protected double speed = 0;
	protected byte direction = 0;
	protected byte movingDirection = 0;
	protected byte movingState = 0;
	
	double maxSpeed;
	double aimedSpeed;
	double maxAcceleration;
	double maxDecceleration;
	
	public boolean isInTrain;
	public boolean isLeadingTrain;
	public boolean isClicked;
	
	double bonusSpeed;
	
	protected double axleDistance;
	
	protected boolean onRail;
	protected BlockPos railBlock;
	protected boolean removeTrackPoints;
	
	protected int maxPassengersCount;
	protected Seat[] seats;
	
	private Ticket chunkLoaderTicket;
	public float lastPartialTick;
	
	public EntityRailcar(World worldIn) {
		super(worldIn);
		clientCreated = false;
		syncCooldown = SYNC_TIME;
		speedSyncCooldown = SPEED_SYNC_TIME;
		axleDistance = 6;
		this.ignoreFrustumCheck = true;
		 if(!this.world.isRemote) {
			chunkLoaderTicket = ForgeChunkManager.requestTicket(TrainsMod.instance, this.world, Type.ENTITY);
			chunkLoaderTicket.bindEntity(this);
		 }
		 this.maxSpeed = 30;
		 this.aimedSpeed = 0;
		 this.maxAcceleration = 1.5;
		 this.maxDecceleration = 1.5;
		 this.bonusSpeed = 0;
		 this.lastPartialTick = -1;
		 this.trainUniqueID = TrainUID.generateID();
	}
	
	public EntityRailcar(World worldIn, BlockPos placedOn) {
		this(worldIn);
		onRail = true;
		railBlock = placedOn;
		
		this.posX = railBlock.getX() + 0.5;
		this.posY = railBlock.getY() + 0.125;
		this.posZ = railBlock.getZ() + 0.5;
		
		
		
		byte dir;
		if(worldIn.getBlockState(railBlock).getValue(BlockAMRail.FACING).equals(EnumFacing.NORTH)) {
			dir = 2;
		} else if(worldIn.getBlockState(railBlock).getValue(BlockAMRail.FACING).equals(EnumFacing.EAST)) {
			dir = 3;
		} else if(worldIn.getBlockState(railBlock).getValue(BlockAMRail.FACING).equals(EnumFacing.SOUTH)) {
			dir = 0;
		} else {
			dir = 1;
		}
		
		if(dir == 0) {
			this.rearPosX = this.posX;
			this.rearPosZ = this.posZ - this.axleDistance;
		} else if(dir == 1) {
			this.rearPosX = this.posX + this.axleDistance;
			this.rearPosZ = this.posZ;
		} else if(dir == 2) {
			this.rearPosX = this.posX;
			this.rearPosZ = this.posZ + this.axleDistance;
		} else {
			this.rearPosX = this.posX - this.axleDistance;
			this.rearPosZ = this.posZ;
		}
		
		setRotation(dir);
		
	}
	
	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if(player.getHeldItem(hand).getItem().getRegistryName().equals(ModItems.item_train_rotator.getRegistryName())) {
			if(speed == 0 && this.isOnStraight()) {
				setRotation((byte) ((direction + 2) % 4));
				if(!this.world.isRemote) player.sendMessage(new TextComponentString("Rotated the train!"));
			}
		} else if(player.getHeldItem(hand).getItem().getRegistryName().equals(ModItems.item_train_starter.getRegistryName())) {
			if(this.isLeadingTrain) {
				if(speed == 0) {
					this.aimedSpeed = 10;
					if(!this.world.isRemote) player.sendMessage(new TextComponentString("Started the train!"));
				} else {
					this.aimedSpeed = 0;
					if(!this.world.isRemote) player.sendMessage(new TextComponentString("Stopped the train!"));
				}
				for(EntityRailcar car : this.train.railcars) {
					car.aimedSpeed = aimedSpeed;
				}
			} else if(!this.isInTrain) {
				if(speed == 0) {
					this.aimedSpeed = 10;
					if(!this.world.isRemote) player.sendMessage(new TextComponentString("Started the train!"));
				} else {
					this.aimedSpeed = 0;
					if(!this.world.isRemote) player.sendMessage(new TextComponentString("Stopped the train!"));
				}
			}
			
		} else if(player.getHeldItem(hand).getItem().getRegistryName().equals(ModItems.item_train_remover.getRegistryName())) {
				if(!this.world.isRemote) {
				this.setDead();
				player.sendMessage(new TextComponentString("Removed the train!"));
			}
		} else if(player.getHeldItem(hand).getItem().getRegistryName().equals(ModItems.item_train_connector.getRegistryName())) {
			if(!this.world.isRemote && isOnStraight()) {
				isClicked = true;
				List<EntityRailcar> entities =  this.world.getEntitiesWithinAABB(EntityRailcar.class, new AxisAlignedBB(this.posX - 100, this.posY - 100, this.posZ - 100, this.posX + 100, this.posY + 100, this.posZ + 100));
				boolean found = false;
				for(EntityRailcar car : entities) {
					if(car.trainUniqueID != this.trainUniqueID && car.isClicked == true && car.isOnStraight()) {
						if(this.isInTrain) {
							this.isClicked = false;
						} else {
								if(!car.isInTrain) {
								car.isInTrain = true;
								car.isLeadingTrain = true;
								car.train = new Train();
								this.train = car.train;
								this.train.railcars.add(car);		
							} else {
								this.train = car.train;
							}
							this.train.railcars.add(this);
							this.train.calcAllPositions();
							this.isInTrain = true;
							car.isClicked = false;
							this.isClicked = false;
							player.sendMessage(new TextComponentString("Connected the two railcars!"));
							found = true;
						}
						break;
					}
				}
				if(!found) {
					player.sendMessage(new TextComponentString("Connect another Railcar!"));
				}
			}
		} else {
			if(this.isPassenger(player)) {
				int index = -1;
				for(int i = 0; i < this.maxPassengersCount; i++) {
					if(seats[i].sittingEntity != null && seats[i].sittingEntity.equals(player)) {
						index = i;
						break;
					}
				}
				if(index > -1) {
					int oldIndex = index;
					for(int i = 0; i < this.seats.length - 1; i++) {
						if(index + 1 == this.seats.length) index = 0;
						else index++;
						if(seats[index].sittingEntity == null) {
							seats[index].sittingEntity = player;
							sendPassengerSync(index, false, player.getEntityId());
							this.seats[oldIndex].sittingEntity = null;
							sendPassengerSync(oldIndex, true, 0);
							player.rotationYaw += this.seats[index].yawOffset - this.seats[oldIndex].yawOffset;
							break;
						}
					}
				}
			} else {
				boolean seatFound = false;
				for(int i = 0; i < this.maxPassengersCount; i++) {
					if(seats[i].sittingEntity == null) {
						if(!this.world.isRemote) player.startRiding(this);
						this.seats[i].sittingEntity = player;
						sendPassengerSync(i, false, player.getEntityId());
						seatFound = true;
						break;
					}
				}
				if(!seatFound) player.sendMessage(new TextComponentString("The train is full!"));
				
			}
		}	

		return true;
		
	}
	
	
	
	@Override
    public void onUpdate() {
		
		
		if(!this.world.isRemote) {
			if(this.syncCooldown > 0) this.syncCooldown--;
			else {
				this.syncCooldown = SYNC_TIME;
				sendSync();
				//System.out.println("sy?");
			}
			if(this.speedSyncCooldown > 0) this.speedSyncCooldown--;
			else {
				this.speedSyncCooldown = SPEED_SYNC_TIME;
				sendSpeedSync();
			}
		}
		
		double accel = 0;
		if(movingState == -2) {
			accel = -this.maxDecceleration / 20.0;
		} else if(movingState == -1) {
			accel = -this.maxDecceleration / 40.0;
		} else if(this.movingState == 0) {
			accel = -0.05 / 20.0;
		} else if(this.movingState == 1) {
			accel = 0;
		}else if(this.movingState == 2) {
			accel = this.maxAcceleration / 40.0;
		} else if(this.movingState == 3) {
			accel = this.maxAcceleration / 20.0;
		}
		
		this.speed = this.speed += accel;
		if(this.speed < 0) this.speed = 0;
		if(this.speed > this.maxSpeed) this.speed = this.maxSpeed;
		this.aimedSpeed = this.speed;
		
		
		/*
		if(!this.world.isRemote) {
			this.speed += this.currentAcceleration / 20;
			if(this.speed > this.maxSpeed) this.speed = this.maxSpeed;
			else if(this.speed < 0) this.speed = 0;
			this.aimedSpeed = this.speed;
			
			this.currentAcceleration = 0;
			
			System.out.println(this.speed);
		}
		*/
		
		if(speed + (maxAcceleration / 20) < aimedSpeed) {
			speed += (maxAcceleration / 20);
		} else if(speed - (maxDecceleration / 20) > aimedSpeed) {
			speed -= (maxDecceleration / 20);
		} else {
			speed = aimedSpeed;
		}
		
		if(!clientCreated && !this.world.isRemote) sendSync();
		
		//System.out.println(this.speed);
		
	    this.prevPosX = posX;
	    this.prevPosY = posY;
	    this.prevPosZ = posZ; 	 
	    this.prevRotationYaw = rotationYaw;
	    this.prevRotationPitch = rotationPitch;
		
        super.onUpdate();
	    calculateMotion();
	    calculateRearPos();
	    calculateRotation();

        this.move(MoverType.SELF, this.frontTrainMotionX, this.motionY, this.frontTrainMotionZ);
        
        if(this.trainRotYaw > 315) this.direction = 0;
        else if(this.trainRotYaw > 225) this.direction = 3;
        else if(this.trainRotYaw > 135) this.direction = 2;
        else if(this.trainRotYaw > 45) this.direction = 1;
        else this.direction = 0;
        
        
        if(!this.world.isRemote) {
	        ChunkPos newChunkPos = new ChunkPos(new BlockPos(this));
	        ChunkPos oldChunkPos = new ChunkPos(new BlockPos(this.prevPosX, this.prevPosY, this.prevPosZ));

	        if((newChunkPos.x != oldChunkPos.x) || (newChunkPos.z != oldChunkPos.z)) {

	        	ForgeChunkManager.forceChunk(chunkLoaderTicket, newChunkPos);
	        	ForgeChunkManager.unforceChunk(chunkLoaderTicket, oldChunkPos);
	        }
        }
    }

	@Override
	public void setDead() {
		if(this.isInTrain) this.train.removeRailcar(this);
		if(chunkLoaderTicket != null) {
			ForgeChunkManager.releaseTicket(chunkLoaderTicket);
		}
		
		super.setDead();
	}
	
	@Override
	public void updatePassenger(Entity passenger) {
		for(Seat seat : seats) {
			if(seat.sittingEntity != null && seat.sittingEntity.equals(passenger)) {
				Vec3d vec3d = (new Vec3d(seat.xOffset, seat.yOffset, seat.zOffset)).rotateYaw((float) Math.toRadians(-rotationYaw));
				passenger.setPosition(this.posX + vec3d.x, this.posY + passenger.getYOffset() + vec3d.y, this.posZ + vec3d.z);
				passenger.rotationYaw += (rotationYaw - prevRotationYaw);
				passenger.setRenderYawOffset(this.trainRotYaw + seat.yawOffset);
				if(this.world.isRemote) {
					
					
				}
			}
		}
	}
	
	@Override
	public void move(MoverType type, double x, double y, double z)
    {
		
        if (this.noClip)
        {
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, y, z));
            this.resetPositionToBB();
        }
        else
        {
            this.world.profiler.startSection("move");
            
            AxisAlignedBB modelAABB;
	        modelAABB = getBoundBox().offset(posX, posY, posZ);
	            
	        if (y != 0.0D)
	        {
	            modelAABB = modelAABB.offset(0.0D, y, 0.0D);
	        }
	        if (x != 0.0D)
	        {
	            modelAABB = modelAABB.offset(x, 0.0D, 0.0D);
	        }
	        if (z != 0.0D)
	        {
	            modelAABB = modelAABB.offset(0.0D, 0.0D, z);
	        }
            
	        this.world.profiler.endSection();
            this.world.profiler.startSection("rest");
            
            this.posX = modelAABB.minX - getBoundBox().minX;
	        this.posY = modelAABB.minY;
            this.posZ = modelAABB.minZ - getBoundBox().minZ;
            posBoundBox = modelAABB;
            
            this.world.profiler.endSection();
        }
    }
	
	protected void calculateMotion() {
		double x = this.posX;
		double y = this.posY;
		double z = this.posZ;
		double length = 0;
		this.railBlock = new BlockPos(posX, posY, posZ);
		
		double tickDistance = (speed + bonusSpeed) / 20;
		do {
			Block trackBlock = this.world.getBlockState(railBlock).getBlock();
			if(trackBlock instanceof ITrackBlock) {
				Vec3d nPos = ((ITrackBlock) trackBlock).getNextPosition(this.world, this.railBlock, new Vec3d(x, y, z), new Vec3d(-Math.sin(Math.toRadians(this.rotationYaw)) * (tickDistance - length), 0, Math.cos(Math.toRadians(this.rotationYaw)) * (tickDistance - length)));
				length += Math.sqrt(Math.pow(nPos.x - x, 2) + Math.pow(nPos.z - z, 2));
				x = nPos.x;
				y = nPos.y;
				z = nPos.z;
				this.railBlock = new BlockPos(x, y, z);
			} else {
				TileEntity te = this.world.getTileEntity(this.railBlock);
				if(te instanceof ITrack) {
					Vec3d nPos = ((ITrack) te).getNextPosition(new Vec3d(x, y, z), new Vec3d(-Math.sin(Math.toRadians(this.rotationYaw)) * (tickDistance - length), 0, Math.cos(Math.toRadians(this.rotationYaw)) * (tickDistance - length)));
					length += Math.sqrt(Math.pow(nPos.x - x, 2) + Math.pow(nPos.z - z, 2));
					//double add = Math.sqrt(Math.pow(nPos.x - x, 2) + Math.pow(nPos.z - z, 2));
					x = nPos.x;
					y = nPos.y;
					z = nPos.z;
					this.railBlock = new BlockPos(x, y, z);
				} else {
					boolean br = false;
					int xStart;
					int zStart;
					int xEnd;
					int zEnd;
					
					Vec3d forward = new Vec3d(-Math.sin(Math.toRadians(this.rotationYaw)), 0, Math.cos(Math.toRadians(this.rotationYaw)));
					Vec3d across = forward.crossProduct(new Vec3d(0, 1, 0));
					
					ArrayList<BlockPos> possiblePositions = new ArrayList<>(); 
					for(int i = 0; i < 6; i++) {
						for(int j = 0 - i; j < 2 * i + 1; j++) {
							Vec3d vectorPos = new Vec3d(x, y, z).add(forward.scale(i)).add(across.scale(j));
							BlockPos cPos = new BlockPos(vectorPos.x, vectorPos.y, vectorPos.z);
							TileEntity nte = this.world.getTileEntity(cPos);
							if(nte instanceof ITrack || this.world.getBlockState(new BlockPos(this.railBlock.getX() + i, this.railBlock.getY(), this.railBlock.getZ() + j)).getBlock() instanceof ITrackBlock) {
								possiblePositions.add(nte.getPos());
								br = true;
							}
						}
					}
					if(br) {
						BlockPos nearest = possiblePositions.get(0);
						double nearestLength = Math.sqrt(Math.pow(nearest.getX() - railBlock.getX(), 2) + Math.pow(nearest.getZ() - railBlock.getZ(), 2));
						for(int i = 1; i < possiblePositions.size(); i++) {
							BlockPos currentBP = possiblePositions.get(i);
							double distance = Math.sqrt(Math.pow(currentBP.getX() - railBlock.getX(), 2) + Math.pow(currentBP.getZ() - railBlock.getZ(), 2));
							if(distance < nearestLength) {
								nearestLength = distance;
								nearest = currentBP;
							}
						}
						this.railBlock = nearest;
					}
					
					
					if(!br) break;
				}
			}
			
		} while(length < (tickDistance * 0.9999) && speed > 0.0000001);
		this.frontTrainMotionX = x - posX;
		this.frontTrainMotionZ = z - posZ;
	}
	
	protected void calculateRearPos() {
		double x = this.posX;
		double y = this.posY;
		double z = this.posZ;
		double length = 0;
		BlockPos rearRailBlock = new BlockPos(posX, posY, posZ);
		
		do {
			Block trackBlock = this.world.getBlockState(rearRailBlock).getBlock();
			if(trackBlock instanceof ITrackBlock) {
				Vec3d nPos = ((ITrackBlock) trackBlock).getNextPosition(this.world, rearRailBlock, new Vec3d(x, y, z), new Vec3d(-Math.sin(Math.toRadians(this.rotationYaw + 180)) * (this.axleDistance - length), 0, Math.cos(Math.toRadians(this.rotationYaw + 180)) * (this.axleDistance - length)));
				length += Math.sqrt(Math.pow(nPos.x - x, 2) + Math.pow(nPos.z - z, 2));
				x = nPos.x;
				y = nPos.y;
				z = nPos.z;
				rearRailBlock = new BlockPos(x, y, z);
			} else {
				TileEntity te = this.world.getTileEntity(rearRailBlock);
				if(te instanceof ITrack) {
					Vec3d nPos = ((ITrack) te).getNextPosition(new Vec3d(x, y, z), new Vec3d(-Math.sin(Math.toRadians(this.rotationYaw + 180)) * (this.axleDistance - length), 0, Math.cos(Math.toRadians(this.rotationYaw + 180)) * (this.axleDistance - length)));
					length += Math.sqrt(Math.pow(nPos.x - x, 2) + Math.pow(nPos.z - z, 2));
					double add = Math.sqrt(Math.pow(nPos.x - x, 2) + Math.pow(nPos.z - z, 2));
					x = nPos.x;
					y = nPos.y;
					z = nPos.z;
					rearRailBlock = new BlockPos(x, y, z);
				} else {
					boolean br = false;
					int xStart;
					int zStart;
					int xEnd;
					int zEnd;
					
					Vec3d forward = new Vec3d(-Math.sin(Math.toRadians(this.rotationYaw)), 0, Math.cos(Math.toRadians(this.rotationYaw)));
					forward = new Vec3d(-forward.x, 0, -forward.z);
					Vec3d across = forward.crossProduct(new Vec3d(0, 1, 0));
					
					ArrayList<BlockPos> possiblePositions = new ArrayList<>(); 
					for(int i = 0; i < 6; i++) {
						for(int j = 0 - i; j < 2 * i + 1; j++) {
							Vec3d vectorPos = new Vec3d(x, y, z).add(forward.scale(i)).add(across.scale(j));
							BlockPos cPos = new BlockPos(vectorPos.x, vectorPos.y, vectorPos.z);
							TileEntity nte = this.world.getTileEntity(cPos);
							if(nte instanceof ITrack && !(nte instanceof TileEntityAMRail)) {
								possiblePositions.add(nte.getPos());
								br = true;
							}
						}
					}
					if(br) {
						BlockPos nearest = possiblePositions.get(0);
						double nearestLength = Math.sqrt(Math.pow(nearest.getX() - rearRailBlock.getX(), 2) + Math.pow(nearest.getZ() - rearRailBlock.getZ(), 2));
						for(int i = 1; i < possiblePositions.size(); i++) {
							BlockPos currentBP = possiblePositions.get(i);
							double distance = Math.sqrt(Math.pow(currentBP.getX() - rearRailBlock.getX(), 2) + Math.pow(currentBP.getZ() - rearRailBlock.getZ(), 2));
							if(distance < nearestLength) {
								nearestLength = distance;
								nearest = currentBP;
							}
						}
						rearRailBlock = nearest;
					}
					
					
					if(!br) break;
				}
			}
		} while(length < this.axleDistance * 0.9999);
		this.rearPosX = x;
		this.rearPosZ = z;
	}
	
	private void calculateRotation() {
		this.trainRotYaw = rotationYaw % 360;
		if(this.trainRotYaw < 0) this.trainRotYaw += 360;
		double xDif = posX - rearPosX;
		double zDif = posZ - rearPosZ;
		float rot = (float)Math.toDegrees(Math.atan2(-xDif, zDif));
		
		if(rot < 0) rot += 360;
		double dif = rot - this.trainRotYaw;
		if(dif > 180) dif -= 360;
		else if(dif < -180) dif += 360;
	
		this.rotationYaw += dif;
		this.trainRotYaw = rot;
	}
	
	private TileEntityAMRailCurved getCurveEnd(byte dir, BlockPos pos) {
		float lxD = 0;
		float lzD = 0;
		int ix = 0;
		int iz = 0;
		if(dir == 2) {
			ix = pos.getX() - 2;
			iz = pos.getZ() - 5;
			lxD = -0.5f;
		} else if(dir == 3) {
			ix = pos.getX() + 1;
			iz = pos.getZ() - 2;
			lxD = -1f;
			lzD = -0.5f;
		} else if(dir == 0) {
			ix = pos.getX() - 2;
			iz = pos.getZ() + 1;
			lxD = -0.5f;
			lzD = -1f;
		} else {
			ix = pos.getX() - 5;
			iz = pos.getZ() - 2;
			lzD = -0.5f;
		}
		
		int ixE = ix + 5;
		int izE = iz + 5;
		for(; ix < ixE; ix++) {
			for(; iz < izE; iz++) {
				BlockPos p = new BlockPos(ix, pos.getY(), iz);
				if(this.world.getBlockState(p).getBlock().getRegistryName().equals(ModBlocks.AM_RAIL_CURVED.getRegistryName())) {
					TileEntityAMRailCurved te = (TileEntityAMRailCurved) this.world.getTileEntity(p);
					Vector2d[][] allPoints = te.getAllPoints();
					Vector2d point = allPoints[allPoints.length - 1][6];
					
					BlockPos endPoint = new BlockPos(Math.round(point.x + lxD), p.getY(), Math.round(point.y + lzD)).add(te.getPos().getX(), 0, te.getPos().getZ());
					if(pos.equals(endPoint)) return te;
				}
			}
			iz -= 5;
		}
		return null;
	}
	public void sync(double posX, double posY, double posZ, double rearPosX, double rearPosY, double rearPosZ, double speed, byte direction, byte movingDirection, byte movingState, float rotationYaw, float rotationPitch, float trainRotYaw, float trainRotPitch, boolean onRail, BlockPos railBlock) {
		if(this.world.isRemote) {
			double xDif = posX - this.posX;
			double zDif = posZ - this.posZ;
			float rot = (float)Math.toDegrees(Math.atan2(-xDif, zDif));
			if(rot < 0) rot += 360;
			double dif = Math.sqrt(Math.pow(posX - this.posX, 2) + Math.pow(posZ - this.posZ, 2));
			if(Math.abs(rot - trainRotYaw) > 90) dif *= -1;

			this.bonusSpeed = dif / (SYNC_TIME / 20);
			
			this.speed = speed;
			
			this.direction = direction;
			this.movingDirection = movingDirection;
			this.movingState = movingState;
			double maxRot = speed;
			if(maxRot > 10) maxRot = 10;
			this.onRail = onRail;
			this.railBlock = railBlock;
			this.removeTrackPoints = true;
			
		}
	}
	
	public void revertMovingDirection() {
		this.movingDirection = (byte) ((this.movingDirection +  2) % 4);
	}
	
	@Override
	public void applyEntityCollision(Entity entityIn) {
		entityIn.setDead();
		super.applyEntityCollision(entityIn);
	}
	public void calcPositionInTrain() {
		EntityRailcar leadingTrain = this.train.railcars.get(0);
		movingDirection = leadingTrain.movingDirection;
		
		boolean thisLong = false;
		boolean leadingLong = false;
		
		if(this.movingDirection != direction) thisLong = true;
		if(leadingTrain.movingDirection == leadingTrain.direction) leadingLong = true;
		
		double distance = 0;
		
		if(thisLong) distance -= this.boundBox.minZ;
		else distance += this.boundBox.maxZ;
		if(leadingLong) distance -= leadingTrain.boundBox.minZ;
		else distance += leadingTrain.boundBox.maxZ;
		
		int p = train.railcars.indexOf(this);
		
		for(int i = 1; i < p; i++) {
			AxisAlignedBB box = this.train.railcars.get(i).boundBox;
			distance += (box.maxZ - box.minZ);
		}
		if(movingDirection == 0) {
			this.posX = leadingTrain.posX;
			this.posZ = leadingTrain.posZ - distance;
		} else if(movingDirection == 1) {
			this.posX = leadingTrain.posX + distance;
			this.posZ = leadingTrain.posZ;
		} else if(movingDirection == 2) {
			this.posX = leadingTrain.posX;
			this.posZ = leadingTrain.posZ + distance;
		} else {
			this.posX = leadingTrain.posX - distance;
			this.posZ = leadingTrain.posZ;
		}
		
		resetRearPoints();
		this.railBlock = new BlockPos(posX, posY, posZ);
		
	}
	
	public void userInput(int playerID, byte key) {
		boolean allowedToSteer = false;
		for(int i = 0; i < this.seats.length; i++) {
			if(this.seats[i].sittingEntity != null && this.seats[i].sittingEntity.getEntityId() == playerID && this.seats[i].drivingSeat) allowedToSteer = true;
		}
		if(allowedToSteer) {
			byte change = 0;
			if(key == ModKeys.TRAIN_ACCELERATION) change = 1;
			else if(key == ModKeys.TRAIN_BRAKE) change = -1;
			
			this.movingState += change;
			if(this.movingState > 3) this.movingState = 3;
			else if(this.movingState < -2) this.movingState = -2;
			
			if(!this.world.isRemote) {
				EntityPlayer player = (EntityPlayer) this.world.getEntityByID(playerID);
				player.sendMessage(new TextComponentString("Set the moving state to " + this.movingState));
			}
		}
	}
	
	
	public void syncSpeed(double speed, double aimedSpeed) {
		this.speed = speed;
		this.aimedSpeed = aimedSpeed;
	}
	
	public void setRotation(byte rot) {
		rotationYaw = rot * 90;
		this.prevRotationYaw = rotationYaw;
		this.trainRotYaw = rotationYaw;
		direction = rot;
		movingDirection = rot;
		resetRearPoints();		
		System.out.println("ROT");
	}
	
	public boolean isOnStraight() {
		return this.world.getBlockState(railBlock).getBlock().getRegistryName().equals(ModBlocks.AM_RAIL.getRegistryName());
	}
	
	public void setBoundBox(AxisAlignedBB box) {
		this.boundBox = box;
		this.posBoundBox = box;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void resetRearPoints() {
		if(this.movingDirection == 0) {
			this.rearPosX = this.posX;
			this.rearPosZ = this.posZ - this.axleDistance;
		} else if(this.movingDirection == 1) {
			this.rearPosX = this.posX + this.axleDistance;
			this.rearPosZ = this.posZ;
		} else if(this.movingDirection == 2) {
			this.rearPosX = this.posX;
			this.rearPosZ = this.posZ + this.axleDistance;
		} else {
			this.rearPosX = this.posX - this.axleDistance;
			this.rearPosZ = this.posZ;
		}
	}
	
	public void setAxleDistance(double axleDistance) {
		this.axleDistance = axleDistance;
		resetRearPoints();
		
	}
	
	private void sendSync() {
		if(onRail == true) {
			PacketHandler.INSTANCE.sendToAllAround(new TrainSyncMessage(this.getEntityId(), posX, posY, posZ, rearPosX, rearPosY, rearPosZ, speed, direction, movingDirection, movingState, rotationYaw, rotationPitch, trainRotYaw, trainRotPitch, onRail, railBlock), new TargetPoint(this.dimension, posX, posY, posZ, 400));
		}
		clientCreated = true;
	}
	
	private void sendSpeedSync() {
		if(onRail == true) {
			PacketHandler.INSTANCE.sendToAllAround(new TrainSyncSpeedMessage(this.getEntityId(), speed, aimedSpeed), new TargetPoint(this.dimension, posX, posY, posZ, 400));
		}
	}
	
	private void sendPassengerSync(int passengerSlot, boolean noEntity, int entityID) {
		PacketHandler.INSTANCE.sendToAll(new TrainPassengerSyncMessage(this.getEntityId(), passengerSlot, noEntity, entityID));
	}
	
	public void syncPassenger(int passengerSlot, Entity entity) {
		this.seats[passengerSlot].sittingEntity = entity;
	}
	
	public AxisAlignedBB getBoundBox() {
		if(direction == 0) return boundBox;
		else return AMMaths.rotateY(boundBox, direction);
	}
	
	public double getTrainPosX() {
		return posX;
	}

	public double getTrainPosY() {
		return posY;
	}

	public double getTrainPosZ() {
		return posZ;
	}

	@Override
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch,
			int posRotationIncrements, boolean teleport) {
		
	}
	
	public abstract void setup();
	
	/*
	@Override
	public boolean canRiderInteract() {
		return true;
	}
	*/
	@Override
	protected void setRotation(float yaw, float pitch) {
		super.setRotation(yaw, pitch);
	}
	
	@Override
	protected void removePassenger(Entity passenger) {
		super.removePassenger(passenger);
		passenger.setPosition(passenger.getPosition().getX(), passenger.getPosition().getY() + 3, passenger.getPosition().getZ());
		for(int i = 0; i < seats.length; i++) {
			if(seats[i].sittingEntity != null && seats[i].sittingEntity.equals(passenger)) {
				seats[i].sittingEntity = null;
				sendPassengerSync(i, true, 0);
			}
		}
	}
	public void setClientCreated(boolean clientCreated) {
		this.clientCreated = clientCreated;
	}
	
	@Override
	public AxisAlignedBB getEntityBoundingBox() {
		return this.getBoundBox().offset(this.posX, this.posY, this.posZ);
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return this.getEntityBoundingBox();
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox() {
		return this.getEntityBoundingBox();
	}
	
	@Override
	protected boolean canFitPassenger(Entity passenger) {
		return this.getPassengers().size() < maxPassengersCount;
	}
	
	@Override
	public boolean canBePushed() {
		return false;
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}	
	
	@Override
	protected void entityInit() {
		
		
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		/*
		if(!this.world.isRemote) {
			compound.setLong("trainUID", this.trainUniqueID);
			//TrainUID.addID(this.trainUniqueID);
			compound.setInteger("trainLength", this.train.railcars.size());
			for(int i = 0; i < this.train.railcars.size(); i++) {
				compound.setLong("train" + i, this.train.railcars.get(i).trainUniqueID);
			}
		}
		*/
		compound.setDouble("rearPosX", this.rearPosX);
		compound.setDouble("rearPosY", this.rearPosY);
		compound.setDouble("rearPosZ", this.rearPosZ);
		compound.setDouble("speed", this.speed);
		compound.setByte("direction", this.direction);
		compound.setDouble("aimedSpeed", this.aimedSpeed);
		compound.setFloat("trainRotYaw", this.trainRotYaw);
		compound.setFloat("trainRotPitch", this.trainRotPitch);
		compound.setBoolean("onRail", this.onRail);
		compound.setInteger("railBlockX", this.railBlock.getX());
		compound.setInteger("railBlockY", this.railBlock.getY());
		compound.setInteger("railBlockZ", this.railBlock.getZ());

	}
		
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		this.rearPosX = compound.getDouble("rearPosX");
		this.rearPosY = compound.getDouble("rearPosY");
		this.rearPosZ = compound.getDouble("rearPosZ");
		this.speed = compound.getDouble("speed");
		this.direction = compound.getByte("direction");
		this.aimedSpeed = compound.getDouble("aimedSpeed");
		this.trainRotYaw = compound.getFloat("trainRotYaw");
		this.trainRotPitch = compound.getFloat("trainRotPitch");
		this.onRail = compound.getBoolean("onRail");
		this.railBlock = new BlockPos(compound.getInteger("railBlockX"), compound.getInteger("railBlockY"), compound.getInteger("railBlockZ"));
		this.posBoundBox = boundBox.offset(posX, posY, posZ);
		this.clientCreated = true;
		this.syncCooldown = SYNC_TIME;
	}

	

	
	
}
