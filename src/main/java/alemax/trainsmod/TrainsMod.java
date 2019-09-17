package alemax.trainsmod;

import java.util.HashMap;

import alemax.trainsmod.proxy.CommonProxy;
import alemax.trainsmod.util.ChunkLoadingCallback;
import alemax.trainsmod.util.Reference;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

//I wonder if anyone actually reads this, and if yes: Hi! :)

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class TrainsMod {
	
	@Instance
	public static TrainsMod instance;
	
	public static HashMap<ChunkPos, Integer> ticketList;
	private static Ticket chunkLoaderTicket;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		World.MAX_ENTITY_RADIUS = Math.max(World.MAX_ENTITY_RADIUS, 32);
		proxy.preInit(event);
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event) {
		proxy.init(event);
	}
	
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		ticketList = new HashMap<ChunkPos, Integer>();
		ForgeChunkManager.setForcedChunkLoadingCallback(instance, new ChunkLoadingCallback());
	}
	
}
