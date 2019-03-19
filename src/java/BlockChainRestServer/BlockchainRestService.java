package BlockChainRestServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author chentianxin
 * BlockchainRestService is the Rest service program on the server side
 * to perform corresponding requests sent from the user. It has the doGet
 * and doPut methods to handle tasks based on the status of the object.
 */
@WebServlet(name = "BlockchainRestService", urlPatterns = {"/BlockchainRestService/*"})
public class BlockchainRestService extends HttpServlet {
    //initialize the blockchain
    BlockChain bc = new BlockChain();
     
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("Console: doGET visited");
        
        //initialize the result
        String result;
        
        // The choice is on the path /choice so skip over the '/'
        String choice = (request.getPathInfo()).substring(1);
        
        switch (choice) {
            //return the string concantenation of validation result and time taken
            case "2":
                Timestamp start_time=bc.getTime();
                Timestamp end_time=bc.getTime();
                long timetaken = end_time.getTime()-start_time.getTime();
                result= bc.isChainValid()+";"+timetaken;
                break;
                
                //return the string representation of the block chain
            case "3":
                result = bc.toString();
                break;
                
                // return 401 if name not provided
            default:
                response.setStatus(401);
                return;
        }
        
        
        // Things went well so set the HTTP response code to 200 OK
        response.setStatus(200);
        // tell the client the type of the response
        response.setContentType("text/plain;charset=UTF-8");
        
        // return the value from a GET request
        PrintWriter out = response.getWriter();
        out.println(result);
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String result="";
        System.out.println("Console: doPut visited");
        // Read what the client has placed in the PUT data area
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String data = br.readLine();
        if(!data.split(",")[0].equals("1")){
            response.setStatus(401);
            return;
        }
        String difficulty = data.split(",")[1];
        String transaction = data.split(",")[2];
        
        System.out.println(difficulty);
        int blockIndex = bc.getChainSize();
        java.sql.Timestamp blockTime = bc.getTime();
        int blockDif;
        try{
            blockDif = Integer.parseInt(difficulty);
        }catch(NumberFormatException e){
            response.setStatus(500);
            return;
        }
        Block newBlock = new Block(blockIndex,blockTime,transaction,blockDif);
        try {
            bc.addBlock(newBlock);
        } catch (Exception ex) {
            response.setStatus(500);
            return;
        }
        
        
        // prepare response code
        response.setStatus(200);
    }
}
