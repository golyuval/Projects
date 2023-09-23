#include "../include/StompProtocol.h"
#include <iostream>




StompProtocol::StompProtocol(User& user,ConnectionHandler& handler):  is_Connected(false), user(user), C_handler(handler), server_output() {}

StompProtocol::~StompProtocol(){}




StompProtocol::StompProtocol(const StompProtocol& other):is_Connected(other.is_Connected), user(other.user) , C_handler(other.C_handler),server_output(other.server_output)  {}

StompProtocol::StompProtocol(StompProtocol&& other):is_Connected(other.is_Connected), user(other.user), C_handler(other.C_handler),server_output(other.server_output)  {}


#include <fstream>

StompProtocol& StompProtocol::operator=(const StompProtocol& other)
{
    if(this != &other) {
    
        StompProtocol s(other.user, other.C_handler);
        s.is_Connected = other.is_Connected;
        s.server_output = other.server_output;
        *this = s;

    }

    return *this;
    
}

StompProtocol& StompProtocol::operator=(StompProtocol&& other)
{
    if(this!= &other) {

        StompProtocol s(other.user, other.C_handler);
        s.is_Connected = move(other.is_Connected);
        s.server_output = move(other.server_output);
        *this = s;

    }

    return *this;
}


bool StompProtocol::process(string message){
    
    string partial = "";
    string login = "";
    string subscribe = "";
    string unsubscribe = "";
    string path = "";
    string messageCopy = message;



    string cmd = message.substr(0,message.find(' '));

   
    int switchCase = 0;
    if(cmd == "login"){
        switchCase = 1;
    }
    else if (cmd == "join"){
        switchCase = 2;
    }
    else if (cmd == "exit"){
        switchCase = 3;
    }
    else if (cmd == "report"){
        switchCase = 4;
    }
    else if (cmd == "summary"){
        switchCase = 5;
    }
    else if (cmd == "logout"){
        switchCase = 6;
    }
    else{
        switchCase = 7;

    }
    




    switch (switchCase) {
        case 1:{
            partial = message.substr(21);
            //std::cerr << partial << std::endl;
            login.append("CONNECT\n");
            login.append("accept-version:1.2\n");
            login.append("host:stomp.cs.bgu.ac.il\n");
            login.append("login:");

            user.setUserName(partial.substr(0, partial.find(" ")));

            login.append(partial.substr(0, partial.find(" ")));
            login.append("\npasscode:");
            login.append(partial.substr(partial.find(" ")+1));
            login.append("\n\n");


      

            return send(login);
            break;
        }

        case 2: {
            string channel = message.substr(message.find(' ')+1);
            
            subscribe.append("SUBSCRIBE\n");
            subscribe.append("destination:");
            subscribe.append(channel);
            subscribe.append("\nid:");
            subscribe.append( std::to_string(user.subCtr));
            subscribe.append("\nreceipt:");
            subscribe.append(std::to_string(user.recCtr));
            subscribe.append("\n\n");
            subscribe.append("subscribe to ");
            subscribe.append(channel);

            

            
            //user.addChannel(user.subCtr++,channel);
            user.addReceipt(user.recCtr++,message);
            
        
            if(send(subscribe))
                return process_server(message);
            
            //cout<<"111"<<endl;
            return false;
            
        
        }
// login 127.0.0.1:7777 yuval gol

    // join germany_japan

    // exit germany_japan

    // logout

    // report /home/users/bsc/2023/omereliy/Downloads/shit/ass3(2)/ass3/client/data/events1_partial.json

    // report 

    //string cmd = message.substr(0,message.find('\n'));

        case 3: {

            string subID = message.substr(message.find(' ')+1);

            unsubscribe.append("UNSUBSCRIBE\n");
            unsubscribe.append("id:");
            unsubscribe.append(std::to_string(user.getSubscriptionId(subID)));
            unsubscribe.append("\nreceipt:");
            unsubscribe.append(std::to_string(user.recCtr));
            unsubscribe.append("\n\n");

            

            //user.removeChannel(subID);
            user.addReceipt(user.recCtr++,message);

            return send(unsubscribe);
            break;
        
        }
        case 4: {
            
            string report_p = "";
            report_p.append(message.substr(message.find(' ')+1));
            
            names_and_events name_event = parseEventsFile(report_p, user.getUserName());
           
            string teamA = name_event.team_a_name;
            string teamB = name_event.team_b_name;
            
            for(auto event : name_event.events) {

                string event_message = "";
                event_message.append("SEND\n");
                event_message.append("destination:");
                event_message.append(event.get_team_a_name()+"_"+event.get_team_b_name());
                event_message.append("\nreceipt:");
                event_message.append(std::to_string(user.recCtr)+"\n\n");

                event_message.append("user:"+event.get_sender()+"\n");
                event_message.append("Team A:"+event.get_team_a_name()+"\n");
                event_message.append("Team B:"+event.get_team_b_name()+"\n");
                event_message.append("Event Name:"+event.get_name()+"\n");
                event_message.append("Time:"+std::to_string(event.get_time()) + "\n");


                event_message.append("General Game Updates:\n");
                for(auto update : event.get_game_updates())
                    event_message.append( update.first + ":" + update.second + "\n");

                event_message.append("Team A Updates:\n");
                for(auto update : event.get_game_updates())
                    event_message.append( update.first + ":" + update.second + "\n");

                event_message.append("Team B Updates:\n");
                for(auto update : event.get_game_updates())
                    event_message.append( update.first + ":" + update.second + "\n");

                event_message.append("description:"+ event.get_discription()+"\n");
                user.addReceipt(user.recCtr++,event_message);



                send(event_message);
            }
            break;
        
        }
        case 5: {

            string partial = message.substr(message.find(' ')+1);
            
            string game = partial.substr(0,partial.find(' '));
            partial = partial.substr(partial.find(' ')+1);

            string sender = partial.substr(0,partial.find(' '));
            string file = partial.substr(partial.find(' ')+1);
            
            string general_stats = game.substr(0,game.find('_')) + " vs " +game.substr(game.find('_')+1) +"\n";
            //general_stats.append("Game stats:\n");
            //general_stats.append("General stats:\n");
            
            string a_stats = game.substr(0,game.find('_')) + " stats:\n" ;
            string b_stats = game.substr(game.find('_')+1) + " stats:\n" ;
            string game_reports = "Game event reports:\n";
            
            //    std::cout<<"gameee "+game<<endl;

            int subIdToChannel = user.channels_subscriptions.find(game)->second;

            //std::cout<<subIdToChannel<<endl;
            map<int, list<string>>::iterator iterator;
            list<string> l = user.subscriptions_events.find(subIdToChannel)->second;
            list<string>::iterator lst;
            //std::cout<<"Events: "<<endl;


                for(lst = l.begin() ; lst != l.end() ; lst++){ 
                    string name = (*lst).substr((*lst).find(':') + 1 , (*lst).find('\n',2) - (*lst).find(':') - 1);
                    
//std::cout <<  sender << std::endl;



                    if(sender == name){ 
                        
                        string Name = (*lst).substr((*lst).find("Event Name:")+11,(*lst).substr((*lst).find("Event Name:")).length()-12 - (*lst).substr((*lst).find("Time:")).length());
                        
                        string Time = (*lst).substr((*lst).find("Time:")+5,(*lst).substr((*lst).find("Time:")).length()-5 - (*lst).substr((*lst).find("Time:")).length());
                        
                        string eventDescription = (*lst).substr((*lst).find("description:")+12);
                        
                        string a_updates = (*lst).substr((*lst).find("Team A Updates:")+15,(*lst).substr((*lst).find("Team A Updates:")).length()-16 - (*lst).substr((*lst).find("Team B Updates:")).length());
                        
                        string b_updates = (*lst).substr((*lst).find("Team B Updates:")+15,(*lst).substr((*lst).find("Team B Updates:")).length()-16 - (*lst).substr((*lst).find("description:")).length());
                        
                        string generalStats = (*lst).substr((*lst).find("General Game Updates:")+21,(*lst).substr((*lst).find("General Game Updates:")).length()-21 - (*lst).substr((*lst).find("Team A Updates:")).length());
                        
                        general_stats.append(generalStats);
                        a_stats.append(a_updates+"\n");
                        b_stats.append(b_updates+"\n");
                        game_reports.append(Time + " - " + Name);
                        
                    }
                }
                   
                    general_stats.append(a_stats) ;
                    general_stats.append(b_stats) ;
                    general_stats.append(game_reports) ;
                  // std::cout <<  general_stats << std::endl;
                   

                ofstream MyFile(file);
                MyFile << general_stats;
                MyFile.close();
                break;
            
        }
        case 6: {

            string disconnect = "";
            disconnect.append( "DISCONNECT\nreceipt:");
            disconnect.append(std::to_string(user.recCtr));
            disconnect.append("\n\n");

            


            user.addReceipt(user.recCtr++,message);

            return send(disconnect);
            break;
        }
        case 7: {cout << "please enter legal command..." << endl;return true;}
    }
}

