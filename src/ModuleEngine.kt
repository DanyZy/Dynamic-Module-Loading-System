import java.io.File

object ModuleEngine {
    @JvmStatic
    fun main(args: Array<String>) {
        // Test path, change later
        val modulePath = "C:\\Users\\Daniil\\IdeaProjects\\Construction_materials\\regularTest\\out\\production\\regularTest\\"
        /**
         * Create a module loader
         */
        val loader = ModuleLoader(modulePath, ClassLoader.getSystemClassLoader())
        /**
         * Get a list of available modules
         */
        val dir = File(modulePath)
        val modules = dir.list()
        /**
         * Load and execute each module
         */
        if (modules != null) {
            for (module in modules) {
                try {
                    val moduleName = module.split(".class".toRegex()).toTypedArray()[0]
                    val clazz = loader.loadClass(moduleName)
                    val execute: IPlugin = clazz.newInstance() as IPlugin
                    execute.load()
                } catch (ex: Exception) {
                    when (ex) {
                        is ClassNotFoundException,
                        is InstantiationException,
                        is IllegalAccessException -> {
                            ex.printStackTrace()
                        }
                    }
                }
            }
        }
    }
}