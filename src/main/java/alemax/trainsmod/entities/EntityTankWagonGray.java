package alemax.trainsmod.entities;

import alemax.trainsmod.util.Seat;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityTankWagonGray extends EntityRailcar {

	public EntityTankWagonGray(World worldIn) {
		super(worldIn);
		setup();
	}


	public EntityTankWagonGray(World worldIn, BlockPos placedOn) {
		super(worldIn, placedOn);
		setup();
	}
	
	public void setup() {
		setBoundBox(new AxisAlignedBB(-1.5000007152557373, 0.0, -13.5625, 1.500000238418579, 4.5, 2.1875));
		this.maxSpeed = 30;
		this.acceleration = 1.5;
		this.maxPassengersCount = 0;
		this.axleDistance = 11.4375;
		this.seats = new Seat[maxPassengersCount];
		
	}
	
}
