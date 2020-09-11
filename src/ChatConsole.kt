package ChatServer.src

//all sent messages printed in console

object  ChatConsole : ChatHistoryObserver {
    override fun newMessage(message: String) {
        println(message)
    }
}

