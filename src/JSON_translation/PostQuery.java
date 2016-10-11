package JSON_translation;

import javax.print.attribute.standard.RequestingUserName;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.org.apache.bcel.internal.generic.NEW;

import UI.UIStrings;
import UIKit.DFNotificationCenter;
import database.DFDatabase;
import database.DFDatabaseCallbackDelegate;
import database.DFError;
import database.dfDatabaseFramework.DFSQL.DFSQL;
import database.dfDatabaseFramework.DFSQL.DFSQLError;
import database.dfDatabaseFramework.WebServerCommunicator.DFDataUploaderReturnStatus;
import objects.User;
import objects.User.UserType;

import objects.Debate.*;
import objects.Post.*;

public class PostQuery implements DFDatabaseCallbackDelegate {

	@Override
	public void returnedData(JsonObject jsonObject, DFError error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadStatus(DFDataUploaderReturnStatus success, DFError error) {
		// TODO Auto-generated method stub
		
	}

}
