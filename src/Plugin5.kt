class Plugin5: IPlugin {
    override fun load() {
        if (Plugin6.getTestVar()) {
            println("${javaClass.name} loaded successfully")
        } else {
            println("${javaClass.name} load failed")
            throw Exception()
        }
    }
}


