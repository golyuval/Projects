
<br>
<br>

---
# <span style="color:#dfd688;">***Table Of Contents***</span>

<br>

## <span style="color:#988d81;">***Projects***</span>



### 1. <span style="color:#9c8358;">**E-commerce system :**</span> modular delivery management system

- Language : C#
- My part : implemented 1 out of 4 modules (**Transport**, HR, Branches, Supplier)
- Key Features :
    - delivery scheduling
    - truck management
    - data visualization supported by UML documentation.

- ***for further implementat information [press here](#erp-system)***

### 2. <span style="color:#9c8358;">**Football Broadcast :**</span>  real-time server communication with multiple clients
- Language : cpp, java
- My Part : server side implementation
- key Features :
    - Communication (Server-Client, UDP/TCP, RCI)
    - Concrrency ( Reactor pattern | Thread per client | Thread pool )

- ***for further implementation information [press here](#football-broadcast)***

### 3. <span style="color:#9c8358;">**Set Card Game :**</span> an implementation of the original set card game. 
- Language : Java
- Key Features :
    - Concurrency (Thread-Pool, Thread-Per-Client)
    - Visitor pattern
    - data visualizations
    - logger (game tracking)

- ***for further information [press here](#set-card-game)***

<br>

### 4. <span style="color:#9c8358;">**Algo Trading System :**</span>  market recommendation system for optimal entry points
- Language : python
- My Part : my **own** research project
- key Features :
    - Recommendation System
    - NLP (sentiment analysis) / heuristic based strategies 
    - data transfer optimizations
    - RESTful API, with plans for future open-source release.

- ***for further implementation information [press here]()***


<br>

## <span style="color:#988d81;">***Self Use :*** code for self use</span>
- usefull code scopes 
- syntax / library reviews
- algorithm implementations
- ***for further information [press_here](#self-use)***

<br>


<br><br><br><br><br><br><br><br><br><br><br><br>


---
# <span style="color:#72d891;">***Detailed Information***</span>

<br><br>

## <span style="color:#a2c09a;">***Projects***</span>

<br>



### ***ERP System :*** 

<a name="erp-system"></a>

<span style="color:#77b268;"></span> 

<br>

<a name="yo"></a>

* **Description**

    The System manages a delivery company which is based on 4 different modules : 
    - <span style="color:#db320c;">Transport</span>
    - HR
    - Branches
    - Supplier

    The <span style="color:#db320c;">Transport</span> module is responsible for :
    - Orders, deliveries and trucks data management
    - Scheduling new deliveries and handling overweight
    - Truck scheduling by demand
    - Providing informative presentation of stored data information and data manipulations

<br>

* **Diagrams** (docs)

    - activity diagram : send delivery (Transport module)
    - sequence diagram : send delivery (Transport module)
    - sequence diagram : schedule delivery (Transport module)
    - class diagram (HR & Transport module)
    - object diagram (HR & Transport module)


<br>

* **Code Structure** (each layer is devided to 4 different modules, mine is Delivery)


    - Presentation Layer (Swing) 

    - Service Layer 

        - DeliveryService
        - FormService 
        - TruckService 
        
        - Generator : responsible for generating fictive objects
        - Validator : responsible for input validations

    <br>
    
    - Business Layer


        - Controllers

            - Delivery Controller, responsibilities :
                - create delivery and order DAOs
                - store deliveries data (and initiate data by DAOs)
                - record an handle truck overloads (1,2,3)
                - schedule deliveries
                

            - Forms Controller, responsibilities :
                - classify deliveries by their status (scheduled, in progress, done)
                - store all forms (destiny, overload)
                - create destform, overload DAOs
                - create and gather forms back to service layer

            - Trucks Controller, responsibilities :
                - initiating base site
                - store trucks by their status (available, in delivery)
                - create truck dao
                - provide suitable truck by demand
                - provide information about existing trucks


        - Forms

            - Delivery form
            - Destiny form
            - Overload form
        

        - Structures

            - Address
            - Order
            - Requested Order
            - Site
            - Truck

    <br>

    - Data Access Layer (generic implementation of DTO and DAO will be given by demand)

        - DAO : object per structure
        - DTO : object per structure

        


 <br>



### ***Football Broadcast :*** 

<a name="football-broadcast"></a>

<span style="color:#77b268;"></span>

* **Code Structure**
    
    - client
        - data (generated events)
        - include (classes header files)
        - source (classes cpp files)
            - User (user object)
            - safe queue
            - Connection Handler
            - event
            - echo client
            - protocol stomp
            - client stomp


    - server
        - api (protocol and encoder/decoder interfaces)

        - implementation
            - echo (echo server server implementation : client say x -> server repeat x)
            - news feed (server use RCI to communicate)
            - RCI (implementation of server which uses serializable objects as a encode/decode strategy)
            - stomp (new implementation of server)
                - Message (a message include's command, headers and body)
                - User (class for every user in the system)
                - Connection implementation
                - stomp command (initiate type of commands)
                - stomp encoder/decoder (given encoder/decoder)
                - stomp protocol (given protocol)
                - stomp server (run server using : reactor | thread per client )
        
        - srv (implementations)
            - actor thread pool
            - connections
            - connection handler
            - blocking connection handler
            - non-blocking connecion handler
            - reactor
            - base server
            - server
            


* **Flow**



* **Extra**


 <br>



### ***Set Card Game :*** 

<a name="set-card-game"></a>

 <span style="color:#77b268;"></span>

* **Code Structure**
    - java
    - Multi-Thread
    - GUI
    - Log
    - Maven

     <br>

* **Game Properties** 

    - Cards : 81 cards in deck (maximum) in total, 12 on table. Divided to:

        - 3 colors (red, green, purple)
        - 3 quantities (1, 2, 3)
        - 3 shapes (S, U, Diamond)
        - 3 fills (stripes, full, blank) 
        
    <br>


    - Players : between 1-4 players (computer/human)

        - Artificial : 10 different levels (implemented on AI_IsCheater,AI_Action)
        - Human : 12 keys on keyboard for each player (4 keys per row)

            |  player 1  |  player 2  |
            | :--------  | :--------  |
            |  q w e r   |  u i o p   |
            |   a s d f  |   j k l ;  |
            |    z x c v |    m , . / |

    <br>

    - Rules :

        - A player can choose only 3 cards (it is possible to un-choose)
        - valid trio is 3 cards that are different by color & quantity & shape & fill
        - player who chose valid trio will get a point
        - player who chose invalid trio will be freezed for 3 seconds (no ability to play)

    <br>

* **Game Flow**

    - Start :
        
        - deck starts with 81 shuffled cards
        - dealer put 12 cards on table


    - Reshuffle :

        - there is no legal trio on table
        - time countdown to 0

    - Timer :

        - countdown of each reshuffle set to 30 seconds
        - countdown of each reshuffle resets when a player win a point
        - freeze countdown set to 3 seconds

    - End :

        - X button was pressed
        - there is no legal trio on deck + table
        <span style="color:#a2c09a;">!!! Winner with the best score is announced !!!</span>

<br><br>



## Self Use

<a name="self-use"></a>

<span style="color:#a2c09a;"></span>

* **Algorithms** : different algorithms implementations in python
    
    - sorting algorithms
    - sorter implementation (difference between implementations using tqdm)
        
    <br>

* **Language Syntax** : important syntax that I have came across in the following languages:

    - python
    - c++
    - java

<br>

* **Reviews** : important syntax that I have came across in the following libraries:

    - pandas
    - numpy
    - sympy
    - tensorflow
    - pytorch
    - sklearn
    - mathplotlib

<br>





