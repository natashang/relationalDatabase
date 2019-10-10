
import java.util.*;

public class Database {

	private static LinkedList<Relation> relations = new LinkedList<Relation>();

	// Accessor
	public static LinkedList<Relation> getRelations () {
		return relations;
	}

	// Inserts a new relation in the list of relations
	public static void createNewRelation(String name, String[] columnLabels, String[] format ) {
		deleteRelationIfItExists(name);
		relations.add(new Relation(name, columnLabels, format));
	}

	public static void createNewTempRelation(String name, String[] columnLabels, String[] format , boolean bool) {
		deleteRelationIfItExists(name);
		relations.add(new Relation(name, columnLabels, format, bool));
	}

	// Adds a tuple to the relevant relation's list of tuples
	public static void insertTupleIntoRelation(Relation relation, String[] tupleData) {
		relation.insertTuple(tupleData);
	}

	// Calls the print function for any relevant relation(s)
	public static void printRelation (LinkedList<Relation> tablesToPrint) {
		//Iterates through list of tables indicated by the print command input.
		for (Relation relation : tablesToPrint)
		{
			relation.print();
		}
	}

	private static void deleteRelationIfItExists(String newRelationLabel)
	{
		for (Relation relation : relations)
		{
			if (relation.name.equals(newRelationLabel))
			{
				removeRelation(relation);
			}
		}
	}

	public static void removeRelation(Relation relationToDestroy)
	{
		relations.remove(relationToDestroy);
		System.out.println("\"" + relationToDestroy.getName() + "\"" + " has been destroyed.");
	}
	
	public static void addInstantiatedRelation (Relation relation)
	{
		relations.add(relation);
	}
}