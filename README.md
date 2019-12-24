# POO Project : MessageApp

## Getting Started :

### Prerequisites:

- Having a recent version of the JDK installed
- Downloading this project or creating a copy with "git clone"

### Compiling:

To compile : javac -d bin src/*.java

### Running:

To run :
- Open 2 terminals
- In the first terminal : java -cp "bin:lib/*" Main Port1 Port2
- In the second terminal : java -cp "bin:lib/*" Main Port2 Port1

## What's working :

### The graphical interface :

In brief : our whole graphical interface is functional. Some ergonomy assets can still be improved.

The first frame displays the list of the connected users on the left and a field to choose a pseudo
on the right. Users can't choose a pseudo with more than 16 characters (we are still debating on that),
neither can they choose a pseudo already taken by another user or an empty pseudo. Each of those cases
are explained to the user by a message dialog window.

After entering a valid pseudo and pressing the "Connexion" button or pressing the Enter key, the login frame closes and the chat frame appears.

On the left our current pseudo is displayed and under it a button sending us back to the login frame to choose another pseudo. Under that you can find the list of the connected users. Each entry is clickable and clicking on one creates a tab on the right side of the frame.

On the right side you can find the different tabs opened (Note: if too much tabs are opened, a scrolling option appears). On each tab the message history with the corresponding user is displayed. The message borders don't have the same color whether you are the sender or the receiver. Under the message history there are a textfield to enter a message and a button to send the content of the textfield. If the user tries to send an empty message a window appears to warn him and ask him if it is intentional or not. The user can also send the message by pressing the Enter key.

When a new message is sent or is received the frame is updated and the last message is displayed (the scrollbar is set at the end).
When a user connects or disconnects the list of connected users is updated (for now you can use the "+" and "-" button on the login frame to add and remove "Test User" to the list).

### The communication between the SocketManager and the database

In brief : for now we can locally send both "classic" messages and add them to our local database (based on Sqlite) and "system" messages.

In our current Main we send 3 messages :
- a "classic" message that we add to the database (we display the database contents on reception)
- a "Hello" system message indicating that a new user just connected. On reception of such a message, we add the user to the list of online users and then display that list (The user added in our example is Test_User)
- a "Goodbye" system message indicating that an user just disconnected. On reception of such a message, we delete the user from the list of online users and then display that list again (The user removed in our example is Test_User)

All of those tests are displayed in the terminal !

## Authors :

- Matthieu Lacote
- Lucas Perard
- Rafael Achega
