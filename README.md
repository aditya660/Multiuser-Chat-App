# Multiuser-Chat-App
A Client-Server architecture based application to enable messaging between two or more users. 

Communication types :

1. User --> Server

    * login / logoff
    * join / leave

2. Server --> User

    * online / offline status

3. User --> User

    * direct messages
    * broadcast messages / group messages


Commands:

1. Login syntax-  
   login  <user> <password>
    
2. Logoff syntax -
  logoff/quit

3.Status syntax -  * ONLINE <user1>
                                * ONLINE <user2>
                                * OFFLINE<user1>
4. Direct message syntax-  
msg <receiver_name>  <message_body>

5. Join / leave group syntax-
  join <#group_name>  <message_body>
  leave <#group_name>
    
