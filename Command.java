
public interface Command
{
	public void execute(String[] input);
	public void detailedHelpMessage();
	public String simpleHelpMessage();
}
