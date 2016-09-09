import dfDatabase.*;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			DFSQL sql = new DFSQL().update("Hello", "userID", "531126");
			sql.formattedSQLStatement();
		} catch (DFSQLError e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
