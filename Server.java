import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.management.*;

 /*@Author Luke Evarretta & James Ngo */
 /*
    This is a Multi Threaded program that handles multiple client at ones.
 */


/**
 * This class is a thread handler for each request recieved
 */
class serverHandler extends Thread{
    private String request;
    private Socket socket;
    public serverHandler(String request, Socket socket){
        this.request = request;
        this.socket = socket;
    }

    @Override
    public void run(){
        try{
            InputStream input = socket.getInputStream();
            InputStreamReader rdr = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(rdr);
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            //all the cases based on the request made.
            if(request.equals("1")){
                        writer.println(new Date().toString());
            }
            if(request.equals("2")){
                writer.println(Server.upTimeHandler());
            }
            if(request.equals("3")){
                writer.println(Server.usedMemoryHandler() + " bytes");
            }
            if(request.equals("4")){
                String netStat = Server.netStatHandler();
                writer.println(netStat);
            }
            if(request.equals("5")){
                String users = Server.currentUserHandler();
                writer.println(users);
            }
            if(request.equals("6")){
                String processes = Server.runProcHandler();
                writer.println(processes);
            }
            System.out.println("Finished");//Prints this line if this thread is finished.
            while(!reader.readLine().equals("-1")){//waiting for ack from host
                //nothing
            }
            socket.close();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}//end of serverHandler class


public class Server {
 
    public static void main(String[] args) {
        //If the port and only the port is not printed in therminal this program will terminate, 
        //and must be run again with appropriate port number.
        if(args.length != 1){
            System.out.println("Error: You need to pass in the 'Port' number in the terminal when running this program.");
            return;
        }
        //Initializing the port number.
        int port = Integer.parseInt(args[0]);

        //tries if ServerSocket is valid.
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Server is listening on port " + port);
 
            while (true) {
                Socket socket = serverSocket.accept();//If client is accepted it resumes
                System.out.println("New client connected");

                //This chuck of code is for reading/receiving in what was sent from the client.
                InputStream input = socket.getInputStream();
                InputStreamReader rdr = new InputStreamReader(input);
                BufferedReader reader = new BufferedReader(rdr);
                String request = reader.readLine();//This variale represent that request.
                serverHandler thread = new serverHandler(request, socket);//The thread is created.
                thread.start();//Starts the thread immediately      
                //the iterates again for another request/thread without waiting for the current thread to finish          
            }
 
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /** 
    * Handles the NetStat information
    *  @return String on the nestat information
    */
    public static String netStatHandler(){
        try{
        //This will hold each line of the information. Each line in their respective cell.
        ArrayList<String> strToReturn = new ArrayList<>();

        //Proccessing the linux command
        ProcessBuilder prcBld = new ProcessBuilder("netstat");
        Process process = prcBld.start();

        //Object used to read in the information of the linux command
        InputStream input = process.getInputStream();
        InputStreamReader rdr = new InputStreamReader(input);
        BufferedReader reader = new BufferedReader(rdr);
        
        String line;
        //Reading the infromation and makes sures all the information is read
        while((line = reader.readLine()) != null){
            strToReturn.add(line);
        }
    
        reader.close();
        int length = strToReturn.toString().length();//length of the ArraList.toString()
        int exitCode = process.waitFor();//waits for the process to end.
        return strToReturn.toString().substring(1,length-1);//Making sure that brackets at the beginning and end of the the is removed.
        
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return "Failed";
        }
   
    }

    /** 
     * Handles the current connected users information
    *   @return String on the current users connected the server.
    */
    public static String currentUserHandler(){
        try{
        //This will hold each line of the information. Each line in their respective cell.
        ArrayList<String> strToReturn = new ArrayList<>();

        //Proccessing the linux command
        ProcessBuilder prcBld = new ProcessBuilder("users");
        Process process = prcBld.start();
        //Object used to read in the information of the linux command
        InputStream input = process.getInputStream();
        InputStreamReader rdr = new InputStreamReader(input);
        BufferedReader reader = new BufferedReader(rdr);

        String line;
        //Reading the infromation and makes sures all the information is read
        while((line = reader.readLine()) != null){
            strToReturn.add(line);
            
        }
        // System.out.println(strToReturn.toString());
        reader.close();
        int length = strToReturn.toString().length();
        int exitCode = process.waitFor();
        return strToReturn.toString().substring(1,length-1);//Making sure that brackets at the beginning and end of the the is removed.
        
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return "Failed";
        }
        
    }

    /** 
     * Handles the current processes running
    *   @return String on the processes running
    */
    public static String runProcHandler(){
        try{
        //This will hold each line of the information. Each line in their respective cell.
        ArrayList<String> strToReturn = new ArrayList<>();

        //Proccessing the linux command
        ProcessBuilder prcBld = new ProcessBuilder("ps", "-e");
        Process process = prcBld.start();

        //Object used to read in the information of the linux command
        InputStream input = process.getInputStream();
        InputStreamReader rdr = new InputStreamReader(input);
        BufferedReader reader = new BufferedReader(rdr);

        String line;
        //Reading the infromation and makes sures all the information is read
        while((line = reader.readLine()) != null){
            strToReturn.add(line);
            
        }
        reader.close();
        
        int length = strToReturn.toString().length();
        int exitCode = process.waitFor();
        return strToReturn.toString().substring(1,length-1);//Making sure that brackets at the beginning and end of the the is removed.
        
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return "Failed";
        }
    }
    /** 
     * Handles the uptime information of the server.
    *   @return String on the uptime of the server.
    */
    public static String upTimeHandler(){
        try{
        //Proccessing the linux command
        ProcessBuilder prcBld = new ProcessBuilder("uptime");
        Process process = prcBld.start();

        //Object used to read in the information of the linux command
        InputStream input = process.getInputStream();
        InputStreamReader rdr = new InputStreamReader(input);
        BufferedReader reader = new BufferedReader(rdr);

        //Only one line to read for uptime linux command
        String line = reader.readLine();
        reader.close();
        int exitCode = process.waitFor();
        return line;
        
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return "Failed";
        }
    }

    /** 
     * Handles the used memory information
    *   @return String on the used memory information
    */
    public static String usedMemoryHandler(){
         try{
        //This will hold each line of the information. Each line in their respective cell.
        ArrayList<String> strToReturn = new ArrayList<>();

        //Proccessing the linux command
        ProcessBuilder prcBld = new ProcessBuilder("free");
        Process process = prcBld.start();
        //Object used to read in the information of the linux command
        InputStream input = process.getInputStream();
        InputStreamReader rdr = new InputStreamReader(input);
        BufferedReader reader = new BufferedReader(rdr);

        String line;
        //Reading the infromation and makes sures all the information is read
        while((line = reader.readLine()) != null){
            strToReturn.add(line);
            
        }
        //removes the multiple white spaces.
        String[] arr = strToReturn.get(1).split("\\s+");// regex just lik js.
        reader.close();
        int exitCode = process.waitFor();
        return arr[2];//this is the col where the used memory is hold
        
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return "Failed";
        }
    }
}