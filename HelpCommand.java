import java.util.*;
import java.util.Map.Entry;

public class HelpCommand implements Command
{

	@Override
	public void execute(String[] input)
	{
		if(input.length < 2)
		{
			printSimpleHelp();
		}
		else
		{
			printDetailedHelp(input[1].toUpperCase());
		}
		
	}

	private void printSimpleHelp()
	{
		System.out.println("For more information on a specific command, type \"HELP [Command-name]\"");
		String[][] commandsAndHelp  = commandHelpList();
		int commandPadding = 0;
		for (int i = 0; i < commandsAndHelp.length; i++)
		{
			if(commandPadding < commandsAndHelp[i][0].length())
			{
				commandPadding = commandsAndHelp[i][0].length();
			}
		}
		for (int i = 0; i < commandsAndHelp.length; i++)
		{
			System.out.print(PrintHelper.padRight(commandsAndHelp[i][0], commandPadding + 5));
			System.out.println(commandsAndHelp[i][1]);
		}
		
		
	}

	public void printDetailedHelp(String input)
	{
		Command command = getCommand(input);
		if(command != null)
		{
			command.detailedHelpMessage();
		}
		else
		{
			System.out.println(input + "is not a valid command.");
		}
	}

	@Override
	public void detailedHelpMessage()
	{
		System.out.println("Usage: HELP <command>");
		System.out.println("Example: HELP RELATION");
		System.out.println("Shows detailed usage information about the entered command.");
	}

	@Override
	public String simpleHelpMessage()
	{
		return "Provides help information for SURLY commands.";
	}
	
	private Command getCommand(String input)
	{
		return Parser.getCommands().get(input);
	}
	
	private String[][] commandHelpList()
	{
		Map<String, Command> commands = Parser.getCommands();
		String[][] commandsAndHelp = new String[commands.size()][2];
		int i=0;
		for (Entry<String, Command> entry : commands.entrySet())
		{
			commandsAndHelp[i][0] = entry.getKey();
			commandsAndHelp[i][1] = entry.getValue().simpleHelpMessage();
			i++;
		}
		sortCommandHelpList(commandsAndHelp);
		return commandsAndHelp;
	}
	
	private void sortCommandHelpList (String[][] commandsAndHelp)
	{

        int n = commandsAndHelp.length;
 
        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n-1; i++)
        {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i+1; j < n; j++)
                if (commandsAndHelp[j][0].compareTo(commandsAndHelp[min_idx][0]) < 0)
                    min_idx = j;
 
            // Swap the found minimum element with the first
            // element
            String[] temp = commandsAndHelp[min_idx];
            commandsAndHelp[min_idx] = commandsAndHelp[i];
            commandsAndHelp[i] = temp;
        }
	}
}
