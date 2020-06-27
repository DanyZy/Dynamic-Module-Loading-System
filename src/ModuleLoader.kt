import java.io.*

class ModuleLoader(
    private val pathtobin: String = "",
    private val data: ByteArray? = null
    ): ClassLoader(getSystemClassLoader()) {

    @Throws(ClassNotFoundException::class)
    public override fun findClass(className: String): Class<*> {
        return try {
            if (data != null) {
                defineClass(className, data, 0, data.size)
            } else {
                val bytes = fetchClassBytes("$pathtobin$className.class")
                defineClass(className, bytes, 0, bytes.size)
            }
        } catch (ex: Exception) {
            when (ex) {
                is FileNotFoundException,
                is IOException -> {
                    super.findClass(className)
                }
                else -> throw ex
            }
        }
    }
}