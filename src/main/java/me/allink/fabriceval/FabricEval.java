package me.allink.fabriceval;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.allink.fabriceval.commands.EvalCommand;
import me.allink.fabriceval.threads.EvalThread;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.command.argument.MessageArgumentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Environment(net.fabricmc.api.EnvType.CLIENT)
public class FabricEval implements ModInitializer {
    public static Timer timer = new Timer();
    public static boolean taskExists = false;
    public static List<String> lines = new ArrayList<>();
    public static List<EvalThread> threads = new ArrayList<>();

    public static void createMessageTimerTask() {
        taskExists = true;
        FabricEval.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (lines.size() > 0) {
                    try {
                        EvalCommand.client.player.sendChatMessage(lines.get(0));
                        lines.remove(0);
                    } catch (Exception e) {
                        // Ignored
                    }
                }
            }
        }, 0, 1000);
    }

    public static void killMessageTimerTask() {
        clearArray();
        timer.cancel();
        createMessageTimerTask();
    }

    public static void clearArray() {
        lines.clear();
        lines = new ArrayList<>();
    }

    @Override
    public void onInitialize() {
        CommandDispatcher<FabricClientCommandSource> dispatcher = ClientCommandManager.DISPATCHER;


        LiteralCommandNode<FabricClientCommandSource> evaluateRoot = ClientCommandManager
                .literal("eval")
                .build();

        LiteralCommandNode<FabricClientCommandSource> executeNode = ClientCommandManager
                .literal("execute")
                .build();

        ArgumentCommandNode<FabricClientCommandSource, MessageArgumentType.MessageFormat> commandArgument = ClientCommandManager.argument("command", MessageArgumentType.message())
                .executes(EvalCommand::eval)
                .build();

        executeNode.addChild(commandArgument);

        LiteralCommandNode<FabricClientCommandSource> stopNode = ClientCommandManager
                .literal("stop")
                .executes(EvalCommand::stop)
                .build();


        evaluateRoot.addChild(executeNode);
        evaluateRoot.addChild(stopNode);
        dispatcher.getRoot().addChild(evaluateRoot);

        createMessageTimerTask();
    }
}
