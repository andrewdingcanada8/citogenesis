<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- In real-world webapps, css is usually minified and
         concatenated. Here, separate normalize from our code, and
         avoid minification for clarity. -->
    <link rel="stylesheet" href="css/normalize.css">
    <link rel="stylesheet" href="css/html5bp.css">
    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="css/socket-demo.css">
</head>

<body>
<h1>Sockets Demo</h1>
<div id="content">
    <input id="socket-form" name="socket-form" class="u-full-width"
           type="text"
           placeholder="What you say here should appear below when you press the button:">
    <button id="submit">Submit</button>
    <p class="hover-demo">Hover on me!</p>
    <p id="result"></p>
</div>

<script src="js/jquery-2.1.1.js"></script>
<script src="js/annotate-socket.js"></script>
<#--<script src="js/websocket.js"></script>-->
</body>
</html>
