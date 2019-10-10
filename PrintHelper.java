import java.util.LinkedList;

public class PrintHelper
{
	private static String centerPad(String s, int padSize)
	{
		StringBuilder str = new StringBuilder();
		int startIndex = (padSize / 2) - (s.length() / 2);
		if (s.length() % 2 != 0 && padSize % 2 == 0)
		{
			startIndex--;
		}
		for (int i = 0; i < padSize; i++)
		{
			if (i < startIndex)
			{
				str.append(" ");
			} else if (i == startIndex)
			{
				str.append(s);
				i += s.length() - 1;
			} else
			{
				str.append(" ");
			}
		}
		return str.toString();
	}

	public static void printBoundedLine(String firstBorder, String lastBorder, String character, int middleSpacing)
	{
		System.out.print(firstBorder);
		for (int i = 0; i < middleSpacing; i++)
		{
			System.out.print(character);
		}
		System.out.println(lastBorder);
	}

	private static String dataFormatter(String text, int length)
	{
		return String.format("|" + " " + "%-" + length + "." + length + "s" + " ", text);
	}

	public static void PrintAttributeLine(String[] attributeLabels, int[] formatter)
	{
		for (int i = 0; i < attributeLabels.length; i++)
		{
			System.out.print(PrintHelper.dataFormatter(attributeLabels[i], formatter[i]));
		}
		System.out.println("|");
	}

	public static void PrintTitle(String name, int titleSpacing)
	{
		System.out.println("|" + PrintHelper.centerPad(name, titleSpacing) + "|");
	}

	public static void PrintAllTuples(LinkedList<Tuple> tupleList, int[] formatter)
	{
		for (Tuple tuple : tupleList)
		{
			int i = 0;
			// Iterate through attributes
			for (Attribute attribute : tuple.getAttributeList())
			{
				System.out.print(PrintHelper.dataFormatter(attribute.getValue(), formatter[i]));
				i++;
			}
			System.out.println("|");
		}
	}

	public static String padRight(String string, int length)
	{
		return String.format("%1$-" + length + "s", string);
	}

	public static void PrintCommands()
	{
		System.out.println("Here is a list of all implemented commands and their formats:\n");
		System.out.println(PrintHelper.padRight("RELATION (<label> <dataType> <maxLength>,...)", 50)
				+ "Adds relation with format denoted by each triple of label, dataType, and maxLength.");
		System.out.println(PrintHelper.padRight("INSERT <relationName> <value1> <value2>...", 50)
				+ "Inserts a tuple into the given relation with the given attribute(s) (use '<value>' for values that contain a space).");
		System.out.println(PrintHelper.padRight("PRINT <course1>, <course2>...", 50)
				+ "Prints the title, labels, and tuples of the given table(s) in an ascii table.");
		System.out.println(PrintHelper.padRight("FILE <file location>", 50)
				+ "Parses the given file as though it was passed in as an argument.");
		System.out.println(PrintHelper.padRight("EXIT", 50) + "Closes the program.\n");
	}
}
