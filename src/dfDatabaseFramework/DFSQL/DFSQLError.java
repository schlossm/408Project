package dfDatabaseFramework.DFSQL;
import java.lang.Exception;

@SuppressWarnings("serial")
public class DFSQLError extends Exception
{
	static final DFSQLError conditionAlreadyExists			= new DFSQLError("Condition Already Exists");
	static final DFSQLError conditionNotOfEquivelentSize	= new DFSQLError("Conditions Not of Equivelent Size");
	static final DFSQLError cannotUseAllRowsSQLSpecifier 	= new DFSQLError("Cannot Use All Rows SQL Specifier");
	static final DFSQLError cannotUseEmptyValue 			= new DFSQLError("Cannot Use Empty Value");
	static final DFSQLError lengthTooLong 					= new DFSQLError("Length Too Long");
	static final DFSQLError conditionsMustBeEqual 			= new DFSQLError("Conditions Must Be Equal");
	
	DFSQLError(String message) 
	{
        super(message);
    }
	
	DFSQLError(String message, Throwable throwable) 
	{
        super(message, throwable);
    }
}
