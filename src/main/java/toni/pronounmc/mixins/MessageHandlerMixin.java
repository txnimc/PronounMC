package toni.pronounmc.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import toni.pronounmc.api.PronounCache;
import toni.pronounmc.PronounMC;

import java.time.Instant;

@Mixin(ChatListener.class)
public abstract class MessageHandlerMixin {
    @Shadow protected abstract boolean showMessageToPlayer(ChatType.Bound boundChatType, PlayerChatMessage chatMessage, Component decoratedServerContent, GameProfile gameProfile, boolean onlyShowSecureChat, Instant timestamp);

    @ModifyVariable(method = "showMessageToPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/chat/ChatTrustLevel;createTag(Lnet/minecraft/network/chat/PlayerChatMessage;)Lnet/minecraft/client/GuiMessageTag;"), argsOnly = true)
    private Component decorateTextWithPronouns(Component decorated, ChatType.Bound params, PlayerChatMessage message, Component dontuse, GameProfile senderEntry) {
        var sender = senderEntry.getId();
        var pronouns = PronounCache.getPronounsFor(sender);

        return PronounMC.getTextWithColoredPronoun(decorated, pronouns);
    }
}
