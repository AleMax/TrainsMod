package alemax.trainsmod.entities;

import alemax.trainsmod.util.Seat;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntitySNexasRailcar extends EntityRailcar {

	public EntitySNexasRailcar(World worldIn) {
		super(worldIn);
		setup();
	}


	public EntitySNexasRailcar(World worldIn, BlockPos placedOn) {
		super(worldIn, placedOn);
		setup();
	}
	
	public void setup() {
		setBoundBox(new AxisAlignedBB(-1.7500004768371582, 0.0, -20.625, 1.7499996423721313, 4.4375, 4.75));
		this.maxSpeed = 30;
		this.acceleration = 1.5;
		this.maxPassengersCount = 0;
		this.axleDistance = 17.1875;
		this.seats = new Seat[maxPassengersCount];
		
	}
	
}
