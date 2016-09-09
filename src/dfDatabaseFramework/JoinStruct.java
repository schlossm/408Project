package dfDatabaseFramework;

class JoinStruct 
{
	public DFSQLConjunctionClause joinType;
	public String table;
	public DFSQLClauseStruct clause;
	
	public JoinStruct(DFSQLConjunctionClause type, String table, DFSQLClauseStruct clause)
	{
		this.joinType = type;
		this.table = table;
		this.clause = clause;
	}
}
