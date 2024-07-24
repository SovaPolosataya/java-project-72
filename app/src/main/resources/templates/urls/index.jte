@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.urls.UrlsPage

@param UrlsPage page

@template.layout.page(
    page = page,
    content = @`
        <div class="container-lg mt-5">
            <h1>URLs</h1>
                <table class="table table-bordered table-hover mt-3">
                    <thead>
                        <tr>
                            <th class="col-1">ID</th>
                            <th>Name</th>
                        </tr>
                    </thead>

                    <tbody>
                        @for(var url : page.getUrls())
                        <tr>
                            <td>
                                ${url.getId()}
                            </td>
                            <td>
                                 <a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a>
                            </td>
                        </tr>
                        @endfor
                    </tbody>
                </table>
        </div>
    `
)
