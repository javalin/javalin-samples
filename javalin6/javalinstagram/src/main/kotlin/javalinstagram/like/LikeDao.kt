package javalinstagram.like

import javalinstagram.Database

object LikeDao {

    fun add(photoId: String, ownerId: String) = Database.useHandle<Exception> { handle ->
        handle.createUpdate("insert into like (photoid, ownerid) values (:photoid, :ownerid)")
                .bind("photoid", photoId)
                .bind("ownerid", ownerId)
                .execute()
    }

    fun delete(photoId: String, ownerId: String) = Database.useHandle<Exception> { handle ->
        handle.createUpdate("delete from like where photoid=:photoid and ownerid=:ownerid")
                .bind("photoid", photoId)
                .bind("ownerid", ownerId)
                .execute()
    }

}
