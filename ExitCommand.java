
public class ExitCommand implements Command
{

	@Override
	public void execute(String[] input)
	{
		System.out.println("The program will now exit.");
		System.exit(0);
	}

	@Override
	public void detailedHelpMessage()
	{
		System.out.println("Usage: EXIT");
		System.out.println("Exits the SURLY program.");
		
	}

	@Override
	public String simpleHelpMessage()
	{
		return "Exits the SURLY program.";
	}

}
