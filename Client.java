import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.Scanner;
import java.lang.*;

/** 
 * @Author James Ngo, Luke Evarretta 
 * */


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.

/*
personal note that if you simply put get long star timer can print that out
it would be find the difference of current time minus jan 1 of 1970
 */
class threadHandler extends Thread{
    //properties
    private String networkAddress;
    private int portNumber;
    private int request;
    private int threadNumber;



    //class constructor
    public threadHandler(String networkAddress,int portNumber,int request,int threadNumber){
        this.networkAddress = networkAddress;
        this.portNumber = portNumber;
        this.request = request;
        this.threadNumber = threadNumber;
    }
    @Override
    public void run(){
        try {
            long start = System.currentTimeMillis();//starts the timer
            Socket socket = new Socket(this.networkAddress, this.portNumber);

            // Create input and output streams for the socket
            OutputStream outStream = socket.getOutputStream();
            PrintWriter out = new PrintWriter(outStream, true);
            InputStream input = socket.getInputStream();
            InputStreamReader rdr = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(rdr);
            // Send the request to the server
            out.println(request);
            // Sending the thread number and for the server side information.
            out.println(threadNumber);

            //*****  Read and process the server's response  ********
            if(request != 4 && request != 5 && request != 6 && request != 2){
            String receivedInfo = reader.readLine();
            long end = System.currentTimeMillis();
            long currentDiff = end - start;
            Client.incrementOverallTime(currentDiff);
            StringBuilder strOutput = new StringBuilder();
            strOutput.append(receivedInfo + " Thread " + this.threadNumber + " takes " + currentDiff + " ms");
            Client.addOutputString(strOutput.toString());
            }
            else if(request == 5){
                String string = reader.readLine();
                StringBuilder strOutput = new StringBuilder();
                long end = System.currentTimeMillis();
                long currentDiff = end - start;
                Client.incrementOverallTime(currentDiff);
                strOutput.append(string);
                strOutput.append("!!!Thread " + this.threadNumber + " takes " + currentDiff + " ms");
                Client.addOutputString(strOutput.toString());                
            }
            else if(request == 2){
                StringBuilder strOutput = new StringBuilder();
                String[] receivedInfo = reader.readLine().split(",");
                String[] days = receivedInfo[0].split(" ");
                long end = System.currentTimeMillis();
                long currentDiff = end - start;
                Client.incrementOverallTime(currentDiff);
                strOutput.append(days[2]+" "+days[3] +" days " + receivedInfo[1].trim() + " Thread " + 
                                    this.threadNumber + " takes " + currentDiff + " ms");
                Client.addOutputString(strOutput.toString());
                
            }
            else {
                String string = reader.readLine();
                StringBuilder strOutput = new StringBuilder();
                long end = System.currentTimeMillis();
                long currentDiff = end - start;
                Client.incrementOverallTime(currentDiff);
                strOutput.append(string);
                strOutput.append("!!!Thread " + this.threadNumber + " takes " + currentDiff + " ms");
                Client.addOutputString(strOutput.toString());
            }

            out.println("-1");//sending an ack that process is done.

            
            
            // Close the socket
            socket.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        } 
        


    }

}
public class Client {

    public static long overallTime = 0;//represent total turn-around time
    private static ArrayList<String> outputString = new ArrayList<>();

    /**
     * method that prints out the message for user input
     */
    public static void printMenu(){
        System.out.println();
        System.out.println("Please select an option(An integer code)");
        System.out.println("1: Date and time");
        System.out.println("2: Uptime");
        System.out.println("3: Memory Use");
        System.out.println("4: Netstat");
        System.out.println("5: Current Users");
        System.out.println("6: Running Processes");
        System.out.println("7: QUIT");
        System.out.println();
    }

    /**
     *
     * @return the selection code from menu
     */
    public static int pickFromMenu(){
        System.out.print("Enter your selection: ");
        Scanner scnr = new Scanner(System.in);
        return scnr.nextInt();
    }

