package alemax.trainsmod.blocks.properties;

import javax.vecmath.Vector2d;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyRailPoints implements IUnlistedProperty<Vector2d[][]> {

	private final String name;
	
	public UnlistedPropertyRailPoints(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isValid(Vector2d[][] value) {
		return true;
	}

	@Override
	public Class<Vector2d[][]> getType() {
		return Vector2d[][].class;
	}

	@Override
	public String valueToString(Vector2d[][] value) {
		return value.toString();
	}

}
