
public class FileCommand implements Command
{

	@Override
	public void execute(String[] input)
	{
		if(input.length > 1)
		{
			SURLY.ParseFile(input[1]);
		}
		else
		{
			System.out.println("No file path provided. Aborting...");
		}
	}

	@Override
	public void detailedHelpMessage()
	{
		System.out.println("Usage: FILE <filepath>");
		System.out.println("Example: FILE C:\\TEST-input.txt");
		System.out.println("Parses all commands in a file line-by-line, treating each line as a complete command."
				+" Each command will have its success/failure message(s) printed as normal.");
	}

	@Override
	public String simpleHelpMessage()
	{
		return "Processes a file containing lines of commands into the database.";
		
	}

}
