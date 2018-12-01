package alemax.trainsmod.proxy;

import org.lwjgl.input.Keyboard;

import alemax.trainsmod.blocks.models.BakedModelAMRailCurved;
import alemax.trainsmod.blocks.models.BakedModelLoader;
import alemax.trainsmod.blocks.models.BakedModelTrack;
import alemax.trainsmod.blocks.tileentities.TileEntityTrackMarker;
import alemax.trainsmod.blocks.tileentities.tesr.FastTESRTrackMarker;
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
import alemax.trainsmod.entities.renders.RenderBR143;
import alemax.trainsmod.entities.renders.RenderFreightcar;
import alemax.trainsmod.entities.renders.RenderICRailcar;
import alemax.trainsmod.entities.renders.RenderICWagon;
import alemax.trainsmod.entities.renders.RenderLivestockcar;
import alemax.trainsmod.entities.renders.RenderSNexasRailcar;
import alemax.trainsmod.entities.renders.RenderSNexasWagon;
import alemax.trainsmod.entities.renders.RenderTankWagonBlue;
import alemax.trainsmod.entities.renders.RenderTankWagonGray;
import alemax.trainsmod.entities.renders.RenderTankWagonGreen;
import alemax.trainsmod.entities.renders.RenderTankWagonYellow;
import alemax.trainsmod.entities.renders.RenderTrain;
import alemax.trainsmod.init.ModBlocks;
import alemax.trainsmod.init.ModItems;
import alemax.trainsmod.networking.PacketHandler;
import alemax.trainsmod.networking.TrainClientKeysSendMessage;
import alemax.trainsmod.networking.TrainKeysSendMessage;
import alemax.trainsmod.util.ModKeys;
import alemax.trainsmod.util.Reference;
import alemax.trainsmod.util.objloader.OBJHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.animation.AnimationTESR;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	public static KeyBinding keyTrainAccelerate;
	public static KeyBinding keyTrainBrake;
	
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		OBJHandler.INSTANCE = new alemax.trainsmod.util.objloader.OBJLoader();
		ModelLoaderRegistry.registerLoader(new BakedModelLoader());
		OBJLoader.INSTANCE.addDomain(Reference.MODID);
		
		RenderingRegistry.registerEntityRenderingHandler(EntityBR143.class, RenderBR143.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityFreightcar.class, RenderFreightcar.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityLivestockcar.class, RenderLivestockcar.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityICRailcar.class, RenderICRailcar.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntitySNexasWagon.class, RenderSNexasWagon.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntitySNexasRailcar.class, RenderSNexasRailcar.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityICWagon.class, RenderICWagon.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityTankWagonGray.class, RenderTankWagonGray.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityTankWagonBlue.class, RenderTankWagonBlue.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityTankWagonGreen.class, RenderTankWagonGreen.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityTankWagonYellow.class, RenderTankWagonYellow.FACTORY);

		
		MinecraftForge.EVENT_BUS.register(new RenderTrain());
		
				
	}
	
	@Override
	public void init(FMLInitializationEvent e) {
		
		keyTrainAccelerate = new KeyBinding("key.trainsmod.accel", Keyboard.KEY_W, "key.trainsmod.category");
		keyTrainBrake = new KeyBinding("key.trainsmod.brake", Keyboard.KEY_S, "key.trainsmod.category");
		
		ClientRegistry.registerKeyBinding(keyTrainAccelerate);
		ClientRegistry.registerKeyBinding(keyTrainBrake);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTrackMarker.class, new FastTESRTrackMarker());
		
		
		super.init(e);
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.am_rail), 0, new ModelResourceLocation(ModBlocks.am_rail.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.track_marker), 0, new ModelResourceLocation(ModBlocks.track_marker.getRegistryName(), "inventory"));
		
		//DO THIS FOR TRACK TOO
		StateMapperBase stateMapper = new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return BakedModelAMRailCurved.BAKED_MODEL;
			}
		};
		
		ModelLoader.setCustomStateMapper(ModBlocks.am_rail_curved, stateMapper);
		
		StateMapperBase stateMapperTrack = new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return BakedModelTrack.BAKED_MODEL;
			}
		};
		
		ModelLoader.setCustomStateMapper(ModBlocks.track, stateMapperTrack);
		
		ModelLoader.setCustomModelResourceLocation(ModItems.item_br143, 0, new ModelResourceLocation(ModItems.item_br143.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.item_train_rotator, 0, new ModelResourceLocation(ModItems.item_train_rotator.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.item_train_starter, 0, new ModelResourceLocation(ModItems.item_train_starter.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.item_train_remover, 0, new ModelResourceLocation(ModItems.item_train_remover.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.item_train_connector, 0, new ModelResourceLocation(ModItems.item_train_connector.getRegistryName(), "inventory"));
		
		
	
	}
	
	@SubscribeEvent
	public static void onKeyInput(KeyInputEvent event) {
		
		//System.out.println("INPUT");
		
		KeyBinding keyTrainAccelerate = ClientProxy.keyTrainAccelerate;
		KeyBinding keyTrainBrake = ClientProxy.keyTrainBrake;
		
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		Entity train = player.getRidingEntity();
		
		if(keyTrainAccelerate.isPressed() && train != null) {
			PacketHandler.INSTANCE.sendToServer(new TrainKeysSendMessage(player.getEntityId(), train.getEntityId(), ModKeys.TRAIN_ACCELERATION));
			PacketHandler.INSTANCE.sendToAll(new TrainClientKeysSendMessage(player.getEntityId(), train.getEntityId(), ModKeys.TRAIN_ACCELERATION));
		}
		if(keyTrainBrake.isPressed() && train != null) {
			PacketHandler.INSTANCE.sendToServer(new TrainKeysSendMessage(player.getEntityId(), train.getEntityId(), ModKeys.TRAIN_BRAKE));
			PacketHandler.INSTANCE.sendToAll(new TrainClientKeysSendMessage(player.getEntityId(), train.getEntityId(), ModKeys.TRAIN_BRAKE));
		}
		
	}
	
}
