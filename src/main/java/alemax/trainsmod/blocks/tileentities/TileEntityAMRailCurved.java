package alemax.trainsmod.blocks.tileentities;

import javax.vecmath.Vector2d;

import alemax.trainsmod.util.TrackPoint;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import scala.util.Random;
import trackapi.lib.ITrack;

@net.minecraftforge.fml.common.Optional.Interface(iface = "trackapi.lib.ITrack", modid = "trackapi")
public class TileEntityAMRailCurved extends TileEntity implements ITrack {
		
	private long id;
	private byte comingFrom = 3;
	private byte goingTo = 0;
	private float radius = 10.5f;
	private int steps = 0;
	private int currentStep = 0;
	private byte length = 1;
	private Vector2d[][] points;
	
	public void set(long id, byte comingFrom, byte goingTo, float radius, int steps, int currentStep, byte length, Vector2d[][] points) {
		this.id = id;
		this.comingFrom = comingFrom;
		this.goingTo = goingTo;
		this.radius = radius;
		this.steps = steps;
		this.currentStep = currentStep;
		this.length = length;
		this.points = points;
		markDirty();
	}
	
	public int getCurrentStep() {
		return currentStep;
	}

	public byte getLength() {
		return length;
	}

	public void set(byte comingFrom, byte goingTo, float radius) {
		Random rdm = new Random();
		this.id = rdm.nextLong();
		
		this.comingFrom = comingFrom;
		this.goingTo = goingTo;
		this.radius = radius;
		
		boolean eastWest = false;
		if(comingFrom == 1 || comingFrom == 3) eastWest = true;
		
		boolean goingRight = false;
		
		if(comingFrom == 0 && goingTo == 3) goingRight = true;
		if(comingFrom - goingTo == 1) goingRight = true;
		
		int directionX = -1;
		int directionZ = -1;
		
		if(goingTo == 0) directionZ = 1;
		else if(goingTo == 3) directionX = 1;
		
		if(comingFrom == 1) directionX = 1;
		else if(comingFrom == 2) directionZ = 1;
		
		double circleLength = 0.5 * Math.PI * radius;
		this.steps = (int) Math.round(circleLength);
		
		double angle = 0.5 * Math.PI / (steps - 1);
		double factor = 0.0;
		for(int i = 0; i < steps; i++) {
			factor += Math.cos(i * angle);
		}
		factor = radius / factor;
		
		double factors[] = new double[7];
		if(goingRight) {
			factors[0] = (radius - 0.875) / radius * factor;
			factors[1] = (radius - 0.8125) / radius * factor;
			factors[2] = (radius - 0.75) / radius * factor;
			factors[3] = (radius + 0.75) / radius * factor;
			factors[4] = (radius + 0.8125) / radius * factor;
			factors[5] = (radius + 0.875) / radius * factor;
		} else {
			factors[5] = (radius - 0.875) / radius * factor;
			factors[4] = (radius - 0.8125) / radius * factor;
			factors[3] = (radius - 0.75) / radius * factor;
			factors[2] = (radius + 0.75) / radius * factor;
			factors[1] = (radius + 0.8125) / radius * factor;
			factors[0] = (radius + 0.875) / radius * factor;
		}
		
		
		factors[6] = factor;
		
		this.points = new Vector2d[steps + 1][7];
		if(comingFrom == 0) {
			points[0][0] = new Vector2d(1.375, 1.0);
			points[0][1] = new Vector2d(1.3125, 1.0);
			points[0][2] = new Vector2d(1.25, 1.0);
			points[0][3] = new Vector2d(-0.25, 1.0);
			points[0][4] = new Vector2d(-0.3125, 1.0);
			points[0][5] = new Vector2d(-0.375, 1.0);
			points[0][6] = new Vector2d(0.5, 1.0);
		} else if(comingFrom == 3) {
			points[0][0] = new Vector2d(1.0, -0.375);
			points[0][1] = new Vector2d(1.0, -0.3125);
			points[0][2] = new Vector2d(1.0, -0.25);
			points[0][3] = new Vector2d(1.0, 1.25);
			points[0][4] = new Vector2d(1.0, 1.3125);
			points[0][5] = new Vector2d(1.0, 1.375);
			points[0][6] = new Vector2d(1.0, 0.5);
		} else if(comingFrom == 2) {
			points[0][0] = new Vector2d(-0.375, 0.0);
			points[0][1] = new Vector2d(-0.312, 0.0);
			points[0][2] = new Vector2d(-0.25, 0.0);
			points[0][3] = new Vector2d(1.25, 0.0);
			points[0][4] = new Vector2d(1.3125, 0.0);
			points[0][5] = new Vector2d(1.375, 0.0);
			points[0][6] = new Vector2d(0.5, 0.0);
		} else if(comingFrom == 1) {
			points[0][0] = new Vector2d(0.0, 1.375);
			points[0][1] = new Vector2d(0.0, 1.3125);
			points[0][2] = new Vector2d(0.0, 1.25);
			points[0][3] = new Vector2d(0.0, -0.25);
			points[0][4] = new Vector2d(0.0, -0.3125);
			points[0][5] = new Vector2d(0.0, -0.375);
			points[0][6] = new Vector2d(0.0, 0.5);
		}
		
		for(int j = 0; j < 7; j++) {
			for(int i = 1; i < steps + 1; i++) {
				points[i][j] = new Vector2d();
				if(eastWest) {
					points[i][j].x = points[i-1][j].x + Math.cos((i - 1) * angle) * factors[j] * directionX;
					points[i][j].y = points[i-1][j].y + Math.sin((i - 1) * angle) * factors[j] * directionZ;
				} else {
					points[i][j].x = points[i-1][j].x + Math.sin((i - 1) * angle) * factors[j] * directionX;
					points[i][j].y = points[i-1][j].y + Math.cos((i - 1) * angle) * factors[j] * directionZ;
				}
			}
		}
		currentStep = 0;
		length = 2;
		if(steps < 2) length = 1;
		
		markDirty();
	}
	
	
	@Override
	public Vec3d getNextPosition(Vec3d position, Vec3d motion) {
		//motion = new Vec3d(-motion.x, motion.y, -motion.z);
		//if(!this.world.isRemote) System.out.println(motion.x + "\t" + motion.z);
		if(position.x > 516.9) {
			int j = 5;
			//System.out.println(j);
		}
		int index = 0;
		float speed = (float) Math.sqrt(Math.pow(motion.x, 2) + Math.pow(motion.y, 2) + Math.pow(motion.z, 2));
		boolean backwards = false;
		Vec3d point;
		Vec3d dir;
		if(comingFrom == 0 && goingTo == 1) {
			while(true) {
				if(index == points.length) {
					index -= 1;
					break;
				}
				TrackPoint tp = getTrackPoint(index);
				if(!(tp.x >= position.x && tp.z >= position.z)) break;
				index++;
			}
			if(index < 1) index = 1;
			
			TrackPoint currentTP = getTrackPoint(index);
			TrackPoint lastTP = getTrackPoint(index - 1);
			Vec3d pointVector = new Vec3d(currentTP.x - lastTP.x, currentTP.y - lastTP.y, currentTP.z - lastTP.z);
			
			double numerator = motion.dotProduct(pointVector);
			double denominator = Math.abs(motion.lengthVector() * pointVector.lengthVector());
			double angle = Math.acos(numerator / denominator);
			if(numerator / denominator > 1) {
				angle = 0;
			} else if(numerator / denominator < -1) {
				angle = 180;
			}
			if(Math.abs(angle) > (0.5 * Math.PI)) backwards = true;
			if(!backwards) {
				dir = new Vec3d(currentTP.x - lastTP.x, 0, currentTP.z - lastTP.z);
				dir = dir.normalize().scale(speed);
				point = position.add(dir);
				float xPot = 0.00f;
				if(dir.x > 0) xPot = 0.01f;
				else if(dir.x < 0) xPot = -0.01f;
				float zPot = 0.00f;
				if(dir.z > 0) zPot = 0.01f;
				else if(dir.z < 0) zPot = -0.01f;
				if(index != (points.length - 1)) {
					if(point.x < currentTP.x + xPot || point.z < currentTP.z + zPot) { //CHANGE
						point = new Vec3d(currentTP.x + xPot, position.y, currentTP.z + zPot); 
					}
				} else {
					if(point.x < (currentTP.x - 0.1)) { //CHANGE
						point = new Vec3d(currentTP.x - 0.1, position.y, currentTP.z); //CHANGE
					}
				}
				
			} else {
				dir = new Vec3d(lastTP.x - currentTP.x, 0, lastTP.z - currentTP.z);
				dir = dir.normalize().scale(speed);
				point = position.add(dir);
				float xPot = 0.00f;
				if(dir.x > 0) xPot = 0.01f;
				else if(dir.x < 0) xPot = -0.01f;
				float zPot = 0.00f;
				if(dir.z > 0) zPot = 0.01f;
				else if(dir.z < 0) zPot = -0.01f;
				if(index != 1) {
					if(point.x > lastTP.x + xPot || point.z > lastTP.z + zPot) { //CHANGE
						point = new Vec3d(lastTP.x + xPot, position.y, lastTP.z + zPot); 
					}
				} else {
					if(point.z > (lastTP.z + 0.1)) { //CHANGE
						point = new Vec3d(lastTP.x, position.y, lastTP.z + 0.1);  //CHANGE
					}
				}
			}
		} else if(comingFrom == 0 && goingTo == 3) {
			while(true) {
				if(index == points.length) {
					index -= 1;
					break;
				}
				TrackPoint tp = getTrackPoint(index);
				if(!(tp.x <= position.x && tp.z >= position.z)) break;
				index++;
			}
			if(index < 1) index = 1;
			
			TrackPoint currentTP = getTrackPoint(index);
			TrackPoint lastTP = getTrackPoint(index - 1);
			Vec3d pointVector = new Vec3d(currentTP.x - lastTP.x, currentTP.y - lastTP.y, currentTP.z - lastTP.z);
			
			double numerator = motion.dotProduct(pointVector);
			double denominator = Math.abs(motion.lengthVector() * pointVector.lengthVector());
			double angle = Math.acos(numerator / denominator);
			if(numerator / denominator > 1) {
				angle = 0;
			} else if(numerator / denominator < -1) {
				angle = 180;
			}
			if(Math.abs(angle) > (0.5 * Math.PI)) backwards = true;
			if(!backwards) {
				dir = new Vec3d(currentTP.x - lastTP.x, 0, currentTP.z - lastTP.z);
				dir = dir.normalize().scale(speed);
				point = position.add(dir);
				float xPot = 0.00f;
				if(dir.x > 0) xPot = 0.01f;
				else if(dir.x < 0) xPot = -0.01f;
				float zPot = 0.00f;
				if(dir.z > 0) zPot = 0.01f;
				else if(dir.z < 0) zPot = -0.01f;
				if(index != (points.length - 1)) {
					if(point.x > currentTP.x + xPot || point.z < currentTP.z + zPot) { //CHANGE
						point = new Vec3d(currentTP.x + xPot, position.y, currentTP.z + zPot); 
					}
				} else {
					if(point.x > (currentTP.x + 0.1)) { //CHANGE
						point = new Vec3d(currentTP.x + 0.1, position.y, currentTP.z); //CHANGE
					}
				}
				
			} else {
				dir = new Vec3d(lastTP.x - currentTP.x, 0, lastTP.z - currentTP.z);
				dir = dir.normalize().scale(speed);
				point = position.add(dir);
				float xPot = 0.00f;
				if(dir.x > 0) xPot = 0.01f;
				else if(dir.x < 0) xPot = -0.01f;
				float zPot = 0.00f;
				if(dir.z > 0) zPot = 0.01f;
				else if(dir.z < 0) zPot = -0.01f;
				if(index != 1) {
					if(point.x < lastTP.x + xPot || point.z > lastTP.z + zPot) { //CHANGE
						point = new Vec3d(lastTP.x + xPot, position.y, lastTP.z + zPot); 
					}
				} else {
					if(point.z > (lastTP.z + 0.1)) { //CHANGE
						point = new Vec3d(lastTP.x, position.y, lastTP.z + 0.1);  //CHANGE
					}
				}
			}
		} else if(comingFrom == 1 && goingTo == 0) {
			while(true) {
				if(index == points.length) {
					index -= 1;
					break;
				}
				TrackPoint tp = getTrackPoint(index);
				if(!(tp.x <= position.x && tp.z <= position.z)) break;
				index++;
			}
			if(index < 1) index = 1;
			
			TrackPoint currentTP = getTrackPoint(index);
			TrackPoint lastTP = getTrackPoint(index - 1);
			Vec3d pointVector = new Vec3d(currentTP.x - lastTP.x, currentTP.y - lastTP.y, currentTP.z - lastTP.z);
			
			double numerator = motion.dotProduct(pointVector);
			double denominator = Math.abs(motion.lengthVector() * pointVector.lengthVector());
			double angle = Math.acos(numerator / denominator);
			if(numerator / denominator > 1) {
				angle = 0;
			} else if(numerator / denominator < -1) {
				angle = 180;
			}
			if(Math.abs(angle) > (0.5 * Math.PI)) backwards = true;
			if(!backwards) {
				dir = new Vec3d(currentTP.x - lastTP.x, 0, currentTP.z - lastTP.z);
				dir = dir.normalize().scale(speed);
				point = position.add(dir);
				float xPot = 0.00f;
				if(dir.x > 0) xPot = 0.01f;
				else if(dir.x < 0) xPot = -0.01f;
				float zPot = 0.00f;
				if(dir.z > 0) zPot = 0.01f;
				else if(dir.z < 0) zPot = -0.01f;
				if(index != (points.length - 1)) {
					if(point.x > currentTP.x + xPot || point.z > currentTP.z + zPot) { //CHANGE
						point = new Vec3d(currentTP.x + xPot, position.y, currentTP.z + zPot); 
					}
				} else {
					if(point.z > (currentTP.z + 0.1)) { //CHANGE
						point = new Vec3d(currentTP.x, position.y, currentTP.z + 0.1); //CHANGE
					}
				}
				
			} else {
				dir = new Vec3d(lastTP.x - currentTP.x, 0, lastTP.z - currentTP.z);
				dir = dir.normalize().scale(speed);
				point = position.add(dir);
				float xPot = 0.00f;
				if(dir.x > 0) xPot = 0.01f;
				else if(dir.x < 0) xPot = -0.01f;
				float zPot = 0.00f;
				if(dir.z > 0) zPot = 0.01f;
				else if(dir.z < 0) zPot = -0.01f;
				if(index != 1) {
					if(point.x < lastTP.x + xPot || point.z < lastTP.z + zPot) { //CHANGE
						point = new Vec3d(lastTP.x + xPot, position.y, lastTP.z + zPot); 
					}
				} else {
					if(point.x < (lastTP.x - 0.1)) { //CHANGE
						point = new Vec3d(lastTP.x - 0.1, position.y, lastTP.z);  //CHANGE
					}
				}
			}
		} else if(comingFrom == 1 && goingTo == 2) {
			while(true) {
				if(index == points.length) {
					index -= 1;
					break;
				}
				TrackPoint tp = getTrackPoint(index);
				if(!(tp.x <= position.x && tp.z >= position.z)) break;
				index++;
			}
			if(index < 1) index = 1;
			
			TrackPoint currentTP = getTrackPoint(index);
			TrackPoint lastTP = getTrackPoint(index - 1);
			Vec3d pointVector = new Vec3d(currentTP.x - lastTP.x, currentTP.y - lastTP.y, currentTP.z - lastTP.z);
			
			double numerator = motion.dotProduct(pointVector);
			double denominator = Math.abs(motion.lengthVector() * pointVector.lengthVector());
			double angle = Math.acos(numerator / denominator);
			if(numerator / denominator > 1) {
				angle = 0;
			} else if(numerator / denominator < -1) {
				angle = 180;
			}
			if(Math.abs(angle) > (0.5 * Math.PI)) backwards = true;
			if(!backwards) {
				dir = new Vec3d(currentTP.x - lastTP.x, 0, currentTP.z - lastTP.z);
				dir = dir.normalize().scale(speed);
				point = position.add(dir);
				float xPot = 0.00f;
				if(dir.x > 0) xPot = 0.01f;
				else if(dir.x < 0) xPot = -0.01f;
				float zPot = 0.00f;
				if(dir.z > 0) zPot = 0.01f;
				else if(dir.z < 0) zPot = -0.01f;
				if(index != (points.length - 1)) {
					if(point.x > currentTP.x + xPot || point.z < currentTP.z + zPot) { //CHANGE
						point = new Vec3d(currentTP.x + xPot, position.y, currentTP.z + zPot); 
					}
				} else {
					if(point.z < (currentTP.z + 0.1)) { //CHANGE
						point = new Vec3d(currentTP.x, position.y, currentTP.z - 0.1); //CHANGE
					}
				}
				
			} else {
				dir = new Vec3d(lastTP.x - currentTP.x, 0, lastTP.z - currentTP.z);
				dir = dir.normalize().scale(speed);
				point = position.add(dir);
				float xPot = 0.00f;
				if(dir.x > 0) xPot = 0.01f;
				else if(dir.x < 0) xPot = -0.01f;
				float zPot = 0.00f;
				if(dir.z > 0) zPot = 0.01f;
				else if(dir.z < 0) zPot = -0.01f;
				if(index != 1) {
					if(point.x < lastTP.x + xPot || point.z > lastTP.z + zPot) { //CHANGE
						point = new Vec3d(lastTP.x + xPot, position.y, lastTP.z + zPot); 
					}
				} else {
					if(point.x < (lastTP.x - 0.1)) { //CHANGE
						point = new Vec3d(lastTP.x - 0.1, position.y, lastTP.z);  //CHANGE
					}
				}
			}
		} else if(comingFrom == 2 && goingTo == 1) {
			while(true) {
				if(index == points.length) {
					index -= 1;
					break;
				}
				TrackPoint tp = getTrackPoint(index);
				if(!(tp.x >= position.x && tp.z <= position.z)) break;
				index++;
			}
			if(index < 1) index = 1;
			
			TrackPoint currentTP = getTrackPoint(index);
			TrackPoint lastTP = getTrackPoint(index - 1);
			Vec3d pointVector = new Vec3d(currentTP.x - lastTP.x, currentTP.y - lastTP.y, currentTP.z - lastTP.z);
			
			double numerator = motion.dotProduct(pointVector);
			double denominator = Math.abs(motion.lengthVector() * pointVector.lengthVector());
			double angle = Math.acos(numerator / denominator);
			if(numerator / denominator > 1) {
				angle = 0;
			} else if(numerator / denominator < -1) {
				angle = 180;
			}
			if(Math.abs(angle) > (0.5 * Math.PI)) backwards = true;
			if(!backwards) {
				dir = new Vec3d(currentTP.x - lastTP.x, 0, currentTP.z - lastTP.z);
				dir = dir.normalize().scale(speed);
				point = position.add(dir);
				float xPot = 0.00f;
				if(dir.x > 0) xPot = 0.01f;
				else if(dir.x < 0) xPot = -0.01f;
				float zPot = 0.00f;
				if(dir.z > 0) zPot = 0.01f;
				else if(dir.z < 0) zPot = -0.01f;
				if(index != (points.length - 1)) {
					if(point.x < currentTP.x + xPot || point.z > currentTP.z + zPot) { //CHANGE
						point = new Vec3d(currentTP.x + xPot, position.y, currentTP.z + zPot); 
					}
				} else {
					if(point.x < (currentTP.x - 0.1)) { //CHANGE
						point = new Vec3d(currentTP.x - 0.1, position.y, currentTP.z); //CHANGE
					}
				}
				
			} else {
				dir = new Vec3d(lastTP.x - currentTP.x, 0, lastTP.z - currentTP.z);
				dir = dir.normalize().scale(speed);
				point = position.add(dir);
				float xPot = 0.00f;
				if(dir.x > 0) xPot = 0.01f;
				else if(dir.x < 0) xPot = -0.01f;
				float zPot = 0.00f;
				if(dir.z > 0) zPot = 0.01f;
				else if(dir.z < 0) zPot = -0.01f;
				if(index != 1) {
					if(point.x > lastTP.x + xPot || point.z < lastTP.z + zPot) { //CHANGE
						point = new Vec3d(lastTP.x + xPot, position.y, lastTP.z + zPot); 
					}
				} else {
					if(point.z < (lastTP.z - 0.1)) { //CHANGE
						point = new Vec3d(lastTP.x, position.y, lastTP.z - 0.1);  //CHANGE
					}
				}
			}
		} else if(comingFrom == 2 && goingTo == 3) {
			while(true) {
				if(index == points.length) {
					index -= 1;
					break;
				}
				TrackPoint tp = getTrackPoint(index);
				if(!(tp.x <= position.x && tp.z <= position.z)) break;
				index++;
			}
			if(index < 1) index = 1;
			
			TrackPoint currentTP = getTrackPoint(index);
			TrackPoint lastTP = getTrackPoint(index - 1);
			Vec3d pointVector = new Vec3d(currentTP.x - lastTP.x, currentTP.y - lastTP.y, currentTP.z - lastTP.z);
			
			double numerator = motion.dotProduct(pointVector);
			double denominator = Math.abs(motion.lengthVector() * pointVector.lengthVector());
			double angle = Math.acos(numerator / denominator);
			if(numerator / denominator > 1) {
				angle = 0;
			} else if(numerator / denominator < -1) {
				angle = 180;
			}
			if(Math.abs(angle) > (0.5 * Math.PI)) backwards = true;
			if(!backwards) {
				dir = new Vec3d(currentTP.x - lastTP.x, 0, currentTP.z - lastTP.z);
				dir = dir.normalize().scale(speed);
				point = position.add(dir);
				float xPot = 0.00f;
				if(dir.x > 0) xPot = 0.01f;
				else if(dir.x < 0) xPot = -0.01f;
				float zPot = 0.00f;
				if(dir.z > 0) zPot = 0.01f;
				else if(dir.z < 0) zPot = -0.01f;
				if(index != (points.length - 1)) {
					if(point.x > currentTP.x + xPot || point.z > currentTP.z + zPot) { //CHANGE
						point = new Vec3d(currentTP.x + xPot, position.y, currentTP.z + zPot); 
					}
				} else {
					if(point.x > (currentTP.x + 0.1)) { //CHANGE
						point = new Vec3d(currentTP.x + 0.1, position.y, currentTP.z); //CHANGE
					}
				}
				
			} else {
				dir = new Vec3d(lastTP.x - currentTP.x, 0, lastTP.z - currentTP.z);
				dir = dir.normalize().scale(speed);
				point = position.add(dir);
				float xPot = 0.00f;
				if(dir.x > 0) xPot = 0.01f;
				else if(dir.x < 0) xPot = -0.01f;
				float zPot = 0.00f;
				if(dir.z > 0) zPot = 0.01f;
				else if(dir.z < 0) zPot = -0.01f;
				if(index != 1) {
					if(point.x < lastTP.x + xPot || point.z < lastTP.z + zPot) { //CHANGE
						point = new Vec3d(lastTP.x + xPot, position.y, lastTP.z + zPot); 
					}
				} else {
					if(point.z < (lastTP.z - 0.1)) { //CHANGE
						point = new Vec3d(lastTP.x, position.y, lastTP.z - 0.1);  //CHANGE
					}
				}
			}
		} else if(comingFrom == 3 && goingTo == 0) {
			while(true) {
				if(index == points.length) {
					index -= 1;
					break;
				}
				TrackPoint tp = getTrackPoint(index);
				if(!(tp.x >= position.x && tp.z <= position.z)) break;
				index++;
			}
			if(index < 1) index = 1;
			
			TrackPoint currentTP = getTrackPoint(index);
			TrackPoint lastTP = getTrackPoint(index - 1);
			Vec3d pointVector = new Vec3d(currentTP.x - lastTP.x, currentTP.y - lastTP.y, currentTP.z - lastTP.z);
			
			double numerator = motion.dotProduct(pointVector);
			double denominator = Math.abs(motion.lengthVector() * pointVector.lengthVector());
			double angle = Math.acos(numerator / denominator);
			if(numerator / denominator > 1) {
				angle = 0;
			} else if(numerator / denominator < -1) {
				angle = 180;
			}
			if(Math.abs(angle) > (0.5 * Math.PI)) backwards = true;
			if(!backwards) {
				dir = new Vec3d(currentTP.x - lastTP.x, 0, currentTP.z - lastTP.z);
				dir = dir.normalize().scale(speed);
				point = position.add(dir);
				float xPot = 0.00f;
				if(dir.x > 0) xPot = 0.01f;
				else if(dir.x < 0) xPot = -0.01f;
				float zPot = 0.00f;
				if(dir.z > 0) zPot = 0.01f;
				else if(dir.z < 0) zPot = -0.01f;
				if(index != (points.length - 1)) {
					if(point.x < currentTP.x + xPot || point.z > currentTP.z + zPot) { //CHANGE
						point = new Vec3d(currentTP.x + xPot, position.y, currentTP.z + zPot); 
					}
				} else {
					if(point.z > (currentTP.z + 0.1)) { //CHANGE
						point = new Vec3d(currentTP.x, position.y, currentTP.z + 0.1); //CHANGE
					}
				}
				
			} else {
				dir = new Vec3d(lastTP.x - currentTP.x, 0, lastTP.z - currentTP.z);
				dir = dir.normalize().scale(speed);
				point = position.add(dir);
				float xPot = 0.00f;
				if(dir.x > 0) xPot = 0.01f;
				else if(dir.x < 0) xPot = -0.01f;
				float zPot = 0.00f;
				if(dir.z > 0) zPot = 0.01f;
				else if(dir.z < 0) zPot = -0.01f;
				if(index != 1) {
					if(point.x > lastTP.x + xPot || point.z < lastTP.z + zPot) { //CHANGE
						point = new Vec3d(lastTP.x + xPot, position.y, lastTP.z + zPot); 
					}
				} else {
					if(point.x > (lastTP.x + 0.1)) { //CHANGE
						point = new Vec3d(lastTP.x + 0.1, position.y, lastTP.z);  //CHANGE
					}
				}
			}
		} else {
			while(true) {
				if(index == points.length) {
					index -= 1;
					break;
				}
				TrackPoint tp = getTrackPoint(index);
				if(!(tp.x >= position.x && tp.z >= position.z)) break;
				index++;
			}
			if(index < 1) index = 1;
			
			TrackPoint currentTP = getTrackPoint(index);
			TrackPoint lastTP = getTrackPoint(index - 1);
			Vec3d pointVector = new Vec3d(currentTP.x - lastTP.x, currentTP.y - lastTP.y, currentTP.z - lastTP.z);
			
			double numerator = motion.dotProduct(pointVector);
			double denominator = Math.abs(motion.lengthVector() * pointVector.lengthVector());
			double angle = Math.acos(numerator / denominator);
			if(numerator / denominator > 1) {
				angle = 0;
			} else if(numerator / denominator < -1) {
				angle = 180;
			}
			if(Math.abs(angle) > (0.5 * Math.PI)) backwards = true;
			if(!backwards) {
				dir = new Vec3d(currentTP.x - lastTP.x, 0, currentTP.z - lastTP.z);
				dir = dir.normalize().scale(speed);
				point = position.add(dir);
				float xPot = 0.00f;
				if(dir.x > 0) xPot = 0.01f;
				else if(dir.x < 0) xPot = -0.01f;
				float zPot = 0.00f;
				if(dir.z > 0) zPot = 0.01f;
				else if(dir.z < 0) zPot = -0.01f;
				if(index != (points.length - 1)) {
					if(point.x < currentTP.x + xPot || point.z < currentTP.z + zPot) { //CHANGE
						point = new Vec3d(currentTP.x + xPot, position.y, currentTP.z + zPot); 
					}
				} else {
					if(point.z < (currentTP.z - 0.1)) { //CHANGE
						point = new Vec3d(currentTP.x, position.y, currentTP.z - 0.1); //CHANGE
					}
				}
				
			} else {
				dir = new Vec3d(lastTP.x - currentTP.x, 0, lastTP.z - currentTP.z);
				dir = dir.normalize().scale(speed);
				point = position.add(dir);
				float xPot = 0.00f;
				if(dir.x > 0) xPot = 0.01f;
				else if(dir.x < 0) xPot = -0.01f;
				float zPot = 0.00f;
				if(dir.z > 0) zPot = 0.01f;
				else if(dir.z < 0) zPot = -0.01f;
				if(index != 1) {
					if(point.x > lastTP.x + xPot || point.z > lastTP.z + zPot) { //CHANGE
						point = new Vec3d(lastTP.x + xPot, position.y, lastTP.z + zPot); 
					}
				} else {
					if(point.x > (lastTP.x + 0.1)) { //CHANGE
						point = new Vec3d(lastTP.x + 0.1, position.y, lastTP.z);  //CHANGE
					}
				}
			}
		}

		return point;
	}

