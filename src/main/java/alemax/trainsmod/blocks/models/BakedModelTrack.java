package alemax.trainsmod.blocks.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import javax.vecmath.Vector2d;
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

	public static final ModelResourceLocation BAKED_MODEL = new ModelResourceLocation(Reference.MODID + ":track");

    private TextureAtlasSprite sprite;
    private VertexFormat format;

    public BakedModelTrack(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
    	this.format = format;
    	sprite = bakedTextureGetter.apply(new ResourceLocation(Reference.MODID, "blocks/am_rail"));
	}
    
	@Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		
		//System.out.println("getQuads");
		
	   	List<BakedQuad> quads = new ArrayList<>();
		
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
        Vector3d[] trackPoints = extendedBlockState.getValue(BlockTrack.TRACK_POINTS);
        //Vector3d[] leftPoints = getLeftRightPoints(trackPoints, -1.25);
        //Vector3d[] rightPoints = getLeftRightPoints(trackPoints, 1.25);
        
        //System.out.println(trackPoints.length);
        
        
        
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

	private Vector3d[] getLeftRightPoints(Vector3d[] points, double direction) {
		Vector3d[] newPoints = new Vector3d[points.length];
		if(points.length > 0) {
			Vector2d[] currentPoints = {new Vector2d(2 * points[0].x - points[1].x, 2 * points[0].z - points[1].z), new Vector2d(points[0].x, points[0].z) , new Vector2d(points[1].x, points[1].z)};
			Vector2d point = getLeftRightPoint(currentPoints, direction);
			newPoints[0] = new Vector3d(point.x, points[0].y, point.y);
		}
		for(int i = 1; i < points.length - 1; i++) {
			Vector2d[] currentPoints = {new Vector2d(points[i - 1].x, points[i - 1].z), new Vector2d(points[i].x, points[i].z), new Vector2d(points[i + 1].x, points[i + 1].z)};
			Vector2d point = getLeftRightPoint(currentPoints, direction);
			newPoints[i] = new Vector3d(point.x, points[i].y, point.y);
		}
		if(points.length > 1) {
			Vector2d[] currentPoints = {new Vector2d(points[points.length - 2].x, points[points.length - 2].z), new Vector2d(points[points.length - 1].x, points[points.length - 1].z), new Vector2d(2 * points[points.length - 1].x - points[points.length - 2].x, 2 * points[points.length - 1].z - points[points.length - 2].z)};
			Vector2d point = getLeftRightPoint(currentPoints, direction);
			newPoints[points.length - 1] = new Vector3d(point.x, points[points.length - 1].y, point.y);
		}
		return newPoints;
	}
	
	private Vector2d getLeftRightPoint(Vector2d[] point, double direction) {
		if(point.length == 3) {
			Vector2d ab = new Vector2d(point[0].x - point[1].x, point[0].y - point[1].y);
			Vector2d bc = new Vector2d(point[2].x - point[1].x, point[2].y - point[1].y);
			double angleDifference = getCounterClockwiseAngle(ab, bc);
			double angle = getAngleBetweenPoints(point[1], point[0]);
			double newAngle = angle - (angleDifference / 2.0);
			return new Vector2d(point[1].x - Math.sin(Math.toRadians(newAngle)) * direction, point[1].y + Math.cos(Math.toRadians(newAngle)) * direction);
		}
		return null;
	}
	
	private double getCounterClockwiseAngle(Vector2d firstVector, Vector2d secondVector) {
		double dot = firstVector.x * secondVector.x + firstVector.y * secondVector.y;     
		double det = firstVector.x * secondVector.y - firstVector.y * secondVector.x;
		double angle = Math.toDegrees(Math.atan2(det, dot));
		if(angle < 0) angle += 360;
		return angle;
	}
	
	private double getAngleBetweenPoints(Vector2d firstPoint, Vector2d secondPoint) {
		Vector2d direction = new Vector2d(secondPoint.x - firstPoint.x, secondPoint.y - firstPoint.y);
		double angle = Math.toDegrees(direction.angle(new Vector2d(0,1)));
		double angleTo90 = (float) Math.toDegrees(direction.angle(new Vector2d(-1, 0)));
		
		if(angleTo90 > 90) angle = 360 - angle;
		return angle;
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
		return null;
	}

	@Override
	public ItemOverrideList getOverrides() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
