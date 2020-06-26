import java.io.*
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.net.JarURLConnection
import java.net.URL
import java.net.URLClassLoader
import java.util.*
import java.util.jar.Attributes
import java.util.jar.JarEntry
import java.util.jar.JarFile


public fun fetchPluginsFromJar(pathtobin: String): List<JarEntry?> {
    val jar: JarFile = JarFile(pathtobin)
    val entries: Enumeration<JarEntry> = jar.entries()
    val plugins: MutableList<JarEntry?> = mutableListOf()
    return try {
        while (entries.hasMoreElements()) {
            val entry: JarEntry = entries.nextElement()
            if (entry.isDirectory || !entry.name.endsWith(".class")) continue
            try {
                plugins.add(entry)
            } catch (e: Exception) {
                continue
            }
        }
        plugins
    } catch (ex: Exception) {
        emptyList<JarEntry>()
    }
}

@Throws(ClassNotFoundException::class, NoSuchMethodException::class,
    InvocationTargetException::class)
public fun fetchMainClass(name: String?, args: Array<String?>, pathtobin: String): Method? {
    return try {
        val url: URL = URL("jar", "", -1, "file:$pathtobin!/")
        val c: Class<*> = URLClassLoader(arrayOf(url)).loadClass(name)
        val m: Method = c.getMethod("main", args.javaClass)
        m.isAccessible = true
        val mods: Int = m.modifiers
        if (m.returnType != Void.TYPE || !Modifier.isStatic(mods) ||
            !Modifier.isPublic(mods)
        ) {
            throw NoSuchMethodException("main")
        }
        m
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}

@Throws(IOException::class)
public fun fetchMainClassName(pathtobin: String): String? {
    val url: URL = URL("jar", "", -1, "file:$pathtobin!/")
    val urlConnection: JarURLConnection = url.openConnection() as JarURLConnection
    val attributes: Attributes = urlConnection.mainAttributes
    return attributes.getValue(Attributes.Name.MAIN_CLASS)
}

@Throws(FileNotFoundException::class, IOException::class)
public fun fetchClassBytes(path: String): ByteArray {
    val inputStream: InputStream = FileInputStream(File(path))

    /**
     * Get the size of the file
     */
    val length = File(path).length()
    if (length > Int.MAX_VALUE) {
        // TODO: logic If file is too large
    }
    /**
     * Create the byte array to hold the data
     */
    val bytes = ByteArray(length.toInt())

    /**
     * Read in the bytes
     */
    var offset = 0
    var numRead = 0
    while (offset < bytes.size
        && inputStream.read(bytes, offset, bytes.size - offset).also { numRead = it } >= 0
    ) {
        offset += numRead
    }
    /**
     * Ensure all the bytes have been read in
     */
    if (offset < bytes.size) {
        throw IOException("Could not completely read file $path")
    }
    /**
     * Close the input stream and return bytes
     */
    inputStream.close()
    return bytes
}
