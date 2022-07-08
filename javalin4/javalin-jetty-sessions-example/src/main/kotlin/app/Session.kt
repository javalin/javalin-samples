package app

import org.eclipse.jetty.nosql.mongodb.MongoSessionDataStoreFactory
import org.eclipse.jetty.server.session.*
import java.io.File

fun fileSessionHandler() = SessionHandler().apply {
    sessionCache = DefaultSessionCache(this).apply {
        sessionDataStore = FileSessionDataStore().apply {
            val baseDir = File(System.getProperty("java.io.tmpdir"))
            this.storeDir = File(baseDir, "javalin-session-store").apply { mkdir() }
        }
    }
    httpOnly = true
    // make additional changes to your SessionHandler here
}

fun mongoSessionHandler() = SessionHandler().apply {
    sessionCache = DefaultSessionCache(this).apply {
        sessionDataStore = MongoSessionDataStoreFactory().apply {
            connectionString = "..."
            dbName = "..."
            collectionName = "..."
        }.getSessionDataStore(sessionHandler)
    }
    httpOnly = true
    // make additional changes to your SessionHandler here
}

fun sqlSessionHandler(driver: String, url: String) = SessionHandler().apply {
    sessionCache = DefaultSessionCache(this).apply {
        sessionDataStore = JDBCSessionDataStoreFactory().apply {
            setDatabaseAdaptor(DatabaseAdaptor().apply {
                setDriverInfo(driver, url)
                // setDatasource(myDataSource) // you can set data source here (for connection pooling, etc)
            })
        }.getSessionDataStore(sessionHandler)
    }
    httpOnly = true
    // make additional changes to your SessionHandler here
}
