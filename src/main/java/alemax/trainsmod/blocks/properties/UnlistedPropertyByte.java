package alemax.trainsmod.blocks.properties;

import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyByte implements IUnlistedProperty<Byte> {

	private final String name;
	
	public UnlistedPropertyByte(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isValid(Byte value) {
		return true;
	}

	@Override
	public Class<Byte> getType() {
		return Byte.class;
	}

	@Override
	public String valueToString(Byte value) {
		return value.toString();
	}

}
