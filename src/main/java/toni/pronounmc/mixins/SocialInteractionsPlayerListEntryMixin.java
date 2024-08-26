package toni.pronounmc.mixins;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.social.PlayerEntry;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import toni.pronounmc.api.PronounCache;
import toni.pronounmc.PronounMC;

import java.util.UUID;

@Mixin(PlayerEntry.class)
public class SocialInteractionsPlayerListEntryMixin {
    @Shadow @Final private UUID id;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I"))
    private int replaceText(GuiGraphics instance, Font font, String text, int x, int y, int color, boolean dropShadow) {
        var pronouns = PronounCache.getPronounsFor(id);
        var name = PronounMC.getTextWithColoredPronoun(Component.literal(text), pronouns);

        return instance.drawString(font, name, x, y, color);
    }
}
