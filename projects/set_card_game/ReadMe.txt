
_____ Set Game Card: _____


* Code:
    - Multi-Thread
    - GUI
    - Log
    - Maven



* Game Properties :

    - Cards : 81 cards in deck (maximum) in total, 12 on table. Divided to:
        -- 3 colors (red, green, purple)
        -- 3 quantities (1, 2, 3)
        -- 3 shapes (S, U, Diamond)
        -- 3 fills (stripes, full, blank)

    - Players : between 1-4 players (computer/human)
        -- Artificial : 10 different levels (implemented on AI_IsCheater,AI_Action)
        -- Human : player 1 --> | q w e r   |   player 2 --> | u i o p   |
                                |  a s d f  |                |  j k l ;  |
                                |   z x c v |                |   m , . / |

    - Rules :
        -- A player can choose only 3 cards (it is possible to un-choose)
        -- valid trio is 3 cards that are different by color & quantity & shape & fill
        -- player who chose valid trio will get a point
        -- player who chose invalid trio will be freezed for 3 seconds (no ability to play)



* Game Flow :

    - Start :
        -- deck starts with 81 shuffled cards
        -- dealer put 12 cards on table

    - Reshuffle :
        -- there is no legal trio on table
        -- time countdown to 0

    - Timer :
        -- countdown of each reshuffle set to 30 seconds
        -- countdown of each reshuffle resets when a player win a point
        -- freeze countdown set to 3 seconds

    - End :
        -- X button was pressed
        -- there is no legal trio on deck + table
        !!! Winner with the best score is announced !!!