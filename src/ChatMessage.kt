package ChatServer.src
import java.time.LocalDateTime

// reformats user input into a timestamped message for ChatHistory to use

open class ChatMessage(private val text: String, private val sender: String){

    var time = LocalDateTime.now()

    override fun toString(): String{
        return "[$sender@$time] $text"

    }


}