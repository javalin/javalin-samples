package javalinstagram

import org.eclipse.jetty.ee10.servlet.SessionHandler
import org.eclipse.jetty.session.DefaultSessionCache
import org.eclipse.jetty.session.FileSessionDataStore
import java.io.File

object Session {

    fun fileSessionHandler() = SessionHandler().apply {
        isHttpOnly = true
        sessionCache = DefaultSessionCache(this).apply {
            sessionDataStore = FileSessionDataStore().apply {
                val baseDir = File(System.getProperty("java.io.tmpdir"))
                this.storeDir = File(baseDir, "javalin-session-store").apply { mkdir() }
            }
        }
    }

}
