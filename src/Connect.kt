import java.sql.*


object Connect {
    /**
     * Connect to a sample database
     */
    fun connect(): Connection? {
        var conn: Connection? = null
        try {
            // db parameters
            val url = "jdbc:sqlite:C:/Users/Daniil/sqlite/db/plugins.db"
            // create a connection to the database
            conn = DriverManager.getConnection(url)
            println("Connection to SQLite has been established.")
        } catch (ex: SQLException) {
            println(ex.message)
        } /*finally {
            try {
                conn?.close()
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }*/
        return conn
    }

    fun insert(name: String, data: ByteArray) {
        val sql = "INSERT INTO plugins(plugin_name, plugin_data) VALUES(?, ?)"
        val conn: Connection? = this.connect()
        try {
            val pstmt: PreparedStatement = conn!!.prepareStatement(sql)
            pstmt.setString(1, name);
            pstmt.setBytes(2, data);
            pstmt.executeUpdate();
        } catch (ex: SQLException) {
            println(ex.message)
        }
    }

    fun getPluginByName(name: String): ByteArray? {
        val sql = "SELECT * FROM plugins WHERE plugin_name = '$name'"
        val conn: Connection? = this.connect()
        return try {
            val stmt: Statement = conn!!.createStatement()
            val res: ResultSet = stmt.executeQuery(sql)
            res.getBytes("plugin_data")
        } catch (ex: SQLException) {
            println(ex.message)
            null
        }
    }

    /**
     * @param args the command line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        //insert("Plugin1", ModuleFetcher("").fetchClassBytes("C:\\Users\\Daniil\\IdeaProjects\\Construction_materials\\dependencyTest\\out\\production\\dependencyTest\\Plugin4.class"))
        getPluginByName("Plugin1")
    }
}