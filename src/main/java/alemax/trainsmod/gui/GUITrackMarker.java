package alemax.trainsmod.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.text.LiteralText;

public class GUITrackMarker extends LightweightGuiDescription {

    public GUITrackMarker() {

        WPlainPanel root = new WPlainPanel();
        root.setSize(200,200);
        setRootPanel(root);

        WLabel labelChannel = new WLabel(new LiteralText("Channel:"), WLabel.DEFAULT_TEXT_COLOR);
        root.add(labelChannel, 15,10,2,1);

        WLabel labelAngle = new WLabel(new LiteralText("Angle:"), WLabel.DEFAULT_TEXT_COLOR);
        root.add(labelAngle, 15,35,2,1);

        WLabel labelHeight = new WLabel(new LiteralText("Height:"), WLabel.DEFAULT_TEXT_COLOR);
        root.add(labelHeight, 15,60,2,1);

        WLabel labelTrackType = new WLabel(new LiteralText("TrackType:"), WLabel.DEFAULT_TEXT_COLOR);
        root.add(labelTrackType, 15,85,2,1);

        WTextField textFieldChannel = new WTextField();
        root.add(textFieldChannel, 85, 5, 200,20);
        textFieldChannel.setEditable(true);

        WSlider sliderAngle = new WSlider(0,180, Axis.HORIZONTAL);
        root.add(sliderAngle, 85, 30, 200,20);

        WSlider sliderHeight = new WSlider(0,16, Axis.HORIZONTAL);
        root.add(sliderHeight, 85, 55, 200,20);

        WButton buttonTrackType = new WButton(new LiteralText("Concrete Rails"));
        root.add(buttonTrackType, 85, 80, 100, 20);

        WButton buttonSnapRail = new WButton(new LiteralText("Snap to Rail"));
        root.add(buttonSnapRail, 15, 105, 100, 20);

        WButton buttonSnapMarker = new WButton(new LiteralText("Snap to Marker"));
        root.add(buttonSnapMarker, 135, 105, 100, 20);

        WButton buttonPreview = new WButton(new LiteralText("Preview"));
        root.add(buttonPreview, 15, 140, 100, 20);

        WButton buttonBuild = new WButton(new LiteralText("Build"));
        root.add(buttonBuild, 185, 140, 100, 20);
    }

    public void addPainters() {
        if (this.rootPanel != null) {
            this.rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(0));
        }

    }

}
