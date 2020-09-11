package ChatServer.src


object Commands {
    val commandlist = listOf(
        ":help -> brings you here!",
        ":user [name] -> creates a new user.",
        ":who -> shows you who's online.",
        ":quit -> you can probably guess x)",
        ":history -> shows you earlier messages."
    )
    private val allCommands = listOf(":help",":user",":modmeXDee123",":help",":who",":kick",":quit")

    fun checkCommands() : List<String> {
        /* if user input starts with a ":" but isn't in the allCommands list ->
           "no such command" (from CommandInterpreter logic -> .checkCommands())       */
        return allCommands
    }
}