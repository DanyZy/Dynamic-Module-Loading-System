public class Plugin4: IPlugin{
    public override fun load() {
        println("${javaClass.name} loaded successfully")
    }
}