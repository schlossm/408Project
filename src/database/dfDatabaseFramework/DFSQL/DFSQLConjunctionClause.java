package database.dfDatabaseFramework.DFSQL;

///The various clauses to perform Joins and Where Statements
public enum DFSQLConjunctionClause
{
	and, or, none, equals, notEquals, greaterThan, lessThan, greaterThanOrEqualTo, lessThanOrEqualTo,
	leftOuter, rightOuter, fullOuter, cross, inner, natural;
}