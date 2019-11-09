package alemax.trainsmod.mixin;

import alemax.trainsmod.global.trackmarker.TrackMarkerInstances;
import alemax.trainsmod.init.TMPackets;
import alemax.trainsmod.networking.TMPacket;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {

    @Inject(at = @At("RETURN"), method = "onPlayerConnect")
    private void onPlayerConnect(ClientConnection clientConnection_1, ServerPlayerEntity serverPlayerEntity_1, CallbackInfo info) {

        System.out.println("Created ServerPlayer! " + serverPlayerEntity_1.world.isClient);
        System.out.println(serverPlayerEntity_1.networkHandler);

        TMPackets.packetS2CSyncGlobalOnPlayerJoin.send(TrackMarkerInstances.HANDLER.count, serverPlayerEntity_1);

    }

}
