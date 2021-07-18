package me.allink.fabriceval.threads;

import me.allink.fabriceval.FabricEval;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

public class EvalThread extends Thread {
    private final String shellCommand;
    AtomicBoolean interrupted = new AtomicBoolean(false);

    public EvalThread(String shellCommand) {
        this.shellCommand = shellCommand;
    }

    public void run() {
        try {
            Process process = Runtime.getRuntime().exec("cmd.exe /c " + shellCommand, null, new File(System.getProperty("user.home")));

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while((line = inputReader.readLine()) != null) {
                if (interrupted.get()) {
                    process.destroyForcibly();
                    line = null;
                    return;
                }
                if (!line.isEmpty() && !line.isBlank()) {
                    FabricEval.lines.add(line);
                }
                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void stopMe() {
        interrupted.set(true);
    }
}
