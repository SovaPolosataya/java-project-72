@import java.time.format.DateTimeFormatter
@import java.util.List
@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.model.Url
@import hexlet.code.model.UrlCheck
@import hexlet.code.util.NamedRoutes

@param UrlPage page


!{var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");}

@template.layout.page(
page = page,
    content = @`
    !{List<UrlCheck> urlChecks = page.getUrlChecks();
    Url url = page.getUrl();}

        <div class="container-lg mt-5">
            <h1>${url.getName()}</h1>
            <table class="table table-bordered table-hover mt-3">
                <tr>
                    <td>ID</td>
                    <td>${url.getId()}</td>
                </tr>
                <tr>
                    <td>Имя</td>
                    <td>${url.getName()}</td>
                </tr>
                <tr>
                    <td>Дата создания</td>
                    <td>${url.getCreatedAt().toLocalDateTime().format(formatter)}</td>
                </tr>
            </table>
        </div>

        <div class="container-lg mt-5">
            <h1>Проверки</h1>
            <form action="${NamedRoutes.urlCheckPath(url.getId())}" method="post" class="rss-form text-body">
                <button type="submit" class="btn btn-primary">Запустить проверку</button>
            </form>
            @if(!urlChecks.isEmpty())
            <table class="table table-bordered table-hover mt-3">
                <thead>
                    <tr>
                        <th class="col-1">ID</th>
                        <th>Код ответа</th>
                        <th>title</th>
                        <th>h1</th>
                        <th>description</th>
                        <th>Дата проверки</th>
                    </tr>
                </thead>

                <tbody>
                @for(var check : urlChecks)
                    <tr>
                        <td>${check.getId()}</td>
                        <td>${check.getStatusCode()}</td>
                        <td>${check.getTitle()}</td>
                        <td>${check.getH1()}</td>
                        <td>${check.getDescription()}</td>
                        <td>${check.getCreatedAt().toLocalDateTime().format(formatter)}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        @endif
        </div>
    `
)
