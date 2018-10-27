package alemax.trainsmod.guis;

import java.io.IOException;

import javax.annotation.Nullable;

import org.lwjgl.util.Color;

import com.google.common.base.Predicate;

import alemax.trainsmod.blocks.tileentities.TileEntityTrackMarker;
import alemax.trainsmod.util.TrackType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
	GuiButton buttonRailSnap;
	GuiButton buttonPreview;
	GuiButton buttonBuild;
	
	TileEntityTrackMarker tileEntity;
	TrackType trackType;
	String trackTypeText;
	

	public GUITrackMarker(World world, int posX, int posY, int posZ) {
		TileEntity te = world.getTileEntity(new BlockPos(posX, posY, posZ));
		if(te instanceof TileEntityTrackMarker) {
			tileEntity = (TileEntityTrackMarker) te;
			this.trackType = tileEntity.getTrackType();
			this.trackTypeText = getTrackTypeString();
		} else {
			closeGui();
		}
		this.trackType = TrackType.CONCRETE;
		this.trackTypeText = getTrackTypeString();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
        int centerX = (width / 2);
        int centerY = (height / 2);

        textFieldChannel.drawTextBox();
        
		this.fontRenderer.drawString("Channel:", centerX - 135, centerY - 90, 0xFFFFFF);
		this.fontRenderer.drawString("Angle:", centerX - 135, centerY - 65, 0xFFFFFF);
		this.fontRenderer.drawString("Height:", centerX - 135, centerY - 40, 0xFFFFFF);
		this.fontRenderer.drawString("Track Type:", centerX - 135, centerY - 15, 0xFFFFFF);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void initGui() {
		int componentID = 0;
		buttonList.clear();
		
		textFieldChannel = new GuiTextField(componentID++, this.fontRenderer, width / 2 - 65, height / 2 - 95, 200, 20);
		textFieldChannel.setText(tileEntity.getChannel());
		
		int startAngle = tileEntity.getAngle();
		if(startAngle < 0) startAngle = 0;
		else if(startAngle > 179) startAngle = 179;
		
		sliderAngle = new GuiSlider(componentID++, width / 2 - 65, height / 2 - 70, 200, 20, "", "°", 0, 179, startAngle, false, true);
		buttonList.add(sliderAngle);
		
		int startHeight = tileEntity.getHeight();
		if(startHeight < 3) startHeight = 3;
		else if(startHeight > 18) startHeight = 18;
		
		sliderHeight = new GuiSlider(componentID++, width / 2 - 65, height / 2 - 45, 200, 20, "", "px", 3, 18, startHeight, false, true);
		buttonList.add(sliderHeight);
		
		buttonTrackType = new GuiButton(componentID++, width / 2 - 65, height / 2 - 20, 100, 20, trackTypeText);
		buttonList.add(buttonTrackType);
		
		buttonRailSnap = new GuiButton(componentID++, width / 2 - 135, height / 2 + 05, 100, 20, "Snap to Rail");
		buttonList.add(buttonRailSnap);
		
		buttonPreview = new GuiButton(componentID++, width / 2 - 135, height / 2 + 40, 100, 20, "Preview");
		buttonList.add(buttonPreview);
		
		buttonBuild = new GuiButton(componentID++, width / 2 + 35, height / 2 + 40, 100, 20, "Build");
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
		
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		textFieldChannel.textboxKeyTyped(typedChar, keyCode);
		
		KeyBinding keyUse = Minecraft.getMinecraft().gameSettings.keyBindInventory;
		keyUse.getKeyCode();
		
		if(keyCode == 1 || keyCode == keyUse.getKeyCode()) {
			updateTileEntity();
			closeGui();
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        textFieldChannel.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
	
	private void updateTileEntity() {
		
	}
	
	private void closeGui() {
		this.mc.displayGuiScreen(null);
		if (this.mc.currentScreen == null) {
			this.mc.setIngameFocus();
		}
	}
	
	
	
}