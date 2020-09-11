package ChatServer.src

/* a Singleton for registering specific
   user's messages sent and printing
   the names of top four Chatters along with
   the amount of messages they've sent
 */
object TopChatter : ChatHistoryObserver {

    private val chatters = mutableMapOf<String,Int>()

    fun addMsg(user : String){
        val increase = chatters.getValue(user) + 1
        chatters[user] = increase
        userAndMsgSent()
    }
    fun addChatter(name: String){
        chatters[name] = 0
    }
    fun removeChatter(name: String){
        chatters.remove(name)
    }
    private fun userAndMsgSent(){
        val topChatters = chatters.toSortedMap(compareByDescending{chatters.getValue(it)})
        var x = 0
        if (chatters.size >= 4) {
            val topFour = mutableMapOf<String,String>()

            while (x < 4)
                for (i in topChatters) {
                    println("User: ${i.key} | Messages sent: ${i.value}")
                    x += 1
                }
        }else
            for (i in topChatters)
                println("User: ${i.key} | Messages sent: ${i.value}")
    }
}