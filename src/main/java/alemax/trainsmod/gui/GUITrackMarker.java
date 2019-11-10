package alemax.trainsmod.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.LiteralText;

public class GUITrackMarker extends LightweightGuiDescription {

    public GUITrackMarker() {

        WPlainPanel root = new WPlainPanel();
        root.setSize(280,160);
        setRootPanel(root);

        WLabel labelChannel = new WLabel(new LiteralText("Channel:"), WLabel.DEFAULT_TEXT_COLOR);
        root.add(labelChannel, 5,5,2,1);

        WLabel labelAngle = new WLabel(new LiteralText("Angle:"), WLabel.DEFAULT_TEXT_COLOR);
        root.add(labelAngle, 5,30,2,1);

        WLabel labelHeight = new WLabel(new LiteralText("Height:"), WLabel.DEFAULT_TEXT_COLOR);
        root.add(labelHeight, 5,55,2,1);

        WLabel labelTrackType = new WLabel(new LiteralText("TrackType:"), WLabel.DEFAULT_TEXT_COLOR);
        root.add(labelTrackType, 5,80,2,1);

        WTextField textFieldChannel = new WTextField();
        root.add(textFieldChannel, 75, 0, 200,20);
        textFieldChannel.setEditable(true);

        WLabeledSlider sliderAngle = new WLabeledSlider(0,180);
        root.add(sliderAngle, 75, 25, 200,20);

        WLabeledSlider sliderHeight = new WLabeledSlider(0,16);
        root.add(sliderHeight, 75, 50, 200,20);

        WButton buttonTrackType = new WButton(new LiteralText("Concrete Rails"));
        root.add(buttonTrackType, 75, 75, 100, 20);

        WButton buttonSnapRail = new WButton(new LiteralText("Snap to Rail"));
        root.add(buttonSnapRail, 5, 100, 100, 20);

        WButton buttonSnapMarker = new WButton(new LiteralText("Snap to Marker"));
        root.add(buttonSnapMarker, 125, 100, 100, 20);

        WButton buttonPreview = new WButton(new LiteralText("Preview"));
        root.add(buttonPreview, 5, 135, 100, 20);

        WButton buttonBuild = new WButton(new LiteralText("Build"));
        root.add(buttonBuild, 175, 135, 100, 20);


        root.validate(this);

    }

    public void addPainters() {
        if (this.rootPanel != null) {
            this.rootPanel.setBackgroundPainter(BackgroundPainter.VANILLA);
        }

    }

}
