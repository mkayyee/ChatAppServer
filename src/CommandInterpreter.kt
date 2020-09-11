package ChatServer.src

import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.lang.Exception
import java.net.Socket
import java.util.Scanner
import javax.jws.WebParam

// This is the Command Interpreter where most of the servers logic is

class CommandInterpreter
    (input: InputStream, output: OutputStream, s: Socket) : ChatHistoryObserver, Runnable {

    private val scanIn = Scanner(input)
    private val scanOut = PrintStream(output, true)
    private val socket = s
    private var userExists = false
    private var username = ""
    private var command = ""

    override fun newMessage(message: String) {
        scanOut.println(message)                /*  overriding observers newMessage method, so that
                                                    notifyObserver in ChatHistory will
                                                    iterate observers with a PrintStream  */
    }
    override fun run() {
        scanOut.println(
                            "WELCOME! To chat – you must create a user first. " +
                            "Type \":help\" for useful commands - \":quit\" to quit."
        )
        try {
            command = scanIn.nextLine().substringAfter(":")
        }
        catch (e: Exception){
            println("command error: $e")}

        //the first nextLine() has unicode symbols in it so had to figure out a way around it
        when {
            command.startsWith("user") -> command = ":$command"
            command.startsWith("help") -> command = ":$command"
            command.startsWith("who") -> command = ":$command"
            command.startsWith("history") -> command = ":$command"
            command.startsWith("quit") -> command = ":$command"
        }
        try {
            while (command != ":quit" && socket.isConnected) {
                val commandSplit = command.split(" ")
                when {
                    command.startsWith(":") -> {
                        if (commandSplit[0] == ":user" ) {
                            if (!userExists) {
                                if (commandSplit.size > 1 && commandSplit[1].isNotEmpty()) {
                                    val name = commandSplit[1]
                                    if (name in Users.userlist) {
                                        scanOut.println("Username taken.") }
                                    else { /* here we create a user associated with a socket
                                                 and register them as a ChatHistory observer  */
                                        Users.addUser(name, socket)
                                        scanOut.println("User '$name' created.")
                                        userExists = true
                                        username = name
                                        ChatHistory.notifyObserver("$username has joined the chat.")
                                        ChatHistory.registerObserver(this)
                                    }
                                } else
                                    scanOut.println("Username must be at least one character long.")
                            } else {
                                scanOut.println("You have already created a user.")
                            }
                        }
                        if (command == ":help") {
                            for (c in Commands.commandlist) {
                                scanOut.println(c)
                            }
                        }
                        else if (command == ":who") {
                            if (Users.userlist.size > 0) {
                                for (u in Users.userlist)
                                    scanOut.println("-$u")
                            }
                            else
                                scanOut.println("No users online.")
                        }
                        else if (command == ":history") {
                            if (ChatHistory.history.size > 0) {
                                for (msg in ChatHistory.history) {
                                    scanOut.println(msg)
                                }
                            }
                            else
                                scanOut.println("No previous messages.")
                        }
                        else if (command == ":modmeXDee123") {
                            Moderators.addMod(username)
                            ChatHistory.notifyObserver("$username has gained the ultimate power!")
                        }
                        else if (command.startsWith(":kick") && username in Moderators.moderatorlist) {
                            val user = commandSplit[1]
                            if (user in Users.userlist) {
                                    // optional message explaining why the user got kicked
                                if (commandSplit.size > 2){
                                    val reason = "${command.substringAfter(commandSplit[1])} "
                                    Moderators.removeUser(user,"-> Reason: $reason")
                                }
                                else {
                                    Moderators.removeUser(user)
                                }
                                Moderators.kickedList.add(user)
                            }
                            else scanOut.println("User $user wasn't found.")
                        }
                                // user trying to kick someone isn't a Moderator
                        else if (command.startsWith(":kick") && username !in Moderators.moderatorlist) {
                            scanOut.println("You don't have the authority to do that.")
                        }
                        else {
                            if (commandSplit[0] !in Commands.checkCommands()) {
                                scanOut.println("No such command.")
                            }
                        }
                    }

                    else -> /* user input doesn't start with ":" and
                       a user has been created -> command turns into message */
                        if (userExists) {
                            // capitalizing the first letter of the message
                            val capitalizedFirst = command[0].toUpperCase() + command.substringAfter(command[0])
                            // message into a ChatMessage object
                            val message = ChatMessage(capitalizedFirst, username)
                            ChatHistory.addMessage(message.toString())
                            ChatHistory.notifyObserver("$username: $capitalizedFirst")
                            TopChatter.addMsg(username)
                        }
                        else
                            scanOut.println("To chat - you must create a user first.")
                }
                command = scanIn.nextLine()
            }
        } catch (error: Exception) {
        }
        // when user input = ":quit" or unexpected disconnection ->
        Users.removeUser(username)
        if (username !in Moderators.kickedList) {
            ChatHistory.notifyObserver("$username has left the chat.")
        }
        ChatHistory.deregisterObserver(this)
        println(
            "A client from IP-address: " + socket.inetAddress.hostAddress +
                    " has disconnected from the server."
        )
        socket.close()
    }
}