bool StompProtocol::send(const string  &frameToServer)
{

    // std::cout << "-------------------Sent----------------------" << std::endl;
    // std::cout << frameToServer << std::endl;
    // std::cout << "-------------------Sent----------------------" << std::endl;

    return C_handler.sendFrameAscii(frameToServer, '\u0000');
}


bool StompProtocol:: process_server(string& message){

    // std::cout << "-------------------recieved----------------------" << std::endl;
    // std::cout << message << std::endl;
    // std::cout << "-------------------recieved----------------------" << std::endl;




    string cmd = message.substr(0,message.find(' '));
    cmd = cmd.substr(0,cmd.find('\n'));

    
    int switchCase = 0;
    if(cmd == "MESSAGE"){
        switchCase = 1;
    }
    else if(cmd == "RECEIPT"){
        switchCase = 2;
    }
    else if(cmd == "join"){
        switchCase = 3;
    }
    else if(cmd == "exit"){
        switchCase = 4;
    }
    else if(cmd == "logout"){
        switchCase = 5;
    }
    else if(cmd == "ERROR"){
        switchCase = 6;
    }
    else if(cmd == "CONNECTED"){
        switchCase = 7;
    }

    switch(switchCase){
        case 0: {
            return true;
        }
        case 1: {
            string SubIdStr = "subscription:";
            string messageIdStr = "message-id:";
            string subIdStr = message.substr(message.find(SubIdStr)+SubIdStr.length(),message.find(messageIdStr));
            int subId = stoi(subIdStr);
            string body = message.substr(message.find("\n\n"));
            user.addEvent(subId,body);
            return true;
            break;
            }

    case 2: {
        string receipStr = message.substr(message.find(':')+1);
        int receiptId = std::stoi(receipStr.substr(0,message.find('\n')));
        string action = user.receipts.find(receiptId)->second;
        cmd = action.substr(0,action.find(' '));
        return true;
        break;
        }

    case 3: {
        // string receipStr = message.substr(message.find(':')+1);
        // int receiptId = std::stoi(receipStr.substr(0,message.find('\n')));
        // string action = user.receipts.find(receiptId)->second;
        // std::cout << "Joined channel " + action.substr(action.find(' ')+1) << endl;
        // string channel = action.substr(action.find(' ')+1);
        // user.addChannel(user.subCtr++, channel);
        string channel = message.substr(message.find(' ')+1);
        cout << "Joined channel " + channel << endl;
        user.addChannel(user.subCtr++, channel);
        return true;
        break;
        }

    case 4: {
        string receipStr = message.substr(message.find(':')+1);
        int receiptId = std::stoi(receipStr.substr(0,message.find('\n')));
        string action = user.receipts.find(receiptId)->second;

        string channel = action.substr(action.find(' ')+1);
        std::cout << "Exited channel " + action.substr(action.find(' ')+1) << endl;
        return true;
        break;
        }
    case 5: {
        user.removeAllTopics();
        C_handler.close();
        return false;
        break;
        }
    case 6: {
        //std::cout <<  message << endl;
        C_handler.close();
        return false;
        break;
        }
    case 7: {
        std::cout <<  "Login Successful" << endl;
        return true;
        break;
        }
    
    }
    
}


