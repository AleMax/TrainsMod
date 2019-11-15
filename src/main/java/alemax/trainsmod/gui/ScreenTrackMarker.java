package alemax.trainsmod.gui;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.ClientCottonScreen;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class ScreenTrackMarker extends ClientCottonScreen {

    public ScreenTrackMarker(BlockPos pos) {
        super(new GUITrackMarker(pos));
    }


}
