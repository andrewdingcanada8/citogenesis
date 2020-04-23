<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/normalize.css">
    <link rel="stylesheet" href="css/html5bp.css">
    <link rel="stylesheet" href="/css/main.css">
</head>
<body>
<div class="content">
    <form method="GET" action="/search" class="search">
        <label> URL
            <input type="text" name="url">
        </label>
        <input type="submit">
    </form>
    ${content}
</div>
<script src="js/jquery-2.1.1.js"></script>
</body>
</html>