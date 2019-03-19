package BlockchainRestServer;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author chentianxin
 * The Project3Task3Client program is the client side, where it will print out
 * the menu with listed choices, and takes user input of the choice. The
 * program follows the single argument design where there is only one method
 * provided on the service side.
 */

// A simple class to wrap a result.
class Result{
    String value;
    
    public String getValue(){
        return value;
    }
    
    public void setValue(String value){
        this.value = value;
    }
}

public class Project3Task3Client {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        OUTER:
        while (true) {
            System.out.println("Block Chain Menu");
            System.out.println("1. Add a transaction to the blockchain");
            System.out.println("2. Verify the blockchain.");
            System.out.println("3. View the blockchain.");
            System.out.println("4. Exit.");
            
            //read in the user choice
            Scanner sc = new Scanner(System.in);
            String user_choice = sc.nextLine();
            switch (user_choice) {
                
                //if user choice is 1, takes input of difficulty and transaction,
                //and call the addBlockChain with the string containing CSV message
                case "1":
                    System.out.println("Enter difficulty > 0");
                    String difficulty = sc.nextLine();
                    System.out.println("Enter transaction");
                    String transaction = sc.nextLine();
                    System.out.println(addBlockChain(user_choice+","+difficulty+","+transaction));
                    break;
                    
                    //if user choice is 2 or 3, directly call the functions  
                case "2":
                case "3":
                    System.out.println(opeBlockChain(user_choice));
                    break;
                    
                    //if user choice is 4, break the loop
                case "4":
                    break OUTER;
                    
                    //if user choice is something else, print the message, and continue the program
                default:
                    System.out.println("Something went wrong");
                    continue;
            }
            System.out.println("-----------------------------");
        }
    }
    
    /**
     * 
     * @param user_choice Takes user choice of the function to be performed
     * @return the String output of either viewing the block chain or verifying the block chain.
     */
    public static String opeBlockChain(String user_choice){
        Result r = new Result();
        int status;
        if((status = doGet(user_choice,r))!=200) return "Error from server " + status;
        if(user_choice.equals("3")) return "View the Blockchain\n"+r.getValue();
        else return "Verifying entire chain\n" +"Chain verification: "+r.getValue().split(";")[0]+
                "\nTotal execution time required to verify the chain was "+r.getValue().split(";")[1]+" milliseconds";
    }
    
    /**
     * 
     * @param user_inputs Takes combined user input of the difficulty and transaction information
     * @return the string output of the time taken to add a block
     */
    public static String addBlockChain(String user_inputs){
        //initialize the status code
        int status;
        
        //get the start time
        long start_time = System.currentTimeMillis();
        if((status = doPut(user_inputs))!=200) return "Error from server " + status;
        
        //get the end time
        long end_time = System.currentTimeMillis();
        
        //print the time taken to add the block
        long timetaken = end_time - start_time;
        return "The total execution time to add this block was "+timetaken+" milliseconds";
    }
    
    public static int doGet(String user_choice, Result r) {
        // Make an HTTP GET passing the name on the URL line
        
        r.setValue("");
        String response = "";
        HttpURLConnection conn;
        int status = 0;
        
        try {
            
            // pass the name on the URL line
            URL url = new URL("http://localhost:8090/BlockchainRestServer/BlockchainRestService"+"//"+user_choice);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // tell the server what format we want back
            conn.setRequestProperty("Accept", "text/plain");
            
            // wait for response
            status = conn.getResponseCode();
            
            // If things went poorly, don't try to read any response, just return.
            if (status != 200) {
                // not using msg
                String msg = conn.getResponseMessage();
                return conn.getResponseCode();
            }
            String output = "";
            // things went well so let's read the response
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            
            while ((output = br.readLine()) != null) {
                response += output;
                
            }
            
            conn.disconnect();
            
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }   catch (IOException e) {
            e.printStackTrace();
        }
        
        // return value from server
        // set the response object
        r.setValue(response);
        // return HTTP status to caller
        return status;
    }
    
    public static int doPut(String userInput) {
        
        
        int status = 0;
        try {
            URL url = new URL("http://localhost:8090/BlockchainRestServer/BlockchainRestService");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(userInput);
            out.close();
            status = conn.getResponseCode();
            
            conn.disconnect();
            
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }
}
