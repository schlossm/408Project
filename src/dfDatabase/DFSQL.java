package dfDatabase;
import dfDatabase.JoinStruct;
import dfDatabase.WhereStruct;
import dfDatabase.DFSQLClauseStruct;

public class DFSQL 
{
    private String[] selectRows;
    private String[] fromTables;
    private JoinStruct[] joinStatements;
    private WhereStruct[] whereStatements;
    
    private String[] insertRows;
    private String[] insertData;
    
    private DFSQLClauseStruct[] updateStatements;
    private DFSQL[] appendedDFSQL;
    
    public DFSQL append(DFSQL object)
    {
    	appendedDFSQL.append(object);
        
        return self;
    }
    
    //MARK: - SELECT Constructors
    
    public DFSQL select(String row) throws DFSQLError
    {
        if selectRows != nil            { throw MSSQLError.conditionAlreadyExists; }
        if row.contains("*") 		   	{ throw MSSQLError.cannotUseAllRowsSQLSpecifier; }
        if row == ""                    { throw MSSQLError.cannotUseEmptyValue; }
        if row.characters.count > 64	{ throw MSSQLError.lengthTooLong; }
        
        selectRows = [row];
        
        return self;
    }
    
    public DFSQL select(String[] rows) throws DFSQLError
    {
        if selectRows != nil	{ throw MSSQLError.conditionAlreadyExists; }
        
        for row in rows
        {
            if row.contains("*")			{ throw MSSQLError.cannotUseAllRowsSQLSpecifier; }
            if row == ""                    { throw MSSQLError.cannotUseEmptyValue; }
            if row.characters.count > 64	{ throw MSSQLError.lengthTooLong; }
        }
        
        selectRows = rows;
        
        return self;
    }
    
    //MARK: - FROM Constructors
    
    public DFSQL from(String table) throws DFSQLError
    {
        if fromTables == nil; else            	{ throw MSSQLError.conditionAlreadyExists; }
        if.contains("*") == false; else			{ throw MSSQLError.cannotUseAllRowsSQLSpecifier; }
        if table != ""; else               		{ throw MSSQLError.cannotUseEmptyValue; }
        if table.characters.count <= 64; else 	{ throw MSSQLError.lengthTooLong; }
        
        fromTables = [table];
        
        return self;
    }
    
    public DFSQL from(String[] tables) throws DFSQLError
    {
        if fromTables == nil; else					{ throw MSSQLError.conditionAlreadyExists; }
        
        for table in tables
        {
            if table.contains("*") == false; else	{ throw MSSQLError.cannotUseAllRowsSQLSpecifier; }
            if table != ""; else                  	{ throw MSSQLError.cannotUseEmptyValue; }
            if table.characters.count <= 64; else 	{ throw MSSQLError.lengthTooLong; }
        }
        
        fromTables = tables;
        
        return self;
    }
    
    //MARK: - UPDATE SET Constructor
    
    public DFSQL update(String table, String leftHandSide, String rightHandSide) throws DFSQLError
    {
        if fromTables == nil else            { throw MSSQLError.conditionAlreadyExists; }
        if table.contains("*") == false else { throw MSSQLError.cannotUseAllRowsSQLSpecifier; }
        if table != "" else                  { throw MSSQLError.cannotUseEmptyValue; }
        if table.characters.count <= 64 else { throw MSSQLError.lengthTooLong; }
        
        fromTables = [table];
        
        if leftHandSide.contains("*") == false else  { throw MSSQLError.cannotUseAllRowsSQLSpecifier; }
        if leftHandSide != "" else                   { throw MSSQLError.cannotUseEmptyValue; }
        if leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong; }
        
