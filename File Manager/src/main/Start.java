package main;

import java.io.File;
import java.util.ArrayList;
import GUI.Explorer;
import java.awt.Toolkit;

public class Start {
	public static void main(String[] args) {
		Explorer explorer = new Explorer();
		explorer.setIconImage(Toolkit.getDefaultToolkit().getImage(Start.class.getResource("/graphics/Icon.png")));
	}

	public static void restartApplication() throws Exception {
		final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		final File currentJar = new File(Start.class.getProtectionDomain().getCodeSource().getLocation().toURI());

		if (!currentJar.getName().endsWith(".jar"))
			return;

		final ArrayList<String> command = new ArrayList<String>();
		command.add(javaBin);
		command.add("-jar");
		command.add(currentJar.getPath());

		final ProcessBuilder builder = new ProcessBuilder(command);
		builder.start();
		System.exit(0);
	}
}