package alemax.trainsmod.global.train;

import net.minecraft.entity.Entity;

public class Seat {

    public float xOffset;
    public float yOffset;
    public float zOffset;
    public float yawOffset;
    public boolean drivingSeat;
    public Entity sittingEntity;

    public Seat(float xOffset, float yOffset, float zOffset, float yawOffset, boolean drivingSeat) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.yawOffset = yawOffset;
        this.drivingSeat = drivingSeat;
    }

}