        if rightHandSide.contains("*") == false else { throw MSSQLError.cannotUseAllRowsSQLSpecifier; }
        if rightHandSide != "" else                  { throw MSSQLError.cannotUseEmptyValue; }
        if rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong; }
        
        updateStatements = [MSSQLClause(leftHandSide: leftHandSide, rightHandSide: rightHandSide)];
        
        return self;
    }
    
    public DFSQL update(String table, DFSQLClause[] statements) throws DFSQLError
    {
        if fromTables == nil else            { throw MSSQLError.conditionAlreadyExists; }
        if table.contains("*") == false else { throw MSSQLError.cannotUseAllRowsSQLSpecifier; }
        if table != "" else                  { throw MSSQLError.cannotUseEmptyValue; }
        if table.characters.count <= 64 else { throw MSSQLError.lengthTooLong; }
        
        fromTables = [table];
        
        updateStatements = [MSSQLClause]();
        
        for statement in statements
        {
            if statement.leftHandSide.contains("*") == false else  { throw MSSQLError.cannotUseAllRowsSQLSpecifier; }
            if statement.leftHandSide != "" else                   { throw MSSQLError.cannotUseEmptyValue; }
            if statement.leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong; }
            
            if statement.rightHandSide.contains("*") == false else { throw MSSQLError.cannotUseAllRowsSQLSpecifier; }
            if statement.rightHandSide != "" else                  { throw MSSQLError.cannotUseEmptyValue; }
            if statement.rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong; }
            
            updateStatements.append(MSSQLClause(leftHandSide: statement.leftHandSide, rightHandSide: statement.rightHandSide));
        }
        
        return self;
    }
    
    //MARK: - INSERT INTO Constructor
    
    public DFSQL insert(String table, String[] values, String[] rows) throws DFSQLError
    {
        if fromTables == nil else            { throw MSSQLError.conditionAlreadyExists; }
        if insertData == nil else            { throw MSSQLError.conditionAlreadyExists; }
        if insertRows == nil else            { throw MSSQLError.conditionAlreadyExists; }
        
        if table.contains("*") == false else { throw MSSQLError.cannotUseAllRowsSQLSpecifier; }
        if table != "" else                  { throw MSSQLError.cannotUseEmptyValue; }
        if table.characters.count <= 64 else { throw MSSQLError.lengthTooLong; }
        
        fromTables = [table];
        
        insertData = [String]();
        insertRows = [String]();
        
        for value in values
        {
            if value.contains("*") == false else  { throw MSSQLError.cannotUseAllRowsSQLSpecifier; }
            if value != "" else                   { throw MSSQLError.cannotUseEmptyValue; }
            if value.characters.count <= 64 else  { throw MSSQLError.lengthTooLong; }
        }
        
        for row in rows
        {
            if row.contains("*") == false else  { throw MSSQLError.cannotUseAllRowsSQLSpecifier; }
            if row != "" else                   { throw MSSQLError.cannotUseEmptyValue; }
            if row.characters.count <= 64 else  { throw MSSQLError.lengthTooLong; }
        }
        
        insertData = values;
        insertRows = rows;
        
        return self;
    }
    
    //MARK: - JOIN ON Constructors
    
    func join(_ table: String, on leftHandSide: String, rightHandSide: String) throws -> newMSSQL
    {
        if joinStatements == nil else                { throw MSSQLError.conditionAlreadyExists }
        
        if leftHandSide.contains("*") == false else  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if leftHandSide != "" else                   { throw MSSQLError.cannotUseEmptyValue }
        if leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
        
        if rightHandSide.contains("*") == false else { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if rightHandSide != "" else                  { throw MSSQLError.cannotUseEmptyValue }
        if rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
        
        if table.contains("*") == false else         { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if table != "" else                          { throw MSSQLError.cannotUseEmptyValue }
        if table.characters.count <= 64 else         { throw MSSQLError.lengthTooLong }
        
        joinStatements = [(.natural, table, MSSQLClause(leftHandSide: leftHandSide, rightHandSide: rightHandSide))]
        
        return self
    }
    
    func join(on joins: [(table: String, leftHandSide: String, rightHandSide: String)]) throws -> newMSSQL
    {
        if joinStatements == nil else                { throw MSSQLError.conditionAlreadyExists }
        
        joinStatements = [(joinType: MSSQLConjunctionClause, table: String, clause: MSSQLClause)]()
        
        for statement in joins
        {
            if statement.leftHandSide.contains("*") == false else  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.leftHandSide != "" else                   { throw MSSQLError.cannotUseEmptyValue }
            if statement.leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
            
            if statement.rightHandSide.contains("*") == false else { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.rightHandSide != "" else                  { throw MSSQLError.cannotUseEmptyValue }
            if statement.rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
            
            if statement.table.contains("*") == false else         { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.table != "" else                          { throw MSSQLError.cannotUseEmptyValue }
            if statement.table.characters.count <= 64 else         { throw MSSQLError.lengthTooLong }
            
            joinStatements.append((.natural, statement.table, MSSQLClause(leftHandSide: statement.leftHandSide, rightHandSide: statement.rightHandSide)))
        }
        
        return self
    }
    
    func join(natural table: String, on leftHandSide: String, rightHandSide: String) throws -> newMSSQL
    {
        if joinStatements == nil else                { throw MSSQLError.conditionAlreadyExists }
        
        if leftHandSide.contains("*") == false else  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if leftHandSide != "" else                   { throw MSSQLError.cannotUseEmptyValue }
        if leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
        
        if rightHandSide.contains("*") == false else { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if rightHandSide != "" else                  { throw MSSQLError.cannotUseEmptyValue }
        if rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
        
        if table.contains("*") == false else         { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if table != "" else                          { throw MSSQLError.cannotUseEmptyValue }
        if table.characters.count <= 64 else         { throw MSSQLError.lengthTooLong }
        
        joinStatements = [(.natural, table, MSSQLClause(leftHandSide: leftHandSide, rightHandSide: rightHandSide))]
        
        return self
    }
    
    func join(natural joins: [(table: String, leftHandSide: String, rightHandSide: String)]) throws -> newMSSQL
    {
        if joinStatements == nil else                { throw MSSQLError.conditionAlreadyExists }
        
        joinStatements = [(joinType: MSSQLConjunctionClause, table: String, clause: MSSQLClause)]()
        
        for statement in joins
        {
            if statement.leftHandSide.contains("*") == false else  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.leftHandSide != "" else                   { throw MSSQLError.cannotUseEmptyValue }
            if statement.leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
            
            if statement.rightHandSide.contains("*") == false else { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.rightHandSide != "" else                  { throw MSSQLError.cannotUseEmptyValue }
            if statement.rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
            
            if statement.table.contains("*") == false else         { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.table != "" else                          { throw MSSQLError.cannotUseEmptyValue }
            if statement.table.characters.count <= 64 else         { throw MSSQLError.lengthTooLong }
            
            joinStatements.append((.natural, statement.table, MSSQLClause(leftHandSide: statement.leftHandSide, rightHandSide: statement.rightHandSide)))
        }
        
        return self
    }
    
    func join(leftOuter table: String, on leftHandSide: String, rightHandSide: String) throws -> newMSSQL
    {
        if joinStatements == nil else                { throw MSSQLError.conditionAlreadyExists }
        
        if leftHandSide.contains("*") == false else  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if leftHandSide != "" else                   { throw MSSQLError.cannotUseEmptyValue }
        if leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
        
        if rightHandSide.contains("*") == false else { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if rightHandSide != "" else                  { throw MSSQLError.cannotUseEmptyValue }
        if rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
        
        if table.contains("*") == false else         { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if table != "" else                          { throw MSSQLError.cannotUseEmptyValue }
        if table.characters.count <= 64 else         { throw MSSQLError.lengthTooLong }
        
        joinStatements = [(.leftOuter, table, MSSQLClause(leftHandSide: leftHandSide, rightHandSide: rightHandSide))]
        
        return self
    }
    
    func join(leftOuter joins: [(table: String, leftHandSide: String, rightHandSide: String)]) throws -> newMSSQL
    {
        if joinStatements == nil else                { throw MSSQLError.conditionAlreadyExists }
        
        joinStatements = [(joinType: MSSQLConjunctionClause, table: String, clause: MSSQLClause)]()
        
        for statement in joins
        {
            if statement.leftHandSide.contains("*") == false else  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.leftHandSide != "" else                   { throw MSSQLError.cannotUseEmptyValue }
            if statement.leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
            
            if statement.rightHandSide.contains("*") == false else { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.rightHandSide != "" else                  { throw MSSQLError.cannotUseEmptyValue }
            if statement.rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
            
            if statement.table.contains("*") == false else         { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.table != "" else                          { throw MSSQLError.cannotUseEmptyValue }
            if statement.table.characters.count <= 64 else         { throw MSSQLError.lengthTooLong }
            
            joinStatements.append((.leftOuter, statement.table, MSSQLClause(leftHandSide: statement.leftHandSide, rightHandSide: statement.rightHandSide)))
        }
        
        return self
    }
    
    func join(rightOuter table: String, on leftHandSide: String, rightHandSide: String) throws -> newMSSQL
    {
        if joinStatements == nil else                { throw MSSQLError.conditionAlreadyExists }
        
        if leftHandSide.contains("*") == false else  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if leftHandSide != "" else                   { throw MSSQLError.cannotUseEmptyValue }
        if leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
        
        if rightHandSide.contains("*") == false else { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if rightHandSide != "" else                  { throw MSSQLError.cannotUseEmptyValue }
        if rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
        
        if table.contains("*") == false else         { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if table != "" else                          { throw MSSQLError.cannotUseEmptyValue }
        if table.characters.count <= 64 else         { throw MSSQLError.lengthTooLong }
        
        joinStatements = [(.rightOuter, table, MSSQLClause(leftHandSide: leftHandSide, rightHandSide: rightHandSide))]
        
        return self
    }
    
    func join(rightOuter joins: [(table: String, leftHandSide: String, rightHandSide: String)]) throws -> newMSSQL
    {
        if joinStatements == nil else                { throw MSSQLError.conditionAlreadyExists }
        
        joinStatements = [(joinType: MSSQLConjunctionClause, table: String, clause: MSSQLClause)]()
        
        for statement in joins
        {
            if statement.leftHandSide.contains("*") == false else  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.leftHandSide != "" else                   { throw MSSQLError.cannotUseEmptyValue }
            if statement.leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
            
            if statement.rightHandSide.contains("*") == false else { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.rightHandSide != "" else                  { throw MSSQLError.cannotUseEmptyValue }
            if statement.rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
            
            if statement.table.contains("*") == false else         { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.table != "" else                          { throw MSSQLError.cannotUseEmptyValue }
            if statement.table.characters.count <= 64 else         { throw MSSQLError.lengthTooLong }
            
            joinStatements.append((.rightOuter, statement.table, MSSQLClause(leftHandSide: statement.leftHandSide, rightHandSide: statement.rightHandSide)))
        }
        
        return self
    }
    
    func join(fullOuter table: String, on leftHandSide: String, rightHandSide: String) throws -> newMSSQL
    {
        if joinStatements == nil else                { throw MSSQLError.conditionAlreadyExists }
        
        if leftHandSide.contains("*") == false else  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if leftHandSide != "" else                   { throw MSSQLError.cannotUseEmptyValue }
        if leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
        
        if rightHandSide.contains("*") == false else { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if rightHandSide != "" else                  { throw MSSQLError.cannotUseEmptyValue }
        if rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
        
        if table.contains("*") == false else         { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if table != "" else                          { throw MSSQLError.cannotUseEmptyValue }
        if table.characters.count <= 64 else         { throw MSSQLError.lengthTooLong }
        
        joinStatements = [(.fullOuter, table, MSSQLClause(leftHandSide: leftHandSide, rightHandSide: rightHandSide))]
        
        return self
    }
    
    func join(fullOuter joins: [(table: String, leftHandSide: String, rightHandSide: String)]) throws -> newMSSQL
    {
        if joinStatements == nil else                { throw MSSQLError.conditionAlreadyExists }
        
        joinStatements = [(joinType: MSSQLConjunctionClause, table: String, clause: MSSQLClause)]()
        
        for statement in joins
        {
            if statement.leftHandSide.contains("*") == false else  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.leftHandSide != "" else                   { throw MSSQLError.cannotUseEmptyValue }
            if statement.leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
            
            if statement.rightHandSide.contains("*") == false else { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.rightHandSide != "" else                  { throw MSSQLError.cannotUseEmptyValue }
            if statement.rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
            
            if statement.table.contains("*") == false else         { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.table != "" else                          { throw MSSQLError.cannotUseEmptyValue }
            if statement.table.characters.count <= 64 else         { throw MSSQLError.lengthTooLong }
            
            joinStatements.append((.fullOuter, statement.table, MSSQLClause(leftHandSide: statement.leftHandSide, rightHandSide: statement.rightHandSide)))
        }
        
        return self
    }
    
    func join(cross table: String, on leftHandSide: String, rightHandSide: String) throws -> newMSSQL
    {
        if joinStatements == nil else                { throw MSSQLError.conditionAlreadyExists }
        
        if leftHandSide.contains("*") == false else  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if leftHandSide != "" else                   { throw MSSQLError.cannotUseEmptyValue }
        if leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
        
        if rightHandSide.contains("*") == false else { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if rightHandSide != "" else                  { throw MSSQLError.cannotUseEmptyValue }
        if rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
        
        if table.contains("*") == false else         { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if table != "" else                          { throw MSSQLError.cannotUseEmptyValue }
        if table.characters.count <= 64 else         { throw MSSQLError.lengthTooLong }
        
        joinStatements = [(.cross, table, MSSQLClause(leftHandSide: leftHandSide, rightHandSide: rightHandSide))]
        
        return self
    }
    
    func join(cross joins: [(table: String, leftHandSide: String, rightHandSide: String)]) throws -> newMSSQL
    {
        if joinStatements == nil else                { throw MSSQLError.conditionAlreadyExists }
        
        joinStatements = [(joinType: MSSQLConjunctionClause, table: String, clause: MSSQLClause)]()
        
        for statement in joins
        {
            if statement.leftHandSide.contains("*") == false else  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.leftHandSide != "" else                   { throw MSSQLError.cannotUseEmptyValue }
            if statement.leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
            
            if statement.rightHandSide.contains("*") == false else { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.rightHandSide != "" else                  { throw MSSQLError.cannotUseEmptyValue }
            if statement.rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
            
            if statement.table.contains("*") == false else         { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.table != "" else                          { throw MSSQLError.cannotUseEmptyValue }
            if statement.table.characters.count <= 64 else         { throw MSSQLError.lengthTooLong }
            
            joinStatements.append((.cross, statement.table, MSSQLClause(leftHandSide: statement.leftHandSide, rightHandSide: statement.rightHandSide)))
        }
        
        return self
    }
    
    func join(inner table: String, on leftHandSide: String, rightHandSide: String) throws -> newMSSQL
    {
        if joinStatements == nil else                { throw MSSQLError.conditionAlreadyExists }
        
        if leftHandSide.contains("*") == false else  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if leftHandSide != "" else                   { throw MSSQLError.cannotUseEmptyValue }
        if leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
        
        if rightHandSide.contains("*") == false else { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if rightHandSide != "" else                  { throw MSSQLError.cannotUseEmptyValue }
        if rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
        
        if table.contains("*") == false else         { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if table != "" else                          { throw MSSQLError.cannotUseEmptyValue }
        if table.characters.count <= 64 else         { throw MSSQLError.lengthTooLong }
        
        joinStatements = [(.inner, table, MSSQLClause(leftHandSide: leftHandSide, rightHandSide: rightHandSide))]
        
        return self
    }
    
    func join(inner joins: [(table: String, leftHandSide: String, rightHandSide: String)]) throws -> newMSSQL
    {
        if joinStatements == nil else                { throw MSSQLError.conditionAlreadyExists }
        
        joinStatements = [(joinType: MSSQLConjunctionClause, table: String, clause: MSSQLClause)]()
        
        for statement in joins
        {
            if statement.leftHandSide.contains("*") == false else  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.leftHandSide != "" else                   { throw MSSQLError.cannotUseEmptyValue }
            if statement.leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
            
            if statement.rightHandSide.contains("*") == false else { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.rightHandSide != "" else                  { throw MSSQLError.cannotUseEmptyValue }
            if statement.rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
            
            if statement.table.contains("*") == false else         { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if statement.table != "" else                          { throw MSSQLError.cannotUseEmptyValue }
            if statement.table.characters.count <= 64 else         { throw MSSQLError.lengthTooLong }
            
            joinStatements.append((.inner, statement.table, MSSQLClause(leftHandSide: statement.leftHandSide, rightHandSide: statement.rightHandSide)))
        }
        
        return self
    }
    
    //MARK: - WHERE Constructors
    
    //MARK: Single
    
    func `where`(_ leftHandSide: String, equals rightHandSide: String) throws -> newMSSQL
    {
        if whereStatements == nil else               { throw MSSQLError.conditionAlreadyExists }
        if leftHandSide != "*" else                  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if rightHandSide != "*" else                 { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
        if rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
        
        whereStatements = [(.none, .equals, MSSQLClause(leftHandSide: leftHandSide, rightHandSide: rightHandSide))]
        
        return self
    }
    
    func `where`(_ leftHandSide: String, doesNotEqual rightHandSide: String) throws -> newMSSQL
    {
        if whereStatements == nil else               { throw MSSQLError.conditionAlreadyExists }
        if leftHandSide != "*" else                  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if rightHandSide != "*" else                 { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
        if rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
        
        whereStatements = [(.none, .notEquals, MSSQLClause(leftHandSide: leftHandSide, rightHandSide: rightHandSide))]
        
        return self
    }
    
    func `where`(_ leftHandSide: String, isLessThan rightHandSide: String) throws -> newMSSQL
    {
        if whereStatements == nil else               { throw MSSQLError.conditionAlreadyExists }
        if leftHandSide != "*" else                  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if rightHandSide != "*" else                 { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
        if rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
        
        whereStatements = [(.none, .lessThan, MSSQLClause(leftHandSide: leftHandSide, rightHandSide: rightHandSide))]
        
        return self
    }
    
    func `where`(_ leftHandSide: String, lessThanOrEqual rightHandSide: String) throws -> newMSSQL
    {
        if whereStatements == nil else               { throw MSSQLError.conditionAlreadyExists }
        if leftHandSide != "*" else                  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if rightHandSide != "*" else                 { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
        if rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
        
        whereStatements = [(.none, .lessThanOrEqualTo, MSSQLClause(leftHandSide: leftHandSide, rightHandSide: rightHandSide))]
        
        return self
    }
    
    func `where`(_ leftHandSide: String, greaterThan rightHandSide: String) throws -> newMSSQL
    {
        if whereStatements == nil else               { throw MSSQLError.conditionAlreadyExists }
        if leftHandSide != "*" else                  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if rightHandSide != "*" else                 { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
        if rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
        
        whereStatements = [(.none, .greaterThan, MSSQLClause(leftHandSide: leftHandSide, rightHandSide: rightHandSide))]
        
        return self
    }
    
    func `where`(_ leftHandSide: String, greaterThanOrEqual rightHandSide: String) throws -> newMSSQL
    {
        if whereStatements == nil else               { throw MSSQLError.conditionAlreadyExists }
        if leftHandSide != "*" else                  { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if rightHandSide != "*" else                 { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
        if leftHandSide.characters.count <= 64 else  { throw MSSQLError.lengthTooLong }
        if rightHandSide.characters.count <= 64 else { throw MSSQLError.lengthTooLong }
        
        whereStatements = [(.none, .greaterThanOrEqualTo, MSSQLClause(leftHandSide: leftHandSide, rightHandSide: rightHandSide))]
        
        return self
    }
    
    //MARK: AND
    
    func `where`(_ leftHandSides: [String], andEquals rightHandSides: [String]) throws -> newMSSQL
    {
        if leftHandSides.count == rightHandSides.count else  { throw MSSQLError.conditionsMustBeEqual }
        
        if whereStatements == nil else                       { throw MSSQLError.conditionAlreadyExists }
        
        whereStatements = [(conjunction: MSSQLConjunctionClause, joiner: MSSQLConjunctionClause, clause: MSSQLClause)]()
        
        for leftHandSide in leftHandSides
        {
            if leftHandSide != "*" else                      { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if leftHandSide.characters.count <= 64 else      { throw MSSQLError.lengthTooLong }
        }
        
        for rightHandSide in rightHandSides
        {
            if rightHandSide != "*" else                     { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            
            if rightHandSide.characters.count <= 64 else     { throw MSSQLError.lengthTooLong }
        }
        
        for index in 0..<leftHandSides.count - 1
        {
            let lhs = leftHandSides[index]
            let rhs = rightHandSides[index]
            
            whereStatements.append((.and, .equals, MSSQLClause(leftHandSide: lhs, rightHandSide: rhs)))
        }
        
        whereStatements.append((.none, .equals, MSSQLClause(leftHandSide: leftHandSides[leftHandSides.count - 1], rightHandSide: rightHandSides[rightHandSides.count - 1])))
        
        return self
    }
    
    func `where`(_ leftHandSides: [String], andDoesNotEqual rightHandSides: [String]) throws -> newMSSQL
    {
        if leftHandSides.count == rightHandSides.count else  { throw MSSQLError.conditionsMustBeEqual }
        
        if whereStatements == nil else                       { throw MSSQLError.conditionAlreadyExists }
        
        whereStatements = [(conjunction: MSSQLConjunctionClause, joiner: MSSQLConjunctionClause, clause: MSSQLClause)]()
        
        for leftHandSide in leftHandSides
        {
            if leftHandSide != "*" else                      { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if leftHandSide.characters.count <= 64 else      { throw MSSQLError.lengthTooLong }
        }
        
        for rightHandSide in rightHandSides
        {
            if rightHandSide != "*" else                     { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            
            if rightHandSide.characters.count <= 64 else     { throw MSSQLError.lengthTooLong }
        }
        
        for index in 0..<leftHandSides.count - 1
        {
            let lhs = leftHandSides[index]
            let rhs = rightHandSides[index]
            
            whereStatements.append((.and, .notEquals, MSSQLClause(leftHandSide: lhs, rightHandSide: rhs)))
        }
        
        whereStatements.append((.none, .notEquals, MSSQLClause(leftHandSide: leftHandSides[leftHandSides.count - 1], rightHandSide: rightHandSides[rightHandSides.count - 1])))
        
        return self
    }
    
    func `where`(_ leftHandSides: [String], andGreaterThan rightHandSides: [String]) throws -> newMSSQL
    {
        if leftHandSides.count == rightHandSides.count else  { throw MSSQLError.conditionsMustBeEqual }
        
        if whereStatements == nil else                       { throw MSSQLError.conditionAlreadyExists }
        
        whereStatements = [(conjunction: MSSQLConjunctionClause, joiner: MSSQLConjunctionClause, clause: MSSQLClause)]()
        
        for leftHandSide in leftHandSides
        {
            if leftHandSide != "*" else                      { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if leftHandSide.characters.count <= 64 else      { throw MSSQLError.lengthTooLong }
        }
        
        for rightHandSide in rightHandSides
        {
            if rightHandSide != "*" else                     { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            
            if rightHandSide.characters.count <= 64 else     { throw MSSQLError.lengthTooLong }
        }
        
        for index in 0..<leftHandSides.count - 1
        {
            let lhs = leftHandSides[index]
            let rhs = rightHandSides[index]
            
            whereStatements.append((.and, .greaterThan, MSSQLClause(leftHandSide: lhs, rightHandSide: rhs)))
        }
        
        whereStatements.append((.none, .greaterThan, MSSQLClause(leftHandSide: leftHandSides[leftHandSides.count - 1], rightHandSide: rightHandSides[rightHandSides.count - 1])))
        
        return self
    }
    
    func `where`(_ leftHandSides: [String], andGreaterThanOrEqual rightHandSides: [String]) throws -> newMSSQL
    {
        if leftHandSides.count == rightHandSides.count else  { throw MSSQLError.conditionsMustBeEqual }
        
        if whereStatements == nil else                       { throw MSSQLError.conditionAlreadyExists }
        
        whereStatements = [(conjunction: MSSQLConjunctionClause, joiner: MSSQLConjunctionClause, clause: MSSQLClause)]()
        
        for leftHandSide in leftHandSides
        {
            if leftHandSide != "*" else                      { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if leftHandSide.characters.count <= 64 else      { throw MSSQLError.lengthTooLong }
        }
        
        for rightHandSide in rightHandSides
        {
            if rightHandSide != "*" else                     { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            
            if rightHandSide.characters.count <= 64 else     { throw MSSQLError.lengthTooLong }
        }
        
        for index in 0..<leftHandSides.count - 1
        {
            let lhs = leftHandSides[index]
            let rhs = rightHandSides[index]
            
            whereStatements.append((.and, .greaterThanOrEqualTo, MSSQLClause(leftHandSide: lhs, rightHandSide: rhs)))
        }
        
        whereStatements.append((.none, .greaterThanOrEqualTo, MSSQLClause(leftHandSide: leftHandSides[leftHandSides.count - 1], rightHandSide: rightHandSides[rightHandSides.count - 1])))
        
        return self
    }
    
    func `where`(_ leftHandSides: [String], andLessThan rightHandSides: [String]) throws -> newMSSQL
    {
        if leftHandSides.count == rightHandSides.count else  { throw MSSQLError.conditionsMustBeEqual }
        
        if whereStatements == nil else                       { throw MSSQLError.conditionAlreadyExists }
        
        whereStatements = [(conjunction: MSSQLConjunctionClause, joiner: MSSQLConjunctionClause, clause: MSSQLClause)]()
        
        for leftHandSide in leftHandSides
        {
            if leftHandSide != "*" else                      { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if leftHandSide.characters.count <= 64 else      { throw MSSQLError.lengthTooLong }
        }
        
        for rightHandSide in rightHandSides
        {
            if rightHandSide != "*" else                     { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            
            if rightHandSide.characters.count <= 64 else     { throw MSSQLError.lengthTooLong }
        }
        
        for index in 0..<leftHandSides.count - 1
        {
            let lhs = leftHandSides[index]
            let rhs = rightHandSides[index]
            
            whereStatements.append((.and, .lessThan, MSSQLClause(leftHandSide: lhs, rightHandSide: rhs)))
        }
        
        whereStatements.append((.none, .lessThan, MSSQLClause(leftHandSide: leftHandSides[leftHandSides.count - 1], rightHandSide: rightHandSides[rightHandSides.count - 1])))
        
        return self
    }
    
    func `where`(_ leftHandSides: [String], andLessThanOrEqual rightHandSides: [String]) throws -> newMSSQL
    {
        if leftHandSides.count == rightHandSides.count else  { throw MSSQLError.conditionsMustBeEqual }
        
        if whereStatements == nil else                       { throw MSSQLError.conditionAlreadyExists }
        
        whereStatements = [(conjunction: MSSQLConjunctionClause, joiner: MSSQLConjunctionClause, clause: MSSQLClause)]()
        
        for leftHandSide in leftHandSides
        {
            if leftHandSide != "*" else                      { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if leftHandSide.characters.count <= 64 else      { throw MSSQLError.lengthTooLong }
        }
        
        for rightHandSide in rightHandSides
        {
            if rightHandSide != "*" else                     { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            
            if rightHandSide.characters.count <= 64 else     { throw MSSQLError.lengthTooLong }
        }
        
        for index in 0..<leftHandSides.count - 1
        {
            let lhs = leftHandSides[index]
            let rhs = rightHandSides[index]
            
            whereStatements.append((.and, .lessThanOrEqualTo, MSSQLClause(leftHandSide: lhs, rightHandSide: rhs)))
        }
        
        whereStatements.append((.none, .lessThanOrEqualTo, MSSQLClause(leftHandSide: leftHandSides[leftHandSides.count - 1], rightHandSide: rightHandSides[rightHandSides.count - 1])))
        
        return self
    }
    
    //MARK: OR
    
    func `where`(_ leftHandSides: [String], orEquals rightHandSides: [String]) throws -> newMSSQL
    {
        if leftHandSides.count == rightHandSides.count else  { throw MSSQLError.conditionsMustBeEqual }
        
        if whereStatements == nil else                       { throw MSSQLError.conditionAlreadyExists }
        
        whereStatements = [(conjunction: MSSQLConjunctionClause, joiner: MSSQLConjunctionClause, clause: MSSQLClause)]()
        
        for leftHandSide in leftHandSides
        {
            if leftHandSide != "*" else                      { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if leftHandSide.characters.count <= 64 else      { throw MSSQLError.lengthTooLong }
        }
        
        for rightHandSide in rightHandSides
        {
            if rightHandSide != "*" else                     { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            
            if rightHandSide.characters.count <= 64 else     { throw MSSQLError.lengthTooLong }
        }
        
        for index in 0..<leftHandSides.count - 1
        {
            let lhs = leftHandSides[index]
            let rhs = rightHandSides[index]
            
            whereStatements.append((.or, .equals, MSSQLClause(leftHandSide: lhs, rightHandSide: rhs)))
        }
        
        whereStatements.append((.none, .equals, MSSQLClause(leftHandSide: leftHandSides[leftHandSides.count - 1], rightHandSide: rightHandSides[rightHandSides.count - 1])))
        
        return self
    }
    
    func `where`(_ leftHandSides: [String], orNotEqual rightHandSides: [String]) throws -> newMSSQL
    {
        if leftHandSides.count == rightHandSides.count else  { throw MSSQLError.conditionsMustBeEqual }
        
        if whereStatements == nil else                       { throw MSSQLError.conditionAlreadyExists }
        
        whereStatements = [(conjunction: MSSQLConjunctionClause, joiner: MSSQLConjunctionClause, clause: MSSQLClause)]()
        
        for leftHandSide in leftHandSides
        {
            if leftHandSide != "*" else                      { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if leftHandSide.characters.count <= 64 else      { throw MSSQLError.lengthTooLong }
        }
        
        for rightHandSide in rightHandSides
        {
            if rightHandSide != "*" else                     { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            
            if rightHandSide.characters.count <= 64 else     { throw MSSQLError.lengthTooLong }
        }
        
        for index in 0..<leftHandSides.count - 1
        {
            let lhs = leftHandSides[index]
            let rhs = rightHandSides[index]
            
            whereStatements.append((.or, .notEquals, MSSQLClause(leftHandSide: lhs, rightHandSide: rhs)))
        }
        
        whereStatements.append((.none, .notEquals, MSSQLClause(leftHandSide: leftHandSides[leftHandSides.count - 1], rightHandSide: rightHandSides[rightHandSides.count - 1])))
        
        return self
    }
    
    func `where`(_ leftHandSides: [String], orGreaterThan rightHandSides: [String]) throws -> newMSSQL
    {
        if leftHandSides.count == rightHandSides.count else  { throw MSSQLError.conditionsMustBeEqual }
        
        if whereStatements == nil else                       { throw MSSQLError.conditionAlreadyExists }
        
        whereStatements = [(conjunction: MSSQLConjunctionClause, joiner: MSSQLConjunctionClause, clause: MSSQLClause)]()
        
        for leftHandSide in leftHandSides
        {
            if leftHandSide != "*" else                      { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if leftHandSide.characters.count <= 64 else      { throw MSSQLError.lengthTooLong }
        }
        
        for rightHandSide in rightHandSides
        {
            if rightHandSide != "*" else                     { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            
            if rightHandSide.characters.count <= 64 else     { throw MSSQLError.lengthTooLong }
        }
        
        for index in 0..<leftHandSides.count - 1
        {
            let lhs = leftHandSides[index]
            let rhs = rightHandSides[index]
            
            whereStatements.append((.or, .greaterThan, MSSQLClause(leftHandSide: lhs, rightHandSide: rhs)))
        }
        
        whereStatements.append((.none, .greaterThan, MSSQLClause(leftHandSide: leftHandSides[leftHandSides.count - 1], rightHandSide: rightHandSides[rightHandSides.count - 1])))
        
        return self
    }
    
    func `where`(_ leftHandSides: [String], orGreaterThanOrEqual rightHandSides: [String]) throws -> newMSSQL
    {
        if leftHandSides.count == rightHandSides.count else  { throw MSSQLError.conditionsMustBeEqual }
        
        if whereStatements == nil else                       { throw MSSQLError.conditionAlreadyExists }
        
        whereStatements = [(conjunction: MSSQLConjunctionClause, joiner: MSSQLConjunctionClause, clause: MSSQLClause)]()
        
        for leftHandSide in leftHandSides
        {
            if leftHandSide != "*" else                      { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if leftHandSide.characters.count <= 64 else      { throw MSSQLError.lengthTooLong }
        }
        
        for rightHandSide in rightHandSides
        {
            if rightHandSide != "*" else                     { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            
            if rightHandSide.characters.count <= 64 else     { throw MSSQLError.lengthTooLong }
        }
        
        for index in 0..<leftHandSides.count - 1
        {
            let lhs = leftHandSides[index]
            let rhs = rightHandSides[index]
            
            whereStatements.append((.or, .greaterThanOrEqualTo, MSSQLClause(leftHandSide: lhs, rightHandSide: rhs)))
        }
        
        whereStatements.append((.none, .greaterThanOrEqualTo, MSSQLClause(leftHandSide: leftHandSides[leftHandSides.count - 1], rightHandSide: rightHandSides[rightHandSides.count - 1])))
        
        return self
    }
    
    func `where`(_ leftHandSides: [String], orLessThan rightHandSides: [String]) throws -> newMSSQL
    {
        if leftHandSides.count == rightHandSides.count else  { throw MSSQLError.conditionsMustBeEqual }
        
        if whereStatements == nil else                       { throw MSSQLError.conditionAlreadyExists }
        
        whereStatements = [(conjunction: MSSQLConjunctionClause, joiner: MSSQLConjunctionClause, clause: MSSQLClause)]()
        
        for leftHandSide in leftHandSides
        {
            if leftHandSide != "*" else                      { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if leftHandSide.characters.count <= 64 else      { throw MSSQLError.lengthTooLong }
        }
        
        for rightHandSide in rightHandSides
        {
            if rightHandSide != "*" else                     { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            
            if rightHandSide.characters.count <= 64 else     { throw MSSQLError.lengthTooLong }
        }
        
        for index in 0..<leftHandSides.count - 1
        {
            let lhs = leftHandSides[index]
            let rhs = rightHandSides[index]
            
            whereStatements.append((.or, .lessThan, MSSQLClause(leftHandSide: lhs, rightHandSide: rhs)))
        }
        
        whereStatements.append((.none, .lessThan, MSSQLClause(leftHandSide: leftHandSides[leftHandSides.count - 1], rightHandSide: rightHandSides[rightHandSides.count - 1])))
        
        return self
    }
    
    func `where`(_ leftHandSides: [String], orLessThanOrEqual rightHandSides: [String]) throws -> newMSSQL
    {
        if leftHandSides.count == rightHandSides.count else  { throw MSSQLError.conditionsMustBeEqual }
        
        if whereStatements == nil else                       { throw MSSQLError.conditionAlreadyExists }
        
        whereStatements = [(conjunction: MSSQLConjunctionClause, joiner: MSSQLConjunctionClause, clause: MSSQLClause)]()
        
        for leftHandSide in leftHandSides
        {
            if leftHandSide != "*" else                      { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if leftHandSide.characters.count <= 64 else      { throw MSSQLError.lengthTooLong }
        }
        
        for rightHandSide in rightHandSides
        {
            if rightHandSide != "*" else                     { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            
            if rightHandSide.characters.count <= 64 else     { throw MSSQLError.lengthTooLong }
        }
        
        for index in 0..<leftHandSides.count - 1
        {
            let lhs = leftHandSides[index]
            let rhs = rightHandSides[index]
            
            whereStatements.append((.or, .lessThanOrEqualTo, MSSQLClause(leftHandSide: lhs, rightHandSide: rhs)))
        }
        
        whereStatements.append((.none, .lessThanOrEqualTo, MSSQLClause(leftHandSide: leftHandSides[leftHandSides.count - 1], rightHandSide: rightHandSides[rightHandSides.count - 1])))
        
        return self
    }
    
    //MARK: Custom
    
    func `where`(custom: [(conjunction: MSSQLConjunctionClause, joiner: MSSQLConjunctionClause, clause: MSSQLClause)]) throws -> newMSSQL
    {
        if custom.count != 0 else                                    { throw MSSQLError.cannotUseEmptyValue }
        
        if whereStatements == nil else                               { throw MSSQLError.conditionAlreadyExists }
        
        for whereClause in custom
        {
            let SQLClause = whereClause.clause
            
            if SQLClause.leftHandSide != "*" else                    { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if SQLClause.leftHandSide.characters.count <= 64 else    { throw MSSQLError.lengthTooLong }
            if SQLClause.rightHandSide != "*" else                   { throw MSSQLError.cannotUseAllRowsSQLSpecifier }
            if SQLClause.rightHandSide.characters.count <= 64 else   { throw MSSQLError.lengthTooLong }
        }
        
        whereStatements = custom
        
        return self
    }
}
