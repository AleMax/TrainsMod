package alemax.trainsmod.entities;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityFreightcar extends EntityRailcar {

	public EntityFreightcar(World worldIn) {
		super(worldIn);
		setup();
	}


	public EntityFreightcar(World worldIn, BlockPos placedOn) {
		super(worldIn, placedOn);
		setup();
	}


	@Override
	public void setup() {
		setBoundBox(new AxisAlignedBB(-1.5625005960464478, 0.0, -8.53125, 1.5625003576278687, 3.125000238418579, 1.46875));
		this.maxSpeed = 30;
		this.acceleration = 1.5;
		this.maxPassengersCount = 0;
		this.axleDistance = 7.125;
	}
	
}
