package project2task5;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 *
 * @author chentianxin
 * 
 *  SignID.java provides capabilities to sign messages.
 *  The sign method has only two private members: RSA d and n.
 * 
 *  For signing: the SignID object is constructed with RSA
 *  keys (e,d,n). These keys are not created here but are passed in by the caller.
 *  Then, a caller can sign a message - the string returned by the sign
 *  method is evidence that the signer has the associated private key.
 *  After a message is signed, the message and the string may be transmitted
 *  or stored. 
 *  The signature is represented by a base 10 integer.
 */
public class SignID {
    
    private BigInteger d,n;// n is the modulus for both the private and public keys,d is the exponent of the private key
    public String sign(String message) throws Exception {
        
        // compute the digest with SHA-256
        byte[] bytesOfMessage = message.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bigDigest = md.digest(bytesOfMessage);
        
        
        // we only want two bytes of the hash for BabySign
        // we add a 0 byte as the most significant byte to keep
        // the value to be signed non-negative.
        
        byte[] messageDigest = new byte[bigDigest.length+1];
        messageDigest[0]=0;
        for (int i =0;i<bigDigest.length;i++){
            messageDigest[i+1]=bigDigest[i];
            
        }
        
        // The message digest now has three bytes. Two from SHA-256
        // and one is 0.
        
        // From the digest, create a BigInteger
        BigInteger m = new BigInteger(messageDigest);
        
        // encrypt the digest with the private key
        BigInteger c = m.modPow(d, n);
        
        // return this as a big integer string
        return c.toString();
    }
    
    public void set_n(BigInteger n){
        this.n = n;
    }
    
    public void set_d(BigInteger d){
        this.d = d;
    }
}
