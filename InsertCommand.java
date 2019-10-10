import java.util.ArrayList;
import java.util.List;

public class InsertCommand implements Command
{

	@Override
	public void execute(String[] input)
	{
		List<String> values = getValues(input);
		Relation relation = findRelation(input[1].toUpperCase());
		if (relation != null)
		{
			if(relation.isTemp())
			{
				System.out.println("Cannot INSERT into temporary relation. Aborting...");
				return;
			}
			Database.insertTupleIntoRelation(relation, values.toArray(new String[values.size()]));
		}
		else
		{
			return;
		}
	}
	
	private List<String> getValues(String[] input)
	{
		List<String> values = new ArrayList<String>();
		for (int i = 2; i < input.length; i++)
		{
			// Check for and combine strings with spaces using ' as a
			// delimiter
			if (input[i].charAt(0) == '\'')
			{
				String temp = input[i].substring(1, input[i].length());
				while (input[i].charAt(input[i].length() - 1) != '\'' && input[i].charAt(input[i].length() - 1) != ';')
				{
					i++;
					temp = temp + " " + input[i];
				}
				if(temp.charAt(temp.length()-1) == ';')
				{
					values.add(temp.substring(0, temp.length()-2));
				}
				else
				{
					values.add(temp.substring(0, temp.length()-1));
				}
			}
			// Otherwise add to list
			else
			{
				if(input[i].charAt(input[i].length()-1) == ';')
				{
					values.add(input[i].substring(0, input[i].length()-1));
				}
				else
				{
					values.add(input[i]);
				}
			}
		}
		return values;
	}
	
	private Relation findRelation(String name)
	{
		for (Relation relation : Database.getRelations())
		{
			if (relation.name.toUpperCase().equals(name))
			{
				return relation;
			}
		}
		System.out.println("Relation not found. Insert aborted.");
		return null;
	}

	@Override
	public void detailedHelpMessage()
	{
		System.out.println("Usage: INSERT relationname <tuple>;");
		System.out.println("INSERT COURSE CSCI241 'DATA STRUCTURES' 4;");
		System.out.println("Description. INSERT adds a new tuple to relationname. The relation " 
		+ "must already exist. Tuple values must agree in type and order with the corresponding" 
				+ " attribute list for the relation, with values longer than the given length being" 
		+ " truncated as necessary. If too many or too few tuple variables are encountered in the tuple," 
				+ " an error message is generated and the insertion is aborted.");
	}

	@Override
	public String simpleHelpMessage()
	{
		return "Inserts a tuple with the given data into the given relation.";
	}

}
