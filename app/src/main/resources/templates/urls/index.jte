@import hexlet.code.model.Url
@import hexlet.code.model.UrlCheck
@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.urls.UrlsPage
@import java.time.format.DateTimeFormatter
@import java.util.Map

@param UrlsPage page

!{var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");}
@template.layout.page(
    page = page,
    content = @`
        <div class="container-lg mt-5">
            <h1>Сайты</h1>
            <table class="table table-bordered table-hover mt-3">
                <thead>
                <tr>
                    <th class="col-1">ID</th>
                    <th>Имя</th>
                    <th>Последняя проверка</th>
                    <th>Код ответа</th>
                </tr>
                </thead>

                <tbody>
                @for(Map.Entry<Url, UrlCheck> urlMap : page.getMapChecks().entrySet())
                    <tr>
                        <td>
                            ${urlMap.getKey().getId()}
                        </td>
                        <td>
                            <a href="${NamedRoutes.urlPath(urlMap.getKey().getId())}">${urlMap.getKey().getName()}</a>
                        </td>
                        @if(urlMap.getValue() != null)
                        <td>
                            ${urlMap.getValue().getCreatedAt().toLocalDateTime().format(formatter)}
                        </td>
                        <td>
                            ${urlMap.getValue().getStatusCode()}
                        </td>
                        @endif
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>
    `
)
