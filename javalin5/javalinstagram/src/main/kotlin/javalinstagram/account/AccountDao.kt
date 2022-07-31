package javalinstagram.account

import javalinstagram.Database
import javalinstagram.parseTimestamp
import java.util.*

data class Account(val id: String, val password: String, val created: Date)

object AccountDao {

    fun add(id: String, password: String) = Database.useHandle<Exception> { handle ->
        handle.createUpdate("insert into user (id, password) values (:id, :password)")
                .bind("id", id)
                .bind("password", password)
                .execute()
    }

    fun findById(id: String) = Database.withHandle<Account?, Exception> { handle ->
        handle.createQuery("select * from user where id=:id").bind("id", id).map { rs, ctx ->
            Account(rs.getString("id"), rs.getString("password"), rs.parseTimestamp("created"))
        }.firstOrNull()
    }

}
