import java.sql.*


class DatabaseConnection(url: String) {

    var conn: Connection? = null

    init {
        try {
            // create a connection to the database
            conn = DriverManager.getConnection(url)
            val sql: String = """CREATE TABLE IF NOT EXISTS plugins (
                                 plugin_id integer PRIMARY KEY,
	                             plugin_name text NOT NULL UNIQUE,
	                             plugin_data blob NOT NULL
                                 );"""
            val stmt: Statement = conn!!.createStatement()
            stmt.execute(sql);
            println("Connection has been established.")
        } catch (ex: SQLException) {
            println(ex.message)
        }
    }
}