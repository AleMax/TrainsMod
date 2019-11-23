package alemax.trainsmod.gui;

import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import alemax.trainsmod.init.TMPackets;
import alemax.trainsmod.networking.TMPacket;
import alemax.trainsmod.util.TrackType;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class GUITrackMarker extends LightweightGuiDescription {

    private BlockPos pos;
    private TrackMarker marker;

    private WPlainPanel root;

    private WLabel labelChannel;
    private WLabel labelAngle;
    private WLabel labelHeight;
    private WLabel labelTrackType;

    private WTextField textFieldChannel;
    private WLabeledSlider sliderAngle;
    private WLabeledSlider sliderHeight;
    private WButton buttonTrackType;

    private WButton buttonSnapRail;
    private WButton buttonSnapMarker;

    private WButton buttonPreview;
    private WButton buttonBuild;

    private float angle;
    private TrackType type;

    public GUITrackMarker(BlockPos pos) {
        this.pos = pos;
        this.marker = TrackMarkerInstances.OVERWORLD.getTrackMarker(pos);

        String currentChannel = marker.channel;
        this.angle = marker.angle;
        byte height = marker.height;
        this.type = marker.trackType;

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
        textFieldChannel.setMaxLength(TrackMarker.MAX_CHANNEL_LENGTH);
        textFieldChannel.setText(currentChannel);
        root.add(textFieldChannel, 75, 0, 200,20);

        //TODO: Add markings on angle and height slider

        sliderAngle = new WLabeledSlider(0,179);
        sliderAngle.setValue(Math.round(angle));
        sliderAngle.setValueChangeListener(new IntConsumer() {
            @Override
            public void accept(int i) {
                angle = sliderAngle.getValue();
            }
        });
        root.add(sliderAngle, 75, 25, 200,20);

        sliderHeight = new WLabeledSlider(3,18);
        sliderHeight.setValue(height);
        root.add(sliderHeight, 75, 50, 200,20);

        setButtonTrackType(type);

        root.add(buttonTrackType, 75, 75, 100, 20);

        buttonSnapRail = new WButton(new LiteralText("Snap to Rail"));
        root.add(buttonSnapRail, 5, 100, 100, 20);

        buttonSnapMarker = new WButton(new LiteralText("Snap to Marker"));
        root.add(buttonSnapMarker, 125, 100, 100, 20);

        WButton buttonPreview = new WButton(new LiteralText("Preview"));
        root.add(buttonPreview, 5, 135, 100, 20);

        WButton buttonBuild = new WButton(new LiteralText("Build"));
        buttonBuild.setOnClick(new Runnable() {
            @Override
            public void run() {
                saveChanges();
                TMPackets.packetC2STrackBuild.send(marker.channel);
            }
        });
        root.add(buttonBuild, 175, 135, 100, 20);


        root.validate(this);

    }

    public void saveChanges() {
        TMPackets.packetC2SSaveGUITrackMarker.send(pos, textFieldChannel.getText(), angle, (byte) sliderHeight.getValue(), type);
    }


    private void setButtonTrackType(TrackType type) {
        root.remove(buttonTrackType);
        switch(type) {
            case CONCRETE:
                buttonTrackType = new WButton(new LiteralText("Concrete Rails"));
                this.type = TrackType.CONCRETE;
                break;
            case WOOD:
                buttonTrackType = new WButton(new LiteralText("Wooden Rails"));
                this.type = TrackType.WOOD;
                break;
            case LEVEL_CROSSING:
                buttonTrackType = new WButton(new LiteralText("Level Crossing"));
                this.type = TrackType.LEVEL_CROSSING;
                break;
            case TRAM:
                buttonTrackType = new WButton(new LiteralText("Tram Tracks"));
                this.type = TrackType.TRAM;
                break;
        }
        buttonTrackType.setOnClick(new Runnable() {
            @Override
            public void run() {
                increaseTrackType();
            }
        });
        root.add(buttonTrackType, 75, 75, 100, 20);

    }

    private void increaseTrackType() {
        switch(this.type) {
            case CONCRETE:
                setButtonTrackType(TrackType.WOOD);
                break;
            case WOOD:
                setButtonTrackType(TrackType.LEVEL_CROSSING);
                break;
            case LEVEL_CROSSING:
                setButtonTrackType(TrackType.TRAM);
                break;
            case TRAM:
                setButtonTrackType(TrackType.CONCRETE);
                break;
        }
    }

    public void addPainters() {
        if (this.rootPanel != null) {
            this.rootPanel.setBackgroundPainter(BackgroundPainter.VANILLA);
        }

    }

}
