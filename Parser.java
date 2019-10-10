import java.util.HashMap;
import java.util.Map;
public class Parser
{
	private static Map<String, Command> commands;

	public static void parseInput(String[] input)
	{
		if(!input[0].equals(""))
		{
			Command command = commands.get(input[0].toUpperCase());
			if(command != null)
			{
				command.execute(input);
			}
			else if (input.length > 2 && input[1].contains("="))
			{
				command = commands.get(input[2].toUpperCase());
				if(command != null)
				{
					command.execute(input);
				}
				else
				{
					System.out.println(input[2] + " is not a valid command.");
				}
			}
			else
			{
				System.out.println(input[0] + " is not a valid command.");
			}
		}
		else
		{
			return;
		}
	}
	public static void populateCommandMap()
	{
		commands = new HashMap<String, Command>();
		commands.put("RELATION", new RelationCommand());
		commands.put("INSERT", new InsertCommand());
		commands.put("PRINT", new PrintCommand());
		commands.put("DESTROY", new DestroyCommand());
		commands.put("FILE", new FileCommand());
		commands.put("HELP", new HelpCommand());
		commands.put("EXIT", new ExitCommand());
		commands.put("DELETE", new DestroyTuple());
		commands.put("PROJECT", new ProjectCommand());
		commands.put("SELECT", new SelectCommand());
		commands.put("JOIN", new JoinCommand());

	}

	public static Map<String, Command> getCommands()
	{
		return commands;
	}
}
