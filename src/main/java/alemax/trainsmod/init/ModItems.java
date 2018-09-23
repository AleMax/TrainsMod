package alemax.trainsmod.init;

import java.util.ArrayList;
import java.util.List;

import alemax.trainsmod.items.ItemBR143;
import alemax.trainsmod.items.ItemFreightcar;
import alemax.trainsmod.items.ItemICRailcar;
import alemax.trainsmod.items.ItemICWagon;
import alemax.trainsmod.items.ItemLivestockcar;
import alemax.trainsmod.items.ItemSNexasRailcar;
import alemax.trainsmod.items.ItemSNexasWagon;
import alemax.trainsmod.items.ItemTankWagonBlue;
import alemax.trainsmod.items.ItemTankWagonGray;
import alemax.trainsmod.items.ItemTankWagonGreen;
import alemax.trainsmod.items.ItemTankWagonYellow;
import alemax.trainsmod.items.ItemTrainConnector;
import alemax.trainsmod.items.ItemTrainRemover;
import alemax.trainsmod.items.ItemTrainRotator;
import alemax.trainsmod.items.ItemTrainStarter;
import net.minecraft.item.Item;

public class ModItems {
	
	public static final List<Item> ITEMS = new ArrayList<Item>();
	public static ItemBR143 item_br143 = new ItemBR143();
	public static ItemFreightcar item_freightcar = new ItemFreightcar();
	public static ItemLivestockcar item_livestockcar = new ItemLivestockcar();
	public static ItemICRailcar item_ic_railcar = new ItemICRailcar();
	public static ItemSNexasWagon item_s_nexas_wagon = new ItemSNexasWagon();
	public static ItemSNexasRailcar item_s_nexas_railcar = new ItemSNexasRailcar();
	public static ItemICWagon item_ic_wagon = new ItemICWagon();
	public static ItemTankWagonGray item_tank_wagon_gray = new ItemTankWagonGray();
	public static ItemTankWagonBlue item_tank_wagon_blue = new ItemTankWagonBlue();
	public static ItemTankWagonGreen item_tank_wagon_green = new ItemTankWagonGreen();
	public static ItemTankWagonYellow item_tank_wagon_yellow = new ItemTankWagonYellow();

	
	public static ItemTrainRotator item_train_rotator = new ItemTrainRotator();
	public static ItemTrainStarter item_train_starter = new ItemTrainStarter();
	public static ItemTrainRemover item_train_remover = new ItemTrainRemover();
	public static ItemTrainConnector item_train_connector = new ItemTrainConnector();
	
}
