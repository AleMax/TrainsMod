package alemax.trainsmod.entities;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityLivestockcar extends EntityRailcar {

	public EntityLivestockcar(World worldIn) {
		super(worldIn);
		setup();
	}


	public EntityLivestockcar(World worldIn, BlockPos placedOn) {
		super(worldIn, placedOn);
		setup();
	}


	@Override
	public void setup() {
		setBoundBox(new AxisAlignedBB(-1.5625008344650269, 0.0, -12.78125, 1.5625004768371582, 4.6875, 2.21875));
		this.maxSpeed = 30;
		this.maxPassengersCount = 0;
		this.axleDistance = 10.625;
	}
	
}
