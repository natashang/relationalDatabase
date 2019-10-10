import java.util.LinkedList;
import java.util.ArrayList;


public class JoinCommand implements Command{

  @Override
  public void execute(String[] inputArray)
  {
    String[] input = new String[inputArray.length];
    // remove comma characters from input
    for(int i = 0; i< inputArray.length; i++){
      input[i] = inputArray[i].replaceAll("[^a-zA-Z0-9=]", "");
    }

      join(input);

  }

  private void join(String[] input){
    Relation relationA = null;
    Relation relationB = null;
    String column1 = "";
    String column2 = "";

    // check for valid input length
    if (input.length != 9)
    {
    	System.out.println("Join command not formatted correctly. Aborting...");
    	return;
    }
    // get relation
    LinkedList<Relation> relationList = Database.getRelations();
			for(Relation r: relationList){
				if(r.getName().equalsIgnoreCase(input[3]) && relationA == null && (!r.isTemp())){
					relationA = r;
				}
        if(r.getName().equalsIgnoreCase(input[4]) && relationB == null && (!r.isTemp())){
          relationB = r;
        }
			}

      if(relationA == null || relationB == null){
        System.out.println("Relation not found");
        return;
      }

      LinkedList<Tuple> tupleListA = relationA.getTupleList();
      LinkedList<Tuple> tupleListB = relationB.getTupleList();

      //get the columns that will be used to merge
      try
      {
      for(int i = 5; i< input.length; i++){
          if(input[i].equalsIgnoreCase("on")){
            column1 = input[i+1];
            column2 = input[i+3];
            break;
          }
      }
      }
      catch (ArrayIndexOutOfBoundsException e)
      {
    	  System.out.println("Invalid input. Aborting...");
    	  return;
      }

      int index = 0;
      int tempCount = 0;
      //merge attribute labels of a and b
      String[] attributeLabelsA = relationA.getAttributeLabels();
      String[] attributeLabelsB = relationB.getAttributeLabels();
      String[] returnRelationAL = new String[attributeLabelsA.length + attributeLabelsB.length - 1];
      for(int i = 0; i< attributeLabelsA.length; i++){
        returnRelationAL[tempCount] = attributeLabelsA[i];
        tempCount++;
      }
		try
		{
			for (int i = 0; i < attributeLabelsB.length; i++)
			{
				if (attributeLabelsB[i].equals(column2))
				{
					index = i;
					continue;
				}
				returnRelationAL[tempCount] = attributeLabelsB[i];
				tempCount++;
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			System.out.println("You entered a parameter that has no matches. Aborting...");
			return;
		}

      tempCount = 0;
      //merge format of a and b
      String[] formatA = relationA.getFormat();
      String[] formatB = relationB.getFormat();
      String[] returnRelationF = new String[formatA.length + formatB.length - 2];
      for(int i = 0; i< formatA.length; i++){
        returnRelationF[tempCount] = formatA[i];
        tempCount++;
      }
      for(int i = 0; i< formatB.length; i+=1){
        if(i == index){
            i+=1;
           continue;
        }
        returnRelationF[tempCount] = formatB[i];
        tempCount++;
      }

      Database.createNewTempRelation(input[0].toUpperCase(), returnRelationAL, returnRelationF, true);
      Relation returnRelation = null;
      for(Relation r: Database.getRelations()){
        if(r.getName().equalsIgnoreCase(input[0])){
          returnRelation = r;
          break;
        }
      }

      for(Tuple tuple: tupleListA){
          for(String[] mergedTuple: mergeEachTuple(tuple, tupleListB, column1, column2, attributeLabelsA, attributeLabelsB)){
            // get the relation from the Database
                returnRelation.silentInsertTuple(mergedTuple);

          }
      }
      returnRelation.print();
  }

  private LinkedList<String[]> mergeEachTuple(Tuple tuple, LinkedList<Tuple> tupleListB, String column1, String column2, String[] attributeLabelsA, String[] attributeLabelsB){
    LinkedList<String[]> returnList = new LinkedList<String[]>();
    ArrayList<String> returnListAttributes = new ArrayList<String>();
    int temp;
    LinkedList<Attribute> attributeListA = tuple.getAttributeList();
    LinkedList<Attribute> attributeListB;
    String c1OfTuple = "";

    // get the value of tuple from A that will be compared to all of those from B
    for(temp = 0; temp < attributeLabelsA.length; temp++){
		  if(attributeLabelsA[temp].equalsIgnoreCase(column1)){
				c1OfTuple = attributeListA.get(temp).getValue();
				break;
			}
		}

    // create a default array of strings of attribute from the tuple from A
    ArrayList<String> defaultAttributeList = new ArrayList<String>();
    for(int i = 0; i< attributeListA.size(); i++){
      defaultAttributeList.add(attributeListA.get(i).getValue());
    }

    // compare all values of c1OfTuple to values of all the column 2 from B
    for(Tuple t: tupleListB){
      attributeListB = t.getAttributeList();

      // get index of column 2 that is temp
      for(temp = 0; temp < attributeListB.size(); temp++){
        if(attributeLabelsB[temp].equalsIgnoreCase(column2)){
          break;
        }
      }

      if(attributeListB.get(temp).getValue().equals(c1OfTuple)){
        // copy everything from the default to the returnListAttributes
        returnListAttributes.clear();
        for(String s: defaultAttributeList){
          returnListAttributes.add(s);
        }

        // store the attributes in an array list that will be converted to array of string later
        // which will be used to call constructor of tuple
        for(Attribute a: attributeListB){
          if(a.getValue().equals(c1OfTuple)){
            continue;
          }
          returnListAttributes.add(a.getValue());
        }

        returnList.add(returnListAttributes.toArray(new String[returnListAttributes.size()]));
      }
    }

    return returnList;
  }


  @Override
  public void detailedHelpMessage()
  {
    System.out.println("USAGE: temprelationname = JOIN relationAname, relationBname ON <join condition>; ");
		System.out.println("Example: J = JOIN COURSE, PREREQ ON CNUM = PNBR;");
		System.out.println("Return combined tuples from the 2 mentioned relations that satisfy specifed condtiion(s)");
  }

  @Override
  public String simpleHelpMessage()
  {
    return "Return combined tuples from the 2 mentioned relations that satisfy specifed condtiion(s)";
  }

}
