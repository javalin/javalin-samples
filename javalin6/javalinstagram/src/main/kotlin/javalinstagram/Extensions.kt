package javalinstagram

import io.javalin.http.Context
import java.sql.ResultSet
import java.text.SimpleDateFormat

var Context.currentUser: String?
    get() = this.sessionAttribute("current-user")
    set(username) = this.sessionAttribute("current-user", username)

fun ResultSet.parseTimestamp(timestamp: String) = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.getString(timestamp))
