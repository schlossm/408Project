package dfDatabaseFramework.DFSQL;

public class JoinParam
{
	String table;
	String leftHandSide;
	String rightHandSide;
	
	public JoinParam(String table, String leftHandSide, String rightHandSide)
	{
		this.table = table;
		this.leftHandSide = leftHandSide;
		this.rightHandSide = rightHandSide;
	}
}
