public class Plugin5: IPlugin {
    public override fun load() {
        if (Plugin6.PluginTest.getTestVar()) {
            println("${javaClass.name} loaded successfully")
        } else {
            throw Exception()
        }
    }
}


