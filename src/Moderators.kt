package ChatServer.src

import java.io.PrintStream
import java.net.Socket

// Moderator singleton for power tripping =D

object Moderators {
    var moderatorlist = mutableSetOf<String>()
    var kickedList = mutableSetOf<String>()

    fun removeUser(name: String, message: String = "") {
        val s: Socket = Users.socketlist.getValue(name)
        val msg = PrintStream(s.getOutputStream())
        msg.println("You have been kicked from the server. -> $message")
        ChatHistory.notifyObserver("[$name was kicked from the server] $message")
        s.close()
    }
    fun addMod(name: String){
        moderatorlist.add(name)
    }
    fun removeMod(name: String){
        moderatorlist.remove(name)
    }


}

