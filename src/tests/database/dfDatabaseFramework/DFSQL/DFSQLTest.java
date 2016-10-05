package tests.database.dfDatabaseFramework.DFSQL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import database.dfDatabaseFramework.DFSQL.*;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by michaelschloss on 9/22/16.
 */
public class DFSQLTest
{
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void append() throws Exception {

    }

    @Test
    public void formattedSQLStatement() throws Exception {
        DFSQL statement = new DFSQL().select("Blah").from("Blah");
        assertEquals(statement.formattedSQLStatement(), "SELECT `Blah` FROM `Blah`;");
    }

    @Test
    public void select() throws Exception {
        DFSQL statement = new DFSQL().select("Blah");
        assertEquals(statement.formattedSQLStatement(), "SELECT `Blah`;");
    }

    @Test
    public void select1() throws Exception {
        DFSQL statement = new DFSQL().select(new String[]{"Blah", "BlahBlah"});
        assertEquals(statement.formattedSQLStatement(), "SELECT `Blah`, `BlahBlah`;");
    }

    @Test
    public void from() throws Exception {
        DFSQL statement = new DFSQL().select("Blah").from("Blah");
        assertEquals(statement.formattedSQLStatement(), "SELECT `Blah` FROM `Blah`;");
    }

    @Test
    public void from1() throws Exception {
        DFSQL statement = new DFSQL().select("Blah").from(new String[]{"Blah", "BlahBlah"});
        assertEquals(statement.formattedSQLStatement(), "SELECT `Blah` FROM `Blah`, `BlahBlah`;");
    }

    @Test
    public void update() throws Exception {
        DFSQL statement = new DFSQL().update("Blah", "userID", "1");
        assertEquals(statement.formattedSQLStatement(), "UPDATE `Blah` SET `userID`=1;");
    }

    @Test
    public void update1() throws Exception {
        DFSQL statement = new DFSQL().update("Blah", new DFSQLClauseStruct[] { new DFSQLClauseStruct("userID", "1") });
        assertEquals(statement.formattedSQLStatement(), "UPDATE `Blah` SET `userID`=1;");
    }

    @Test
    public void insert() throws Exception {
        DFSQL statement = new DFSQL().insert("User", new String[] { "mschlos", "blahblah20"}, new String[] { "userID", "password" });
        assertEquals(statement.formattedSQLStatement(), "INSERT INTO `User`(`userID`,`password`) VALUES ('mschlos','blahblah20');");
    }

    @Test
    public void joinOn() throws Exception {

    }

    @Test
    public void joinOn1() throws Exception {

    }

    @Test
    public void joinNatural() throws Exception {

    }

    @Test
    public void joinNatural1() throws Exception {

    }

    @Test
    public void joinLeftOuter() throws Exception {

    }

    @Test
    public void joinLeftOuter1() throws Exception {

    }

    @Test
    public void joinRightOuter() throws Exception {

    }

    @Test
    public void joinRightOuter1() throws Exception {

    }

    @Test
    public void joinFullOuter() throws Exception {

    }

    @Test
    public void joinFullOuter1() throws Exception {

    }

    @Test
    public void joinCross() throws Exception {

    }

    @Test
    public void joinCross1() throws Exception {

    }

    @Test
    public void joinInner() throws Exception {

    }

    @Test
    public void joinInner1() throws Exception {

    }

    @Test
    public void whereEquals() throws Exception {

    }

    @Test
    public void whereDoesNotEqual() throws Exception {

    }

    @Test
    public void whereIsLessThan() throws Exception {

    }

    @Test
    public void whereLessThanOrEqual() throws Exception {

    }

    @Test
    public void whereGreaterThan() throws Exception {

    }

    @Test
    public void whereGreaterThanOrEqual() throws Exception {

    }

    @Test
    public void whereAndEquals() throws Exception {

    }

    @Test
    public void whereAndDoesNotEqual() throws Exception {

    }

    @Test
    public void whereAndGreaterThan() throws Exception {

    }

    @Test
    public void whereAndGreaterThanOrEqual() throws Exception {

    }

    @Test
    public void whereAndLessThan() throws Exception {

    }

    @Test
    public void whereAndLessThanOrEqual() throws Exception {

    }

    @Test
    public void whereOrEquals() throws Exception {

    }

    @Test
    public void whereOrNotEqual() throws Exception {

    }

    @Test
    public void whereOrGreaterThan() throws Exception {

    }

    @Test
    public void whereOrGreaterThanOrEqual() throws Exception {

    }

    @Test
    public void whereOrLessThan() throws Exception {

    }

    @Test
    public void whereOrLessThanOrEqual() throws Exception {

    }

    @Test
    public void whereCustom() throws Exception {

    }

}