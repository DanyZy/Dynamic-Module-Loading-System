import java.io.File
import java.io.FileFilter
import java.io.FileNotFoundException
import java.io.IOException

object ModuleEngine {
    @JvmStatic
    fun main(args: Array<String>) {
        // Test path, change later
        val modulePath = "C:\\Users\\Daniil\\IdeaProjects\\Construction_materials\\failedTest\\out\\production\\failedTest\\"
        /**
         * Create a module loader
         */
        val loader = ModuleLoader(modulePath, ClassLoader.getSystemClassLoader())
        /**
         * Get an array of available modules from path via mask filter
         */
        val dir = File(modulePath)
        val files = dir.listFiles(FileFilter{maskFilter(it, "class")})
        val modules = mutableListOf<File>()
        /**
         * Transport modules form an array to a list
         */
        arrayToList(files, modules)
        /**
         * Load and execute each module
         */
        moduleListLoad(modules, loader)

        JarLoadTest(loader)
    }

    /**
     * Method to load all modules from list
     */
    @Throws(IOException::class)
    fun moduleListLoad(modules: List<File>, loader: ModuleLoader) {
        val tempList = mutableListOf<File>()
        for (module in modules) {
            try {
                val moduleName = module.name.split(".class".toRegex()).toTypedArray()[0]
                val clazz = loader.loadClass(moduleName)
                val plugin: IPlugin = clazz.newInstance() as IPlugin
                plugin.load()
            } catch (ex: Exception) {
                // TODO: change catcher, not proper exceptions
                when (ex) {
                    is ClassNotFoundException,
                    is InstantiationException,
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

    fun JarLoadTest(loader: ModuleLoader) {
        val path = "C:\\Users\\Daniil\\IdeaProjects\\Construction_materials\\dependencyTest\\out\\artifacts\\dependencyTest_jar\\dependencyTest.jar\\"
        val dll = DynamicLibLoader(path, loader)
        println(dll.getMainClassName())
        dll.invokeClass(dll.getMainClassName(), arrayOf(null))
    }

    /**
     * Helper methods
     */
    @Throws(IOException::class)
    fun arrayToList(array: Array<File>?, list: MutableList<File>) {
        if (array == null) {
            return
        }
        for (el in array) {
            list.add(el)
        }
    }

    @Throws(IOException::class)
    fun maskFilter(file: File, mask: String): Boolean {
        return file.isFile && file.name.endsWith(".$mask")
    }
}