package alemax.trainsmod.init;

import alemax.trainsmod.networking.*;

public class TMPackets {

    public static PacketS2CSyncGlobalOnPlayerJoin packetS2CSyncGlobalOnPlayerJoin;
    public static PacketS2CTrackMarkerPlacement packetS2CTrackMarkerPlacement;
    public static PacketS2CTrackMarkerRemoval packetS2CTrackMarkerRemoval;
    public static PacketS2CSaveGUITrackMarker packetS2CSaveGUITrackMarker;

    public static PacketC2SSaveGUITrackMarker packetC2SSaveGUITrackMarker;

    public static void registerCommon() {
        packetS2CSyncGlobalOnPlayerJoin = new PacketS2CSyncGlobalOnPlayerJoin();
        packetS2CTrackMarkerPlacement = new PacketS2CTrackMarkerPlacement();
        packetS2CTrackMarkerRemoval = new PacketS2CTrackMarkerRemoval();
        packetS2CSaveGUITrackMarker = new PacketS2CSaveGUITrackMarker();

        packetC2SSaveGUITrackMarker = new PacketC2SSaveGUITrackMarker();



        packetC2SSaveGUITrackMarker.register();

    }

    public static void registerClient() {

        packetS2CSyncGlobalOnPlayerJoin.register();
        packetS2CTrackMarkerPlacement.register();
        packetS2CTrackMarkerRemoval.register();
        packetS2CSaveGUITrackMarker.register();

    }

}
