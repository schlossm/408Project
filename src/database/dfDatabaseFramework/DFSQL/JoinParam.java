package database.dfDatabaseFramework.DFSQL;

public class JoinParam
{
	final String table;
	final String leftHandSide;
	final String rightHandSide;
	
	@SuppressWarnings("unused")
	public JoinParam(String table, String leftHandSide, String rightHandSide)
	{
		this.table = table;
		this.leftHandSide = leftHandSide;
		this.rightHandSide = rightHandSide;
	}
}
