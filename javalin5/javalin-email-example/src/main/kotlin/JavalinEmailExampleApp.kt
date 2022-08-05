import io.javalin.Javalin
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail

fun main() {

    val app = Javalin.create().start(7070)

    app.get("/") { ctx ->
        ctx.html("""
                <form action="/contact-us" method="post">
                    <input name="subject" placeholder="Subject">
                    <br>
                    <textarea name="message" placeholder="Your message ..."></textarea>
                    <br>
                    <button>Submit</button>
                </form>
        """.trimIndent())
    }

    app.post("/contact-us") { ctx ->
        SimpleEmail().apply {
            setHostName("smtp.googlemail.com")
            setSmtpPort(465)
            setAuthenticator(DefaultAuthenticator("YOUR_EMAIL", "YOUR_PASSWORD"))
            setSSLOnConnect(true)
            setFrom("YOUR_EMAIL")
            setSubject(ctx.formParam("subject"))
            setMsg(ctx.formParam("message"))
            addTo("RECEIVING_EMAIL")
        }.send() // will throw email-exception if something is wrong
        ctx.redirect("/contact-us/success")
    }

    app.get("/contact-us/success") { ctx -> ctx.html("Your message was sent") }

}
