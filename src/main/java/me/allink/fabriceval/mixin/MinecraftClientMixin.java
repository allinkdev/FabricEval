package me.allink.fabriceval.mixin;

import me.allink.fabriceval.FabricEval;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(at = @At("HEAD"), method = "disconnect()V")
    public void disconnect(CallbackInfo ci) {
        FabricEval.lines.clear();
    }
}
