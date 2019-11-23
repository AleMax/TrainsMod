package alemax.trainsmod.block;

import alemax.trainsmod.block.blockentity.BlockEntityTrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarker;
import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import alemax.trainsmod.gui.ScreenTrackMarker;
import alemax.trainsmod.init.TMItemGroups;
import alemax.trainsmod.init.TMPackets;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BlockTrack extends TMBlock {

    public BlockTrack() {
        super(Settings.of(Material.STONE), "track");
    }

    @Override
    public void onBlockRemoved(BlockState blockState_1, World world_1, BlockPos blockPos_1, BlockState blockState_2, boolean boolean_1) {
        super.onBlockRemoved(blockState_1, world_1, blockPos_1, blockState_2, boolean_1);

    }

}
