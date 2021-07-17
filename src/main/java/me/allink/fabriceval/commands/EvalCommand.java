package me.allink.fabriceval.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.allink.fabriceval.threads.EvalThread;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class EvalCommand implements Command<FabricClientCommandSource> {
    public static MinecraftClient client = MinecraftClient.getInstance();
    public static List<String> lines = new ArrayList<>();
    static public Timer timer = new Timer();

    public static int eval(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        String shellCommand = context.getArgument("command", MessageArgumentType.MessageFormat.class).getContents();
        EvalThread evalThread = new EvalThread(shellCommand);
        evalThread.start();
        return 1;
    }

    @Override
    public int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        Text text = Text.of("You must give a command to execute!");
        Style style = text.getStyle();
        style = style.withExclusiveFormatting(Formatting.RED);
        text = text.getWithStyle(style).get(0);
        context.getSource().sendFeedback(text);

        return 0;
    }
}
