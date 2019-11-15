package alemax.trainsmod.gui;

import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;

public class GUITrackMarker extends LightweightGuiDescription {

    private BlockPos pos;
    private TrackMarker marker;


    WPlainPanel root;

    WLabel labelChannel;
    WLabel labelAngle;
    WLabel labelHeight;
    WLabel labelTrackType;

    WTextField textFieldChannel;
    WLabeledSlider sliderAngle;
    WLabeledSlider sliderHeight;
    WButton buttonTrackType;

    WButton buttonSnapRail;
    WButton buttonSnapMarker;

    WButton buttonPreview;
    WButton buttonBuild;

    public GUITrackMarker(BlockPos pos) {
        this.pos = pos;
        this.marker = TrackMarkerInstances.OVERWORLD.getTrackMarker(pos);

        root = new WPlainPanel();
        root.setSize(280,160);
        setRootPanel(root);

        labelChannel = new WLabel(new LiteralText("Channel:"), WLabel.DEFAULT_TEXT_COLOR);
        root.add(labelChannel, 5,5,2,1);

        labelAngle = new WLabel(new LiteralText("Angle:"), WLabel.DEFAULT_TEXT_COLOR);
        root.add(labelAngle, 5,30,2,1);

        labelHeight = new WLabel(new LiteralText("Height:"), WLabel.DEFAULT_TEXT_COLOR);
        root.add(labelHeight, 5,55,2,1);

        labelTrackType = new WLabel(new LiteralText("TrackType:"), WLabel.DEFAULT_TEXT_COLOR);
        root.add(labelTrackType, 5,80,2,1);

        textFieldChannel = new WTextField();
        root.add(textFieldChannel, 75, 0, 200,20);
        textFieldChannel.setEditable(true);

        sliderAngle = new WLabeledSlider(0,180);
        root.add(sliderAngle, 75, 25, 200,20);

        sliderHeight = new WLabeledSlider(0,16);
        root.add(sliderHeight, 75, 50, 200,20);

        buttonTrackType = new WButton(new LiteralText("Concrete Rails"));
        root.add(buttonTrackType, 75, 75, 100, 20);

        buttonSnapRail = new WButton(new LiteralText("Snap to Rail"));
        root.add(buttonSnapRail, 5, 100, 100, 20);

        buttonSnapMarker = new WButton(new LiteralText("Snap to Marker"));
        buttonSnapMarker.setOnClick(new Runnable() {
            @Override
            public void run() { }
        });
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
