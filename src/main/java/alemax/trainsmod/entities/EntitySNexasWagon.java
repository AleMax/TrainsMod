package alemax.trainsmod.entities;

import alemax.trainsmod.util.Seat;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntitySNexasWagon extends EntityRailcar {

	public EntitySNexasWagon(World worldIn) {
		super(worldIn);
		setup();
	}


	public EntitySNexasWagon(World worldIn, BlockPos placedOn) {
		super(worldIn, placedOn);
		setup();
	}
	
	public void setup() {
		setBoundBox(new AxisAlignedBB(-1.5624998807907104, 0.0, -20.5625, 1.5625009536743164, 4.875, 3.4375));
		this.maxSpeed = 30;
		this.maxPassengersCount = 0;
		this.axleDistance = 17.1875;
		this.seats = new Seat[maxPassengersCount];
		
	}
	
}
