import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;

public class DestroyTuple implements Command{

	@Override
	public void execute(String[] inputArray){
		if(inputArray.length< 2){
			return;
		}
		String temp = "";
		String[] input = new String[inputArray.length];
		// remove the space all space characters from input
		for(int i = 0; i< inputArray.length; i++){
			input[i] = inputArray[i].replaceAll("[^a-zA-Z0-9=<>]", "");
		}

		destroyTuple(parseInput(input));

	}


	private boolean destroyTuple(String[] input){

		Relation relation = null;
		LinkedList<Tuple> tupleList = null;

		// get relation
		LinkedList<Relation> relationList = Database.getRelations();
			for(Relation r: relationList){
				if(input[1].equalsIgnoreCase(r.getName()) && (!r.isTemp())){
					relation = r;
					break;
				}
			}


		if (relation == null){
			System.out.println("Relation not found");
			return false;
		}

		tupleList = relation.getTupleList();

		// create substrings separated by or

		// if no condition specified
		if(input.length<3){
			tupleList.clear();
			return true;
		}

		ArrayList<String> current = new ArrayList<String>();
		int currentIndex = 0;
		LinkedList<String[]> orList = new LinkedList<String[]>();
		// normal cases where there is at least 1 condition
		for(int i = 2; i< input.length; i++){
			if(input[i].equalsIgnoreCase("or")){
				orList.add(current.toArray(new String[current.size()]));
				current.clear();
			}
			else if(input[i].equalsIgnoreCase("and")){
				continue;
			}
			else{
				current.add(input[i]);
			}
		}

		orList.add(current.toArray(new String[current.size()]));

		for(int i = 0; i < tupleList.size(); i++){
			if(compareEachTuple(tupleList.get(i), orList, relation.getAttributeLabels())){
				tupleList.remove(i);
				i--;
			}
		}

		return true;
	}


	private boolean compareEachTuple(Tuple tuple, LinkedList<String[]> condition, String[] attributeLabels){
		LinkedList<Attribute> attributeList = tuple.getAttributeList();
		int temp;
		boolean tempResult;
		boolean result = false;
		String[] currentSubCondition;
		String currentAttribute = "	";
		// each sub condition separated by or
	 	for(int i = 0; i < condition.size(); i++){
	 		tempResult = true;
	 		currentSubCondition = condition.get(i);

	 		for(int j = 0; j< currentSubCondition.length; j+=3){
				// remove space at the end
				if(currentSubCondition[j+2].charAt(currentSubCondition[j+2].length() -1) == ' '){
					currentSubCondition[j+2] = currentSubCondition[j+2].substring(0,currentSubCondition[j+2].length() -1);
				}

				for(temp = 0; temp < attributeLabels.length; temp++){
					if(attributeLabels[temp].equals(currentSubCondition[j])){
						currentAttribute = attributeList.get(temp).getValue();
						break;
					}
				}

				if(currentSubCondition[j+1].equals("=")){
	 				if(!currentAttribute.equals(currentSubCondition[j+2])){
	 					tempResult = false;
						// break;
	 				}
				}
				else if(currentSubCondition[j+1].equals("!=")){
	 				if(currentAttribute.equals(currentSubCondition[j+2])){
	 					tempResult = false;
						// 	break;
	 				}
				}
				else if(currentSubCondition[j+1].equals(">=")){
					if(Double.parseDouble(currentAttribute) < Double.parseDouble(currentSubCondition[j+2])){
						tempResult = false;
						// break;
					}
				}
				else if(currentSubCondition[j+1].equals("<=")){
	 				if(Double.parseDouble(currentAttribute) > Double.parseDouble(currentSubCondition[j+2])){
	 					tempResult = false;
						// 	break;
	 				}
				}
				else if(currentSubCondition[j+1].equals("<")){
	 				if(Double.parseDouble(currentAttribute) >= Double.parseDouble(currentSubCondition[j+2])){
	 					tempResult = false;
						// 	break;
	 				}
				}
				else if(currentSubCondition[j+1].equals(">")){
	 				if(Double.parseDouble(currentAttribute) <= Double.parseDouble(currentSubCondition[j+2])){
	 					tempResult = false;
						// 	break;
	 				}
				}
	 		}
	 		result = result || tempResult;
	 	}

	 	return result;
	}


	private String[] parseInput(String[] input){
		ArrayList<String> inputList = new ArrayList<String>();
		String tempBuffer = "";
		inputList.add(input[0]);

		// combine all elements that is relation name
		int current = 1;
		while(current < input.length){

			if(input[current].toLowerCase().equals("where")){
					tempBuffer = tempBuffer.substring(0, tempBuffer.length()-1);
					break;
			}

			tempBuffer = tempBuffer.concat(input[current]);
			tempBuffer = tempBuffer.concat(" ");
			current++;
		}

		inputList.add(tempBuffer);

		//current is pointing at "where" move it to the next one
		if(current < (input.length-1)){

			current++;
			tempBuffer = "";
		}
		else{
			return inputList.toArray(new String[inputList.size()]);
		}

		for(int i = current; i< input.length; i++){

			if(input[i].equals("=") || input[i].equals("!=") || input[i].equals("<=") || input[i].equals(">=") || input[i].equals("<") || input[i].equals(">") || input[i].equalsIgnoreCase("and") || input[i].equalsIgnoreCase("or")){
				// remove the space at the end
				tempBuffer = tempBuffer.substring(0, tempBuffer.length() - 1);
				inputList.add(tempBuffer);
				tempBuffer = "";
				inputList.add(input[i]);
			}
			else{
				tempBuffer = tempBuffer.concat(input[i]);
				tempBuffer = tempBuffer.concat(" ");
			}
		}
		//add the last element to the list
		inputList.add(tempBuffer);


		return inputList.toArray(new String[inputList.size()]);
	}


	@Override
	public void detailedHelpMessage(){
		System.out.println("USAGE: DELETE relationname WHERE <conditions> ");
		System.out.println("Example: DELETE OFFERING WHERE CID = CSCI241 and SECTION > 27922;");
		System.out.println("Delete tuple(s) that satisfy specifed condtiion from given relation");
	}

	@Override
	public String simpleHelpMessage(){
		return "Delete tuple(s) that satisfy specifed condtiion from given relation";
	}

}