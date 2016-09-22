package database.dfDatabaseFramework.DFSQL;
import database.dfDatabaseFramework.DFSQL.JoinStruct;
import database.dfDatabaseFramework.DFSQL.WhereStruct;
import database.dfDatabaseFramework.DFSQL.DFSQLClauseStruct;
import java.util.*;

public class DFSQL 
{	
    private String[] selectRows;
    private String[] fromTables;
    private ArrayList<JoinStruct> joinStatements;
    private ArrayList<WhereStruct> whereStatements;
    
    private String[] insertRows;
    private String[] insertData;
    
    private DFSQLClauseStruct[] updateStatements;
    private ArrayList<DFSQL> appendedDFSQL;
    
    public DFSQL() { }
    
    public DFSQL append(DFSQL object)
    {
    	appendedDFSQL.add(object);
        
        return this;
    }
    
    public final String formattedSQLStatement()
    {
    	String returnString = "";
    	if (updateStatements != null)	//This will be an UPDATE SET
    	{
    		if (fromTables == null)		{ return ""; }
    		if (fromTables.length != 1)	{ return ""; }
    		
    		returnString = "UPDATE `" + fromTables[0] + "` SET ";
    		
    		for (DFSQLClauseStruct statement : updateStatements)
    		{
    			String left = statement.leftHandSide;
    			String right = statement.rightHandSide;
    			
    			if (right.contains(" ") || hasCharacter(right))
    			{
    				right = "'" + right + "'";
    			}
    			
    			returnString += "`" + left + "`=" + right + ", ";
    		}
    		
    		returnString = returnString.substring(0, returnString.length() - 2) + ";";
    		System.out.println(returnString);
    		return returnString;
    	}
    	
    	if (insertRows != null)
    	{
    		if (fromTables == null)		{ return ""; }
    		if (fromTables.length != 1)	{ return ""; }
    		
    		returnString = "INSERT INTO `" + fromTables[0] + "`(";
    		for (String row : insertRows)
    		{
    			returnString += "`" + row + "`,";
    		}
    		returnString = returnString.substring(0, returnString.length() - 1) + ") VALUES (";
    		for (String value : insertData)
    		{
    			if (value.contains(" ") || hasCharacter(value))
    			{
    				returnString += "'" + value + "',";
    			}
    			else
    			{
    				returnString += value + ",";
    			}
    		}
    		returnString = returnString.substring(0, returnString.length() - 1) + ");";
    		return returnString;
    	}
    	
    	if (selectRows == null) return ""; //We're assuming all that's left is SELECT statements
    	
    	returnString = "SELECT ";
    	for (String row : selectRows)
    	{
    		returnString += "`" + row + "`,";
    	}
    	returnString = returnString.substring(0, returnString.length() - 1) + " FROM ";
    	for (String table : fromTables)
    	{
    		returnString += "`" + table + "`,";
    	}
    	returnString = returnString.substring(0, returnString.length() - 1);
    	
    	if (joinStatements != null)
    	{
    		for (JoinStruct joinStatement : joinStatements)
    		{
    			String left = joinStatement.clause.leftHandSide;
    			String right = joinStatement.clause.rightHandSide;
    			
    			if (right.contains(" "))
    			{
    				right = "'" + right + "'";
    			}
    			
    			//leftOuter, rightOuter, fullOuter, cross, inner, natural
    			if (joinStatement.joinType == DFSQLConjunctionClause.natural)
    			{
    				returnString += " NATURAL JOIN `" + joinStatement.table + "` ON `" + left + "`=" + right;
    			}
    			else if (joinStatement.joinType == DFSQLConjunctionClause.leftOuter)
    			{
    				returnString += " LEFT OUTER JOIN `" + joinStatement.table + "` ON `" + left + "`=" + right;
    			}
    			else if (joinStatement.joinType == DFSQLConjunctionClause.rightOuter)
    			{
    				returnString += " RIGHT OUTER JOIN `" + joinStatement.table + "` ON `" + left + "`=" + right;
    			}
    			else if (joinStatement.joinType == DFSQLConjunctionClause.fullOuter)
    			{
    				returnString += " FULL OUTER JOIN `" + joinStatement.table + "` ON `" + left + "`=" + right;
    			}
    			else if (joinStatement.joinType == DFSQLConjunctionClause.cross)
    			{
    				returnString += " CROSS JOIN `" + joinStatement.table + "` ON `" + left + "`=" + right;
    			}
    			else if (joinStatement.joinType == DFSQLConjunctionClause.inner)
    			{
    				returnString += " INNER JOIN `" + joinStatement.table + "` ON `" + left + "`=" + right;
    			}
    		}
    	}
    	
    	if (whereStatements != null)
    	{
    		returnString += " WHERE";
    		for (WhereStruct whereStatement : whereStatements)
    		{
    			String left = whereStatement.clause.leftHandSide;
    			String right = whereStatement.clause.rightHandSide;
    			
    			if (right.contains(" ") || hasCharacter(right))
    			{
    				right = "'" + right + "'";
    			}
    			String joiner = "";
				if (whereStatement.joiner == DFSQLConjunctionClause.equals)
				{
					joiner = "=";
				}
				else if (whereStatement.joiner == DFSQLConjunctionClause.notEquals)
				{
					joiner = "!=";
				}
				else if (whereStatement.joiner == DFSQLConjunctionClause.greaterThan)
				{
					joiner = ">";
				}
				else if (whereStatement.joiner == DFSQLConjunctionClause.greaterThanOrEqualTo)
				{
					joiner = ">=";
				}
				else if (whereStatement.joiner == DFSQLConjunctionClause.lessThan)
				{
					joiner = "<";
				}
				else if (whereStatement.joiner == DFSQLConjunctionClause.lessThanOrEqualTo)
				{
					joiner = "<=";
				}
				
    			if (whereStatement.conjunction == DFSQLConjunctionClause.none)
    			{
    				returnString += " `" + left + "`" + joiner + right;
    			}
    			else
    			{
    				String conjunction = "";
    				if (whereStatement.conjunction == DFSQLConjunctionClause.and)
    				{
    					conjunction = " AND";
    				}
    				else if (whereStatement.conjunction == DFSQLConjunctionClause.or)
    				{
    					conjunction = " OR";
    				}
    				
    				returnString += " `" + left + "`" + joiner + right + conjunction;
    			}
    		}
    	}
    	
    	returnString += ";";
    	return returnString;
    }
    
