<h1>Concurrent-Server Socket</h1>
Note: Server.java Only works for Linux machines or machines that can run Linux commands. 
These two files are what are needed for this client-server system. 
Server.java is the program that will be running on the server machine.
Client.java is the program that will be running on the client machine.

Essentially, the Server.java is responsible for handling requests made by the Client.java.
The request asks for information regarding the Server machine
The requests are:
1) Date and Time
2) Uptime of the Server machine
3) Memory Use
4) Netstat
5) Current Users of the server
6) Running processes of the server

<h2>Instuctions</h2>
1) Compile and then run the Server.java. To run this file you must pass in the port number that the server machine will listen to. After this, the Server is already set up and is ready for a client request.<br>
2) Compile and then run the Client.java. You will be prompted to type in the port number and IP address of the server (Note: The port number should be the same as the port you passed in Server.java). <br>
After that, you will be given the 6 requests available.
When you pick your option, you will be asked for the number of requests you are sending.(Note: sending a high number of requests will cause diminishing performance and sometimes will causes crashes)
