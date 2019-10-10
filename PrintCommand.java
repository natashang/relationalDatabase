import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PrintCommand implements Command
{
	@Override
	public void execute(String[] input)
	{
		List<String> relations = new ArrayList<>();
		List<String> nameExists = new ArrayList<>();
		LinkedList<Relation> toPrint = new LinkedList<Relation>();

		// Construct list of Relation names
		for (int i = 1; i < input.length; i++)
		{
			relations.add(input[i].replaceAll("[^a-zA-Z0-9]", "").toUpperCase());
		}
		// Find Relations with matching names and construct list for the
		// command.
		for (Relation relation : Database.getRelations())
		{
			if (relations.contains(relation.name))
			{
				toPrint.add(relation);
				nameExists.add(relation.name);
			}
		}
		for (String relation : relations)
		{
			if (!nameExists.contains(relation.toUpperCase()))
			{
				System.out.println(relation + " does not exist.");
			}
		}

		Database.printRelation(toPrint);
	}

	@Override
	public void detailedHelpMessage()
	{
		System.out.println("Usage: PRINT [RELATION LIST]");
		System.out.println("Example: PRINT STUDENT, COURSE");
		System.out.println("Prints any relations supplied by the user as an ASCII table.");
	}

	@Override
	public String simpleHelpMessage()
	{
		return "Prints any relations supplied by the user as an ASCII table.";
	}
}
