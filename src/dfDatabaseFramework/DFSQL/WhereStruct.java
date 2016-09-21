package dfDatabaseFramework.DFSQL;

public class WhereStruct 
{
	DFSQLConjunctionClause conjunction;
	DFSQLConjunctionClause joiner;
	DFSQLClauseStruct clause;
	
	public WhereStruct(DFSQLConjunctionClause conjunction, DFSQLConjunctionClause joiner, DFSQLClauseStruct clause)
	{
		this.conjunction= conjunction;
		this.joiner = joiner;
		this.clause = clause;
	}
}
