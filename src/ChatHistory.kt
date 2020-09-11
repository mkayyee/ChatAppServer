package ChatServer.src

object ChatHistory : ChatHistoryObservable {

    /* registers and de-registers observers / stores ChatMessage objects
       into a list and uses observer pattern to send them to the observers
       when "notifyObservers()" is called. */

    private val observers: MutableList<ChatHistoryObserver> = mutableListOf()
    val history = ArrayList<String>()

    fun addMessage(message: String){
        history.add(message)
    }
    override fun registerObserver(observer: ChatHistoryObserver){
        observers.add(observer)
    }
    override fun deregisterObserver(observer: ChatHistoryObserver){
        observers.remove(observer)
    }
    override fun notifyObserver(message: String){
        observers.forEach{it.newMessage(message)}
    }
}


