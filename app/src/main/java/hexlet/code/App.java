package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
//import hexlet.code.controller.BooksController;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

//import hexlet.code.util.NamedRoutes;
import hexlet.code.repository.BaseRepository;

@Slf4j
public final class App {

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "3000");
        return Integer.valueOf(port);
    }

    private static String getBase() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project-72;DB_CLOSE_DELAY=-1;");

    }

    private static String readResourceFile(String fileName) throws IOException {
        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);

        assert inputStream != null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    public static Javalin getApp() throws IOException, SQLException {

        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getBase());

        var dataSource = new HikariDataSource(hikariConfig);
        String sql = readResourceFile("schema.sql");

        log.info(sql);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });

        app.before(ctx -> {
            ctx.contentType("text/html; charset=utf-8");
        });

//        app.get(NamedRoutes.booksPath(), BooksController::index);
//        app.get(NamedRoutes.bookBuildPath(), BooksController::build);
//        app.get(NamedRoutes.bookPath("{id}"), BooksController::show);
//        app.post(NamedRoutes.booksPath(), BooksController::create);

        return app;
    }

    public static void main(String[] args) throws SQLException, IOException {
        Javalin app = getApp();
        app.get("/", ctx -> ctx.result("Hello World"));
        app.start(getPort());
    }
}
