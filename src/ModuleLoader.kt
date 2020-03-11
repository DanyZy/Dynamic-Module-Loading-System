import java.io.*

class ModuleLoader(private val pathtobin: String, parent: ClassLoader?): ClassLoader(parent) {
    
    @Throws(ClassNotFoundException::class)
    public override fun findClass(className: String): Class<*> {
        return try {
            val bytes = fetchClassBytes("$pathtobin$className.class")
            defineClass(className, bytes, 0, bytes.size)
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

    @Throws(FileNotFoundException::class, IOException::class)
    private fun fetchClassBytes(path: String): ByteArray {
        val inputStream: InputStream = FileInputStream(File(path))
        /**
        * Get the size of the file
        */
        val length = File(path).length()
        if (length > Int.MAX_VALUE)
        {
            /**
             * File is too large
             */
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
            && inputStream.read(bytes, offset, bytes.size - offset).also { numRead = it } >= 0) {
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

}