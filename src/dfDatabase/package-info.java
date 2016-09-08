/**
 * 
 */
/**
 * @author michaelschloss
 *
 */
package dfDatabase;

///SQL class errors
enum MSSQLError
{
	conditionAlreadyExists, conditionNotOfEquivelentSize, cannotUseAllRowsSQLSpecifier, cannotUseEmptyValue, lengthTooLong, conditionsMustBeEqual;
}

///The various clauses to perform Joins and Where Statements
enum MSSQLConjunctionClause
{
	and, or, none, equals, notEquals, greaterThan, lessThan, greaterThanOrEqualTo, lessThanOrEqualTo,
	leftOuter, rightOuter, fullOuter, cross, inner, natural;
}