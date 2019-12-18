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

For now we can locally send both "classic" messages and add them to our local BDD (based on Sqlite) and "system" messages.

In our current Main we send 3 messages :
- a "classic" message that we add to the BDD (we display the BDD contents on reception)
- a "Hello" system message indicating that a new user just connected. On reception of such a message, we add the user to the list of online users and then display that list
- a "Goodbye" system message indicating that an user just disconnected. On reception of such a message, we delete the user from the list of online users and then display that list again

## Authors :

- Matthieu Lacote
- Lucas Perard
- Rafael Achega
