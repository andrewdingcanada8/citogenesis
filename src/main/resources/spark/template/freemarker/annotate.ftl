<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- In real-world webapps, css is usually minified and
         concatenated. Here, separate normalize from our code, and
         avoid minification for clarity. -->
    <link rel="stylesheet" href="/css/normalize.css">
    <link rel="stylesheet" href="/css/html5bp.css">
    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="/css/annotate.css">
    <link rel="stylesheet" href="/css/wiki1.css">
    <link rel="stylesheet" href="/css/wiki2.css">
</head>

<body>
    <div class="topnav"></div>
    <div class="sidenav">
        <a href="http://localhost:4567/search"><h1>CITOGENESIS</h1></a>
        <h3>Annotations:</h3>
        <div class="annotationColumn" id="annotationColumn">
<#--            <div class="annotationCard" id="a-block1">-->
<#--                <a href="google.com">Name</a>-->
<#--                <p>Generating Sources (2):</p>-->
<#--                <ol>-->
<#--                    <li><a href="1">gen source 1</a></li>-->
<#--                    <li><a href="2">gen source 2</a></li>-->
<#--                </ol>-->
<#--                <p>Circular Reporting: True</p>-->
<#--                <p></p>-->
<#--            </div>-->
        </div>
    </div>




    <div class="content">
        <div id="content" class="mw-body" role="main">

        </div>
    </div>
<#--    ${results}-->
    <#--    <script src="js/annotate-socket.js"></script>-->
    <script src="/js/jquery-2.1.1.js"></script>
    <script src="/js/annotate-socket.js"></script>
    <#--<script src="js/socket-demo.js"></script>-->
    <#--<script src="js/websocket.js"></script>-->
</body>
<!-- See http://html5boilerplate.com/ for a good place to start
     dealing with real world issues like old browsers.  -->
</html>