package alemax.trainsmod.init;

import alemax.trainsmod.networking.PacketS2CSyncGlobalOnPlayerJoin;
import alemax.trainsmod.networking.PacketS2CTrackMarkerPlacement;
import alemax.trainsmod.networking.PacketS2CTrackMarkerRemoval;
import alemax.trainsmod.networking.TMPacket;

public class TMPackets {

    public static PacketS2CSyncGlobalOnPlayerJoin packetS2CSyncGlobalOnPlayerJoin;
    public static PacketS2CTrackMarkerPlacement packetS2CTrackMarkerPlacement;
    public static PacketS2CTrackMarkerRemoval packetS2CTrackMarkerRemoval;

    public static void initAll() {
        TMPackets.packetS2CSyncGlobalOnPlayerJoin = new PacketS2CSyncGlobalOnPlayerJoin();
        TMPackets.packetS2CTrackMarkerPlacement = new PacketS2CTrackMarkerPlacement();
        packetS2CTrackMarkerRemoval = new PacketS2CTrackMarkerRemoval();
    }

    public static void registerClient() {
        packetS2CSyncGlobalOnPlayerJoin.register();
        packetS2CTrackMarkerPlacement.register();
        packetS2CTrackMarkerRemoval.register();
    }

    public static void registerServer() {

    }

}
