package database.dfDatabaseFramework.DFSQL;

/**
 * A class for holding a generic clause
 */
@SuppressWarnings("WeakerAccess")
public class DFSQLClauseStruct
{
	final String leftHandSide;
	final String rightHandSide;
	
	public DFSQLClauseStruct(String left, String right)
	{
		leftHandSide = left;
		rightHandSide = right;
	}
}
