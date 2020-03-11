import java.io.File
import java.io.FileFilter
import java.io.IOException

object ModuleEngine {
    @JvmStatic
    fun main(args: Array<String>) {
        // Test path, change later
        val modulePath = "C:\\Users\\Daniil\\IdeaProjects\\Construction_materials\\dependencyTest\\out\\production\\dependencyTest\\"
        /**
         * Create a module loader
         */
        val loader = ModuleLoader(modulePath, ClassLoader.getSystemClassLoader())
        /**
         * Get a array of available modules
         */
        val dir = File(modulePath)
        val files = dir.listFiles(FileFilter{maskFilter(it, "class")})
        val modules = mutableListOf<File>()

        for (file in files) {
            modules.add(file)
        }
        /**
         * Load and execute each module
         */
        moduleListLoad(modules, loader)
    }

    @Throws(IOException::class)
    fun maskFilter(file: File, mask: String): Boolean {
        return file.isFile && file.name.endsWith(".$mask")
    }

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
}