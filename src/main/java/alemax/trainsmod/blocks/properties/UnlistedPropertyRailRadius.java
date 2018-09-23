package alemax.trainsmod.blocks.properties;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyRailRadius implements IUnlistedProperty<Float> {

	private final String name;
	
	public UnlistedPropertyRailRadius(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isValid(Float value) {
		return true;
	}

	@Override
	public Class<Float> getType() {
		return Float.class;
	}
	
	public String valueToString(Float value) {
		return value.toString();
	}

}
