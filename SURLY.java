import java.io.*;
import java.util.*;

public class SURLY
{
	static Scanner inputScanner = null;
	static Scanner fileScanner = null;
	
	public static void main(String[] Args)
	{
		Parser.populateCommandMap();
		
		if (Args.length > 0)
		{
			ParseFile(Args[0]);
		}

		// If no arguments, uses line-entry mode.
		inputScanner = new Scanner(System.in);
		String[] entry = new String[1];
		entry[0] = "";
		System.out.println("Command line starting...");
		System.out.println("Type \"help\" for list of available commands. You can quit at any time by typing \"EXIT\"");
		while (true)
		{
			System.out.print(">");
			entry = inputScanner.nextLine().split(" ");
			Parser.parseInput(entry);
		}
	}
	
	public static void ParseFile(String fileLocation)
	{
		File file = new File(fileLocation);

		try
		{
			fileScanner = new Scanner(file);
		} catch (FileNotFoundException e)
		{
			System.out.println("File not found");
			return;
		}

		while (fileScanner.hasNextLine())
		{
			Parser.parseInput(fileScanner.nextLine().split(" "));
		}
		System.out.println("File parsing complete.");
	}
}
