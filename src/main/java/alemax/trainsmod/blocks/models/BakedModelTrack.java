package alemax.trainsmod.blocks.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import javax.vecmath.Vector3d;

import alemax.trainsmod.blocks.BlockTrack;
import alemax.trainsmod.util.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BakedModelTrack implements IBakedModel {

	public static final ModelResourceLocation BAKED_MODEL = new ModelResourceLocation(Reference.MODID, "track");

    private TextureAtlasSprite sprite;
    private VertexFormat format;

    public BakedModelTrack(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
    	this.format = format;
    	sprite = bakedTextureGetter.apply(new ResourceLocation(Reference.MODID, "blocks/am_rail"));
	}
    
	@Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		
	   	List<BakedQuad> quads = new ArrayList<>();
		
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
        Vector3d[] trackPoints = extendedBlockState.getValue(BlockTrack.TRACK_POINTS);
        
        quads.add(createQuad(new Vec3d(0, 1, 1), new Vec3d(1, 1, 1), new Vec3d(1, 1, 0), new Vec3d(0, 1, 0), sprite));
        
       

        return quads;
    }
	
	private void putVertex(UnpackedBakedQuad.Builder builder, Vec3d normal, double x, double y, double z, float u, float v) {
		for (int e = 0; e < format.getElementCount(); e++) {
			switch (format.getElement(e).getUsage()) {
				case POSITION:
					builder.put(e, (float)x, (float)y, (float)z, 1.0f);
					break;
				case COLOR:
					builder.put(e, 1.0f, 1.0f, 1.0f, 1.0f);
					break;
				case UV:
					if (format.getElement(e).getIndex() == 0) {
						u = sprite.getInterpolatedU(u);
						v = sprite.getInterpolatedV(v);
						builder.put(e, u, v, 0f, 1f);
						break;
					}
				case NORMAL:
					builder.put(e, (float) normal.x, (float) normal.y, (float) normal.z, 0f);
					break;
				default:
					builder.put(e);
					break;
			}
		}
	}
	
	private BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setTexture(sprite);
        putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 0, 16);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 16, 16);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 16, 0);
        return builder.build();
    }

	@Override
	public boolean isAmbientOcclusion() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isGui3d() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return sprite;
	}

	@Override
	public ItemOverrideList getOverrides() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
