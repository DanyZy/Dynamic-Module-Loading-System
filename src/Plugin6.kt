public class Plugin6: IPlugin {
    public override fun load() {
        PluginTest.setTestVar(true)
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


