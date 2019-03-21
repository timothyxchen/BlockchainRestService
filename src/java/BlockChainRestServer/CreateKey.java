package project2task5;

import java.math.BigInteger;
import java.util.Random;
/**
 * @author chentianxin
 * 1. Select at random two large prime numbers p and q.
 * 2. Compute n by the equation n = p * q.
 * 3. Compute phi(n)=  (p - 1) * ( q - 1)
 * 4. Select a small odd integer e that is relatively prime to phi(n).
 * 5. Compute d as the multiplicative inverse of e modulo phi(n). A theorem in
 *    number theory asserts that d exists and is uniquely defined.
 * 6. Publish the pair P = (e,n) as the RSA public key.
 * 7. Keep secret the pair S = (d,n) as the RSA secret key.
 * 8. To encrypt a message M compute C = M^e (mod n)
 * 9. To decrypt a message C compute M = C^d (mod n)
 */
public class CreateKey {
    private BigInteger n,d; // n is the modulus for both the private and public keys,d is the exponent of the private key
    private int seed; //the number to generate different keys
    
    public String[][] createKey() {
        // Each public and private key consists of an exponent and a modulus// d is the exponent of the private key
        
        Random rnd = new Random();
        
        // Step 1: Generate two large random primes.
        // We use 400 bits here, but best practice for security is 2048 bits.
        // Change 400 to 2048, recompile, and run the program again and you will
        // notice it takes much longer to do the math with that many bits.
        rnd.setSeed(seed);
        BigInteger p = new BigInteger(400,100,rnd);
        BigInteger q = new BigInteger(400,100,rnd);
        
        // Step 2: Compute n by the equation n = p * q.
        n = p.multiply(q);
        
        // Step 3: Compute phi(n) = (p-1) * (q-1)
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        
        // Step 4: Select a small odd integer e that is relatively prime to phi(n).
        // By convention the prime 65537 is used as the public exponent.
        BigInteger e = new BigInteger ("65537");
        
        // Step 5: Compute d as the multiplicative inverse of e modulo phi(n).
        d = e.modInverse(phi);
        
        String[][] keySet = new String[][]{{e.toString(),n.toString()},{d.toString(),n.toString()}};
        return keySet;
    }
    
    /**
     *
     * @return the private n
     */
    public BigInteger get_n(){
        return n;
    }
    
    /**
     *
     * @return the private d
     */
    public BigInteger get_d(){
        return d;
    }
    
    /**
     *
     * @param seed set the private seed
     */
    public void set_seed(int seed){
        this.seed=seed;
    }
}

