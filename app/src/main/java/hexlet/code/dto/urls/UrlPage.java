package hexlet.code.dto.urls;

import hexlet.code.dto.MainPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UrlPage extends MainPage {
    private Url url;
    private List<UrlCheck> urlChecks;
}
