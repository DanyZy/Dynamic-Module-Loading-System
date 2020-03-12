class Plugin4: IPlugin{
    override fun load() {
        println("${javaClass.name} loaded successfully")
    }
}