package hexlet.code.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class UrlChecksController {
    public static void checkUrl(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findId(urlId)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + urlId + " not found"));

        String checkUrl = url.getName();
        try {
            HttpResponse<String> response = Unirest.get(checkUrl).asString();
            int statusCode = response.getCode();
            String body = response.getBody();
            Document doc = Jsoup.parse(body);

            String title = doc.title().isEmpty() ? null : doc.title();
            String h1 = doc.selectFirst("h1") == null ? "" : doc.selectFirst("h1").text();
            String description = doc.selectFirst("meta[name=description]") == null ? ""
                    : doc.select("meta[name=description]").attr("content");
            UrlCheck urlCheck = new UrlCheck(statusCode, h1, title, description, urlId,
                    Timestamp.valueOf(LocalDateTime.now()));
            UrlCheckRepository.save(urlCheck);
            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flash-type", "success");
            ctx.redirect(NamedRoutes.urlPath(urlId));
        } catch (UnirestException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect(NamedRoutes.urlPath(urlId));
        }
    }
}
