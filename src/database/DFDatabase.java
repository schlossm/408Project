package database;
import java.lang.reflect.Field;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import dfDatabaseFramework.DFSQL.DFSQL;
import dfDatabaseFramework.Utilities.DFDataSizePrinter;
import dfDatabaseFramework.WebServerCommunicator.DFDataDownloader;
import dfDatabaseFramework.WebServerCommunicator.DFDataUploader;

public class DFDatabase
{
	public static final DFDatabase defaultDatabase = new DFDatabase();
	
	private final String website			= "";
	private final String readFile			= "ReadFile.php";
	private final String writeFile			= "WriteFile.php";
	private final String websiteUserName	= "";
	private final String websiteUserPass	= "";
    private final String databaseUserPass	= "";
    private final String encryptionKey		= "";
    private final char[] hexArray			= "0123456789ABCDEF".toCharArray();
    
    public final DFDataDownloader dataDownloader	= new DFDataDownloader(website, readFile, websiteUserName, websiteUserPass, databaseUserPass);
	public final DFDataUploader dataUploader		= new DFDataUploader(website, writeFile, websiteUserName, websiteUserPass, databaseUserPass);
	
	public final DFDataSizePrinter dataSizePrinter = DFDataSizePrinter.current;
	
	Cipher encryptor, decryptor;
	
	private DFDatabase() 
	{ 
		try 
		{			
			Security.addProvider(new BouncyCastleProvider());
			
			encryptor = Cipher.getInstance("AES/CBC/PKCS5Padding");
			decryptor = Cipher.getInstance("AES/CBC/PKCS5Padding");
			
			byte[] key = encryptionKey.getBytes();
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16); // use only first 128 bit

			SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
			
			byte[] iv = new byte[16];
			SecureRandom random = new SecureRandom();
			random.nextBytes(iv);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
			encryptor.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
			decryptor.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
		} 
		catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e)
		{
			e.printStackTrace();
		}
	}
    
    public void executeSQLStatement(DFSQL statement)
    {
    	try
    	{
    		Field field = DFSQL.class.getDeclaredField("updateStatements");
    		field.setAccessible(true);
    		field.get(statement);
    		
    		dataUploader.uploadDataWith(statement);
    	}
    	catch (Exception e)
    	{
    		if (e.getClass() == NullPointerException.class)
    		{
    			try
    	    	{
    	    		Field field = DFSQL.class.getDeclaredField("insertRows");
    	    		Field field2 = DFSQL.class.getDeclaredField("insertData");
    	    		field.setAccessible(true);
    	    		field2.setAccessible(true);
    	    		field.get(statement);
    	    		field2.get(statement);
    	    		
    	    		dataUploader.uploadDataWith(statement);
    	    	}
    	    	catch (Exception e2)
    	    	{
    	    		dataDownloader.downloadDataWith(statement);
    	    		return;
    	    	}
    		}
    		return;
    	}
    	return;
    }
    
    public String encryptString(String decryptedString)
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
    
    public String decryptString(String encryptedString)
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
        for ( int j = 0; j < bytes.length; j++ ) {
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
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
