import com.google.gson.Gson
import java.io.BufferedReader
import java.io.File
import java.io.FileFilter

object ModuleEngine {
    /**
     * Method to load all modules from list
     */
    public fun moduleListLoad(modules: List<File>, loader: ModuleLoader, CM: ConfigManager? = null) {
        val tempList = mutableListOf<File>()
        for (module in modules) {
            try {
                val moduleName = module.name.split(".class".toRegex()).toTypedArray()[0]
                val clazz = loader.loadClass(moduleName)
                val plugin: IPlugin = clazz.newInstance() as IPlugin
                plugin.load()
                CM?.jsonString?.add(CM.gson.toJson(moduleName))
            } catch (ex: Exception) {
                when (ex) {
                    is IllegalAccessException,
                    is ClassNotFoundException -> {
                        ex.printStackTrace()
                    } else -> tempList.add(module)
                }
            }
        }
        if (tempList.isNotEmpty() && tempList != modules) {
            moduleListLoad(tempList, loader, CM)
        }

        CM?.pluginConfigFile?.writeText(CM.jsonString.toString())
    }

    public fun moduleListLoad(loader: ModuleLoader, CM: ConfigManager) {
        val bufferedReader: BufferedReader = CM.pluginConfigFile.bufferedReader()
        val jsonOutput = bufferedReader.use { it.readText() }
        val pluginNameList = CM.gson.fromJson(jsonOutput, mutableListOf<String>().javaClass)
        for (pluginName in pluginNameList) {
            val clazz = loader.loadClass(pluginName)
            val plugin: IPlugin = clazz.newInstance() as IPlugin
            if (plugin.javaClass.name == pluginName) {
                plugin.load()
            }
        }
    }

    /**
     * Helper method
     */
    fun maskFilter(file: File, mask: String): Boolean {
        return file.isFile && file.name.endsWith(".$mask")
    }
}

fun main(args: Array<String>) {
    // Test path, change later
    val dependencyPath = "C:\\Users\\Daniil\\IdeaProjects\\Construction_materials\\dependencyTest\\out\\production\\dependencyTest\\"
    val modulePath2 = "C:\\Users\\Daniil\\IdeaProjects\\Construction_materials\\dependencyTest\\out\\production\\dependencyTest\\Plugin4.class"

    val dbc = DatabaseConnection("jdbc:sqlite:C:\\Users\\Daniil\\sqlite\\db\\plugins.db")
    val dbm = DatabaseManager(dbc.conn)
    val cm = ConfigManager("src/config/configList.json", Gson(), mutableListOf())

    /**
     * Create a module loader
     */
    val dependencyLoader = ModuleLoader(dependencyPath)
    val dbLoader = ModuleLoader(data = dbm.getPlugin("Plugin4"))

    /**
     * Get an array of available modules from path via mask filter
     */
    val dir = File(dependencyPath)
    val dir2 = File(modulePath2)
    val files = dir.listFiles(FileFilter{ModuleEngine.maskFilter(it, "class")})
    val modules = mutableListOf<File>()
    val modules2 = mutableListOf<File>(dir2)

    /**
     * Transport modules form an array to a list
     */
    arrayToList(files, modules)

    /**
     * Load and execute each module
     */
    ModuleEngine.moduleListLoad(modules, dependencyLoader, cm)
    ModuleEngine.moduleListLoad(modules2, dbLoader)
    ModuleEngine.moduleListLoad(dependencyLoader, cm)
}

fun arrayToList(array: Array<File>?, list: MutableList<File>) {
    if (array == null) return
    for (el in array) {
        list.add(el)
    }
}