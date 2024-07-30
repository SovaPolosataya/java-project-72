package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {

    Javalin app;

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }

    @Test
    public void testWelcome() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    public void testAllUrls() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testShowUrl() throws SQLException {
        String name = "https://www.example.com";
        Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
        Url url = new Url(name, createdAt);
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testCreateUrl() throws SQLException {
        String name = "https://www.example.com";
        Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
        Url url = new Url(name, createdAt);
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("name");
        });
    }

    @Test
    void testUrlNotFound() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/cars/999999");
            assertThat(response.code()).isEqualTo(404);
        });
    }

//    public  void mockTest() throws  Exception {
//        MockWebServer server = new MockWebServer();
//
//        server.enqueue(new MockResponse().setBody("hello, world!"));
//        server.enqueue(new MockResponse().setBody("sup, bra?"));
//        server.enqueue(new MockResponse().setBody("yo dog"));
//
//        server.start();
//
//        HttpUrl baseUrl = server.url("/v1/chat/");
//
//
//    }
}
