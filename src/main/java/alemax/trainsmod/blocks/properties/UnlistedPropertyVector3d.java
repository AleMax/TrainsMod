package alemax.trainsmod.blocks.properties;

import javax.vecmath.Vector3d;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyVector3d implements IUnlistedProperty<Vector3d[]> {

	private final String name;
	
	public UnlistedPropertyVector3d(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isValid(Vector3d[] value) {
		return true;
	}

	@Override
	public Class<Vector3d[]> getType() {
		return Vector3d[].class;
	}

	@Override
	public String valueToString(Vector3d[] value) {
		return value.toString();
	}

}
