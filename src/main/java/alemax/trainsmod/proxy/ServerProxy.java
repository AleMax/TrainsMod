package alemax.trainsmod.proxy;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.SERVER)
public class ServerProxy extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		
		
	}
	
	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
	}
	
	
	
}
