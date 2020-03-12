class Plugin2: IPlugin {
    override fun load() {
        if (Plugin3.getTestVar()) {
            setTestVar(true)
            println("${javaClass.name} loaded successfully")
        } else {
            println("${javaClass.name} load failed")
            throw Exception()
        }
    }

    companion object PluginTest {
        private var testVar = false

        fun getTestVar(): Boolean {
            return this.testVar
        }
        fun setTestVar(testVar: Boolean) {
            this.testVar = testVar
        }
    }
}