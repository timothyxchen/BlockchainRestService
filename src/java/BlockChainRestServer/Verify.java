package project2task5;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 *
 * @author chentianxin
 * 
 *  Verify.java provides capabilities to verify the 
 *  messages. The class has two private members: RSA e and n.
 *  It has two method, one verifies if the user input of concatenated public
 *  keys generate the user id; another verifies if the signature
 *  matches the encrypted message.
 */
public class Verify {
        
    private BigInteger e,n; // n is the modulus for both the private and public keys,
    
    /**
     * 
     * @param messageToCheck The message that is encrypted
     * @param encryptedHashStr The signature, generated when encrypting the message
     * @return Boolean True if the encrypted message and signature matches
     * @throws Exception 
     */
    public boolean verify_sig(String messageToCheck, String encryptedHashStr) throws Exception {     
        BigInteger encryptedHash = new BigInteger(encryptedHashStr);
        // Decrypt it
        BigInteger decryptedHash = encryptedHash.modPow(e, n);
        
        // Get the bytes from messageToCheck
        byte[] bytesOfMessageToCheck = messageToCheck.getBytes("UTF-8");
        
        // compute the digest of the message with SHA-256
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        
        byte[] messageToCheckDigest = md.digest(bytesOfMessageToCheck);
        
        // messageToCheckDigest is a full SHA-256 digest
        // take two bytes from SHA-256 and add a zero byte    
        byte[] messageDigest = new byte[messageToCheckDigest.length+1];
        messageDigest[0]=0;
        for (int i =0;i<messageToCheckDigest.length;i++){
            messageDigest[i+1]=messageToCheckDigest[i];
        }
        
        // Make it a big int
        BigInteger bigIntegerToCheck = new BigInteger(messageDigest);
        
        // inform the client on how the two compare
        if(bigIntegerToCheck.compareTo(decryptedHash) == 0) {
            
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * 
     * @param keys The public key pairs that are stored as the private integer: e and n
     * @param test_id The id that is provided by the client, needs to be checked with the computed id
     * @return Boolean True if the concatenated keys and id matches, and False if not matched 
     * @throws Exception 
     */
    public static boolean verify_id(String keys, String test_id) throws Exception {
        
        //get the hash value of the concatenated keys
        String true_id = ComputeSHA_256_as_Hex_String(keys); 
        
        //if two ids are the same, then return true; otherwise, return false
        if(true_id.compareTo(test_id) == 0) {   
            return true;
        }
        else {
            return false;
        }
    }
    
    
    public static String ComputeSHA_256_as_Hex_String(String text) { 
        try { 
             // Create a SHA256 digest
             MessageDigest digest;
             digest = MessageDigest.getInstance("SHA-256");
             // allocate room for the result of the hash
             byte[] hashBytes;
             // perform the hash
             digest.update(text.getBytes("UTF-8"), 0, text.length());
             // collect result
             hashBytes = digest.digest();
             return convertToHex(Arrays.copyOfRange(hashBytes, hashBytes.length-20,hashBytes.length));
        }
        catch (NoSuchAlgorithmException nsa) {
            System.out.println("No such algorithm exception thrown " + nsa);
        }
        catch (UnsupportedEncodingException uee ) {
            System.out.println("Unsupported encoding exception thrown " + uee);
        }
        return null;
    } 
    // code from Stack overflow
    // converts a byte array to a string.
    // each nibble (4 bits) of the byte array is represented 
    // by a hex characer (0,1,2,3,...,9,a,b,c,d,e,f)
    private static String convertToHex(byte[] data) { 
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do { 
                if ((0 <= halfbyte) && (halfbyte <= 9)) 
                    buf.append((char) ('0' + halfbyte));
                else 
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        } 
        return buf.toString();
    } 
    
    public void set_e(BigInteger e){
        this.e=e;
    }

    public void set_n(BigInteger n){
        this.n=n;
    }    
}
