package alemax.trainsmod;

import alemax.trainsmod.init.TMBlockEntities;
import alemax.trainsmod.init.TMBlocks;
import alemax.trainsmod.init.TMItemGroups;
import alemax.trainsmod.init.TMPackets;
import alemax.trainsmod.networking.PacketS2CSyncGlobalOnPlayerJoin;
import net.fabricmc.api.ModInitializer;

public class TrainsMod implements ModInitializer {

	public static final String modid = "trainsmod";
	public static final String modName = "TrainsMod";
	public static final String modVersion = "0.2.0";

	@Override
	public void onInitialize() {
		System.out.println("Starting TrainsMod");

		//Register ItemGroups
		TMItemGroups.registerAll();

		//Register Blocks
		TMBlocks.registerAll();

		//Register BlockEntities
		TMBlockEntities.registerAll();

		//Packets
		TMPackets.initAll();
	}

}
