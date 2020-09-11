package ChatServer.src

/* the "Observable" of the observer pattern
   with empty methods to work with inside subclasses
 */
interface ChatHistoryObservable {
    fun registerObserver(observer: ChatHistoryObserver){
    }
    fun deregisterObserver(observer: ChatHistoryObserver){
    }
    fun notifyObserver(message: String){
    }
}