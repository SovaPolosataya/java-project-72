@import gg.jte.Content
@import hexlet.code.dto.MainPage

@param Content content
@param MainPage page = null

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Site Analyzer</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css"
        rel="stylesheet"
        integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
        crossorigin="anonymous">
</head>

<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="collapse navbar-collapse" id="navbarNavDropdown">
        <ul class="navbar-nav">
             <li class="nav-item active">
                <a class="nav-link" href="/">Анализатор страниц</a>
             </li>
             <li class="nav-item active">
                <a class="nav-link" href="/">Домашняя страница</a>
             </li>
             <li class="nav-item">
                <a class="nav-link" href="/urls">Cписок сайтов</a>
             </li>
        </ul>
    </div>
</nav>

@if(page != null && page.getFlash() != null)
    <div class="alert alert-${page.getFlashType()}" role="alert">
        ${page.getFlash()}
    </div>
@endif

${content}
</body>
<footer class="footer border-top py-1 mt-4 bg-light">
    <div class="container-xl">
        <div class="text-dark bg-muted text-center">created by <a href="https://github.com/SovaPolosataya" class="text-info">SovaPolosataya</a></div>
    </div>
</footer>
</html>
