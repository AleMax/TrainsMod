package alemax.trainsmod.block.blockentity.render;

import alemax.trainsmod.block.blockentity.BlockEntityTrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

public class BERTrackMarker extends BlockEntityRenderer<BlockEntityTrackMarker> {

    private static ItemStack stack = new ItemStack(Items.STICK, 1);

    private static int displayList;

    @Override
    public void render(BlockEntityTrackMarker blockEntity, double x, double y, double z, float partialTicks, int destroyStage) {

        TrackMarker marker = TrackMarkerInstances.OVERWORLD.getTrackMarker(blockEntity.getPos());

        /*
        if(displayList == 0) {
            displayList = GL11.glGenLists(1);
            GL11.glNewList(displayList, GL11.GL_COMPILE);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glColor3f(1, 0, 0);
            GL11.glVertex3f(0.03f, 0, 0.3f);
            GL11.glVertex3f(0.03f, 0, -0.3f);
            GL11.glVertex3f(-0.03f, 0, -0.3f);
            GL11.glVertex3f(-0.03f, 0, 0.3f);
            GL11.glEnd();
            GL11.glEndList();
        }
        */

        float[] vertices = {0.03f, 0, 0.3f, 0.03f, 0, -0.3f, -0.03f, 0, -0.3f,
                //-0.03f, 0, 0.3f
        };

        if(marker != null) {


            GlStateManager.pushMatrix();
            GlStateManager.translated(x + 0.5, y + 1.001, z + 0.5);

            GlStateManager.rotatef(marker.angle, 0,1,0);

            if(false) {
                displayList = GL11.glGenLists(1);

                GL11.glNewList(displayList, GL11.GL_COMPILE);
                {
                    this.initRender();
                }
                GL11.glEndList();
            }

            GlStateManager.callList(displayList);
            //GlStateManager.newList();
            System.out.println(x + "\t" + y + "\t" + z + "\t" + displayList);

            GlStateManager.popMatrix();




            /*
            int vbo = GL30.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

            System.out.println(x + "\t" + y + "\t" + z);

            GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
            GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

            GlStateManager.pushMatrix();
            GlStateManager.translated(x + 0.5, y + 1.01, z + 0.5);
            GlStateManager.rotatef(marker.angle, 0, 1, 0);

            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);

            GlStateManager.popMatrix();

            /*
            GlStateManager.pushMatrix();
            GlStateManager.translated(x + 0.5, y + 1.01, z + 0.5);
            GlStateManager.rotatef(marker.angle, 0, 1, 0);

            GL11.glCallList(displayList);

            GlStateManager.popMatrix();
             */

/*
            GlStateManager.pushMatrix();
            GlStateManager.translated(x + 0.5, y + 2.5, z + 0.5);
            GlStateManager.rotatef((float) Math.toRadians(marker.angle), 0, 1, 0);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder builder = tessellator.getBufferBuilder();

            //builder.setOffset(blockEntity.getPos().getX(),blockEntity.getPos().getY(),blockEntity.getPos().getZ());



            builder.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR_UV_LMAP);

            builder.vertex(-0.1f, 1, 0.5f).color(1,1,1,1).texture(0,0).texture(0,0).next();
            builder.vertex(-0.1f, 1, -0.5f).color(1,1,1,1).texture(0,0).texture(0,0).next();
            builder.vertex(0.1f, 1, -0.5f).color(1,1,1,1).texture(0,0).texture(0,0).next();
            builder.vertex(0.1f, 1, 0.5f).color(1,1,1,1).texture(0,0).texture(0,0).next();


            builder.vertex(10f, 10, 5f).color(1,1,1,1).texture(0,0).texture(0,0).next();
            builder.vertex(10f, 10, -5f).color(1,1,1,1).texture(0,0).texture(0,0).next();
            builder.vertex(-10f, 10, -5).color(1,1,1,1).texture(0,0).texture(0,0).next();
            builder.vertex(-10f, 10, 5f).color(1,1,1,1).texture(0,0).texture(0,0).next();



            builder.setOffset(0,0,0);
            tessellator.draw();

            //MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Type.GROUND);

            GlStateManager.popMatrix();


 */
        }
        //super.render(blockEntity, x, y, z, partialTicks, destroyStage);

    }

    private void initRender() {
        float[] vertices = {0.03f, 0, 0.3f, 0.03f, 0, -0.3f, -0.03f, 0, -0.3f,
                //-0.03f, 0, 0.3f
        };

        GL11.glBegin(GL11.GL_TRIANGLES);

        GL11.glNormal3f(0, 1, 0);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex3f(vertices[0], vertices[1], vertices[2]);

        GL11.glNormal3f(0, 1, 0);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex3f(vertices[3], vertices[4], vertices[5]);

        GL11.glNormal3f(0, 1, 0);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex3f(vertices[6], vertices[7], vertices[8]);

        GL11.glEnd();
    }




}
