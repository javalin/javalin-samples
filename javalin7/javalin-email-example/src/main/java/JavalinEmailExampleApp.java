import io.javalin.Javalin;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;
import static j2html.TagCreator.br;
import static j2html.TagCreator.button;
import static j2html.TagCreator.form;
import static j2html.TagCreator.input;
import static j2html.TagCreator.textarea;

public class JavalinEmailExampleApp {

    public static void main(String[] args) {

        Javalin.create(config -> {
            config.router.apiBuilder(() -> {
                get("/", ctx -> ctx.html(
                    form().withAction("/contact-us").withMethod("post").with(
                        input().withName("subject").withPlaceholder("Subject"),
                        br(),
                        textarea().withName("message").withPlaceholder("Your message ..."),
                        br(),
                        button("Submit")
                    ).render()
                ));
                post("/contact-us", ctx -> {
                    Email email = new SimpleEmail();
                    email.setHostName("smtp.googlemail.com");
                    email.setSmtpPort(465);
                    email.setAuthenticator(new DefaultAuthenticator("YOUR_EMAIL", "YOUR_PASSWORD"));
                    email.setSSLOnConnect(true);
                    email.setFrom("YOUR_EMAIL");
                    email.setSubject(ctx.formParam("subject"));
                    email.setMsg(ctx.formParam("message"));
                    email.addTo("RECEIVING_EMAIL");
                    email.send(); // will throw email-exception if something is wrong
                    ctx.redirect("/contact-us/success");
                });
                get("/contact-us/success", ctx -> ctx.html("Your message was sent"));
            });
        }).start(7070);
    }

}
