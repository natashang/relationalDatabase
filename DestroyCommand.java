
public class DestroyCommand implements Command
{

	@Override
	public void execute(String[] input)
	{
		for(int i = 1; i < input.length; i++)
		{
			Relation relationToDestroy = findRelation(input[i].replaceAll("[^a-zA-Z0-9]", "").toUpperCase());
			if(relationToDestroy != null)
			{
				Database.removeRelation(relationToDestroy);
			}
			else
			{
				System.out.println("There is no database called \"" + input[1].toUpperCase() + "\"");
			}
		}
	}
	
	private Relation findRelation(String name)
	{
		for (Relation relation : Database.getRelations())
		{
			if (relation.getName().equals(name))
			{
				return relation;
			}
		}
		return null;
	}

	@Override
	public void detailedHelpMessage()
	{
		System.out.println("Usage: DESTROY <relationName>");
		System.out.println("Example: DESTROY COURSE, OFFERINGBYINSTRBYCOURSE;");
		System.out.println("Removes the given relation from the database.");
	}

	@Override
	public String simpleHelpMessage()
	{
		return "Removes a given relation from the database.";
	}

}
