The database/web server communicator framework.

Structure:

database
|- DFDatabase
|- DFDatabaseCallbackDelegate
|- DFError
|- dfDatabaseFramework
   |- DFSQL
      |- DFSQL
      |- DFSQLClauseStruct
      |- DFSQLConjunctionClause
      |- DFSQLError
      |- JoinParam
      |- JoinStruct
   |- Utilities
      |- DFDataSizePrinter
   |- WebServerCommunicator
      |- DFDataDownloader
      |- DFDataUploader
      |- DFDataUploaderReturnStatus