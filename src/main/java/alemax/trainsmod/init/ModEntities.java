package alemax.trainsmod.init;

import alemax.trainsmod.TrainsMod;
import alemax.trainsmod.entities.EntityBR143;
import alemax.trainsmod.entities.EntityFreightcar;
import alemax.trainsmod.entities.EntityICRailcar;
import alemax.trainsmod.entities.EntityICWagon;
import alemax.trainsmod.entities.EntityLivestockcar;
import alemax.trainsmod.entities.EntitySNexasRailcar;
import alemax.trainsmod.entities.EntitySNexasWagon;
import alemax.trainsmod.entities.EntityTankWagonBlue;
import alemax.trainsmod.entities.EntityTankWagonGray;
import alemax.trainsmod.entities.EntityTankWagonGreen;
import alemax.trainsmod.entities.EntityTankWagonYellow;
import alemax.trainsmod.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModEntities {
	
	public static void init() {
		int id = 1;
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, "br143"), EntityBR143.class, "BR-143", id++, TrainsMod.instance, 128, 40, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, "freightcar"), EntityFreightcar.class, "Freightcar", id++, TrainsMod.instance, 128, 40, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, "livestockcar"), EntityLivestockcar.class, "Livestockcar", id++, TrainsMod.instance, 128, 40, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, "ic_railcar"), EntityICRailcar.class, "ICRailcar", id++, TrainsMod.instance, 128, 40, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, "s_nexas_wagon"), EntitySNexasWagon.class, "SNexasWagon", id++, TrainsMod.instance, 128, 40, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, "s_nexas_railcar"), EntitySNexasRailcar.class, "SNexasRailcar", id++, TrainsMod.instance, 128, 40, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, "ic_wagon"), EntityICWagon.class, "ICWagon", id++, TrainsMod.instance, 128, 40, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, "tank_wagon_gray"), EntityTankWagonGray.class, "TankWagonGray", id++, TrainsMod.instance, 128, 40, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, "tank_wagon_blue"), EntityTankWagonBlue.class, "TankWagonBlue", id++, TrainsMod.instance, 128, 40, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, "tank_wagon_green"), EntityTankWagonGreen.class, "TankWagonGreen", id++, TrainsMod.instance, 128, 40, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, "tank_wagon_yellow"), EntityTankWagonYellow.class, "TankWagonYellow", id++, TrainsMod.instance, 128, 40, false);

		
		
	}
	
	@SideOnly(Side.CLIENT)
    public static void initModels() {
        
    }
	
}
