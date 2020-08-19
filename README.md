# Multiuser-Chat-App
A Client-Server architecture based application to enable messaging between two or more users. 

Types of communication :

1. User --> Server

    * login / logoff
    * join / leave

2. Server --> User

    * online / offline status
    * messaging
    * file sharing

3. User --> User/Group

    * direct messages
    * broadcast messages / group messaging

 Syntax:

   * Login syntax : login <username> <password>
   * Status syntax : ONLINE <user1>
                                 ONLINE <user2>
                                 OFFLINE <user1>
   * Direct message syntax : msg <receiver> <message body>
   * Group message syntax : join #<group name> <message body>
// group/topic names must begin with '#'   
