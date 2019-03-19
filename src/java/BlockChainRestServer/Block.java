package BlockChainRestServer;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chentianxin
 * Block is a program which contains multiple operations on the block.
 * It has the constructor to initialize a block, and has a set of get and
 * set method to store and retrieve the block information. It can compute
 * the hash value of a block, and operate to get the correct number of
 * leading zeros.
 */
public class Block extends java.lang.Object{
    
    int index;
    Timestamp timestamp;
    String data;
    int difficulty;
    String previousHash;
    BigInteger nonce;
    
    /**
     *
     * @param index - This is the position within the chain. Genesis is at 0.
     * @param timestamp - This is the time this block was added.
     * @param data - This is the transaction to be included on the blockchain.
     * @param difficulty - This is the number of leftmost nibbles that need to be 0.
     * This the Block constructor.
     */
    public Block(int index,
            java.sql.Timestamp timestamp,
            java.lang.String data,
            int difficulty){
        this.index= index;
        this.timestamp=timestamp;
        this.data=data;
        this.difficulty=difficulty;
    }
    
    /**
     *
     * @return a String holding Hexadecimal characters
     * This method computes a hash of the concatenation of the index, timestamp,
     * data, previousHash, nonce, and difficulty.
     */
    public java.lang.String calculateHash(){
        //initialize the string
        String result= "";
        
        try {
            //compute the hash value of the concatenation of variables
            String message = String.valueOf(index) +String.valueOf(timestamp)
                    + data+ previousHash+ String.valueOf(nonce)+ String.valueOf(difficulty);
            
            // Create a SHA256 digest
            byte[] bytesOfMessage = message.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            //collect hash results
            byte[] data = md.digest(bytesOfMessage);
            
            //convert to the hex string
            result =javax.xml.bind.DatatypeConverter.printHexBinary(data);
            
            //catch exceptions when hashing
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(Block.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    /**
     * 
     * @return a BigInteger representing the nonce for this block.
     * The nonce is a number that has been found to cause the hash of 
     * this block to have the correct number of leading hexadecimal zeroes.
     */
    public java.math.BigInteger getNonce(){
        return this.nonce;
    }
    
    /**
     * 
     * @return a String with a hash that has the appropriate number of leading 
     * hex zeroes. The difficulty value is already in the block. This is the 
     * number of hex 0's a proper hash must have.
     */
    public java.lang.String proofOfWork() throws StringIndexOutOfBoundsException{
        //initialize the nonce to biginteger of zero
        nonce = new BigInteger("0");
        
        //get the hash value of the current block
        String potentialHash = calculateHash();
        
        //compute the leading zeros based on the difficulty level
        String leadingHex="";
        for(int i =0;i<difficulty;i++){
            leadingHex += "0";
        }
        
        //keep adding nonce if the leading zero does not match the difficulty
        while(!potentialHash.substring(0,difficulty).equals(leadingHex)){
            nonce = nonce.add(BigInteger.ONE);
            potentialHash = calculateHash();
        }
        return potentialHash;
    }
    
    /**
     * 
     * @return difficulty
     */
    public int getDifficulty(){
        return this.difficulty;
    }
    
    /**
     * 
     * @param difficulty determines how much work is required to produce a proper hash
     */
    public void setDifficulty(int difficulty){
        this.difficulty=difficulty;
    }
    
    /**
     * 
     * @Overrides toString in class java.lang.Object
     * @return A JSON representation of all of this block's data is returned.
     */
    @Override
    public java.lang.String toString(){
        String jsonRep = "{\"index\":\""+index+"\","+
                "\"timestamp\":\""+timestamp+"\","+
                "\"Tx\":\""+data+"\","+
                "\"PrevHash\":\""+previousHash+"\","+
                "\"nonce\":"+nonce+","+
                "\"difficulty\":"+difficulty+"}" ;
        return jsonRep;
    }
    
    /**
     * 
     * @param previousHash a hashpointer to this block's parent
     */
    public void setPreviousHash(java.lang.String previousHash){
        this.previousHash = previousHash;
    }
    
    /**
     * 
     * @return previous hash
     */
    public java.lang.String getPreviousHash(){
        return this.previousHash;
    }
    
    /**
     * 
     * @return index of block
     */
    public int getIndex(){
        return this.index;
    }
    
    /**
     * 
     * @param index index of block in the chain
     */
    public void setIndex(int index){
        this.index=index;
    }
    
    /**
     * 
     * @param timestamp of when this block was created
     */
    public void setTimestamp(java.sql.Timestamp timestamp){
        this.timestamp=timestamp;
    }
    
    /**
     * 
     * @return timestamp of this block
     */
    public java.sql.Timestamp getTimestamp(){
        return this.timestamp;
    }
    
    /**
     * 
     * @return this block's transaction
     */
    public java.lang.String getData(){
        return this.data;
    }
    
    /**
     * 
     * @param data represents the transaction held by this block
     */
    public void setData(java.lang.String data){
        this.data=data;
    }
    
    /**
     * 
     * @param args unused
     * For internal test purpose
     */
    public static void main(java.lang.String[] args){
        
    }
    
}
