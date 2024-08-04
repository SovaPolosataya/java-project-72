package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;

import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {

    private static Javalin app;
    private static MockWebServer mockServer;
    private static String baseUrl;
    private static final String FIXTURES_FILE_PATH = "src/test/resources/fixtures/";

    private static String readFixture(String fileName) throws IOException, SQLException {
        Path filePath = Path.of(FIXTURES_FILE_PATH + fileName);
        return Files.readString(filePath);
    }

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }

    @BeforeAll
    public static void setServer() throws IOException, SQLException {
        mockServer = new MockWebServer();
        mockServer.enqueue(new MockResponse().setResponseCode(200).setBody(readFixture("test.html")));
        mockServer.start();
        baseUrl = mockServer.url("/").toString();
    }

    @AfterAll
    public static void shutdownServer() throws IOException, SQLException {
        mockServer.shutdown();
    }

    @Test
    public void welcomeTest() throws IOException, SQLException {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.rootPath());

            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body()).isNotNull();
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    public void allUrlsTest() throws IOException, SQLException {
        String name1 = "https://www.example.com";
        String name2 = "https://www.test.ru";
        UrlRepository.save(new Url(name1));
        UrlRepository.save(new Url(name2));

        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.urlsPath());
            String responseBody = response.body().string();

            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body()).isNotNull();

            assertThat(responseBody).contains(name1);
            assertThat(responseBody).contains(name2);
        });
    }

    @Test
    void showUrlTest() throws IOException, SQLException {
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String name = "https://www.example.com";
        Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
        Url url = new Url(name, createdAt);
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.urlPath(url.getId()));
            String responseBody = response.body().string();

            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body()).isNotNull();

            assertThat(responseBody).contains(name);
            assertThat(responseBody).contains(createdAt.toLocalDateTime().format(formatter));
        });
    }

    @Test
    void createValidUrlTest() throws IOException, SQLException {
        JavalinTest.test(app, (server, client) -> {
            String request = "url=https://www.example.com";
            Response response = client.post(NamedRoutes.urlsPath(), request);

            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body()).isNotNull();
            assertThat(response.body().string()).contains("https://www.example.com");
        });
    }

    @Test
    void createInvalidUrlTest() throws IOException, SQLException {
        JavalinTest.test(app, (server, client) -> {
            String request = "url=invalid";
            Response response = client.post(NamedRoutes.urlsPath(), request);

            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body()).isNotNull();
            assertThat(response.body().string()).contains("Бесплатно проверяйте сайты на SEO пригодность");
        });
    }

    @Test
    public void createUrlTest() throws IOException, SQLException {
        String name = "https://www.example.com";
        Url url = new Url(name);
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.urlsPath());
            String responseBody = response.body().string();

            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body()).isNotNull();

            assertThat(responseBody).contains("name");
            assertThat(UrlRepository.getEntities()).hasSize(1);
        });
    }

    @Test
    void checkUrlTest() throws IOException, SQLException {
        var createdAt = new Timestamp(new Date().getTime());
        var url = new Url(baseUrl, createdAt);
        UrlRepository.save(url);

        JavalinTest.test(app, ((server, client) -> {
            Response response = client.post(NamedRoutes.urlCheckPath(url.getId()));
            String responseBody = response.body().string();

            assertThat(response.code()).isEqualTo(200);
            assertThat(responseBody.contains("200"));
            assertThat(responseBody.contains("test title"));
            assertThat(responseBody.contains("test h1"));
            assertThat(responseBody.contains("test description"));

            var urlCheck = UrlCheckRepository.getEntities().get(0);

            assertThat(UrlCheckRepository.getEntities()).hasSize(1);
            assertThat(urlCheck.getUrlId().equals(url.getId()));
            assertThat(urlCheck.getCreatedAt()).isNotNull();
        }));
    }

    @Test
    void urlNotFoundTest() throws IOException, SQLException {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.urlPath("999999"));

            assertThat(response.code()).isEqualTo(404);
        });
    }
}
