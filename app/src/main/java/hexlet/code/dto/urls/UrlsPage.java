package hexlet.code.dto.urls;

import hexlet.code.dto.MainPage;
import hexlet.code.model.Url;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UrlsPage extends MainPage {
    private List<Url> urls;
}
