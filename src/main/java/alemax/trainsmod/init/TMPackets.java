package alemax.trainsmod.init;

import alemax.trainsmod.networking.PacketS2CSyncGlobalOnPlayerJoin;
import alemax.trainsmod.networking.PacketS2CTrackMarkerPlacement;
import alemax.trainsmod.networking.TMPacket;

public class TMPackets {

    public static PacketS2CSyncGlobalOnPlayerJoin packetS2CSyncGlobalOnPlayerJoin;
    public static PacketS2CTrackMarkerPlacement packetS2CTrackMarkerPlacement;

    public static void initAll() {
        TMPackets.packetS2CSyncGlobalOnPlayerJoin = new PacketS2CSyncGlobalOnPlayerJoin();
        TMPackets.packetS2CTrackMarkerPlacement = new PacketS2CTrackMarkerPlacement();
    }

    public static void registerClient() {
        packetS2CSyncGlobalOnPlayerJoin.register();
        packetS2CTrackMarkerPlacement.register();
    }

    public static void registerServer() {

    }

}
