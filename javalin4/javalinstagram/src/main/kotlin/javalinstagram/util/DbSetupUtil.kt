package javalinstagram.util

import javalinstagram.Database

// database-setup/reset
fun main() {
    println("Setting up database...")
    Database.useHandle<Exception> { handle ->
        handle.execute("DROP TABLE IF EXISTS user")
        handle.execute("CREATE TABLE user (id STRING, password STRING, created TIMESTAMP DEFAULT CURRENT_TIMESTAMP)")
        handle.execute("DROP TABLE IF EXISTS photo")
        handle.execute("CREATE TABLE photo (id STRING, ownerid STRING, created TIMESTAMP DEFAULT CURRENT_TIMESTAMP)")
        handle.execute("DROP TABLE IF EXISTS like")
        handle.execute("CREATE TABLE like (photoid STRING, ownerid STRING, UNIQUE(photoid, ownerid) ON CONFLICT REPLACE)")
    }
    println("Database setup complete!")
}
