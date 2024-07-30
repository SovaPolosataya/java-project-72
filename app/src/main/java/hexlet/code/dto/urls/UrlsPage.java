package hexlet.code.dto.urls;

import hexlet.code.dto.MainPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class UrlsPage extends MainPage {
    private Map<Url, UrlCheck> mapChecks;
}
