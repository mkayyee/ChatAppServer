package ChatServer.src

import java.net.Socket

object Users {
    var userlist = mutableListOf<String>()
    var socketlist = mutableMapOf<String,Socket>()

    fun addUser(name: String, socket: Socket) {
        userlist.add(name)
        socketlist[name] = socket
        TopChatter.addChatter(name)
    }
    fun removeUser(name: String){
        userlist.remove(name)
        TopChatter.removeChatter(name)
        if (name in Moderators.moderatorlist) {
            Moderators.removeMod(name)
        }
    }
}