    private boolean hasCharacter(String string)
    {
    	String lowered = string.toLowerCase();
    	if (lowered.contains("a") || lowered.contains("b") || lowered.contains("c") ||
    			lowered.contains("d") || lowered.contains("e") || lowered.contains("f") ||
    			lowered.contains("g") || lowered.contains("h") || lowered.contains("i") ||
    			lowered.contains("j") || lowered.contains("k") || lowered.contains("l") ||
    			lowered.contains("m") || lowered.contains("n") || lowered.contains("o") ||
    			lowered.contains("p") || lowered.contains("q") || lowered.contains("r") ||
    			lowered.contains("s") || lowered.contains("t") || lowered.contains("u") ||
    			lowered.contains("v") || lowered.contains("w") || lowered.contains("x") ||
    			lowered.contains("y") || lowered.contains("z"))
    	{
    		return true;
    	}
    	return false;	
    }
    
    //MARK: - SELECT Constructors
    
    public DFSQL select(String row) throws DFSQLError
    {
        if (selectRows != null)         { throw DFSQLError.conditionAlreadyExists; }
        if (row.contains("*")) 		   	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (row == "")               	{ throw DFSQLError.cannotUseEmptyValue; }
        if (row.length() > 64)			{ throw DFSQLError.lengthTooLong; }
        
        selectRows = new String[] { row };
        
        return this;
    }
    
    public DFSQL select(String[] rows) throws DFSQLError
    {
        if (selectRows != null)	{ throw DFSQLError.conditionAlreadyExists; }
        
        for (String row : rows)
        {
            if (row.contains("*"))	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (row == "")         	{ throw DFSQLError.cannotUseEmptyValue; }
            if (row.length() > 64)	{ throw DFSQLError.lengthTooLong; }
        }
        
        selectRows = rows;
        
        return this;
    }
    
    //MARK: - FROM Constructors
    
    public DFSQL from(String table) throws DFSQLError
    {
        if (fromTables == null); else           { throw DFSQLError.conditionAlreadyExists; }
        if (table.contains("*") == false); else	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (table != ""); else               	{ throw DFSQLError.cannotUseEmptyValue; }
        if (table.length() <= 64); else 		{ throw DFSQLError.lengthTooLong; }
        
        fromTables = new String[] {table};
        
        return this;
    }
    
    public DFSQL from(String[] tables) throws DFSQLError
    {
        if (fromTables == null); else					{ throw DFSQLError.conditionAlreadyExists; }
        
        for (String table : tables)
        {
            if (table.contains("*") == false); else	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (table != ""); else                  { throw DFSQLError.cannotUseEmptyValue; }
            if (table.length() <= 64); else 		{ throw DFSQLError.lengthTooLong; }
        }
        
        fromTables = tables;
        
        return this;
    }
    
    //MARK: - UPDATE SET Constructor
    
