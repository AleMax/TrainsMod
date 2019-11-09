package alemax.trainsmod;

import alemax.trainsmod.init.TMPackets;
import net.fabricmc.api.ClientModInitializer;

public class TrainsModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        //Register Client Packets
        TMPackets.registerClient();

    }
}
