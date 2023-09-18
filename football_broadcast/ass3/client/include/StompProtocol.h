#pragma once
#include <string>
#include <iostream>
#include <fstream>
#include <vector>
#include "../include/ConnectionHandler.h"
#include "../include/User.h"
#include "../include/event.h"


using namespace std;

class StompProtocol
{

private:

    bool is_Connected;
    User& user;
    ConnectionHandler& C_handler;
    vector<string> server_output;

public:

    StompProtocol(User& user, ConnectionHandler& handler);
    virtual ~StompProtocol();
    StompProtocol(const StompProtocol& other);
    StompProtocol(StompProtocol&& other);
    StompProtocol& operator=(const StompProtocol& other);
    StompProtocol& operator=(StompProtocol&& other);

    bool process(string msg);
    bool send(const string &frameToServer); 
    bool process_server(string & msg);

};