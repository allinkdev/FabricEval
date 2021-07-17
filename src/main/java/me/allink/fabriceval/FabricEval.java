package me.allink.fabriceval;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.allink.fabriceval.commands.EvalCommand;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.command.argument.MessageArgumentType;

import java.util.TimerTask;

@Environment(net.fabricmc.api.EnvType.CLIENT)
public class FabricEval implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandDispatcher<FabricClientCommandSource> dispatcher = ClientCommandManager.DISPATCHER;

        LiteralCommandNode<FabricClientCommandSource> evaluateRoot = ClientCommandManager
                .literal("eval")
                .build();

        LiteralCommandNode<FabricClientCommandSource> executeNode = ClientCommandManager
                .literal("execute")
                .then(ClientCommandManager.argument("command", MessageArgumentType.message()))
                .executes(EvalCommand::eval)
                .build();

        LiteralCommandNode<FabricClientCommandSource> stopNode = ClientCommandManager
                .literal("stop")
                .executes(EvalCommand::stop)
                .build();

        evaluateRoot.addChild(executeNode);
        evaluateRoot.addChild(stopNode);
        dispatcher.getRoot().addChild(evaluateRoot);

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
