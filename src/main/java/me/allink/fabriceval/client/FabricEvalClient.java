package me.allink.fabriceval.client;

import me.allink.fabriceval.commands.EvalCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.command.argument.MessageArgumentType;

import java.util.TimerTask;

@Environment(net.fabricmc.api.EnvType.CLIENT)
public class FabricEvalClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("eval").then(ClientCommandManager.argument("command", MessageArgumentType.message()).executes(EvalCommand::eval)));
        EvalCommand.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(EvalCommand.lines.size() > 0) {
                    try {
                        EvalCommand.client.player.sendChatMessage(EvalCommand.lines.get(0));
                        EvalCommand.lines.remove(0);
                    } catch (Exception e) {
                        // Ignored
                    }
                }
            }
        }, 0, 1000);
    }
}