	@Override
	public double getTrackGauge() {
		return 1.5;
	}
	
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
		markDirty();
	}

	public int getSteps() {
		return steps;
	}




	public void setSteps(int steps) {
		this.steps = steps;
		markDirty();
	}




	public void setCurrentStep(int currentStep) {
		this.currentStep = currentStep;
		markDirty();
	}




	public void setPoints(Vector2d[][] points) {
		this.points = points;
		markDirty();
	}




	public void setLength(byte length) {
		this.length = length;
		markDirty();
	}
	
	
	public TrackPoint getTrackPoint(int index) {
		BlockPos pos = getPos();
		byte direction = (byte) ((index + 1 == points.length) ? goingTo : -comingFrom);
		return new TrackPoint(points[index][6].x + pos.getX(), pos.getY() + 0.125, points[index][6].y + pos.getZ(), pos, direction);
	}
	
	public void setComingFrom(byte value) {
		this.comingFrom = value;
		markDirty();
	}
		
	public void setGoingTo(byte value) {
		this.goingTo = value;
		markDirty();
	}
	
	public void setRadius(float value) {
		this.radius = value;
		markDirty();
	}
	
	public Vector2d[][] getAllPoints() {
		return points;
	}
	
	public Vector2d[][] getPoints() {
		Vector2d[][] ret;
		if(points != null ) {
			ret = new Vector2d[length + 1][7];
			for(int i = 0; i < 0 + length + 1; i++) {
				for(int j = 0; j < 7; j++) {
					ret[i][j] = points[2 * currentStep + i][j];
				}
			}
		} else {
			ret = new Vector2d[0][7];
		}
		
		return ret;
	}

	
	public byte getComingFrom() {
		return comingFrom;
	}

	public byte getGoingTo() {
		return goingTo;
	}

	public float getRadius() {
		return radius;
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
		compound.setLong("curve_id", id);
		compound.setByte("comingFrom", comingFrom);
		compound.setByte("goingTo", goingTo);
		compound.setFloat("radius", radius);
		compound.setInteger("steps", steps);
		compound.setInteger("currentStep", currentStep);
		compound.setByte("length", length);
		for(int i = 0; i < points.length; i++) {
			for(int j = 0; j < 7; j++) {
				compound.setDouble("points" + i + "x" + j, points[i][j].x);
				compound.setDouble("points" + i + "y" + j, points[i][j].y);
			}
		}
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		id = compound.getLong("curve_id");
		comingFrom = compound.getByte("comingFrom");
		goingTo = compound.getByte("goingTo");
		radius = compound.getFloat("radius");
		steps = compound.getInteger("steps");
		currentStep = compound.getInteger("currentStep");
		length = compound.getByte("length");
		points = new Vector2d[steps + 1][7];
		for(int i = 0; i < steps + 1; i++) {
			for(int j = 0; j < 7; j++) {
				points[i][j] = new Vector2d();
				points[i][j].x = compound.getDouble("points" + i + "x" + j);
				points[i][j].y = compound.getDouble("points" + i + "y" + j);
			}
		}
	}

	
	
}
