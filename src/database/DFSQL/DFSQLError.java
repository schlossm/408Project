package database.DFSQL;

/**
 * The Exception class for DFSQL
 */
@SuppressWarnings({"serial", "ThrowableInstanceNeverThrown"})
public class DFSQLError extends Exception
{
	/**
	 * Specifies that the condition has already been created
	 */
	static final DFSQLError conditionAlreadyExists			    = new DFSQLError("Condition Already Exists");

	/**
	 * Specifies that `*` was attempted to be used in the SQL statement
	 */
	static final DFSQLError cannotUseAllRowsSQLSpecifier 	    = new DFSQLError("Cannot Use All Rows SQL Specifier");

	/**
	 * Specifies that the method was delivered "" or null
	 */
	static final DFSQLError cannotUseEmptyValue 			    = new DFSQLError("Cannot Use Empty Value");

	/**
	 * Specifies that the parameter exceeded 64 characters length
	 */
	static final DFSQLError lengthTooLong 					    = new DFSQLError("Length Too Long");

	/**
	 * Specifies that the conditions given do not equal each other in size
	 */
	static final DFSQLError conditionsMustBeEqual 			    = new DFSQLError("Conditions Must Be Equal");

	/**
	 * Specifies that the DFSQLConjunctionClause enum given doesn't match expected
	 */
	static final DFSQLError DFSQLConjunctionClauseDoesntMatch   = new DFSQLError("DFSQL Conjunction Clause Doesn't Match What Is Expected");
	
	private DFSQLError(String message)
	{
        super(message);
    }
}
