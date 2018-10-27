package alemax.trainsmod.guis;

import java.io.IOException;

import javax.annotation.Nullable;

import org.lwjgl.util.Color;

import com.google.common.base.Predicate;

import alemax.trainsmod.util.TrackType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.client.config.GuiSlider;
import scala.swing.event.Key;

public class GUITrackMarker extends GuiScreen {
	
	GuiTextField textFieldChannel;
	GuiSlider sliderAngle;
	GuiSlider sliderHeight;
	GuiButton buttonTrackType;
	GuiButton buttonPreview;
	GuiButton buttonBuild;
	
	TrackType trackType;
	String trackTypeText;

	public GUITrackMarker(World world, int posX, int posY, int posZ) {
		this.trackType = TrackType.CONCRETE;
		this.trackTypeText = getTrackTypeString();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
        int centerX = (width / 2);
        int centerY = (height / 2);

        textFieldChannel.drawTextBox();
        
		this.fontRenderer.drawString("Channel:", centerX - 100, centerY - 100, 0xFFFFFF);
		this.fontRenderer.drawString("Angle:", centerX - 100, centerY - 70, 0xFFFFFF);
		this.fontRenderer.drawString("Height:", centerX - 100, centerY - 40, 0xFFFFFF);
		this.fontRenderer.drawString("Track Type:", centerX - 100, centerY - 10, 0xFFFFFF);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void initGui() {
		int componentID = 0;
		buttonList.clear();
		
		textFieldChannel = new GuiTextField(componentID++, this.fontRenderer, width / 2 - 30, height / 2 - 100, 150, 20);
		
		sliderAngle = new GuiSlider(componentID++, width / 2 - 30, height / 2 - 70, 181, 20, "", "°", 0, 179, 0, false, true);
		buttonList.add(sliderAngle);
		
		sliderHeight = new GuiSlider(componentID++, width / 2 - 30, height / 2 - 40, 181, 20, "", "px", 3, 19, 6, false, true);
		buttonList.add(sliderHeight);
		
		buttonTrackType = new GuiButton(componentID++, width / 2 - 30, height / 2 - 10, 100, 20, trackTypeText);
		buttonList.add(buttonTrackType);
		
		buttonPreview = new GuiButton(componentID++, width / 2 - 100, height / 2 + 20, 80, 20, "Preview");
		buttonList.add(buttonPreview);
		
		buttonBuild = new GuiButton(componentID++, width / 2 + 40, height / 2 +20, 80, 20, "Build");
		buttonList.add(buttonBuild);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if(button == buttonTrackType) {
			switch(trackType) {
			case CONCRETE:
				this.trackType = TrackType.WOOD;
				this.trackTypeText = getTrackTypeString();
				this.buttonTrackType.displayString = trackTypeText;
				break;
			case WOOD:
				this.trackType = TrackType.LEVEL_CROSSING;
				this.trackTypeText = getTrackTypeString();
				this.buttonTrackType.displayString = trackTypeText;
				break;
			case LEVEL_CROSSING:
				this.trackType = TrackType.TRAM;
				this.trackTypeText = getTrackTypeString();
				this.buttonTrackType.displayString = trackTypeText;
				break;
			case TRAM:
				this.trackType = TrackType.CONCRETE;
				this.trackTypeText = getTrackTypeString();
				this.buttonTrackType.displayString = trackTypeText;
				break;
			}
		}
	}
	
	private String getTrackTypeString() {
		switch(trackType) {
		case CONCRETE: return "Concrete Rails";
		case WOOD: return "Wooden Rails";
		case LEVEL_CROSSING: return "Level Crossing";
		case TRAM: return "Tram Rails";
		}
		return "";
	}
	
	@Override
	public void updateScreen() {
		//sliderAngle.updateSlider();
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		textFieldChannel.textboxKeyTyped(typedChar, keyCode);

		if(keyCode == 1) {
			this.mc.displayGuiScreen(null);
			if (this.mc.currentScreen == null)
				this.mc.setIngameFocus();
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        textFieldChannel.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
	
	
	
	
}