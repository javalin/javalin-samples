package app.index;

import io.javalin.http.Handler;
import java.util.Map;

import app.util.Path;
import app.util.ViewUtil;

import static app.Main.*;

public class IndexController {
    public static Handler serveIndexPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        model.put("users", userDao.getAllUserNames());
        model.put("book", bookDao.getRandomBook());
        ctx.render(Path.Template.INDEX, model);
    };
}