    public DFSQL update(String table, String leftHandSide, String rightHandSide) throws DFSQLError
    {
        if (fromTables == null); else          	{ throw DFSQLError.conditionAlreadyExists; }
        if (table.contains("*") == false); else { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (table != ""); else                  { throw DFSQLError.cannotUseEmptyValue; }
        if (table.length() <= 64); else 		{ throw DFSQLError.lengthTooLong; }
        
        fromTables = new String[] {table};
        
        if (leftHandSide.contains("*") == false); else	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (leftHandSide != ""); else                	{ throw DFSQLError.cannotUseEmptyValue; }
        if (leftHandSide.length() <= 64); else  		{ throw DFSQLError.lengthTooLong; }
        
        if (rightHandSide.contains("*") == false); else { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (rightHandSide != ""); else                  { throw DFSQLError.cannotUseEmptyValue; }
        if (rightHandSide.length() <= 64); else 		{ throw DFSQLError.lengthTooLong; }
        
        updateStatements = new DFSQLClauseStruct[] {new DFSQLClauseStruct(leftHandSide, rightHandSide)};
        
        return this;
    }
    
    public DFSQL update(String table, DFSQLClauseStruct[] statements) throws DFSQLError
    {
        if (fromTables == null); else          	{ throw DFSQLError.conditionAlreadyExists; }
        if (table.contains("*") == false); else { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (table != ""); else                  { throw DFSQLError.cannotUseEmptyValue; }
        if (table.length() <= 64); else 		{ throw DFSQLError.lengthTooLong; }
        
        fromTables = new String[] {table};
        
        for (DFSQLClauseStruct statement : statements)
        {
            if (statement.leftHandSide.contains("*") == false); else  	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.leftHandSide != ""); else                   	{ throw DFSQLError.cannotUseEmptyValue; }
            if (statement.leftHandSide.length() <= 64); else  			{ throw DFSQLError.lengthTooLong; }
            
            if (statement.rightHandSide.contains("*") == false); else 	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.rightHandSide != ""); else                  	{ throw DFSQLError.cannotUseEmptyValue; }
            if (statement.rightHandSide.length() <= 64); else 			{ throw DFSQLError.lengthTooLong; }
        }
        
        updateStatements = statements;
        
        return this;
    }
    
    //MARK: - INSERT INTO Constructor
    
    public DFSQL insert(String table, String[] values, String[] rows) throws DFSQLError
    {
        if (fromTables == null); else           { throw DFSQLError.conditionAlreadyExists; }
        if (insertData == null); else           { throw DFSQLError.conditionAlreadyExists; }
        if (insertRows == null); else  			{ throw DFSQLError.conditionAlreadyExists; }
        
        if (table.contains("*") == false); else { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (table != ""); else                  { throw DFSQLError.cannotUseEmptyValue; }
        if (table.length() <= 64); else 		{ throw DFSQLError.lengthTooLong; }
        
        fromTables = new String[] {table};
        
        for (String value : values)
        {
            if (value.contains("*") == false); else	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (value != ""); else               	{ throw DFSQLError.cannotUseEmptyValue; }
            if (value.length() <= 64); else  		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (String row : rows)
        {
            if (row.contains("*") == false); else	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (row != ""); else                   	{ throw DFSQLError.cannotUseEmptyValue; }
            if (row.length() <= 64); else  			{ throw DFSQLError.lengthTooLong; }
        }
        
        insertData = values;
        insertRows = rows;
        
        return this;
    }
    
    //MARK: - JOIN ON Constructors
    
    public DFSQL joinOn(String table, String leftHandSide, String rightHandSide) throws DFSQLError
    {
        if (joinStatements == null); else               { throw DFSQLError.conditionAlreadyExists; }
        
        if (leftHandSide.contains("*") == false); else	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (leftHandSide != ""); else                   { throw DFSQLError.cannotUseEmptyValue; }
        if (leftHandSide.length() <= 64); else  		{ throw DFSQLError.lengthTooLong; }
        
        if (rightHandSide.contains("*") == false); else { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (rightHandSide != ""); else                  { throw DFSQLError.cannotUseEmptyValue; }
        if (rightHandSide.length() <= 64); else 		{ throw DFSQLError.lengthTooLong; }
        
        if (table.contains("*") == false); else         { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (table != ""); else                          { throw DFSQLError.cannotUseEmptyValue; }
        if (table.length() <= 64); else         		{ throw DFSQLError.lengthTooLong; }
        
        joinStatements = new ArrayList<JoinStruct>();
        joinStatements.add(new JoinStruct(DFSQLConjunctionClause.natural, table, new DFSQLClauseStruct(leftHandSide, rightHandSide)));
        
        return this;
    }
    
    public DFSQL joinOn(JoinParam[] joins) throws DFSQLError
    {
        if (joinStatements == null); else                { throw DFSQLError.conditionAlreadyExists; }
        
        joinStatements = new ArrayList<JoinStruct>();
        
        for (JoinParam statement : joins)
        {
            if (statement.leftHandSide.contains("*") == false); else	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.leftHandSide != ""); else                   	{ throw DFSQLError.cannotUseEmptyValue; }
            if (statement.leftHandSide.length() <= 64); else  			{ throw DFSQLError.lengthTooLong; }
            
            if (statement.rightHandSide.contains("*") == false); else 	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.rightHandSide != ""); else                  	{ throw DFSQLError.cannotUseEmptyValue; }
            if (statement.rightHandSide.length() <= 64); else 			{ throw DFSQLError.lengthTooLong; }
            
            if (statement.table.contains("*") == false); else         	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.table != ""); else                          	{ throw DFSQLError.cannotUseEmptyValue; }
            if (statement.table.length() <= 64); else         			{ throw DFSQLError.lengthTooLong; }
            
            joinStatements.add(new JoinStruct(DFSQLConjunctionClause.natural, statement.table, new DFSQLClauseStruct(statement.leftHandSide, statement.rightHandSide)));
        }
        
        return this;
    }
    
    public DFSQL joinNatural(String table, String leftHandSide, String rightHandSide) throws DFSQLError
    {
        if (joinStatements == null); else       		{ throw DFSQLError.conditionAlreadyExists; }
        
        if (leftHandSide.contains("*") == false); else  { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (leftHandSide != ""); else                   { throw DFSQLError.cannotUseEmptyValue; }
        if (leftHandSide.length() <= 64); else  		{ throw DFSQLError.lengthTooLong; }
        
        if (rightHandSide.contains("*") == false); else { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (rightHandSide != ""); else                  { throw DFSQLError.cannotUseEmptyValue; }
        if (rightHandSide.length() <= 64); else 		{ throw DFSQLError.lengthTooLong; }
        
        if (table.contains("*") == false); else         { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (table != ""); else                          { throw DFSQLError.cannotUseEmptyValue; }
        if (table.length() <= 64); else         		{ throw DFSQLError.lengthTooLong; }
        
        joinStatements = new ArrayList<JoinStruct>();
        joinStatements.add(new JoinStruct(DFSQLConjunctionClause.natural, table, new DFSQLClauseStruct(leftHandSide, rightHandSide)));
        
        return this;
    }
    
    public DFSQL joinNatural(JoinParam[] joins) throws DFSQLError
    {
        if (joinStatements == null); else                { throw DFSQLError.conditionAlreadyExists; }
        
        joinStatements = new ArrayList<JoinStruct>();
        
        for (JoinParam statement : joins)
        {
            if (statement.leftHandSide.contains("*") == false); else  	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.leftHandSide != ""); else                   	{ throw DFSQLError.cannotUseEmptyValue; }
            if (statement.leftHandSide.length() <= 64); else  			{ throw DFSQLError.lengthTooLong; }
            
            if (statement.rightHandSide.contains("*") == false); else	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.rightHandSide != ""); else                  	{ throw DFSQLError.cannotUseEmptyValue; }
            if (statement.rightHandSide.length() <= 64); else 			{ throw DFSQLError.lengthTooLong; }
            
            if (statement.table.contains("*") == false); else         	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.table != ""); else                          	{ throw DFSQLError.cannotUseEmptyValue; }
            if (statement.table.length() <= 64); else         			{ throw DFSQLError.lengthTooLong; }
            
            joinStatements.add(new JoinStruct(DFSQLConjunctionClause.natural, statement.table, new DFSQLClauseStruct(statement.leftHandSide, statement.rightHandSide)));
        }
        
        return this;
    }
    
    public DFSQL joinLeftOuter(String table, String leftHandSide, String rightHandSide) throws DFSQLError
    {
        if (joinStatements == null); else              	{ throw DFSQLError.conditionAlreadyExists; }
        
        if (leftHandSide.contains("*") == false); else  { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (leftHandSide != ""); else                   { throw DFSQLError.cannotUseEmptyValue; }
        if (leftHandSide.length() <= 64); else  		{ throw DFSQLError.lengthTooLong; }
        
        if (rightHandSide.contains("*") == false); else { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (rightHandSide != ""); else                  { throw DFSQLError.cannotUseEmptyValue; }
        if (rightHandSide.length() <= 64); else 		{ throw DFSQLError.lengthTooLong; }
        
        if (table.contains("*") == false); else         { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (table != ""); else                          { throw DFSQLError.cannotUseEmptyValue; }
        if (table.length() <= 64); else         		{ throw DFSQLError.lengthTooLong; }
        
        joinStatements = new ArrayList<JoinStruct>();
        joinStatements.add(new JoinStruct(DFSQLConjunctionClause.leftOuter, table, new DFSQLClauseStruct(leftHandSide, rightHandSide)));
        
        return this;
    }
    
    public DFSQL joinLeftOuter(JoinParam[] joins) throws DFSQLError
    {
        if (joinStatements == null); else	{ throw DFSQLError.conditionAlreadyExists; }
        joinStatements = new ArrayList<JoinStruct>();
        
        for (JoinParam statement : joins)
        {
            if (statement.leftHandSide.contains("*") == false); else	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.leftHandSide != ""); else                   	{ throw DFSQLError.cannotUseEmptyValue; }
            if (statement.leftHandSide.length() <= 64); else 		 	{ throw DFSQLError.lengthTooLong; }
            
            if (statement.rightHandSide.contains("*") == false); else 	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.rightHandSide != ""); else                  	{ throw DFSQLError.cannotUseEmptyValue; }
            if (statement.rightHandSide.length() <= 64); else 			{ throw DFSQLError.lengthTooLong; }
            
            if (statement.table.contains("*") == false); else         	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.table != ""); else                          	{ throw DFSQLError.cannotUseEmptyValue; }
            if (statement.table.length() <= 64); else         			{ throw DFSQLError.lengthTooLong; }
            
            joinStatements.add(new JoinStruct(DFSQLConjunctionClause.leftOuter, statement.table, new DFSQLClauseStruct(statement.leftHandSide, statement.rightHandSide)));
        }
        
        return this;
    }
    
    public DFSQL joinRightOuter(String table, String leftHandSide, String rightHandSide) throws DFSQLError
    {
        if (joinStatements == null); else               { throw DFSQLError.conditionAlreadyExists; }
        
        if (leftHandSide.contains("*") == false); else	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (leftHandSide != ""); else                   { throw DFSQLError.cannotUseEmptyValue; }
        if (leftHandSide.length() <= 64); else  		{ throw DFSQLError.lengthTooLong; }
        
        if (rightHandSide.contains("*") == false); else { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (rightHandSide != ""); else                  { throw DFSQLError.cannotUseEmptyValue; }
        if (rightHandSide.length() <= 64); else 		{ throw DFSQLError.lengthTooLong; }
        
        if (table.contains("*") == false); else         { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (table != ""); else                          { throw DFSQLError.cannotUseEmptyValue; }
        if (table.length() <= 64); else         		{ throw DFSQLError.lengthTooLong; }
        
        joinStatements = new ArrayList<JoinStruct>();
        joinStatements.add(new JoinStruct(DFSQLConjunctionClause.rightOuter, table, new DFSQLClauseStruct(leftHandSide, rightHandSide)));
        
        return this;
    }
    
    public DFSQL joinRightOuter(JoinParam[] joins) throws DFSQLError
    {
        if (joinStatements == null); else	{ throw DFSQLError.conditionAlreadyExists; }
        joinStatements = new ArrayList<JoinStruct>();
        
        for (JoinParam statement : joins)
        {
            if (statement.leftHandSide.contains("*") == false); else	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.leftHandSide != ""); else                   	{ throw DFSQLError.cannotUseEmptyValue; }
            if (statement.leftHandSide.length() <= 64); else  			{ throw DFSQLError.lengthTooLong; }
            
            if (statement.rightHandSide.contains("*") == false); else 	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.rightHandSide != ""); else                  	{ throw DFSQLError.cannotUseEmptyValue; }
            if (statement.rightHandSide.length() <= 64); else 			{ throw DFSQLError.lengthTooLong; }
            
            if (statement.table.contains("*") == false); else         	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.table != ""); else                          	{ throw DFSQLError.cannotUseEmptyValue; }
            if (statement.table.length() <= 64); else         			{ throw DFSQLError.lengthTooLong; }
            
            joinStatements.add(new JoinStruct(DFSQLConjunctionClause.rightOuter, statement.table, new DFSQLClauseStruct(statement.leftHandSide, statement.rightHandSide)));
        }
        
        return this;
    }
    
    public DFSQL joinFullOuter(String table, String leftHandSide, String rightHandSide) throws DFSQLError
    {
        if (joinStatements == null); else 				{ throw DFSQLError.conditionAlreadyExists; }
        
        if (leftHandSide.contains("*") == false); else  { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (leftHandSide != ""); else                   { throw DFSQLError.cannotUseEmptyValue; }
        if (leftHandSide.length() <= 64); else  		{ throw DFSQLError.lengthTooLong; }
        
        if (rightHandSide.contains("*") == false); else { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (rightHandSide != ""); else                  { throw DFSQLError.cannotUseEmptyValue; }
        if (rightHandSide.length() <= 64); else 		{ throw DFSQLError.lengthTooLong; }
        
        if (table.contains("*") == false); else         { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (table != ""); else                          { throw DFSQLError.cannotUseEmptyValue; }
        if (table.length() <= 64); else        		 	{ throw DFSQLError.lengthTooLong; }
        
        joinStatements = new ArrayList<JoinStruct>();
        joinStatements.add(new JoinStruct(DFSQLConjunctionClause.fullOuter, table, new DFSQLClauseStruct(leftHandSide, rightHandSide)));
        
        return this;
    }
    
    public DFSQL joinFullOuter(JoinParam[] joins) throws DFSQLError
    {
        if (joinStatements == null); else 	{ throw DFSQLError.conditionAlreadyExists; }
        joinStatements = new ArrayList<JoinStruct>();
        
        for (JoinParam statement : joins)
        {
            if (statement.leftHandSide.contains("*") == false); else  { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.leftHandSide != ""); else                   { throw DFSQLError.cannotUseEmptyValue; }
            if (statement.leftHandSide.length() <= 64); else  { throw DFSQLError.lengthTooLong; }
            
            if (statement.rightHandSide.contains("*") == false); else { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.rightHandSide != ""); else                  { throw DFSQLError.cannotUseEmptyValue; }
            if (statement.rightHandSide.length() <= 64); else { throw DFSQLError.lengthTooLong; }
            
            if (statement.table.contains("*") == false); else         { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.table != ""); else                          { throw DFSQLError.cannotUseEmptyValue; }
            if (statement.table.length() <= 64); else         { throw DFSQLError.lengthTooLong; }
            
            joinStatements.add(new JoinStruct(DFSQLConjunctionClause.fullOuter, statement.table, new DFSQLClauseStruct(statement.leftHandSide, statement.rightHandSide)));
        }
        
        return this;
    }
    
    public DFSQL joinCross(String table, String leftHandSide, String rightHandSide) throws DFSQLError
    {
        if (joinStatements == null); else        		{ throw DFSQLError.conditionAlreadyExists; }
        
        if (leftHandSide.contains("*") == false); else	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (leftHandSide != ""); else                   { throw DFSQLError.cannotUseEmptyValue; }
        if (leftHandSide.length() <= 64); else  		{ throw DFSQLError.lengthTooLong; }
        
        if (rightHandSide.contains("*") == false); else { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (rightHandSide != ""); else                  { throw DFSQLError.cannotUseEmptyValue; }
        if (rightHandSide.length() <= 64); else 		{ throw DFSQLError.lengthTooLong; }
        
        if (table.contains("*") == false); else         { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (table != ""); else                          { throw DFSQLError.cannotUseEmptyValue; }
        if (table.length() <= 64); else         		{ throw DFSQLError.lengthTooLong; }
        
        joinStatements = new ArrayList<JoinStruct>();
        joinStatements.add(new JoinStruct(DFSQLConjunctionClause.cross, table, new DFSQLClauseStruct(leftHandSide, rightHandSide)));
        
        return this;
    }
    
    public DFSQL joinCross(JoinParam[] joins) throws DFSQLError
    {
        if (joinStatements == null); else	{ throw DFSQLError.conditionAlreadyExists; }
        joinStatements = new ArrayList<JoinStruct>();
        
        for (JoinParam statement : joins)
        {
            if (statement.leftHandSide.contains("*") == false); else	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.leftHandSide != ""); else                   	{ throw DFSQLError.cannotUseEmptyValue; }
            if (statement.leftHandSide.length() <= 64); else  			{ throw DFSQLError.lengthTooLong; }
            
            if (statement.rightHandSide.contains("*") == false); else 	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.rightHandSide != ""); else                  	{ throw DFSQLError.cannotUseEmptyValue; }
            if (statement.rightHandSide.length() <= 64); else 			{ throw DFSQLError.lengthTooLong; }
            
            if (statement.table.contains("*") == false); else         	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.table != ""); else                          	{ throw DFSQLError.cannotUseEmptyValue; }
            if (statement.table.length() <= 64); else         			{ throw DFSQLError.lengthTooLong; }
            
            joinStatements.add(new JoinStruct(DFSQLConjunctionClause.cross, statement.table, new DFSQLClauseStruct(statement.leftHandSide, statement.rightHandSide)));
        }
        
        return this;
    }
    
    public DFSQL joinInner(String table, String leftHandSide, String rightHandSide) throws DFSQLError
    {
        if (joinStatements == null); else                { throw DFSQLError.conditionAlreadyExists; }
        
        if (leftHandSide.contains("*") == false); else  { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (leftHandSide != ""); else                   { throw DFSQLError.cannotUseEmptyValue; }
        if (leftHandSide.length() <= 64); else  { throw DFSQLError.lengthTooLong; }
        
        if (rightHandSide.contains("*") == false); else { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (rightHandSide != ""); else                  { throw DFSQLError.cannotUseEmptyValue; }
        if (rightHandSide.length() <= 64); else { throw DFSQLError.lengthTooLong; }
        
        if (table.contains("*") == false); else         { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (table != ""); else                          { throw DFSQLError.cannotUseEmptyValue; }
        if (table.length() <= 64); else         { throw DFSQLError.lengthTooLong; }
        
        joinStatements = new ArrayList<JoinStruct>();
        joinStatements.add(new JoinStruct(DFSQLConjunctionClause.inner, table, new DFSQLClauseStruct(leftHandSide, rightHandSide)));
        
        return this;
    }
    
    public DFSQL joinInner(JoinParam[] joins) throws DFSQLError
    {
        if (joinStatements == null); else                { throw DFSQLError.conditionAlreadyExists; }
        
        joinStatements = new ArrayList<JoinStruct>();
        
        for (JoinParam statement : joins)
        {
            if (statement.leftHandSide.contains("*") == false); else  { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.leftHandSide != ""); else                   { throw DFSQLError.cannotUseEmptyValue; }
            if (statement.leftHandSide.length() <= 64); else  		{ throw DFSQLError.lengthTooLong; }
            
            if (statement.rightHandSide.contains("*") == false); else { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.rightHandSide != ""); else                  { throw DFSQLError.cannotUseEmptyValue; }
            if (statement.rightHandSide.length() <= 64); else { throw DFSQLError.lengthTooLong; }
            
            if (statement.table.contains("*") == false); else         { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (statement.table != ""); else                          { throw DFSQLError.cannotUseEmptyValue; }
            if (statement.table.length() <= 64); else         { throw DFSQLError.lengthTooLong; }
            
            joinStatements.add(new JoinStruct(DFSQLConjunctionClause.inner, statement.table, new DFSQLClauseStruct(statement.leftHandSide, statement.rightHandSide)));
        }
        
        return this;
    }
    
    //MARK: - WHERE Constructors
    
    //MARK: Single
    
    public DFSQL whereEquals(String leftHandSide, String rightHandSide) throws DFSQLError
    {
        if (whereStatements == null); else     	{ throw DFSQLError.conditionAlreadyExists; }
        if (leftHandSide != "*"); else      	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (rightHandSide != "*"); else       	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (leftHandSide.length() <= 64); else  { throw DFSQLError.lengthTooLong; }
        if (rightHandSide.length() <= 64); else { throw DFSQLError.lengthTooLong; }
        
        whereStatements = new ArrayList<WhereStruct>();
        whereStatements.add(new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.equals, new DFSQLClauseStruct(leftHandSide, rightHandSide)));
        
        return this;
    }
    
    public DFSQL whereDoesNotEqual(String leftHandSide, String rightHandSide) throws DFSQLError
    {
        if (whereStatements == null); else      { throw DFSQLError.conditionAlreadyExists; }
        if (leftHandSide != "*"); else          { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (rightHandSide != "*"); else         { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (leftHandSide.length() <= 64); else  { throw DFSQLError.lengthTooLong; }
        if (rightHandSide.length() <= 64); else { throw DFSQLError.lengthTooLong; }
        
        whereStatements = new ArrayList<WhereStruct>();
        whereStatements.add(new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.notEquals, new DFSQLClauseStruct(leftHandSide, rightHandSide)));
        
        return this;
    }
    
    public DFSQL whereIsLessThan(String leftHandSide, String rightHandSide) throws DFSQLError
    {
        if (whereStatements == null); else      { throw DFSQLError.conditionAlreadyExists; }
        if (leftHandSide != "*"); else          { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (rightHandSide != "*"); else         { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (leftHandSide.length() <= 64); else  { throw DFSQLError.lengthTooLong; }
        if (rightHandSide.length() <= 64); else { throw DFSQLError.lengthTooLong; }
        
        whereStatements = new ArrayList<WhereStruct>();
        whereStatements.add(new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.lessThan, new DFSQLClauseStruct(leftHandSide, rightHandSide)));
        
        return this;
    }
    
    public DFSQL whereLessThanOrEqual(String leftHandSide, String rightHandSide) throws DFSQLError
    {
        if (whereStatements == null); else      { throw DFSQLError.conditionAlreadyExists; }
        if (leftHandSide != "*"); else          { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (rightHandSide != "*"); else         { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (leftHandSide.length() <= 64); else  { throw DFSQLError.lengthTooLong; }
        if (rightHandSide.length() <= 64); else { throw DFSQLError.lengthTooLong; }
        
        whereStatements = new ArrayList<WhereStruct>();
        whereStatements.add(new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.lessThanOrEqualTo, new DFSQLClauseStruct(leftHandSide, rightHandSide)));
        
        return this;
    }
    
    public DFSQL whereGreaterThan(String leftHandSide, String rightHandSide) throws DFSQLError
    {
        if (whereStatements == null); else    	{ throw DFSQLError.conditionAlreadyExists; }
        if (leftHandSide != "*"); else          { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (rightHandSide != "*"); else         { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (leftHandSide.length() <= 64); else  { throw DFSQLError.lengthTooLong; }
        if (rightHandSide.length() <= 64); else { throw DFSQLError.lengthTooLong; }
        
        whereStatements = new ArrayList<WhereStruct>();
        whereStatements.add(new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.greaterThan, new DFSQLClauseStruct(leftHandSide, rightHandSide)));
        
        return this;
    }
    
    public DFSQL whereGreaterThanOrEqual(String leftHandSide, String rightHandSide) throws DFSQLError
    {
        if (whereStatements == null); else		{ throw DFSQLError.conditionAlreadyExists; }
        if (leftHandSide != "*"); else          { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (rightHandSide != "*"); else         { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
        if (leftHandSide.length() <= 64); else  { throw DFSQLError.lengthTooLong; }
        if (rightHandSide.length() <= 64); else { throw DFSQLError.lengthTooLong; }
        
        whereStatements = new ArrayList<WhereStruct>();
        whereStatements.add(new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.greaterThanOrEqualTo, new DFSQLClauseStruct(leftHandSide, rightHandSide)));
        
        return this;
    }
    
    //MARK: AND
    
    public DFSQL whereAndEquals(String[] leftHandSides, String[] rightHandSides) throws DFSQLError
    {
        if (leftHandSides.length == rightHandSides.length); else  { throw DFSQLError.conditionsMustBeEqual; }
        if (whereStatements == null); else                      { throw DFSQLError.conditionAlreadyExists; }
        
        whereStatements = new ArrayList<WhereStruct>();
        
        for (String leftHandSide : leftHandSides)
        {
            if (leftHandSide != "*"); else                      { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (leftHandSide.length() <= 64); else      		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (String rightHandSide : rightHandSides)
        {
            if (rightHandSide != "*"); else                     { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (rightHandSide.length() <= 64); else     		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (int index = 0; index < leftHandSides.length - 1; index++)
        {
            String lhs = leftHandSides[index];
            String rhs = rightHandSides[index];
            
            whereStatements.add(new WhereStruct(DFSQLConjunctionClause.and, DFSQLConjunctionClause.equals, new DFSQLClauseStruct(lhs, rhs)));
        }
        
        whereStatements.add(new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.equals, new DFSQLClauseStruct(leftHandSides[leftHandSides.length - 1], rightHandSides[rightHandSides.length - 1])));
        
        return this;
    }
    
    public DFSQL whereAndDoesNotEqual(String[] leftHandSides, String[] rightHandSides) throws DFSQLError
    {
        if (leftHandSides.length == rightHandSides.length); else	{ throw DFSQLError.conditionsMustBeEqual; }
        if (whereStatements == null); else                      { throw DFSQLError.conditionAlreadyExists; }
        
        whereStatements = new ArrayList<WhereStruct>();
        
        for (String leftHandSide : leftHandSides)
        {
            if (leftHandSide != "*"); else                      { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (leftHandSide.length() <= 64); else      		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (String rightHandSide : rightHandSides)
        {
            if (rightHandSide != "*"); else                     { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (rightHandSide.length() <= 64); else     		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (int index = 0; index < leftHandSides.length - 1; index++)
        {
            String lhs = leftHandSides[index];
            String rhs = rightHandSides[index];
            
            whereStatements.add(new WhereStruct(DFSQLConjunctionClause.and, DFSQLConjunctionClause.notEquals, new DFSQLClauseStruct(lhs, rhs)));
        }
        
        whereStatements.add(new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.notEquals, new DFSQLClauseStruct(leftHandSides[leftHandSides.length - 1], rightHandSides[rightHandSides.length - 1])));
        
        return this;
    }
    
    public DFSQL whereAndGreaterThan(String[] leftHandSides, String[] rightHandSides) throws DFSQLError
    {
        if (leftHandSides.length == rightHandSides.length); else  { throw DFSQLError.conditionsMustBeEqual; }
        if (whereStatements == null); else                      { throw DFSQLError.conditionAlreadyExists; }
        
        whereStatements = new ArrayList<WhereStruct>();
        
        for (String leftHandSide : leftHandSides)
        {
            if (leftHandSide != "*"); else                      { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (leftHandSide.length() <= 64); else      		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (String rightHandSide : rightHandSides)
        {
            if (rightHandSide != "*"); else                     { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (rightHandSide.length() <= 64); else     		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (int index = 0; index < leftHandSides.length - 1; index++)
        {
            String lhs = leftHandSides[index];
            String rhs = rightHandSides[index];
            
            whereStatements.add(new WhereStruct(DFSQLConjunctionClause.and, DFSQLConjunctionClause.greaterThan, new DFSQLClauseStruct(lhs, rhs)));
        }
        
        whereStatements.add(new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.greaterThan, new DFSQLClauseStruct(leftHandSides[leftHandSides.length - 1], rightHandSides[rightHandSides.length - 1])));
        
        return this;
    }
    
    public DFSQL whereAndGreaterThanOrEqual(String[] leftHandSides, String[] rightHandSides) throws DFSQLError
    {
        if (leftHandSides.length == rightHandSides.length); else  { throw DFSQLError.conditionsMustBeEqual; }
        if (whereStatements == null); else                      { throw DFSQLError.conditionAlreadyExists; }
        
        whereStatements = new ArrayList<WhereStruct>();
        
        for (String leftHandSide : leftHandSides)
        {
            if (leftHandSide != "*"); else                      { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (leftHandSide.length() <= 64); else      		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (String rightHandSide : rightHandSides)
        {
            if (rightHandSide != "*"); else                     { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (rightHandSide.length() <= 64); else     		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (int index = 0; index < leftHandSides.length - 1; index++)
        {
            String lhs = leftHandSides[index];
            String rhs = rightHandSides[index];
            
            whereStatements.add(new WhereStruct(DFSQLConjunctionClause.and, DFSQLConjunctionClause.greaterThanOrEqualTo, new DFSQLClauseStruct(lhs, rhs)));
        }
        
        whereStatements.add(new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.greaterThanOrEqualTo, new DFSQLClauseStruct(leftHandSides[leftHandSides.length - 1], rightHandSides[rightHandSides.length - 1])));
        
        return this;
    }
    
    public DFSQL whereAndLessThan(String[] leftHandSides, String[] rightHandSides) throws DFSQLError
    {
        if (leftHandSides.length == rightHandSides.length); else  { throw DFSQLError.conditionsMustBeEqual; }
        if (whereStatements == null); else                      { throw DFSQLError.conditionAlreadyExists; }
        
        whereStatements = new ArrayList<WhereStruct>();
        
        for (String leftHandSide : leftHandSides)
        {
            if (leftHandSide != "*"); else                      { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (leftHandSide.length() <= 64); else     			{ throw DFSQLError.lengthTooLong; }
        }
        
        for (String rightHandSide : rightHandSides)
        {
            if (rightHandSide != "*"); else                     { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (rightHandSide.length() <= 64); else     		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (int index = 0; index < leftHandSides.length - 1; index++)
        {
            String lhs = leftHandSides[index];
            String rhs = rightHandSides[index];
            
            whereStatements.add(new WhereStruct(DFSQLConjunctionClause.and, DFSQLConjunctionClause.lessThan, new DFSQLClauseStruct(lhs, rhs)));
        }
        
        whereStatements.add(new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.lessThan, new DFSQLClauseStruct(leftHandSides[leftHandSides.length - 1], rightHandSides[rightHandSides.length - 1])));
        
        return this;
    }
    
    public DFSQL whereAndLessThanOrEqual(String[] leftHandSides, String[] rightHandSides) throws DFSQLError
    {
        if (leftHandSides.length == rightHandSides.length); else	{ throw DFSQLError.conditionsMustBeEqual; }
        if (whereStatements == null); else                      { throw DFSQLError.conditionAlreadyExists; }
        
        whereStatements = new ArrayList<WhereStruct>();
        
        for (String leftHandSide : leftHandSides)
        {
            if (leftHandSide != "*"); else                      { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (leftHandSide.length() <= 64); else      		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (String rightHandSide : rightHandSides)
        {
            if (rightHandSide != "*"); else                     { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (rightHandSide.length() <= 64); else     		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (int index = 0; index < leftHandSides.length - 1; index++)
        {
            String lhs = leftHandSides[index];
            String rhs = rightHandSides[index];
            
            whereStatements.add(new WhereStruct(DFSQLConjunctionClause.and, DFSQLConjunctionClause.lessThanOrEqualTo, new DFSQLClauseStruct(lhs, rhs)));
        }
        
        whereStatements.add(new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.lessThanOrEqualTo, new DFSQLClauseStruct(leftHandSides[leftHandSides.length - 1], rightHandSides[rightHandSides.length - 1])));
        
        return this;
    }
    
    //MARK: OR
    
    public DFSQL whereOrEquals(String[] leftHandSides, String[] rightHandSides) throws DFSQLError
    {
        if (leftHandSides.length == rightHandSides.length); else	{ throw DFSQLError.conditionsMustBeEqual; }
        if (whereStatements == null); else                      { throw DFSQLError.conditionAlreadyExists; }
        
        whereStatements = new ArrayList<WhereStruct>();
        
        for (String leftHandSide : leftHandSides)
        {
            if (leftHandSide != "*"); else                      { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (leftHandSide.length() <= 64); else      		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (String rightHandSide : rightHandSides)
        {
            if (rightHandSide != "*"); else                     { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (rightHandSide.length() <= 64); else     		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (int index = 0; index < leftHandSides.length - 1; index++)
        {
            String lhs = leftHandSides[index];
            String rhs = rightHandSides[index];
            
            whereStatements.add(new WhereStruct(DFSQLConjunctionClause.or, DFSQLConjunctionClause.equals, new DFSQLClauseStruct(lhs, rhs)));
        }
        
        whereStatements.add(new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.equals, new DFSQLClauseStruct(leftHandSides[leftHandSides.length - 1], rightHandSides[rightHandSides.length - 1])));
        
        return this;
    }
    
    public DFSQL whereOrNotEqual(String[] leftHandSides, String[] rightHandSides) throws DFSQLError
    {
        if (leftHandSides.length == rightHandSides.length); else	{ throw DFSQLError.conditionsMustBeEqual; }
        if (whereStatements == null); else                      { throw DFSQLError.conditionAlreadyExists; }
        
        whereStatements = new ArrayList<WhereStruct>();
        
        for (String leftHandSide : leftHandSides)
        {
            if (leftHandSide != "*"); else                      { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (leftHandSide.length() <= 64); else      		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (String rightHandSide : rightHandSides)
        {
            if (rightHandSide != "*"); else                     { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (rightHandSide.length() <= 64); else     		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (int index = 0; index < leftHandSides.length - 1; index++)
        {
            String lhs = leftHandSides[index];
            String rhs = rightHandSides[index];
            
            whereStatements.add(new WhereStruct(DFSQLConjunctionClause.or, DFSQLConjunctionClause.notEquals, new DFSQLClauseStruct(lhs, rhs)));
        }
        
        whereStatements.add(new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.notEquals, new DFSQLClauseStruct(leftHandSides[leftHandSides.length - 1], rightHandSides[rightHandSides.length - 1])));
        
        return this;
    }
    
    public DFSQL whereOrGreaterThan(String[] leftHandSides, String[] rightHandSides) throws DFSQLError
    {
        if (leftHandSides.length == rightHandSides.length); else	{ throw DFSQLError.conditionsMustBeEqual; }
        if (whereStatements == null); else                     	{ throw DFSQLError.conditionAlreadyExists; }
        
        whereStatements = new ArrayList<WhereStruct>();
        
        for (String leftHandSide : leftHandSides)
        {
            if (leftHandSide != "*"); else                    	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (leftHandSide.length() <= 64); else      		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (String rightHandSide : rightHandSides)
        {
            if (rightHandSide != "*"); else                     { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (rightHandSide.length() <= 64); else     		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (int index = 0; index < leftHandSides.length - 1; index++)
        {
            String lhs = leftHandSides[index];
            String rhs = rightHandSides[index];
            
            whereStatements.add(new WhereStruct(DFSQLConjunctionClause.or, DFSQLConjunctionClause.greaterThan, new DFSQLClauseStruct(lhs, rhs)));
        }
        
        whereStatements.add(new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.greaterThan, new DFSQLClauseStruct(leftHandSides[leftHandSides.length - 1], rightHandSides[rightHandSides.length - 1])));
        
        return this;
    }
    
    public DFSQL whereOrGreaterThanOrEqual(String[] leftHandSides, String[] rightHandSides) throws DFSQLError
    {
        if (leftHandSides.length == rightHandSides.length); else	{ throw DFSQLError.conditionsMustBeEqual; }
        if (whereStatements == null); else                      { throw DFSQLError.conditionAlreadyExists; }
        
        whereStatements = new ArrayList<WhereStruct>();
        
        for (String leftHandSide : leftHandSides)
        {
            if (leftHandSide != "*"); else                      { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (leftHandSide.length() <= 64); else      		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (String rightHandSide : rightHandSides)
        {
            if (rightHandSide != "*"); else                     { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (rightHandSide.length() <= 64); else     		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (int index = 0; index < leftHandSides.length - 1; index++)
        {
            String lhs = leftHandSides[index];
            String rhs = rightHandSides[index];
            
            whereStatements.add(new WhereStruct(DFSQLConjunctionClause.or, DFSQLConjunctionClause.greaterThanOrEqualTo, new DFSQLClauseStruct(lhs, rhs)));
        }
        
        whereStatements.add(new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.greaterThanOrEqualTo, new DFSQLClauseStruct(leftHandSides[leftHandSides.length - 1], rightHandSides[rightHandSides.length - 1])));
        
        return this;
    }
    
    public DFSQL whereOrLessThan(String[] leftHandSides, String[] rightHandSides) throws DFSQLError
    {
        if (leftHandSides.length == rightHandSides.length); else	{ throw DFSQLError.conditionsMustBeEqual; }
        if (whereStatements == null); else                      { throw DFSQLError.conditionAlreadyExists; }
        
        whereStatements = new ArrayList<WhereStruct>();
        
        for (String leftHandSide : leftHandSides)
        {
            if (leftHandSide != "*"); else                      { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (leftHandSide.length() <= 64); else      		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (String rightHandSide : rightHandSides)
        {
            if (rightHandSide != "*"); else                     { throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (rightHandSide.length() <= 64); else     		{ throw DFSQLError.lengthTooLong; }
        }
        
        for (int index = 0; index < leftHandSides.length - 1; index++)
        {
            String lhs = leftHandSides[index];
            String rhs = rightHandSides[index];
            
            whereStatements.add(new WhereStruct(DFSQLConjunctionClause.or, DFSQLConjunctionClause.lessThan, new DFSQLClauseStruct(lhs, rhs)));
        }
        
        whereStatements.add(new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.lessThan, new DFSQLClauseStruct(leftHandSides[leftHandSides.length - 1], rightHandSides[rightHandSides.length - 1])));
        
        return this;
    }
    
    public DFSQL whereOrLessThanOrEqual(String[] leftHandSides, String[] rightHandSides) throws DFSQLError
    {
        if (leftHandSides.length == rightHandSides.length); else  { throw DFSQLError.conditionsMustBeEqual; }
        
        if (whereStatements == null); else                   	{ throw DFSQLError.conditionAlreadyExists; }
        
        whereStatements = new ArrayList<WhereStruct>();
        
        for (String leftHandSide : leftHandSides)
        {
            if (leftHandSide != "*"); else         	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (leftHandSide.length() <= 64); else	{ throw DFSQLError.lengthTooLong; }
        }
        
        for (String rightHandSide : rightHandSides)
        {
            if (rightHandSide != "*"); else        	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (rightHandSide.length() <= 64); else	{ throw DFSQLError.lengthTooLong; }
        }
        
        for (int index = 0; index < leftHandSides.length - 1; index++)
        {
            String lhs = leftHandSides[index];
            String rhs = rightHandSides[index];
            
            whereStatements.add(new WhereStruct(DFSQLConjunctionClause.or, DFSQLConjunctionClause.lessThanOrEqualTo, new DFSQLClauseStruct(lhs, rhs)));
        }
        
        whereStatements.add(new WhereStruct(DFSQLConjunctionClause.none, DFSQLConjunctionClause.lessThanOrEqualTo, new DFSQLClauseStruct(leftHandSides[leftHandSides.length - 1], rightHandSides[rightHandSides.length - 1])));
        
        return this;
    }
    
    //MARK: Custom
    
    public DFSQL whereCustom(WhereStruct[] custom) throws DFSQLError
    {
        if (custom.length != 0); else		{ throw DFSQLError.cannotUseEmptyValue; }
        if (whereStatements == null); else	{ throw DFSQLError.conditionAlreadyExists; }
        
        for (WhereStruct whereClause : custom)
        {
        	DFSQLClauseStruct SQLClause = whereClause.clause;
            
            if (SQLClause.leftHandSide != "*"); else         	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (SQLClause.leftHandSide.length() <= 64); else    { throw DFSQLError.lengthTooLong; }
            if (SQLClause.rightHandSide != "*"); else         	{ throw DFSQLError.cannotUseAllRowsSQLSpecifier; }
            if (SQLClause.rightHandSide.length() <= 64); else   { throw DFSQLError.lengthTooLong; }
        }
       
        whereStatements = new ArrayList<WhereStruct>();
        for (WhereStruct whereStruct : custom)
        {
        	whereStatements.add(whereStruct);
        }
        
        return this;
    }
}
