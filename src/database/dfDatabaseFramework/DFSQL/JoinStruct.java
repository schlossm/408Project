package database.dfDatabaseFramework.DFSQL;

class JoinStruct 
{
	DFSQLConjunctionClause joinType;
	String table;
	DFSQLClauseStruct clause;
	
	JoinStruct(DFSQLConjunctionClause type, String table, DFSQLClauseStruct clause)
	{
		this.joinType = type;
		this.table = table;
		this.clause = clause;
	}
}
