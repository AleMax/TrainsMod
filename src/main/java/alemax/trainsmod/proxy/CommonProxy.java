package alemax.trainsmod.proxy;

import alemax.trainsmod.blocks.BlockAMRail;
import alemax.trainsmod.blocks.BlockAMRailCurved;
import alemax.trainsmod.blocks.BlockTrackMarking;
import alemax.trainsmod.blocks.tileentities.TileEntityAMRail;
import alemax.trainsmod.blocks.tileentities.TileEntityAMRailCurved;
import alemax.trainsmod.blocks.tileentities.TileEntityTrackMarking;
import alemax.trainsmod.init.CreativeTabTrainsMod;
import alemax.trainsmod.init.ModBlocks;
import alemax.trainsmod.init.ModEntities;
import alemax.trainsmod.init.ModItems;
import alemax.trainsmod.networking.PacketHandler;
import alemax.trainsmod.networking.TrainClientKeysSendMessage;
import alemax.trainsmod.networking.TrainKeysSendMessage;
import alemax.trainsmod.networking.TrainPassengerSyncMessage;
import alemax.trainsmod.networking.TrainSyncMessage;
import alemax.trainsmod.networking.TrainSyncSpeedMessage;
import alemax.trainsmod.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import scala.tools.nsc.interpreter.Tabulator;

@EventBusSubscriber
public class CommonProxy {
	
	public static final CreativeTabTrainsMod tab_trainsmod = new CreativeTabTrainsMod();
	
	public void preInit(FMLPreInitializationEvent e) {
		ModEntities.init();
		int id = 0;
		PacketHandler.INSTANCE.registerMessage(TrainSyncMessage.TrainSyncMessageHandler.class, TrainSyncMessage.class, id++, Side.CLIENT);
		PacketHandler.INSTANCE.registerMessage(TrainSyncSpeedMessage.TrainSyncSpeedMessageHandler.class, TrainSyncSpeedMessage.class, id++, Side.CLIENT);
		PacketHandler.INSTANCE.registerMessage(TrainPassengerSyncMessage.TrainPassengerSyncMessageHandler.class, TrainPassengerSyncMessage.class, id++, Side.CLIENT);
		PacketHandler.INSTANCE.registerMessage(TrainKeysSendMessage.TrainKeysSendMessageHandler.class, TrainKeysSendMessage.class, id++, Side.SERVER);
		PacketHandler.INSTANCE.registerMessage(TrainClientKeysSendMessage.TrainClientKeysSendMessageHandler.class, TrainClientKeysSendMessage.class, id++, Side.CLIENT);

	
	}
	
	public void init(FMLInitializationEvent e) {
		
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		//event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
		event.getRegistry().register(new BlockAMRail());
		event.getRegistry().register(new BlockAMRailCurved());
		event.getRegistry().register(new BlockTrackMarking());
		
		GameRegistry.registerTileEntity(TileEntityAMRailCurved.class, new ResourceLocation(Reference.MODID, "am_rail_curved_tile_entity"));
		GameRegistry.registerTileEntity(TileEntityAMRail.class, new ResourceLocation(Reference.MODID, "am_rail_tile_entity"));
		GameRegistry.registerTileEntity(TileEntityTrackMarking.class, new ResourceLocation(Reference.MODID, "track_marking_tile_entity"));
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
		event.getRegistry().register(new ItemBlock(ModBlocks.track_marking).setRegistryName(ModBlocks.track_marking.getRegistryName()));
		
	}
	
	@SubscribeEvent
	public static void EntityJoinWorldEvent(EntityJoinWorldEvent event) {
		
	}
	
	

}
