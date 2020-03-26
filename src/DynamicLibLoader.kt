import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.net.JarURLConnection
import java.net.URL
import java.util.jar.Attributes


class DynamicLibLoader(private val pathtobin: String, parent: ClassLoader?): ClassLoader(parent) {

    @Throws(IOException::class)
    public fun getMainClassName(): String? {
        val url: URL = URL("jar", "", "file:$pathtobin!/")
        val urlConnection: JarURLConnection = url.openConnection() as JarURLConnection
        val attributes: Attributes = urlConnection.mainAttributes
        return attributes.getValue(Attributes.Name.MAIN_CLASS)
    }

    @Throws(ClassNotFoundException::class, NoSuchMethodException::class, InvocationTargetException::class)
    public fun invokeClass(name: String?, args: Array<String?>) {
        try {
            val c: Class<*> = loadClass(name)
            val m: Method = c.getMethod("main", args.javaClass)
            m.isAccessible = true
            val mods: Int = m.modifiers
            if (m.returnType != Void.TYPE || !Modifier.isStatic(mods) ||
                !Modifier.isPublic(mods)
            ) {
                throw NoSuchMethodException("main")
            }
            try {
                m.invoke(null, arrayOf<Any>(args))
            } catch (e: IllegalAccessException) {
                // This should not happen, as we have disabled access checks
            }
        } catch (ex: NullPointerException) {
            ex.printStackTrace()
        }
    }
}