    /**
     * grabs the user input for the number of threads they want to run for their assign function
     * @return the int input
     */
    public static int getNumberOfClients(int num){
        Scanner scnr = new Scanner(System.in);
        String[] type = {"Date and time","Uptime","Memory Use","Netstat","Current Users","Running Processes"};
        System.out.println("How many times do you want to run - " + "\" " + type[num - 1] + " \": ");
        return scnr.nextInt();
    }



    public static void main(String[] args) throws Exception{

        System.out.println("Hello world");

        Scanner scnr = new Scanner(System.in);

        //ret
        System.out.print("Enter your specified network address: ");
        String networkAddress = scnr.nextLine();
        System.out.println("Your network address is " + networkAddress);
        System.out.println();

        System.out.print("Enter your specified port number:");
        int portNumber = scnr.nextInt();
        System.out.println("Your port number is " + portNumber);
        scnr.nextLine(); //essentially help remove any issues later on
        System.out.println();


        
        boolean flag = true;
        while(flag){
            overallTime=0;
            printMenu();//prints message and options
            int cmdRequest = pickFromMenu();
            if(cmdRequest == 7){
                System.out.println("SESSION IS ENDING");
                flag = false;
                break;
            }
            while(cmdRequest < 1 || cmdRequest > 7){
                System.out.println(cmdRequest + " is not an option.");
                printMenu();
                cmdRequest = pickFromMenu();
                if(cmdRequest == 7){
                    System.out.println("SESSION IS ENDING");
                    return;
                }
                
            }

            //grabs user input to see how many times they want to run their selected command
            int nmbrRequests = getNumberOfClients(cmdRequest);

            System.out.println();
            //record the starting overall
            long startOverall = System.currentTimeMillis();
            
            threadHandler[] arr = new threadHandler[nmbrRequests];
            //Now we are going to create some threads and startingit
            for(int i = 0; i < nmbrRequests;i++){
                //note to self that function is an int that is the number of the request code
                arr[i] = new threadHandler(networkAddress,portNumber,cmdRequest,i);

                //now we have to launch the thread after making it
                arr[i].start();
               
            }
            for(int i = 0; i < nmbrRequests; i++){
                while(arr[i].isAlive()){
                    //waiting for threads to finish
                }
            }

            if(cmdRequest == 5){
                printOutputStringFive();
            }
            else if(cmdRequest == 4 || cmdRequest==6)   printOutputStringFourSix();
            else if(cmdRequest == 7){ /*nothing*/}
            else    printOutputString();
            
            double averageTime = (double)overallTime / nmbrRequests;//represent average turn-around time.
            
            System.out.println("Average time: " + averageTime + " ms");
            System.out.println("Total time: " + overallTime + " ms");
            


        }


        scnr.close();


    }//end of main method

    //This methods adds the turn-around time of each thread into the total turn-around time.
    public static void incrementOverallTime(long finishedValue){
       overallTime += finishedValue;
    }//end of class

    /**
     * this method allows for a string to be added into ArrayList<String> outputString
     * which hold the ouput that need to be printed.
     */
    public static void addOutputString(String output){
        outputString.add(output);
    }

    /**
     * This method prints the output for option 1, 2, 3
     */
    public static void printOutputString(){
        for(String i : outputString)    System.out.println(i);
        outputString.clear();
    }

    /**
     * This methods prints the output for option 5
     */
    public static void printOutputStringFive(){
        for(String i : outputString){
            String[] twoParts = i.split("!!!");
            String[] string = twoParts[0].split(" ");
            for(String j : string){ 
                System.out.println(j);
                
            }
            System.out.println(twoParts[1]);
            
        }
        outputString.clear();
    }

    /**
     * This method prints the output for option 6
     */
    public static void printOutputStringFourSix(){
        for(String i : outputString){
            String[] twoParts = i.split("!!!");
            String[] string = twoParts[0].split(",");
            for(String j : string){ 
                System.out.println(j);
                
            }
            System.out.println(twoParts[1]);
           
        }
        outputString.clear();
    }
}//end of class