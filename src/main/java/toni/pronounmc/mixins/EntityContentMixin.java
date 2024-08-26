package toni.pronounmc.mixins;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import toni.pronounmc.api.PronounCache;
import toni.pronounmc.PronounMC;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mixin(HoverEvent.EntityTooltipInfo.class)
public class EntityContentMixin {
    @Shadow @Final public EntityType<?> type;

    @Shadow @Final public UUID id;

    @Shadow @Final @Mutable @Nullable public #if AFTER_21_1 Optional<Component> #else Component #endif name;

    @Shadow private @Nullable List<Component> linesCache;

    @Inject(method = "getTooltipLines", at = @At("HEAD"))
    private void modifyTooltip(CallbackInfoReturnable<List<Component>> cir) {
        if (!type.equals(EntityType.PLAYER))
            return;

        var pronouns = PronounCache.getPronounsFor(id);

        #if AFTER_21_1
        if (name != null && name.isPresent())
            name = Optional.of(PronounMC.getTextWithColoredPronoun(name.get(), pronouns));

        #else
        name = PronounMC.getTextWithColoredPronoun(name, pronouns);
        #endif
    }
}
