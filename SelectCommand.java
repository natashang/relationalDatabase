import java.util.ArrayList;
public class SelectCommand implements Command{
	
	@Override
	public void execute(String[] input) {
		if (input.length < 4) {
			System.out.println("Not a valid input.");
			return;
		}
		
		input = upperAll(input);
		
		/*
		 * Parsing input line
		 * tempName: name of temporary relation
		 * relName: name of relation to be projected from
		 * attributes: desired attributes to project
		 */
		
		String tempName = input[0];
		
		String relName = "";
		
		if (input.length == 4) {
			relName = input[3];
			relName = relName.substring(0, relName.length()-1);
		}
		
		else {
			relName = input[3];
		}
		
		Relation curr = findRelation(relName);
		
		
		
		
		if (curr != null) {
			
			int numCond = getNumCond(input);
			//System.out.println("main: numCond " + numCond);
			Relation temp = new Relation (tempName, curr.getAttributeLabels(), curr.getFormat());
			
			/*
			 * Case: no WHERE clause
			 */
					
			if (numCond == 0) {
							
				for (Tuple t: curr.getTupleList()) {
					
					ArrayList<String> tbInserted = new ArrayList<>();
					
					for (Attribute a: t.getAttributeList()) {
						tbInserted.add(a.getValue());
					}
					
					String[] tbInsertedTp = new String[tbInserted.size()];
					tbInsertedTp = tbInserted.toArray(tbInsertedTp);
					
					temp.silentInsertTuple(tbInsertedTp);
					
					tbInsertedTp = null;
						
				}
				temp.print();
				temp.makeTemp();
				Database.addInstantiatedRelation(temp);
				
			}
			
			
			else  {
				//System.out.println("numCond: " + numCond);
				
				// allConnects contains "and", "or", "end"
				ArrayList<String> allConnectsAL = new ArrayList<>();
				
				
				//String[] conditions = parseCondFromInput(input);
				int numTuples = curr.getTupleList().size();
				int numAttrb  = curr.getAttributeLabels().length;
				String attrb = "";
				String op = "";
				String condVal = "";
				String word = "";
				//System.out.println("input.length " + input.length);
				int indexNextCond = 0;
				for (int i = 0; i< input.length; i++) {
					if (input[i].equalsIgnoreCase("where")) 
						indexNextCond = i+1;
				}
				ArrayList<String> conds = new ArrayList<>();
				ArrayList<String> possRows = new ArrayList<>();
				for (int cond = 1; cond <= numCond; cond++) {
					//System.out.println("cond " + cond);
					ArrayList<String> aCond = new ArrayList<>();
					for (int i = indexNextCond; i<= input.length; i++) {

					//	System.out.println("i: " + i);

						
							attrb = input[i];
							op = input[i+1];
							condVal = input[i+2];
							
							if (i == input.length-3) {
								word = "end";
								
							}
							else {
								word = input[i + 3];
							}
							
						allConnectsAL.add(word);
						
						
						
						
/*						attrb 	= input[i];
						op 		= input[i+1];
						condVal = input[i+2];
						
						// (8 + i*4) corr. to index of last word in input
						if (i < (4+i*4)) {
							word = input[i+3];
						}
						else {
							word = "end";
//							word = input[i+3];
						}
						
						*/
						
						//word 	= input[i+3];		//TODO: fix it for OR!
						if (condVal.contains(";")) {
							condVal = condVal.substring(0, condVal.indexOf(';'));
							word = "end";
						}
						// Check if attrb is a valid attribute
						int attrbValid = findAttrbIndex(attrb, curr);
						if (attrbValid != -1) {
//							System.out.println(attrb + " " + op + " " + condVal);
//							System.out.println(attrb + " " + op + " " + condVal + " " + word);
							aCond.add(attrb);
							aCond.add(op);
							aCond.add(condVal);
							aCond.add(word);
						} 
						else {
							System.out.println(attrb + " is not a valid attribute");
							return;
						}
						conds.addAll(aCond);
						aCond = null;
						i = input.length+1;
					}
					indexNextCond +=4;
				}
//				for (String s: conds) {
//					System.out.println("s: " + s.toString());
//				}
//				System.out.println("conds.size " + conds.size() );
				
				// "EQUALS", "NOTEQUALS", "LESSTHAN", "LESSTHANEQ", "GREATERTHAN", "GREATERTHANEQ"
				for (int a = 0; a < conds.size(); a+=4) {
					String anAttrb = conds.get(a);
					String anOp = conds.get(a+1);
					String aVal = conds.get(a+2);
					String aWord = conds.get(a+3);
//					System.out.println("a: " + anAttrb + " " + anOp + " " + aVal + " " + aWord);
					
					// r represents the row that fulfills that current condition
					for (int r = 0; r < numTuples; r++) {
						for (int c = 0; c < numAttrb; c++) {
							// found the column for attribute in condition
							if (curr.getAttributeLabels()[c].equalsIgnoreCase(anAttrb)) {
								String comp = curr.getTupleList().get(r).getAttributeList().get(c).getValue();
								//	System.out.println(comp);
								// Equality
								if (anOp.equals("=")) {
									if (comp.equals(aVal))
									{
//										System.out.println("equality: " + comp + ", " + aVal);
									//	System.out.println("row: " + r);
										possRows.add(r + "");
										//event = anOp;
									}
								}
								
								else if (anOp.equals("!=")) {
									
									if (!comp.equals(aVal)) {
//										System.out.println("not equals: " + comp + ", " + aVal);
										possRows.add(r + "");
									}
									
								}
								
								else if (anOp.equals("<")) {
									if (Integer.parseInt(comp) < Integer.parseInt(aVal)) {
//										System.out.println("less than: " + comp + ", " + aVal);
										possRows.add(r + "");
									}
									
								}
								
								else if (anOp.equals("<=")) {
									if (Integer.parseInt(comp) <= Integer.parseInt(aVal)) {
//										System.out.println("less than or EQ");
										possRows.add(r + "");
									}
								}
								else if (anOp.equals(">")) {
									if (Integer.parseInt(comp) > Integer.parseInt(aVal)) {
//										System.out.println("greater than: " + comp + ", " + aVal);
									//	System.out.println("row: " +r);
										possRows.add(r + "");
										//event = anOp;
									}
								}
								
								else if (anOp.equals(">=")) {
									if (Integer.parseInt(comp) >= Integer.parseInt(aVal)) {
//										System.out.println("greater than or EQ");
										possRows.add(r + "");
									}
								}
								else {
									System.out.println("Not a valid operation");
								}
							}
						}
					}
				}
				// See if any rows are the same
//				ArrayList<Integer> answerRow = getFinalRow(possRows, numCond);
//				ArrayList<Integer> answerRow = getFinalRow(possRows, numCond, word);
				ArrayList<Integer> answerRow = getFinalRow(possRows, numCond, allConnectsAL);
				
				for (int i = 0; i < answerRow.size(); i++) {
					
					for (int j = 0; j < numTuples; j++) {
						
						ArrayList<String> tbInserted = new ArrayList<>();
						
						if (j == answerRow.get(i)) {
							
							for (Attribute a: curr.getTupleList().get(j).getAttributeList()) {
								tbInserted.add(a.getValue());
							}
							
							String[] tbInsertedTp = new String[tbInserted.size()];
							tbInsertedTp 		  = tbInserted.toArray(tbInsertedTp);
							temp.insertTuple(tbInsertedTp);
							tbInsertedTp = null;
						}
						
					}
				
				}
				
				
				temp.print();
			
				
			}
		
			
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
	
	// Gets number of conditions
	private int getNumCond (String[] input) {
		int numCond = -1;
		
		// no WHERE 
		if (input.length == 4) {
			numCond = 0;
	//		System.out.println("no where");
		}
		
		
		// has at least one WHERE
		else {
			
			numCond = 1;
			
			for (int i = 0; i < input.length; i++) {
				if ( (input[i].equalsIgnoreCase("AND")) || (input[i].equalsIgnoreCase("OR")) ) {
					numCond++;
				}
			}
		}
		
		return numCond;
	}
	
	
	// Gets desired attribute from the desired relation
	private int findAttrbIndex (String name, Relation rel) {
	//	System.out.println("in findAttrbIndex: " + name);
		int index = -1;
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < rel.getAttributeLabels().length; j++) {		
				if (name.equalsIgnoreCase(rel.getAttributeLabels()[j])) {
					index = j;
				}
				
			}
		}
	//	System.out.println("fAI index: " + index);
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
	
	private ArrayList<Integer> getFinalRow (ArrayList<String> possRows, int numCond, ArrayList<String> allConnects) {
		ArrayList<Integer> solution = new ArrayList<>();

//		System.out.println("numCond in gFR: " + numCond);


		// Return all tuples that suit that one condition
		if (numCond == 1) {
//			System.out.println("1 condition");
			for (String p : possRows) {
//				System.out.println("String p: " + p);
				solution.add(Integer.parseInt(p));
			}
		}

		// Return the tuples that suit the multiple conditions
		else {

			for (String p : possRows) {
				System.out.println("String p: " + p);
			}


			for (String ac : allConnects) {
				System.out.println("ac: " + ac);

				if (!ac.equalsIgnoreCase("end")) {

					String word = ac;

					System.out.println("word: " + word);

					if (word.equalsIgnoreCase("and")) {

						for (int i = 0; i < possRows.size(); i++) {
							for (int j = i+1; j < possRows.size(); j++) {

								if (j!=i && possRows.get(i).equals (possRows.get(j)) ) {

									String aMatch = possRows.get(i);
									solution.add(Integer.parseInt(aMatch));
								}
							}
						}
					}

					else if (word.equalsIgnoreCase("or")) {

						for (int i = 0; i < possRows.size(); i++) {
							
							boolean isDuplicate = false;
							
							for (int j = i+1; j< possRows.size(); j++) {								
								if (j!=i && possRows.get(i).equals (possRows.get(j)) ) {
									
									// treat for duplicates
									if (possRows.get(i).equals (possRows.get(j) ) ) {

										isDuplicate = true;
										String aMatch = possRows.get(i);
										solution.add(Integer.parseInt(aMatch));
										i+=1;
										
										break;
									}
								}		
							}
							
							if (isDuplicate == false) {
								String aMatch = possRows.get(i);
								System.out.println("aMatch: " + aMatch);
								solution.add(Integer.parseInt(aMatch));
							}	
						}						
					}
					
					
					if (numCond >= 3) {
						System.out.println("numCond +" + numCond);
						
						
						
					}
					
				}
			}


		}

		
		
		
		if (solution.isEmpty() == true) {
			System.out.println("There are no tuples that meet all conditions");
		}
		
		
		return solution;
		}
	
	@Override
	public void detailedHelpMessage() {
		System.out.println("Usage: <temprelname> = SELECT relname WHERE <list of conditions>");
		System.out.println("Example: C = SELECT COURSE;");
		System.out.println("Example: T1 = SELECT OFFERING WHERE CID = CSCI241 AND SECTION > 27922;");
		System.out.println("Example: PRE = SELECT PREREQ WHERE CNBR = CSCI241 or CNBR = CSCI145 and PNBR != CSCI141;");
		System.out.println("SELECT creates a temporary relation containing tuples of a relation that "
				+ "satisfy the conditions stated in the WHERE clause. Conditions accepted are: \n"
				+ "'=', '!=', '<', '<=', '>', '>=' \n"
				+ "SELECT can handle multiple conditions. If no WHERE clause is stated, then "
				+ "no conditions are stated and the relation is projected with a temporary name. "
				+ "The number of conditions are tracked by the number of instances of 'OR' and 'AND'."
				+ "The temporary relation that is finally projected is not stored or added to the database.");
	}
	@Override
	public String simpleHelpMessage() {
		return "Displays a temporary relation containing all tuples that meet the WHERE clause conditions";
	}
}