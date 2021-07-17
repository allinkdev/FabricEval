package me.allink.fabriceval.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.allink.fabriceval.FabricEval;
import me.allink.fabriceval.threads.EvalThread;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class EvalCommand implements Command<FabricClientCommandSource> {
    public static MinecraftClient client = MinecraftClient.getInstance();
    public static List<String> lines = new ArrayList<>();
    public static List<EvalThread> threads = new ArrayList<>();

    public static int eval(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        String shellCommand = context.getArgument("command", MessageArgumentType.MessageFormat.class).getContents();
        EvalThread evalThread = new EvalThread(shellCommand);
        threads.add(evalThread);
        evalThread.start();
        return 1;
    }

    @Override
    public int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        Text text = Text.of("/eval execute <command>\n/eval stop");
        context.getSource().sendFeedback(text);
        return 0;
    }

    public static int stop(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        EvalCommand.lines = new ArrayList<>();
        for(EvalThread thread: threads) {
            thread.interrupt();
        }
        context.getSource().sendFeedback(Text.of("Stopped evaluation."));

        FabricEval.killMessageTimerTask();
        return 1;
    }
}
