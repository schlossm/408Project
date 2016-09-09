package dfDatabaseFramework;

public class WhereStruct 
{
	public DFSQLConjunctionClause conjunction;
	public DFSQLConjunctionClause joiner;
	public DFSQLClauseStruct clause;
	
	public WhereStruct(DFSQLConjunctionClause conjunction, DFSQLConjunctionClause joiner, DFSQLClauseStruct clause)
	{
		this.conjunction= conjunction;
		this.joiner = joiner;
		this.clause = clause;
	}
}
