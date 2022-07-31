package javalinstagram.photo

import io.javalin.http.Context
import javalinstagram.currentUser
import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.geometry.Positions
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

object PhotoController {

    fun upload(ctx: Context) {
        Files.createDirectories(Paths.get("user-uploads/static/p")) // create folder if not exists
        ctx.uploadedFile("photo")?.let { photoFile ->
            val photo = File.createTempFile("temp", "upload").apply {
                photoFile.content.copyTo(this.outputStream())
            }
            val id = UUID.randomUUID().toString().replace("-", "")
            Thumbnails.of(photo)
                    .crop(Positions.CENTER)
                    .size(800, 800)
                    .outputFormat("jpg")
                    .toFile(File("user-uploads/static/p/$id.jpg"))
            PhotoDao.add(photoId = "$id.jpg", ownerId = ctx.currentUser!!)
            ctx.status(201)
        } ?: ctx.status(400).json("No photo found")
    }

    fun getForQuery(ctx: Context) {
        val numberToTake = ctx.queryParam("take") ?: "all"
        val ownerId = ctx.queryParam("owner-id")
        when {
            numberToTake == "all" && ownerId?.isNotEmpty() == true -> {
                ctx.json(PhotoDao.findByOwnerId(ownerId))
            }
            numberToTake == "all" -> ctx.json(PhotoDao.all(ctx.currentUser!!))
            else -> ctx.json(PhotoDao.all(ctx.currentUser!!).take(numberToTake.toInt()))
        }
    }

}
