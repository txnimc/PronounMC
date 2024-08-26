package toni.pronounmc.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import toni.pronounmc.api.PronounCache;

import java.util.Collection;
import java.util.UUID;

@Mixin(ClientPacketListener.class)
public class ClientPlayNetworkHandlerMixin {
    #if BEFORE_20_1
    @Shadow @Final private Minecraft minecraft;
    #endif

    @Unique
    private boolean pronounify$waitingForChunkPacket = false;

    @Inject(method = "handleLogin", at = @At("RETURN"))
    private void onGameJoin(ClientboundLoginPacket packet, CallbackInfo ci) {
        pronounify$waitingForChunkPacket = true;
    }

    @Inject(method = "getOnlinePlayers", at = @At("HEAD"))
    private void onPlayerList(CallbackInfoReturnable<Collection<PlayerInfo>> cir) {
        if (!pronounify$waitingForChunkPacket)
            return;

        pronounify$waitingForChunkPacket = false;
        try {
            #if BEFORE_20_1
            UUID[] uuids = minecraft.getConnection().getOnlinePlayerIds().toArray(UUID[]::new);
            #else
            UUID[] uuids = Minecraft.getInstance().getConnection().getOnlinePlayerIds().toArray(UUID[]::new);
            #endif

            PronounCache.getPronounsFor(uuids);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
