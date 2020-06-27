import java.sql.*


class DatabaseManager(private val connection: Connection?) {

    public fun insert(name: String, data: ByteArray) {
        val sql: String = "INSERT INTO plugins(plugin_name, plugin_data) VALUES(?, ?)"
        val conn: Connection? = connection
        try {
            val pstmt: PreparedStatement = conn!!.prepareStatement(sql)
            pstmt.setString(1, name);
            pstmt.setBytes(2, data);
            pstmt.executeUpdate();
            println("Data has been inserted.")
        } catch (ex: SQLException) {
            println(ex.message)
        }
    }

    public fun getPlugin(name: String): ByteArray? {
        val sql: String = "SELECT * FROM plugins WHERE plugin_name = '$name'"
        val conn: Connection? = connection
        return try {
            val stmt: Statement = conn!!.createStatement()
            val res: ResultSet = stmt.executeQuery(sql)
            res.getBytes("plugin_data")
        } catch (ex: SQLException) {
            println(ex.message)
            null
        }
    }

    public fun getPlugin(): List<ByteArray?>? {
        val sql: String = "SELECT * FROM plugins"
        val conn: Connection? = connection
        return try {
            val stmt: Statement = conn!!.createStatement()
            val res: ResultSet = stmt.executeQuery(sql)
            val pluginsList: MutableList<ByteArray?> = mutableListOf()
            while (res.next()) {
                pluginsList.add(res.getBytes("plugin_data"))
            }
            pluginsList
        } catch (ex: SQLException) {
            println(ex.message)
            null
        }
    }
}

/**
 * @param args the command line arguments
 */

fun main(args: Array<String>) {
    val dbc = DatabaseConnection("jdbc:sqlite:C:\\Users\\Daniil\\sqlite\\db\\plugins.db")
    val dbm = DatabaseManager(dbc.conn)
    dbm.insert("Plugin1", fetchClassBytes("C:\\Users\\Daniil\\IdeaProjects\\Construction_materials\\dependencyTest\\out\\production\\dependencyTest\\Plugin1.class"))
    dbm.insert("Plugin2", fetchClassBytes("C:\\Users\\Daniil\\IdeaProjects\\Construction_materials\\dependencyTest\\out\\production\\dependencyTest\\Plugin2.class"))
    dbm.insert("Plugin3", fetchClassBytes("C:\\Users\\Daniil\\IdeaProjects\\Construction_materials\\dependencyTest\\out\\production\\dependencyTest\\Plugin3.class"))
    dbm.insert("Plugin4", fetchClassBytes("C:\\Users\\Daniil\\IdeaProjects\\Construction_materials\\dependencyTest\\out\\production\\dependencyTest\\Plugin4.class"))
    dbm.insert("Plugin5", fetchClassBytes("C:\\Users\\Daniil\\IdeaProjects\\Construction_materials\\dependencyTest\\out\\production\\dependencyTest\\Plugin5.class"))
    dbm.insert("Plugin6", fetchClassBytes("C:\\Users\\Daniil\\IdeaProjects\\Construction_materials\\dependencyTest\\out\\production\\dependencyTest\\Plugin6.class"))

    println(dbm.getPlugin())
}