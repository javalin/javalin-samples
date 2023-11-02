package javalinstagram.like

import io.javalin.http.Context
import io.javalin.http.queryParamAsClass
import javalinstagram.currentUser

object LikeController {

    fun create(ctx: Context) {
        LikeDao.add(photoId = ctx.validPhotoId(), ownerId = ctx.currentUser!!)
        ctx.status(201)
    }

    fun delete(ctx: Context) {
        LikeDao.delete(photoId = ctx.validPhotoId(), ownerId = ctx.currentUser!!)
        ctx.status(204)
    }

    private fun Context.validPhotoId() = this.queryParamAsClass<String>("photo-id")
        .check({ it.length > 10 }, "IDs must be longer than 10 characters")
        .get()

}
