package database;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import database.dfDatabaseFramework.DFSQL.DFSQL;
import database.dfDatabaseFramework.Utilities.DFDataSizePrinter;
import database.dfDatabaseFramework.WebServerCommunicator.DFDataDownloader;
import database.dfDatabaseFramework.WebServerCommunicator.DFDataUploader;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.security.*;
import java.util.Arrays;

/**
 * The main database communicator class.  All communication with the database will run through this class
 */
@SuppressWarnings("unused")
public class DFDatabase
{
	/**
	 * The singleton instance of DFDatabase
	 */
	public static final DFDatabase defaultDatabase = new DFDatabase();
	
	private final String website			= "http://debateforum.michaelschlosstech.com";
	private final String readFile			= "ReadFile.php";
	private final String writeFile			= "WriteFile.php";
	private final String websiteUserName	= "DFJavaApp";
	private final String websiteUserPass	= "3xT-MA8-HEm-sTd";
    private final String databaseUserPass	= "3xT-MA8-HEm-sTd";
	private final char[] hexArray			= "0123456789ABCDEF".toCharArray();
    
    private final DFDataDownloader dataDownloader	= new DFDataDownloader(website, readFile, websiteUserName, databaseUserPass);
	private final DFDataUploader   dataUploader		= new DFDataUploader(website, writeFile, websiteUserName, databaseUserPass);

	/**
	 * A reference to the data size printer object
	 */
	public final DFDataSizePrinter dataSizePrinter = DFDataSizePrinter.current;

	/**
	 * Wanna debug DFDatabase? Set this flag to 1.
	 */
	public int debug = 0;
	
	private Cipher encryptor, decryptor;

	private DFDatabase()
	{ 
		try 
		{
			Security.addProvider(new BouncyCastleProvider());

			encryptor = Cipher.getInstance("AES/CBC/PKCS5Padding");
			decryptor = Cipher.getInstance("AES/CBC/PKCS5Padding");

			String encryptionKey = "A97525E2C26F8B2DDFDF8212F1D62";
			byte[] key = encryptionKey.getBytes();
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16); // use only first 128 bit

			SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

			byte[] iv = new byte[16];
			SecureRandom random = new SecureRandom();
			random.nextBytes(iv);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
			System.out.print(bytesToHex(ivParameterSpec.getIV()));
			encryptor.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
			decryptor.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

			Authenticator.setDefault (new Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication (websiteUserName, websiteUserPass.toCharArray());
			    }
			});
		} 
		catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @deprecated use `execute(_:, _:)` instead
	 * @param statement the SQL statement to execute backend side
	 * @param delegate the delegate object that will respond to data changes.  This object must conform to the DFDatabaseCallbackDelegate interface
	 */
    @Deprecated public void executeSQLStatement(DFSQL statement, DFDatabaseCallbackDelegate delegate)
    {
		System.out.println(getMethodName(2) + " is now deprecated.  Use `execute(_:, _:)` instead.  Will call new method for you this time");
		execute(statement, delegate);
    }

	/**
	 * @param SQLStatement the SQL statement to execute backend side
	 * @param delegate the delegate object that will respond to data changes.  This object must conform to the DFDatabaseCallbackDelegate interface
	 */
	public void execute(@NotNull DFSQL SQLStatement, DFDatabaseCallbackDelegate delegate)
	{
		if (SQLStatement == null)
		{
			delegate.returnedData(null, new DFError(-3, "Null DFSQL object delivered"));
			return;
		}

		if (SQLStatement.formattedSQLStatement().contains("UPDATE") || SQLStatement.formattedSQLStatement().contains("INSERT"))
		{
			dataUploader.delegate = delegate;
			dataUploader.uploadDataWith(SQLStatement);
		}
		else
		{
			dataDownloader.delegate = delegate;
			dataDownloader.downloadDataWith(SQLStatement);
		}
	}

	public String encryptString(String decryptedString) { return decryptedString; }
	public String decryptString(String encryptedString) { return encryptedString; }

	public String _encryptString(String decryptedString)
    {
    	byte[] byteText = decryptedString.getBytes();
		try
		{
			byte[] byteCipherText = encryptor.doFinal(byteText);
			return bytesToHex(byteCipherText);
		} 
		catch (IllegalBlockSizeException | BadPaddingException e) 
		{
			e.printStackTrace();
			return "";
		}
    }

	public String _decryptString(String encryptedString)
    {
    	byte[] byteText = hexToBytes(encryptedString);
		try
		{
			byte[] byteCipherText = decryptor.doFinal(byteText);
			return new String(byteCipherText);
		} 
		catch (IllegalBlockSizeException | BadPaddingException e) 
		{
			e.printStackTrace();
			return "";
		}
    }
    
    private String bytesToHex(byte[] bytes) 
    {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ )
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    private byte[] hexToBytes(String s) 
    {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) 
        {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

	public static String getMethodName(final int numberOfParameters)
	{
		final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("`");
		stringBuilder.append(ste[Integer.min(ste.length - 1, Integer.max(2, 0))].getMethodName());
		if (numberOfParameters > 0)
		{
			stringBuilder.append("(_:");
			for (int i = 0; i < numberOfParameters - 1; i++)
			{
				stringBuilder.append(", _:");
			}
			stringBuilder.append(")`");
		}

		return stringBuilder.toString();
	}
}
