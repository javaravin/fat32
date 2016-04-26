import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Cli {

	public static String myReadLine(String prompt)
	{
		String line = "";
		BufferedReader bufferedReader =
				new BufferedReader(new InputStreamReader(System.in));
		System.out.print(prompt);
		try {
			line = bufferedReader.readLine();
		} catch (IOException e) {
			line = "";
		}
		return line;
	}

	public Cli() {
		System.out.println("initializing...");
	}

	public void cliRun() {
		String commandLine;
		
		CommandProcessor cp = new CommandProcessor();
		
		boolean done = false;
		
		while (!done) {
			commandLine = myReadLine(">>>");
			done = cp.processCommand(commandLine);
		}
	}
}