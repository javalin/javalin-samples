package app.util;

import io.javalin.http.Handler;
import static app.util.RequestUtil.*;

public class Filters {

    // Locale change can be initiated from any page
    // The locale is extracted from the request and saved to the user's session
    public static Handler handleLocaleChange = ctx -> {
        if (getQueryLocale(ctx) != null) {
            ctx.sessionAttribute("locale", getQueryLocale(ctx));
            ctx.redirect(ctx.path());
        }
    };

}
