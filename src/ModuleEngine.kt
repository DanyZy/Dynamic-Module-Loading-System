import java.io.File

object ModuleEngine {
    @JvmStatic
    fun main(args: Array<String>) {
        val modulePath = "C:\\Users\\Daniil\\IdeaProjects\\Construction_materials\\regularTest\\out\\production\\regularTest"
        /**
         * Создаем загрузчик модулей.
         */
        val loader = ModuleLoader(modulePath, ClassLoader.getSystemClassLoader())
        /**
         * Получаем список доступных модулей.
         */
        val dir = File(modulePath)
        val modules = dir.list()
        /**
         * Загружаем и исполняем каждый модуль.
         */
        for (module in modules) {
            try {
                val moduleName = module.split(".class".toRegex()).toTypedArray()[0]
                val clazz = loader.loadClass(moduleName)
                val execute: IPlugin = clazz.newInstance() as IPlugin
                execute.load()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }
}