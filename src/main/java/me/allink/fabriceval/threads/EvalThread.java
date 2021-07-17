package me.allink.fabriceval.threads;

import me.allink.fabriceval.commands.EvalCommand;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

public class EvalThread extends Thread {
    private String shellCommand;

    public EvalThread(String shellCommand) {
        this.shellCommand = shellCommand;
    }

    public void run() {
        try {
            Process process = Runtime.getRuntime().exec("cmd.exe /c " + shellCommand, null, new File(System.getProperty("user.home")));

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while((line = inputReader.readLine()) != null) {
                EvalCommand.lines.add(line);
                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
