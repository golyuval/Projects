#include "../include/User.h"

using std:: string;

User::User(): channels(), receipts(),subCtr(0),recCtr(0),subscriptions_events(), channels_subscriptions(){};

User::~User(){}
User::User(const User& other): channels(other.channels),receipts(other.receipts),subCtr(other.subCtr),recCtr(other.recCtr),subscriptions_events(other.subscriptions_events){} //copyconstructor
User::User(User&& other):channels(other.channels),receipts(other.receipts),subCtr(other.subCtr),recCtr(other.recCtr),subscriptions_events(other.subscriptions_events){} //moveconstructor
User& User::operator=(const User& other){
    if (this != &other) {
        subCtr = other.subCtr;
        recCtr = other.recCtr;
        channels = other.channels;
        receipts = other.receipts;
        subscriptions_events =other.subscriptions_events;
    }
    return *this;

} //assingment operator
User& User::operator=(const User&& other){
    if (this != &other) {
            subCtr = other.subCtr;
            recCtr = other.recCtr;
            channels = other.channels;
            receipts = other.receipts;
            subscriptions_events =other.subscriptions_events;
        }
    return *this;
} //move assingment operator

void User::addChannel(int subId, string channel){
        if(channels_subscriptions.find(channel)==channels_subscriptions.end()){
            channels.insert({subId, channel});
            channels_subscriptions.insert({channel,subId});
            subId +=1;
        }
        else{
            if(subscriptions_events.find(channels_subscriptions.find(channel) ->second)!=subscriptions_events.end()){
                list<string> l = subscriptions_events.find(channels_subscriptions.find(channel) ->second) ->second;
                subscriptions_events.erase(channels_subscriptions.find(channel) ->second);
                subscriptions_events.insert({subId,l});
            }
            channels.erase(channels_subscriptions.find(channel) ->second);
            channels.insert({subId,channel});
            channels_subscriptions.find(channel) ->second = subId;
        }
    }

void User::removeChannel(int subId){
        string channel = channels.find(subId)->second;
        channels_subscriptions.erase(channel);
        channels.erase(subId);
        subscriptions_events.erase(subId);
    }

void User::removeChannel(string& channel){
    int subId = getSubscriptionId(channel);
    removeChannel(subId);
}

void User::addReceipt(int receiptId, string& receiptAction){
    receipts.insert({receiptId, receiptAction});
}

string User::getReceipt(int receiptId){
    string receipt = receipts.find(receiptId)->second;
    return receipt;
}

string& User::getChannel(int subscriptionID){
    return channels.find(subscriptionID)->second;
}


// int User::getSubscriptionId(string& channel){
//     if(channels_subscriptions.find(channel) != channels_subscriptions.end())
//         return channels_subscriptions.find(channel)->second;
//     return -1;
// }

int User::getSubscriptionId(string& channel){
    
    if(channels_subscriptions.find(channel) != channels_subscriptions.end())
        return channels_subscriptions.find(channel)->second;
    return -1;
}

void User::removeAllTopics(){
     channels.clear();
     channels_subscriptions.clear();
     receipts.clear();
     subscriptions_events.clear();
}

void User::setUserName(string username){
    name = username;
}

string User::getUserName(){
    string username = name;
    return username;
}

void User::addEvent(int subId, string e){
    auto itr =subscriptions_events.find(subId);
    if (itr != subscriptions_events.end()){
        (itr -> second).push_back(e);
    }
    else{
        list<string> l = list<string>();
        l.push_back(e);
        subscriptions_events.insert({subId,l});
    }
}

void User::print(){
    std::cout<<"Username is:\t"+name<<endl;
    std::cout<< name <<endl;
    std::cout<<"Channels are:\t"<<endl;
    map<int, string>::iterator itr;
    for(itr=channels.begin(); itr!=channels.end() ;itr++)
    {
        cout<<itr->first<<" "<<itr->second<<endl;
    }
    std::cout<<"Receipts are:\t"<<endl;
    for(itr=receipts.begin(); itr!=receipts.end() ;itr++)
    {
        cout<<itr->first<<" "<<itr->second<<endl;
    }
    std::cout<<"Events are:\t"<<endl;
    map<int, list<string>>::iterator iter;
    for(iter=subscriptions_events.begin(); iter!=subscriptions_events.end() ;iter++)
    {   
        list<string> q = iter->second;
        list<string>::iterator listIter;
        for(listIter=q.begin(); listIter!=q.end() ;listIter++){
            cout<<iter->first<<" "<<(*listIter)<<endl;
        }        
    }
   
}