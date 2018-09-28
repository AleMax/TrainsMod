package alemax.trainsmod.entities;

import alemax.trainsmod.util.Seat;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityBR143 extends EntityRailcar {

	public EntityBR143(World worldIn) {
		super(worldIn);
		setup();
	}


	public EntityBR143(World worldIn, BlockPos placedOn) {
		super(worldIn, placedOn);
		setup();
	}
	
	public void setup() {
		setBoundBox(new AxisAlignedBB(-1.5625004768371582, 0.0, -11.0625, 1.5625005960464478, 5.625, 3.65625));
		this.maxSpeed = 30;
		this.maxAcceleration = 0.6;
		this.maxDecceleration = 1.2;
		this.maxPassengersCount = 4;
		this.axleDistance = 7.5;
		this.seats = new Seat[maxPassengersCount];
		this.seats[0] = new Seat(0.77, 1.7, 2.1, 0, true);
		this.seats[1] = new Seat(-0.77, 1.7, 2.1, 0, false);
		this.seats[2] = new Seat(-0.77, 1.7, -9.6, 180, false);
		this.seats[3] = new Seat(0.77, 1.7, -9.6, 180, false);
	}
	
}
