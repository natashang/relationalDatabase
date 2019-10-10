/*
 * Tuple object class
 * A tuple consists of a linkedList of type Attribute
 */

import java.util.LinkedList;

public class Tuple {

	// Data fields
	private LinkedList <Attribute> attributeList;

	// Constructor
	public Tuple(String[] attributeValues) {
		
		attributeList = new LinkedList<Attribute>();
		
		for (int i = 0; i < attributeValues.length; i++) {	
			attributeList.add(new Attribute(attributeValues[i]));
		}	
	}
	
	// Getter
	public LinkedList<Attribute> getAttributeList(){
		return this.attributeList;
	}
}
