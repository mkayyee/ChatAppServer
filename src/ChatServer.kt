package ChatServer.src
import java.lang.Exception
import java.net.ServerSocket

// listening to port 42420 and Threading all incoming connections into CI's

class ChatServer{
    fun serve() {
        println("Starting server...")
        try {
            val ss = ServerSocket(42420)
            println("Server online.")
            ChatHistory.registerObserver(ChatConsole)
            while (true) {
                try {
                    val s = ss.accept()
                    println("A client from IP-address " + s.inetAddress.hostAddress + " connected.")
                    val ci = CommandInterpreter(s.getInputStream(), s.getOutputStream(), s)
                    val t = Thread(ci)
                    t.start()
                } catch (error: Exception) {
                    println("Connection error -> $error")
                }
            }
        }catch (error: Exception) {
            println(error)
        }
    }
}