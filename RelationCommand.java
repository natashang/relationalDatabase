import java.util.*;

public class RelationCommand implements Command
{
	@Override
	public void execute(String[] input)
	{
		List<String[]> labelsAndFormat;
		if(inputHasCorrectFormat(input) && (labelsAndFormat = getLabelsAndFormatFromInput(input)) != null)
		{
			String name = getNameFromInput(input);
			String[] columnLabels = labelsAndFormat.remove(0);
			String[] format = labelsAndFormat.remove(0);
			Database.createNewRelation(name, columnLabels, format);
			System.out.println(input[1].toUpperCase() + " relation added.");
		}
		else
		{
			return;
		}
	}
	
	public List<String[]> getLabelsAndFormatFromInput(String[] input)
	{
		
		String[] labels = new String[(input.length - 2) / 3];
		String[] format = new String[labels.length * 2];

		// Instantiate a counter for the labels and format arrays
		int counter = 0;
		for (int i = 2; i < input.length; i += 3)
		{
			labels[counter] = input[i].replaceAll("[^a-zA-Z0-9]", "");
			format[2*counter] = input[i+1].replaceAll("[^a-zA-Z0-9]", "").toUpperCase(); 
			
			if(!validDataTypeGiven(format[2*counter].toUpperCase()))
			{
				return null;
			}
			
			format[2*counter + 1] = input[i + 2].replaceAll("[^a-zA-Z0-9]", "");
			
			if(inputLengthNotANumber(format[2*counter + 1]))
			{
				return null;
			}
			
			counter++;
		}
		List<String[]> output = new ArrayList<String[]>();
		output.add(labels);
		output.add(format);
		return output;
	}
	
	public String getNameFromInput(String[] input)
	{
		return input[1].toUpperCase();
	}
	
	
	public boolean inputHasCorrectFormat(String[] input)
	{
		if((input.length - 2) % 3 != 0 || input.length < 5)
		{
			System.out.println("Relation command is not corectly formatted. Aborting.");
			return false;
		}
		return true;
	}
	
	public static boolean validDataTypeGiven(String formatCheck)
	{
		if (!formatCheck.equals("CHAR") && !formatCheck.equals("NUM"))
		{
			System.out.println("An invalid data type was given. Use only NUM or CHAR. Aborting.");
			return false;
		}
		return true;
	}
	
	public static boolean inputLengthNotANumber(String length)
	{
		try
		{
			Integer.parseInt(length);
		}
		catch (NumberFormatException e)
		{
			System.out.println(length + " is not a valid length. Length must be a number.");
			return true;
		}
		return false;
	}

	@Override
	public void detailedHelpMessage()
	{
		System.out.println("Usage: RELATION relationname (<attrib format>);");
		System.out.println("Example: RELATION COURSE (CNUM CHAR 8, TITLE CHAR 30, CREDITS NUM 4);");
		System.out.println("RELATION enters a new relation into the database. If a relation by that " 
		+ "name already exists, DESTROY will be called on the old relation before the new one is created."  
		+ " The format of an attribute consists of the type of the attribute (NUMeric or CHARacter} " 
		+ "and the maximum length of the attribute. Both types of data will be stored as character strings," 
		+ " and data values may be shorter than the attributes maximum length.");
	}

	@Override
	public String simpleHelpMessage()
	{
		return "Adds a new relation to the database.";
	}
}
