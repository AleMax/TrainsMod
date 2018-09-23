package alemax.trainsmod.entities.models;

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_SHININESS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import alemax.trainsmod.util.objloader.OBJHandler;
import alemax.trainsmod.util.objloader.OBJLoader;
import alemax.trainsmod.util.objloader.Obj;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelRailcar {
	
	private Obj model;
	private boolean compiled;
	private int displayList;
	
	public ModelRailcar(String file) {
		this.model = OBJHandler.INSTANCE.loadModel(getClass().getResourceAsStream("/assets/trainsmod/models/entity/" + file));
		this.model.moveDown(6);
		this.model.scale(0.0625F);
	}
	
	@SideOnly(Side.CLIENT)
	public void render() {
		if (!this.compiled) {
			this.compileDisplayList();
		}
	    GlStateManager.callList(this.displayList);      
	}


	private void compileDisplayList() {
		this.displayList = OBJHandler.INSTANCE.createDisplayList(model);
		compiled = true;
	}
	
}
