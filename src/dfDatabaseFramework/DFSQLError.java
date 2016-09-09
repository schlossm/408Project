package dfDatabaseFramework;
import java.lang.Exception;

@SuppressWarnings("serial")
public class DFSQLError extends Exception
{
	public static DFSQLError conditionAlreadyExists 		= new DFSQLError("Condition Already Exists");
	public static DFSQLError conditionNotOfEquivelentSize	= new DFSQLError("Conditions Not of Equivelent Size");
	public static DFSQLError cannotUseAllRowsSQLSpecifier 	= new DFSQLError("Cannot Use All Rows SQL Specifier");
	public static DFSQLError cannotUseEmptyValue 			= new DFSQLError("Cannot Use Empty Value");
	public static DFSQLError lengthTooLong 					= new DFSQLError("Length Too Long");
	public static DFSQLError conditionsMustBeEqual 			= new DFSQLError("Conditions Must Be Equal");
	
	public DFSQLError(String message) 
	{
        super(message);
    }
	
	public DFSQLError(String message, Throwable throwable) 
	{
        super(message, throwable);
    }
}
