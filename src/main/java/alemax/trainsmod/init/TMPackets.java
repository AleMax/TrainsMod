package alemax.trainsmod.init;

import alemax.trainsmod.networking.*;

public class TMPackets {

    public static PacketS2CSyncGlobalOnPlayerJoin packetS2CSyncGlobalOnPlayerJoin;
    public static PacketS2CTrackMarkerPlacement packetS2CTrackMarkerPlacement;
    public static PacketS2CTrackMarkerRemoval packetS2CTrackMarkerRemoval;
    public static PacketS2CSaveGUITrackMarker packetS2CSaveGUITrackMarker;
    public static PacketS2CTrackBlockPlacement packetS2CTrackBlockPlacement;

    public static PacketC2SSaveGUITrackMarker packetC2SSaveGUITrackMarker;
    public static PacketC2STrackBuild packetC2STrackBuild;

    public static void registerCommon() {
        packetS2CSyncGlobalOnPlayerJoin = new PacketS2CSyncGlobalOnPlayerJoin();
        packetS2CTrackMarkerPlacement = new PacketS2CTrackMarkerPlacement();
        packetS2CTrackMarkerRemoval = new PacketS2CTrackMarkerRemoval();
        packetS2CSaveGUITrackMarker = new PacketS2CSaveGUITrackMarker();
        packetS2CTrackBlockPlacement = new PacketS2CTrackBlockPlacement();

        packetC2SSaveGUITrackMarker = new PacketC2SSaveGUITrackMarker();
        packetC2STrackBuild = new PacketC2STrackBuild();


        packetC2SSaveGUITrackMarker.register();
        packetC2STrackBuild.register();
    }

    public static void registerClient() {

        packetS2CSyncGlobalOnPlayerJoin.register();
        packetS2CTrackMarkerPlacement.register();
        packetS2CTrackMarkerRemoval.register();
        packetS2CSaveGUITrackMarker.register();
        packetS2CTrackBlockPlacement.register();

    }

}
