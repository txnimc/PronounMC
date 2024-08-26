package toni.pronounmc.mixins;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import toni.pronounmc.api.PronounCache;
import toni.pronounmc.PronounMC;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @ModifyVariable(method = "renderNameTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;getFont()Lnet/minecraft/client/gui/Font;"), argsOnly = true)
    private Component modifyNametag(Component text, T entity) {
        if (!(entity instanceof Player player))
            return text;

        var uuid = player.getGameProfile().getId();
        var pronouns = PronounCache.getPronounsFor(uuid);
        text = PronounMC.getTextWithColoredPronoun(text, pronouns);

        return text;
    }
}
