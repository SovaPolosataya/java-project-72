package hexlet.code.controller;

import hexlet.code.dto.MainPage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {

    public static void welcome(Context ctx) {
        var page = new MainPage();

        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("index.jte", Collections.singletonMap("page", page));
    };

    public static void allUrls(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        String flash = ctx.consumeSessionAttribute("flash");
        String flashType = ctx.consumeSessionAttribute("flash-type");

        var page = new UrlsPage(urls);
        page.setFlash(flash);
        page.setFlashType(flashType);

        ctx.render("urls/index.jte", model("page", page));
    }

    public static void showUrl(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findId(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));

        var page = new UrlPage(url);
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void createUrl(Context ctx) throws SQLException {
        var inputUrl = ctx.formParam("url");
        URL parsedUrl;

        try {
            assert inputUrl != null;
            var uri = new URI(inputUrl);
            parsedUrl = uri.toURL();
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        String name = parsedUrl.getProtocol() + "://" + parsedUrl.getAuthority();
        var url = new Url(name, Timestamp.valueOf(LocalDateTime.now()));

        if (UrlRepository.findByName(name).isPresent()) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect(NamedRoutes.rootPath());
        } else {
            UrlRepository.save(url);
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flash-type", "success");
            ctx.redirect(NamedRoutes.urlsPath());
        }
    }
}
