import java.io.*

class ModuleLoader(private val pathtobin: String, parent: ClassLoader?): ClassLoader(parent) {

    // TODO: Добавить метод загрузки динамических библиотек

    @Throws(ClassNotFoundException::class)
    public override fun findClass(className: String): Class<*> {
        return try {
            val b = fetchClassFromFS("$pathtobin$className.class")
            defineClass(className, b, 0, b.size)
        } catch (ex: FileNotFoundException) {
            super.findClass(className)
        } catch (ex: IOException) {
            super.findClass(className)
        }
    }

    @Throws(FileNotFoundException::class, IOException::class)
    private fun fetchClassFromFS(path: String): ByteArray {
        val `is`: InputStream = FileInputStream(File(path))
        // Get the size of the file
        val length = File(path).length()
        if (length > Int.MAX_VALUE) { // File is too large
        }
        // Create the byte array to hold the data
        val bytes = ByteArray(length.toInt())
        // Read in the bytes
        var offset = 0
        var numRead = 0
        while (offset < bytes.size
            && `is`.read(bytes, offset, bytes.size - offset).also { numRead = it } >= 0
        ) {
            offset += numRead
        }
        // Ensure all the bytes have been read in
        if (offset < bytes.size) {
            throw IOException("Could not completely read file $path")
        }
        // Close the input stream and return bytes
        `is`.close()
        return bytes
    }

}