package me.allink.fabriceval.mixin;

import me.allink.fabriceval.commands.EvalCommand;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(at = @At("INVOKE"), method = "Lnet/minecraft/client/MinecraftClient;disconnect()V")
    public void disconnect(CallbackInfo ci) {
        EvalCommand.lines.clear();
    }
}
