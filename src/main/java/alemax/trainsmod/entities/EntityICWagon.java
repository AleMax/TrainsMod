package alemax.trainsmod.entities;

import alemax.trainsmod.util.Seat;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityICWagon extends EntityRailcar {

	public EntityICWagon(World worldIn) {
		super(worldIn);
		setup();
	}


	public EntityICWagon(World worldIn, BlockPos placedOn) {
		super(worldIn, placedOn);
		setup();
	}
	
	public void setup() {
		setBoundBox(new AxisAlignedBB(-1.5625007152557373, 0.0, -26.1875, 1.5625007152557373, 4.3125, 4.5625));
		this.maxSpeed = 30;
		
		this.maxPassengersCount = 0;
		this.axleDistance = 21.6875;
		this.seats = new Seat[maxPassengersCount];
		
	}
	
}
