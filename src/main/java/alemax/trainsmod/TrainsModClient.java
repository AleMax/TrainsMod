package alemax.trainsmod;

import alemax.trainsmod.init.TMBlockEntityRenderer;
import alemax.trainsmod.init.TMPackets;
import net.fabricmc.api.ClientModInitializer;

public class TrainsModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        //Registering BlockEntityRenderer
        TMBlockEntityRenderer.registerAll();

        //Register Client Packets
        TMPackets.registerClient();



    }
}
