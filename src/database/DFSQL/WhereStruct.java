package database.DFSQL;

/**
 * The struct that defines a WHERE statement clause
 */
@SuppressWarnings("WeakerAccess") public class WhereStruct
{
	final DFSQLConjunctionClause conjunction;
	final DFSQLConjunctionClause joiner;
	final DFSQLClauseStruct clause;
	
	public WhereStruct(DFSQLConjunctionClause conjunction, DFSQLConjunctionClause joiner, DFSQLClauseStruct clause)
	{
		this.conjunction= conjunction;
		this.joiner = joiner;
		this.clause = clause;
	}
}
