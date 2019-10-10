import java.util.ArrayList;

public class ProjectCommand implements Command {

	@Override
	public void execute(String[] input) {
		
		if (input.length < 6) {
			System.out.println("Not a valid input.");
			return;
		}
		
		// Case: input is not all upper cased
		input = upperAll(input);
		
		/*
		 * Parsing input line
		 * tempName: name of temporary relation
		 * relName: name of relation to be projected from
		 * attributes: desired attributes to project
		 */

		String tempName = input[0];

		String relName = input[input.length-1];
		relName 	   = relName.replaceAll("[^a-zA-Z0-9]", "");

		String[] attributes = parseAttrbFromInput(input);

		/*
		 * Check 1: is it a valid relation?
		 */
		
		Relation curr = findRelation(relName);

		if (curr!= null) {
			
			int numTuples = curr.getTupleList().size();
			int numAttrb  = curr.getAttributeLabels().length;

			int index = -1;

			ArrayList<String> formatAL = new ArrayList<>();

			/*
			 * Check 2: are these valid attributes?
			 */

			for (String s: attributes) {

				int i = findAttrbIndex(s, curr);

				// Not valid
				if (i == -1) {
					System.out.println(s + " is not a valid attribute");
					return;
				}

				// Valid
				else {
					index = i;

					/*
					 * Gets format for desired attributes
					 */

					for (int fcount = 0; fcount < curr.getFormat().length; fcount+=2) {

						/* Why multiply by 2?
						 * 
						 * ex:
						 * 		index (attribute = cnum) from course: 3 
						 * 		format for cnum: NUM 4
						 * 		
						 * 		format.length = 6, or course attribute.length *2
						 * 		curr.getFormat()[4] = NUM
						 * 		curr.getFormat()[5] = 4
						 * 
						 */

						if (fcount == (2*index) ) {
							
							String dataType = curr.getFormat()[fcount];
							String val 		= curr.getFormat()[fcount+1];
							
							formatAL.add(dataType);
							formatAL.add(val);
						}

					}

				}


			}

			/*
			 * format stores all info about format of desired attributes
			 */
			
			String[] format = new String[formatAL.size()];
			format 			= formatAL.toArray(format);

			// This will be the temporary relation projected
			// It is not stored in database
			Relation temp = new Relation(tempName, attributes, format);

			ArrayList<String> allAttrbVals = new ArrayList<>();
			int ix	   = -2;
			String val = "";

			int numDesAttrb = 0;

			for (String a : attributes) {
				
				numDesAttrb++;
				ix = findAttrbIndex(a, curr);
				
				for (int r = 0; r < numTuples; r++) {
					
					for (int c = 0; c < numAttrb; c++) 	{
						
						// (c, ix) is the (row, col) for desired attribute a
						if (c == ix) {
							val = curr.getTupleList().get(r).getAttributeList().get(c).getValue();
							allAttrbVals.add(val);
						}
						
					}
					
				}

			}

			int vals = allAttrbVals.size();
			int n 	 = numDesAttrb;
			int stop = vals / n;

			String[] tupleAR = new String[allAttrbVals.size()/numDesAttrb];


			for (int i = 0; i < tupleAR.length; i++) {
				ArrayList<String> tbInserted = new ArrayList<>();

				for (int y = 0; y < numDesAttrb; y++) {
					tbInserted.add(allAttrbVals.get(i + (stop * y)));
				}

				String[] tbInsertedTp = new String[tbInserted.size()];
				tbInsertedTp 		  = tbInserted.toArray(tbInsertedTp);

				temp.silentInsertTuple(tbInsertedTp);
				tbInsertedTp = null;

			}

			temp.print();
			temp.makeTemp();
			Database.addInstantiatedRelation(temp);

		}
		else {
			System.out.println(relName + " is not a relation in this database");
			return;
		}

	}

	// Gets relation from all relations in the database
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

	// Gets the desired attribute strings from input line
	private String[] parseAttrbFromInput (String[] input) {

		ArrayList<String> attrbList = new ArrayList<>();

		for (int i = 3; i < input.length-2; i++) {

			// Parses out "," from the attribute
			if (input[i].contains(",")) {
				attrbList.add(input[i].substring(0, input[i].length()-1));
			}
			else {
				attrbList.add(input[i]);
			}
		}

		String[] attributes = attrbList.toArray(new String[attrbList.size()]);
		return attributes;

	}

	// Gets desired attribute from the desired relation
	private int findAttrbIndex (String name, Relation rel) {
		
		int index = -1;

		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < rel.getAttributeLabels().length; j++) {		

				if (name.equals(rel.getAttributeLabels()[j])) {
					index = j;
				}
				
			}
		}
		
		return index;

	}
	
	// Goes through each element of input and uppercases it
	private String[] upperAll (String[] input) {
	
		ArrayList<String> inputUpperCased = new ArrayList<>();
		
		for (String i: input) {
			inputUpperCased.add(i.toUpperCase());
		}
		
		String[] allUpped = new String[inputUpperCased.size()];
		allUpped = inputUpperCased.toArray(allUpped);
		
		return allUpped;
		
		
	}

	@Override
	public void detailedHelpMessage() {

		System.out.println("Usage: <temprelname> = PROJECT <list of attributes> FROM <relation>;");
		System.out.println("Example: P = PROJECT CREDITS, CNUM FROM COURSE;");
		System.out.println("PROJECT creates a temporary relation containing specified attributes of a relation. "
				+ "If a relation or attribute in the list of attributes does not exist, the user will be informed of "
				+ "so and be prompted for another input. If the relation and attribute(s) exist, the program will parse "
				+ "through the relation and insert tuples containing only those specified attributes into a temporary "
				+ "relation. This temporary relation is not stored or added to the database.");
	}

	@Override
	public String simpleHelpMessage() {
		return "Displays a temporary relation containing specified attributes of a relation";
	}

}
