class Plugin6: IPlugin {
    override fun load() {
        setTestVar(true)
        println("${javaClass.name} loaded successfully")
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


