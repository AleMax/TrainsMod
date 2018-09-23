package alemax.trainsmod.blocks.models;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.vecmath.Vector2d;

import alemax.trainsmod.blocks.BlockAMRailCurved;
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
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BakedModelAMRailCurved implements IBakedModel {
	
	public static final ModelResourceLocation BAKED_MODEL = new ModelResourceLocation(Reference.MODID + ":am_rail_curved");
	
	private TextureAtlasSprite sprite;
    private VertexFormat format;
	
    public BakedModelAMRailCurved(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
    	this.format = format;
    	sprite = bakedTextureGetter.apply(new ResourceLocation(Reference.MODID, "blocks/am_rail"));
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
	
	private BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite, Vec2f uv1, Vec2f uv2, Vec2f uv3, Vec2f uv4, boolean invert) {
		Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();
		//Vec3d normal = v2.subtract(v3).crossProduct(v4.subtract(v3)).normalize();
		//Vec3d normal = new Vec3d(-n.x, -n.y, -n.z);
		if(invert) {
			 Vec3d n = new Vec3d(-normal.x, -normal.y, -normal.z);
			 normal = n;
		} 
		
		
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
		builder.setTexture(sprite);
		if(!invert) {
			putVertex(builder, normal, v1.x, v1.y, v1.z, uv1.x, uv1.y);
			putVertex(builder, normal, v2.x, v2.y, v2.z, uv2.x, uv2.y);
			putVertex(builder, normal, v3.x, v3.y, v3.z, uv3.x, uv3.y);
			putVertex(builder, normal, v4.x, v4.y, v4.z, uv4.x, uv4.y);
		} else {
			putVertex(builder, normal, v4.x, v4.y, v4.z, uv4.x, uv4.y);
			putVertex(builder, normal, v3.x, v3.y, v3.z, uv3.x, uv3.y);
			putVertex(builder, normal, v2.x, v2.y, v2.z, uv2.x, uv2.y);
			putVertex(builder, normal, v1.x, v1.y, v1.z, uv1.x, uv1.y);
		}
		return builder.build();
	}
	 
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		try {
			if(side != null) {
				//return Collections.emptyList();
			}
			List<BakedQuad> quads = new ArrayList<>();
			
		    IExtendedBlockState ex = (IExtendedBlockState) state;
		    
			Vector2d[][] points = ex.getValue(BlockAMRailCurved.POINTS);
			if(points.length > 0) {
				byte length = ex.getValue(BlockAMRailCurved.LENGTH);
				
				//comingFrom = 0;
				//goingTo = 1;
				//radius = 10.5;
	
				Vec2f uvI1 = new Vec2f(13f, 15f);
				Vec2f uvI2 = new Vec2f(13f, 15.25f);
				Vec2f uvI3 = new Vec2f(13.25f, 15.25f);
				Vec2f uvI4 = new Vec2f(13.25f, 15f);
				
				Vec2f uvW1 = new Vec2f(9f, 10.75f);
				Vec2f uvW2 = new Vec2f(9f, 11.75f);
				Vec2f uvW3 = new Vec2f(0f, 11.75f);
				Vec2f uvW4 = new Vec2f(0f, 10.75f);
				
				Vec2f uvWS1 = new Vec2f(9f, 11f);
				Vec2f uvWS2 = new Vec2f(0f, 11f);
				Vec2f uvWS3 = new Vec2f(9f, 11.5f);
				Vec2f uvWS4 = new Vec2f(0f, 11.5f);
				Vec2f uvWS5 = new Vec2f(0.25f, 11.75f);
				Vec2f uvWS6 = new Vec2f(0.25f, 10.75f);
				
				for(int i = 0; i < length; i++) {
					double y0 = 0.03125;
					double y1 = 0.09375;
					double y2 = 0.125;
					
					quads.add(createQuad(new Vec3d(points[i+1][0].x, y0, points[i+1][0].y), new Vec3d(points[i+1][0].x, y2, points[i+1][0].y), new Vec3d(points[i][0].x, y2, points[i][0].y), new Vec3d(points[i][0].x, y0, points[i][0].y), sprite, uvI1, uvI2, uvI3, uvI4, false));
					quads.add(createQuad(new Vec3d(points[i+1][0].x, y2, points[i+1][0].y), new Vec3d(points[i+1][1].x, y2, points[i+1][1].y), new Vec3d(points[i][1].x, y2, points[i][1].y), new Vec3d(points[i][0].x, y2, points[i][0].y), sprite, uvI1, uvI2, uvI3, uvI4, false));
					quads.add(createQuad(new Vec3d(points[i+1][1].x, y2, points[i+1][1].y), new Vec3d(points[i+1][1].x, y1, points[i+1][1].y), new Vec3d(points[i][1].x, y1, points[i][1].y), new Vec3d(points[i][1].x, y2, points[i][1].y), sprite, uvI1, uvI2, uvI3, uvI4, false));
					quads.add(createQuad(new Vec3d(points[i+1][1].x, y1, points[i+1][1].y), new Vec3d(points[i+1][2].x, y1, points[i+1][2].y), new Vec3d(points[i][2].x, y1, points[i][2].y), new Vec3d(points[i][1].x, y1, points[i][1].y), sprite, uvI1, uvI2, uvI3, uvI4, false));
					quads.add(createQuad(new Vec3d(points[i+1][2].x, y1, points[i+1][2].y), new Vec3d(points[i+1][2].x, y0, points[i+1][2].y), new Vec3d(points[i][2].x, y0, points[i][2].y), new Vec3d(points[i][2].x, y1, points[i][2].y), sprite, uvI1, uvI2, uvI3, uvI4, false));
					quads.add(createQuad(new Vec3d(points[i+1][2].x, y0, points[i+1][2].y), new Vec3d(points[i+1][0].x, y0, points[i+1][0].y), new Vec3d(points[i][0].x, y0, points[i][0].y), new Vec3d(points[i][2].x, y0, points[i][2].y), sprite, uvI1, uvI2, uvI3, uvI4, false));
					
					quads.add(createQuad(new Vec3d(points[i][5].x, y0, points[i][5].y), new Vec3d(points[i][5].x, y2, points[i][5].y), new Vec3d(points[i+1][5].x, y2, points[i+1][5].y), new Vec3d(points[i+1][5].x, y0, points[i+1][5].y), sprite, uvI1, uvI2, uvI3, uvI4, false));
					quads.add(createQuad(new Vec3d(points[i][5].x, y2, points[i][5].y), new Vec3d(points[i][4].x, y2, points[i][4].y), new Vec3d(points[i+1][4].x, y2, points[i+1][4].y), new Vec3d(points[i+1][5].x, y2, points[i+1][5].y), sprite, uvI1, uvI2, uvI3, uvI4, false));
					quads.add(createQuad(new Vec3d(points[i][4].x, y2, points[i][4].y), new Vec3d(points[i][4].x, y1, points[i][4].y), new Vec3d(points[i+1][4].x, y1, points[i+1][4].y), new Vec3d(points[i+1][4].x, y2, points[i+1][4].y), sprite, uvI1, uvI2, uvI3, uvI4, false));
					quads.add(createQuad(new Vec3d(points[i][4].x, y1, points[i][4].y), new Vec3d(points[i][3].x, y1, points[i][3].y), new Vec3d(points[i+1][3].x, y1, points[i+1][3].y), new Vec3d(points[i+1][4].x, y1, points[i+1][4].y), sprite, uvI1, uvI2, uvI3, uvI4, false));
					quads.add(createQuad(new Vec3d(points[i][3].x, y1, points[i][3].y), new Vec3d(points[i][3].x, y0, points[i][3].y), new Vec3d(points[i+1][3].x, y0, points[i+1][3].y), new Vec3d(points[i+1][3].x, y1, points[i+1][3].y), sprite, uvI1, uvI2, uvI3, uvI4, false));
					quads.add(createQuad(new Vec3d(points[i][3].x, y0, points[i][3].y), new Vec3d(points[i][5].x, y0, points[i][5].y), new Vec3d(points[i+1][5].x, y0, points[i+1][5].y), new Vec3d(points[i+1][3].x, y0, points[i+1][3].y), sprite, uvI1, uvI2, uvI3, uvI4, false));
					
					Vector2d middle = new Vector2d((points[i][6].x + points[i+1][6].x) * 0.5, (points[i][6].y + points[i+1][6].y) * 0.5);
					Vector2d direction = new Vector2d(points[i+1][6].x - points[i][6].x, points[i+1][6].y - points[i][6].y);
					direction.normalize();
					direction.x /= 16.0;
					direction.y /= 16.0;
					Vector2d normalDirection = new Vector2d(-direction.y, direction.x);
					
					Vector2d[] plank1 = new Vector2d[4];
					Vector2d[] plank2 = new Vector2d[4];
	
					plank1[0] = new Vector2d(middle.x + direction.x * 6 + normalDirection.x * 18, middle.y + direction.y * 6 + normalDirection.y * 18);
					plank1[1] = new Vector2d(plank1[0].x - direction.x * 4, plank1[0].y - direction.y * 4);
					plank1[2] = new Vector2d(plank1[1].x - normalDirection.x * 36, plank1[1].y - normalDirection.y * 36);
					plank1[3] = new Vector2d(plank1[2].x + direction.x * 4, plank1[2].y + direction.y * 4);
					
					plank2[0] = new Vector2d(middle.x - direction.x * 2 + normalDirection.x * 18, middle.y - direction.y * 2 + normalDirection.y * 18);
					plank2[1] = new Vector2d(plank2[0].x - direction.x * 4, plank2[0].y - direction.y * 4);
					plank2[2] = new Vector2d(plank2[1].x - normalDirection.x * 36, plank2[1].y - normalDirection.y * 36);
					plank2[3] = new Vector2d(plank2[2].x + direction.x * 4, plank2[2].y + direction.y * 4);
					
					y0 = -y0;
					y1 = -y0;
					
					quads.add(createQuad(new Vec3d(plank1[3].x, y1, plank1[3].y), new Vec3d(plank1[2].x, y1, plank1[2].y), new Vec3d(plank1[1].x, y1, plank1[1].y), new Vec3d(plank1[0].x, y1, plank1[0].y), sprite, uvW1, uvW2, uvW3, uvW4, false));
					quads.add(createQuad(new Vec3d(plank1[0].x, y0, plank1[0].y), new Vec3d(plank1[1].x, y0, plank1[1].y), new Vec3d(plank1[2].x, y0, plank1[2].y), new Vec3d(plank1[3].x, y0, plank1[3].y), sprite, uvW1, uvW2, uvW3, uvW4, false));
					quads.add(createQuad(new Vec3d(plank1[3].x, y1, plank1[3].y), new Vec3d(plank1[0].x, y1, plank1[0].y), new Vec3d(plank1[0].x, y0, plank1[0].y), new Vec3d(plank1[3].x, y0, plank1[3].y), sprite, uvW4, uvW1, uvWS1, uvWS2, false));
					quads.add(createQuad(new Vec3d(plank1[1].x, y1, plank1[1].y), new Vec3d(plank1[2].x, y1, plank1[2].y), new Vec3d(plank1[2].x, y0, plank1[2].y), new Vec3d(plank1[1].x, y0, plank1[1].y), sprite, uvW3, uvW2, uvWS3, uvWS4, false));
					quads.add(createQuad(new Vec3d(plank1[0].x, y1, plank1[0].y), new Vec3d(plank1[1].x, y1, plank1[1].y), new Vec3d(plank1[1].x, y0, plank1[1].y), new Vec3d(plank1[0].x, y0, plank1[0].y), sprite, uvW4, uvW3, uvWS5, uvWS6, false));
					quads.add(createQuad(new Vec3d(plank1[2].x, y1, plank1[2].y), new Vec3d(plank1[3].x, y1, plank1[3].y), new Vec3d(plank1[3].x, y0, plank1[3].y), new Vec3d(plank1[2].x, y0, plank1[2].y), sprite, uvWS5, uvWS6, uvW4, uvW3, false));
	
					
					
					quads.add(createQuad(new Vec3d(plank2[3].x, y1, plank2[3].y), new Vec3d(plank2[2].x, y1, plank2[2].y), new Vec3d(plank2[1].x, y1, plank2[1].y), new Vec3d(plank2[0].x, y1, plank2[0].y), sprite, uvW1, uvW2, uvW3, uvW4, false));
					quads.add(createQuad(new Vec3d(plank2[0].x, y0, plank2[0].y), new Vec3d(plank2[1].x, y0, plank2[1].y), new Vec3d(plank2[2].x, y0, plank2[2].y), new Vec3d(plank2[3].x, y0, plank2[3].y), sprite, uvW1, uvW2, uvW3, uvW4, false));
					quads.add(createQuad(new Vec3d(plank2[3].x, y1, plank2[3].y), new Vec3d(plank2[0].x, y1, plank2[0].y), new Vec3d(plank2[0].x, y0, plank2[0].y), new Vec3d(plank2[3].x, y0, plank2[3].y), sprite, uvW4, uvW1, uvWS1, uvWS2, false));
					quads.add(createQuad(new Vec3d(plank2[1].x, y1, plank2[1].y), new Vec3d(plank2[2].x, y1, plank2[2].y), new Vec3d(plank2[2].x, y0, plank2[2].y), new Vec3d(plank2[1].x, y0, plank2[1].y), sprite, uvW3, uvW2, uvWS3, uvWS4, false));
					quads.add(createQuad(new Vec3d(plank2[0].x, y1, plank2[0].y), new Vec3d(plank2[1].x, y1, plank2[1].y), new Vec3d(plank2[1].x, y0, plank2[1].y), new Vec3d(plank2[0].x, y0, plank2[0].y), sprite, uvW4, uvW3, uvWS5, uvWS6, false));
					quads.add(createQuad(new Vec3d(plank2[2].x, y1, plank2[2].y), new Vec3d(plank2[3].x, y1, plank2[3].y), new Vec3d(plank2[3].x, y0, plank2[3].y), new Vec3d(plank2[2].x, y0, plank2[2].y), sprite, uvWS5, uvWS6, uvW4, uvW3, false));
			}
			
				
			}
		
			return quads;
		} catch(NullPointerException e) {
			e.printStackTrace();
		}
		return new ArrayList<BakedQuad>();
	}

	@Override
	public boolean isAmbientOcclusion() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isGui3d() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemOverrideList getOverrides() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
