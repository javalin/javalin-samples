package app

import org.eclipse.jetty.ee10.servlet.SessionHandler
import org.eclipse.jetty.http.HttpCookie
import org.eclipse.jetty.nosql.mongodb.MongoSessionDataStoreFactory
import org.eclipse.jetty.session.DatabaseAdaptor
import org.eclipse.jetty.session.DefaultSessionCache
import org.eclipse.jetty.session.FileSessionDataStore
import org.eclipse.jetty.session.JDBCSessionDataStoreFactory
import java.io.File

fun fileSessionHandler() = SessionHandler().apply {
    sessionCache = DefaultSessionCache(this).apply {
        sessionDataStore = FileSessionDataStore().apply {
            val baseDir = File(System.getProperty("java.io.tmpdir"))
            this.storeDir = File(baseDir, "javalin-session-store").apply { mkdir() }
        }
    }
    isHttpOnly = true
    // make additional changes to your SessionHandler here
}

fun sqlSessionHandler(driver: String, url: String) = SessionHandler().apply {
    sessionCache = DefaultSessionCache(this).also {
        it.sessionDataStore = JDBCSessionDataStoreFactory().apply {
            setDatabaseAdaptor(DatabaseAdaptor().apply {
                setDriverInfo(driver, url)
                // setDatasource(myDataSource) // you can set data source here (for connection pooling, etc)
            })
        }.getSessionDataStore(this)
    }
    isHttpOnly = true
    // make additional changes to your SessionHandler here
}

fun mongoSessionHandler() = SessionHandler().apply {
    sessionCache = DefaultSessionCache(this).also {
        it.sessionDataStore = MongoSessionDataStoreFactory().apply {
            connectionString = "..."
            dbName = "..."
            collectionName = "..."
        }.getSessionDataStore(this)
    }
    isHttpOnly = true
    // make additional changes to your SessionHandler here
}

fun customSessionHandler() = SessionHandler().apply {
    isHttpOnly = true
    isSecureRequestOnly = true
    sameSite = HttpCookie.SameSite.STRICT
}
