package alemax.trainsmod.util;

import net.minecraft.entity.Entity;

public class Seat {
	
	public double xOffset;
	public double yOffset;
	public double zOffset;
	public float yawOffset;
	public boolean drivingSeat;
	public Entity sittingEntity;
	
	public Seat(double xOffset, double yOffset, double zOffset, float yawOffset, boolean drivingSeat) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.zOffset = zOffset;
		this.yawOffset = yawOffset;
		this.drivingSeat = drivingSeat;
	}
	
}
