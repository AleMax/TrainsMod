package alemax.trainsmod;

import alemax.trainsmod.block.blockentity.render.BERTrackSuper;
import alemax.trainsmod.init.TMBlockEntityRenderer;
import alemax.trainsmod.init.TMPackets;
import alemax.trainsmod.util.Texture;
import alemax.trainsmod.util.obj.OBJHandler;
import alemax.trainsmod.util.obj.OBJLoader;
import com.mojang.blaze3d.platform.TextureUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.impl.client.texture.SpriteRegistryCallbackHolder;
import net.fabricmc.indigo.renderer.helper.TextureHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureStitcher;

import java.io.IOException;

public class TrainsModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        //MinecraftClient.getInstance().getTextureManager().registerTexture("test", SpriteRegistryCallbackHolder)
        //SpriteRegistryCallbackHolder.eventLocal()

        //Loading OBJLoader
        OBJHandler.INSTANCE = new OBJLoader();

        //Registering BlockEntityRenderer
        TMBlockEntityRenderer.registerAll();

        //Register Client Packets
        TMPackets.registerClient();

        //Loading OBJ models
        String trackModelPath = "/assets/" + TrainsMod.modid + "/models/block/";
        BERTrackSuper.model = OBJHandler.INSTANCE.loadModel(getClass().getResourceAsStream(trackModelPath + "track_concrete.obj"));

    }
}
