package alemax.trainsmod.init;

import alemax.trainsmod.networking.PacketS2CSyncGlobalOnPlayerJoin;
import alemax.trainsmod.networking.TMPacket;

public class TMPackets {

    public static PacketS2CSyncGlobalOnPlayerJoin packetS2CSyncGlobalOnPlayerJoin;

    public static void initAll() {
        TMPackets.packetS2CSyncGlobalOnPlayerJoin = new PacketS2CSyncGlobalOnPlayerJoin();

    }

    public static void registerClient() {
        packetS2CSyncGlobalOnPlayerJoin.register();

    }

    public static void registerServer() {



    }

}
