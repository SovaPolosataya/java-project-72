@import java.time.format.DateTimeFormatter
@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.urls.UrlPage

@param UrlPage page

!{var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");}

@template.layout.page(
    content = @`
     <div class="container-lg mt-5">
        <h1>${page.getUrl().getName()}</h1>

        <table class="table table-bordered table-hover mt-3">
            <tr>
                <td>ID</td>
                <td>${page.getUrl().getId()}</td>
            </tr>
            <tr>
                <td>NAME</td>
                <td>${page.getUrl().getName()}</td>
            </tr>
            <tr>
                <td>DATA</td>
                <td>${page.getUrl().getCreatedAt().toLocalDateTime().format(formatter)}</td>
            </tr>
        </table>
     </div>
    `
)
