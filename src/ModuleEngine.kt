import java.io.File
import java.io.FileFilter
import java.util.jar.JarEntry

object ModuleEngine {
    @JvmStatic
    fun main(args: Array<String>) {
        // Test path, change later
        val modulePath = "C:\\Users\\Daniil\\IdeaProjects\\Construction_materials\\dependencyTest\\out\\production\\dependencyTest\\"
        val modulePath2 = "C:\\Users\\Daniil\\IdeaProjects\\Construction_materials\\dependencyTest\\out\\production\\dependencyTest\\Plugin4.class"
        val dllPath = "C:\\Users\\Daniil\\IdeaProjects\\Construction_materials\\jarTest\\out\\artifacts\\jarTest_jar\\jarTest.jar"
        /**
         * Create a module loader
         */
        val moduleLoader = ModuleLoader(modulePath, parent = ClassLoader.getSystemClassLoader())
        //val moduleLoader2 = ModuleLoader(data = Connect.getPluginByName("Plugin1"),
        //   parent = ClassLoader.getSystemClassLoader())
        /**
         * Get an array of available modules from path via mask filter
         */
        val dir = File(modulePath)
        val dir2 = File(modulePath2)
        val files = dir.listFiles(FileFilter{maskFilter(it, "class")})
        val modules = mutableListOf<File>()
        val modules2 = mutableListOf<File>(dir2)
        /**
         * Transport modules form an array to a list
         */
        arrayToList(files, modules)
        /**
         * Load and execute each module
         */
        moduleListLoad(modules, moduleLoader)
        //moduleListLoad(modules2, moduleLoader2)
    }

    /**
     * Method to load all modules from list
     */
    fun moduleListLoad(modules: List<File>, loader: ModuleLoader) {
        val tempList = mutableListOf<File>()
        for (module in modules) {
            try {
                val moduleName = module.name.split(".class".toRegex()).toTypedArray()[0]
                val clazz = loader.loadClass(moduleName)
                val plugin: IPlugin = clazz.newInstance() as IPlugin
                plugin.load()
            } catch (ex: Exception) {
                when (ex) {
                    is IllegalAccessException -> {
                        ex.printStackTrace()
                    } else -> tempList.add(module)
                }
            }
        }
        if (tempList.isNotEmpty() && tempList != modules) {
            moduleListLoad(tempList, loader)
        }
    }

    /**
     * Helper methods
     */
    fun arrayToList(array: Array<File>?, list: MutableList<File>) {
        if (array == null) return
        for (el in array) {
            list.add(el)
        }
    }

    fun maskFilter(file: File, mask: String): Boolean {
        return file.isFile && file.name.endsWith(".$mask")
    }
}