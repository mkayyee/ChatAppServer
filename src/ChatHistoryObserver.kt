package ChatServer.src

// observer patterns "observer" -interface

interface ChatHistoryObserver{
    fun newMessage(message: String){
    }
}