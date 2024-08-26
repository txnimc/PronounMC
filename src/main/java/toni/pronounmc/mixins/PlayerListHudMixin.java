package toni.pronounmc.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.util.CommonColors;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import toni.pronounmc.api.PronounCache;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerListHudMixin {

    @Shadow @Final private Minecraft minecraft;

    @Shadow protected abstract List<PlayerInfo> getPlayerInfos();

    @Inject(method = "renderPingIcon", at = @At("TAIL"))
    public void afterRenderPlayer(GuiGraphics context, int width, int x, int y, PlayerInfo entry, CallbackInfo ci) {
        String txt = PronounCache.getPronounsFor(entry.getProfile().getId());
        if (txt == null) return;

        // positition text correctly
        int textX = x + (width - 12 - minecraft.font.width(txt));

        context.drawString(minecraft.font, txt, textX, y, CommonColors.GRAY, true);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I"))
    public int redirectPlayerListEntryWidth(int a, int b) {
        List<PlayerInfo> entries = getPlayerInfos();
        UUID[] uuids = entries
                .stream()
                .map(ent -> ent.getProfile().getId())
                .toArray(UUID[]::new);

        HashMap<UUID, String> pronouns = PronounCache.getPronounsFor(uuids);

        int longestPronoun = pronouns.values().stream().filter(Objects::nonNull).mapToInt(minecraft.font::width).max().orElse(0);

        return Math.min(a + 5 + longestPronoun, b);
    }

}
