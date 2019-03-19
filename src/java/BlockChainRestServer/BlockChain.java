package BlockChainRestServer;
import java.sql.Timestamp;
import java.util.ArrayList;
/**
 *
 * @author chentianxin
 * BlockChain is a program to build, add, and verify the block chain.
 * It reads in the user choice, and perform different functions. It will initialize
 * a block from Block class and connect all the blocks using the previous hash.
 * If there are corrupted or malformed inputs, the program can handle problems and fix
 * the block chain itself.
 */
public class BlockChain extends java.lang.Object{
    
    ArrayList<Block> blocklist;
    String chainhash;
    
    
    /**
     * This BlockChain has exactly two instance members - an ArrayList
     * to hold Blocks and a chain hash to hold a SHA256 hash of the most
     * recently added Block.This constructor creates an empty ArrayList
     * for Block storage.This constructor sets the chain hash to the
     * empty string.This constructor also assign the first genesis block
     * to the chain
     */
    public BlockChain(){
        //initialize blocklist and chainhash
        blocklist = new ArrayList<>();
        chainhash = "";
        
        //append the first block
        FirstBlock();
    }
    
    /**
     *
     * @param newBlock The block to be added to the chain
     * This method add a block to the chain, by connecting using
     * previousHash and calculate the nonce by calling proofOfWork method.
     * Note: This method takes different time for different difficulty levels;
     * This function takes on average 50 milliseconds to add a block
     * of difficulty 4, and 2000 milliseconds to add a block of difficulty 5.
     */
    public void addBlock(Block newBlock) throws Exception{
        //get the hash value of the last block
        String previousHash = getLatestBlock().calculateHash();
        
        //set the previousHash to be the hash value of the last block
        newBlock.setPreviousHash(previousHash);
        
        //calculate the correct hash and the nonce for the new block
        newBlock.proofOfWork();
        
        //add the block to the blocklist and compute the chainhash
        blocklist.add(newBlock);
        chainhash = newBlock.calculateHash();
    }
    
    /**
     *
     * @return the size of the blocklist
     */
    public int getChainSize(){
        return blocklist.size();
    }
    
    /**
     *
     * @return the last block
     */
    public Block getLatestBlock(){
        return blocklist.get(getChainSize()-1);
    }
    
    /**
     *
     * @return the current system timestamp
     */
    public java.sql.Timestamp getTime(){
        //get the current system timestamp
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp;
    }
    
    /**
     *
     * @return true if and only if the chain is valid
     * Note: The execution time of this method is linearly proportional to the
     * number of blocks in the chain. But the execution time is really fast, since
     * it only computes the hash value once. The average time is 0 millisecond.
     */
    public boolean isChainValid(){
        
        //If the chain only contains one block, the genesis block at position 0,
        //this routine computes the hash of the block and checks that the hash has
        //the requisite number of leftmost 0's (proof of work) as specified in the
        //difficulty field. It also checks that the chain hash is equal to this
        //computed hash. If either check fails, return false. Otherwise, return
        //true.
        if(blocklist.size()==1){
            //calculate the hash of the first block
            Block b_check = blocklist.get(0);
            String hashcheck = b_check.calculateHash();
            
            //get the difficulty, and compute the number of leading zeros
            int dif_check = b_check.getDifficulty();
            String lead_check = "";
            for(int i=0;i<dif_check;i++){
                lead_check += "0";
            }
            
            //compare leading zeros with the level of difficulties and the chainhash with the hash
            //value of the last block
            return hashcheck.substring(0, dif_check).equals(lead_check)&&chainhash.equals(hashcheck);
            
            //  If the chain has more blocks than one, begin checking from block one.
            // Continue checking until you have validated the entire chain. The first
            // check will involve a computation of a hash in Block 0 and a comparison
            // with the hash pointer in Block 1. If they match and if the proof of work
            // is correct, go and visit the next block in the chain. At the end, check
            // that the chain hash is also correct.
        }else{
            
            //initialize count to be zero
            int count = 0;
            
            //for all the blocks
            for(int i=0;i<blocklist.size();i++){
                
                //if the connection using hashvalue is not correct, break the loop and increase the count
                if(i>0&&!blocklist.get(i).previousHash.equals(blocklist.get(i-1).calculateHash())){
                    count++;
                    break;
                }
                
                //compute the hash value of the block i
                String hashcheck = blocklist.get(i).calculateHash();
                
                //get the difficulty, and compute the number of leading zeros
                int dif_check = blocklist.get(i).getDifficulty();
                String lead_check = "";
                for(int j=0;j<dif_check;j++){
                    lead_check += "0";
                }
                
                //compare the difficulty with the leading zeros, if not match, increase the count and
                //break the loop
                if(!hashcheck.substring(0, dif_check).equals(lead_check)){
                    count++;
                    break;
                }
            }
            
            //compare count to zero and the chain hash with the hash value of the last block
            return count == 0&&chainhash.equals(getLatestBlock().calculateHash());
        }
    }
    
    /**
     *
     * @Override toString in class java.lang.Object
     * @return a String representation of the entire chain is returned.
     */
    @Override
    public java.lang.String toString(){
        String chain_String = "{\"ds_chain\" : [";
        
        //append the block info in the json format
        for(int i=0;i<blocklist.size()-1;i++){
            chain_String += blocklist.get(i).toString()+",\n";
        }
        chain_String += blocklist.get(blocklist.size()-1).toString()+"],\n";
        chain_String += "\"chainhash\":"+"\""+chainhash+"\"}";
        return chain_String;
    }
    
    /**
     * private help method: to build the first genesis block
     */
    private void FirstBlock(){
        //get the timestamp, build the first block
        Timestamp first_stamp = Timestamp.valueOf("2019-02-22 17:22:14.133");
        Block first_block = new Block(0,first_stamp,"Genesis",2);
        
        //set the previousHash and run the proofOfWork
        first_block.setPreviousHash("");
        first_block.proofOfWork();
        
        //add the block to the list and update the chainhash
        blocklist.add(first_block);
        chainhash = first_block.calculateHash();
    }
}
