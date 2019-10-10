import java.util.LinkedList;

public class Relation{
	
	// data fields
	protected String name;
	private LinkedList<Tuple> tupleList;
	private String[] attributeLabels;
	private String[] format;
	private boolean isTemp;

	// constructor
	public Relation(String name, String[] labels, String[] format){
		this.name = name;
		tupleList = new LinkedList<Tuple>();
		attributeLabels = labels;
		this.format = format;
		this.isTemp = false;
	}
	
	// constructor for temp relation
	// the isTemp in this constructor is kind of sloppy since you can put true or
	//	false and it will all work, but the goal here is to make it difference from
	// 	the constructor of the normal relation
	public Relation(String name, String[] labels, String[] format, boolean isTemp){
		this.name = name;
		tupleList = new LinkedList<Tuple>();
		attributeLabels = labels;
		this.format = format;
		this.isTemp = true;
	}
	
	// getters
	public String getName(){
		return this.name;
	}
	public LinkedList<Tuple> getTupleList(){
		return this.tupleList;
	}
	public String[] getAttributeLabels(){
		return this.attributeLabels;
	}
	public String[] getFormat(){
		return this.format;
	}
	public boolean isTemp(){
		return this.isTemp;
	}
	public void makeTemp(){
		this.isTemp = true;
	}
	
	// Inserts new tuple into the LinkedList of tuples.
	public void insertTuple(String[] value){
		int i;
		int maxLength = 0;
		
		//Handle value size check
		if(value.length != attributeLabels.length)
		{
			System.out.println("Parameter count mis-match. Aborting insert.");
			return;
		}
		
		//NEW//Check that values follow format. Truncate if too long (right if CHAR, left if NUM); reject insert if format not followed.
		for (i = 0; i < value.length; i++) {
			maxLength = Integer.parseInt(format[2 * i + 1]);
			if (format[2 * i].equals("NUM")) {
				try {
					Float.parseFloat(value[i]);
					if (value[i].length() > maxLength) {
						value[i] = value[i].substring(value[i].length() - maxLength);
						System.out.println("Number value too long. Value truncated.");
					}
				} 
				catch (NumberFormatException e) {
					System.out.println("Insert does not follow table format. Aborting Insert");
					return;
				}
			} 
			else {
				if (value[i].length() > maxLength) {
					value[i] = value[i].substring(0, maxLength);
					System.out.println("Char value too long. Value truncated.");
				}
			}
		}
		for (i = 0; i < value.length; i++) {
			maxLength = Integer.parseInt(format[2*i+1]);
			if(value[i].length() > maxLength){
				value[i] = value[i].substring(0,maxLength);
			}
		}
		tupleList.add(new Tuple(value));
		System.out.println("Insertion successful.");
	}
	
	public void silentInsertTuple(String[] value)
	{
		int i;
		int maxLength = 0;
		
		//Handle value size check
		if(value.length != attributeLabels.length)
		{
			return;
		}
		
		//NEW//Check that values follow format. Truncate if too long (right if CHAR, left if NUM); reject insert if format not followed.
		for (i = 0; i < value.length; i++) {
			maxLength = Integer.parseInt(format[2 * i + 1]);
			if (format[2 * i].equals("NUM")) {
				try {
					Float.parseFloat(value[i]);
					if (value[i].length() > maxLength) {
						value[i] = value[i].substring(value[i].length() - maxLength);
	
					}
				} 
				catch (NumberFormatException e) {

					return;
				}
			} 
			else {
				if (value[i].length() > maxLength) {
					value[i] = value[i].substring(0, maxLength);

				}
			}
		}
		for (i = 0; i < value.length; i++) {
			maxLength = Integer.parseInt(format[2*i+1]);
			if(value[i].length() > maxLength){
				value[i] = value[i].substring(0,maxLength);
			}
		}
		tupleList.add(new Tuple(value));
	}
	
	// Prints a formatted ASCII table of the relation
	public void print(){
		int[] formatter = new int[this.format.length / 2];
		int titleSpacing = 0;
		
		//Determine max space needed
		for (int i = 0; i<formatter.length; i++)
		{
			if(Integer.parseInt(format[i*2+1]) >= attributeLabels[i].length())
			{
				formatter[i]=Integer.parseInt(format[i*2+1]);
			}
			else
			{
				formatter[i] = attributeLabels[i].length();
			}
			titleSpacing += formatter[i];
		}
		titleSpacing += attributeLabels.length*2 + attributeLabels.length - 1;
		
		//Print relation
		System.out.println();
		PrintHelper.printBoundedLine("+", "+", "-", titleSpacing);
		PrintHelper.PrintTitle(this.name, titleSpacing);
		PrintHelper.printBoundedLine("+", "+", "=", titleSpacing);
		PrintHelper.PrintAttributeLine(this.attributeLabels, formatter);
		PrintHelper.printBoundedLine("+", "+", "=", titleSpacing);
		PrintHelper.PrintAllTuples(tupleList, formatter);
		PrintHelper.printBoundedLine("+", "+", "-", titleSpacing);
		System.out.print("\n");
	}
}