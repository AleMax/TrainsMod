package alemax.trainsmod.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.ClientCottonScreen;
import net.minecraft.text.Text;

public class ScreenTrackMarker extends ClientCottonScreen {
    public ScreenTrackMarker() {
        super(new GUITrackMarker());
    }
}
