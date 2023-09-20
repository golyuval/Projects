#include <stdlib.h>
#include <iostream>
#include <thread>
#include <string>
#include "../include/ConnectionHandler.h"
#include "../include/StompProtocol.h"
#include "../include/User.h"


using namespace std;
using std::string;

void readFromServerRun(ConnectionHandler* connectionHandler, bool& ThreadShouldTerminate,bool& isLogIn,User* user,StompProtocol* stmpP){

	while(!ThreadShouldTerminate){
        std::string answer;
        if (!(*connectionHandler).getFrameAscii(answer,'\0')) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
			ThreadShouldTerminate = true;
            break;
        }
		isLogIn = (*stmpP).process_server(answer);
		//cout << isLogIn <<endl;
	}
	
}

int main(int argc, char *argv[]) {
	while (true) {
		std::cerr << "Please Enter Loggin Command: "  << std::endl;
		User user = User();
		bool isLogIn = bool(false);
		bool shouldTerminate = false;
		bool threadShouldTerminate = false;
		bool endProgram = false;
		std::string host;
		short port;
		bool triedToLogIn = false;
		std::string msg;
    	
		
		while (!triedToLogIn) {
			
			const short bufsize = 1024;
			char buf[bufsize];
			std::cin.getline(buf, bufsize);
			std::string line(buf);
			std::string command = line.substr(0,line.find(' '));

			//std::cerr << command  << std::endl;

			if (command == "login") {
				//std::cerr << "logged: "  << std::endl;
				std::string subString = line.substr(line.find(' ') + 1);
				host = subString.substr(0, subString.find(':'));
				subString = line.substr(line.find(':') + 1);
				std::string portStr = subString.substr(0, subString.find(' '));
				port = stoi(portStr);
				msg = line;
				triedToLogIn = true;
				
			}
			else
			{
				if(command == "end")
				return 0;
				cout<<"Waiting for a login command..."<<endl;
			}
		}

		
		ConnectionHandler connectionHandler(host, port);
		if (!connectionHandler.connect()) {
			std::cerr << "Can't connect to " << host << ":" << port << std::endl;
			return 1;
		}
		
		StompProtocol stmP = StompProtocol(user, connectionHandler);

		std::thread readFromServerThread(readFromServerRun, &connectionHandler, std::ref(threadShouldTerminate), std::ref(isLogIn), &user, &stmP);
		stmP.process(msg);

        while (!shouldTerminate) {
    		std::cout << "New Command : " << std::endl;
    		const short bufsize = 1024;
    		char buf[bufsize];
    		std::cin.getline(buf, bufsize);
    		std::string line(buf);
            string command = line.substr(0, line.find(' '));

			if(!isLogIn) {cout<< "USER WENT OFFLINE." << endl; break;}
            
			int switchCase = 0;
			if (command =="login"){
				switchCase = 1;
			}
			else if (command =="exit"){
				switchCase = 2;
			}
			else if (command =="logout"){
				switchCase = 3;
			}
			else if (command =="print"){
				switchCase = 4;
			}
			else{
				switchCase = 5;
			}

				
			
            switch (switchCase) {
    		    case 1:{
    			    std::cout << "The client is already logged in, log out before trying again" << std::endl;
    			    break;
				}
    		    case 2:
				{
    			    //command = "logout";
    			    stmP.process(line);
    			    //shouldTerminate = true;
    			    //endProgram = true;
					std::cout << "Exited channel "+line.substr(line.find(' ')+1) << std::endl;
    			    break;
				}
    		    case 3:{
    			    stmP.process(line);
    			    shouldTerminate = true;
                    break;
				}
                case 4:
				{
                    user.print();
                    break;
				}
                case 5:
				{
                    stmP.process(line);
                    break;
				}
    		   }
    		if (endProgram) break;
    	}

		threadShouldTerminate = true;
    	readFromServerThread.join();
    	user.removeAllTopics();
		
    	if (endProgram) break;
		cout<<"Waiting for login command..."<<endl;
    }
    return 0;
}


