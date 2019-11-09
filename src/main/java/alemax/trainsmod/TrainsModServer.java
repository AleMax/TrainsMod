package alemax.trainsmod;

import alemax.trainsmod.init.TMPackets;
import net.fabricmc.api.DedicatedServerModInitializer;

public class TrainsModServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {

        //Register Server Packets
        TMPackets.registerServer();
    }
}
