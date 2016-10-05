package database.dfDatabaseFramework.DFSQL;

/**
 * The various clauses to perform Joins and Where Statements
 */

public enum DFSQLConjunctionClause
{
	/**
	 * Used for JOIN and WHERE
	 */
	and, or, none,

	/**
	 * Used for WHERE
	 */
	equals, notEquals, greaterThan, lessThan, greaterThanOrEqualTo, lessThanOrEqualTo,

	/**
	 * Used for WHERE
	 */
	leftOuter, rightOuter, fullOuter, cross, inner, natural
}