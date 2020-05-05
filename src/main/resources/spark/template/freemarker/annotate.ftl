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
    <link rel="stylesheet" type="text/css" href="/semantic/dist/semantic.min.css">
    <link rel="stylesheet" href="/css/wiki1.css">
    <link rel="stylesheet" href="/css/wiki2.css">
    <link rel="stylesheet" href="/css/annotate.css">
</head>

<body>
    <div class="upper-section">
        <div class="ui menu">
            <div class="header item">
                <a href="http://localhost:4567/search">CITOGENESIS</a>
            </div>
            <a class="item" href="javascript:history.back()">
                <i class="arrow left icon"></i>
            </a>
            <a class="item" href="javascript:history.forward()">
                <i class="arrow right icon"></i>
            </a>
            <div class="item" id="article-labels">
                <a class="ui blue label">
                    <i class="exclamation triangle icon"></i>
                    5 Generating Sources
                </a>
                <a class="ui red label">
                    <i class="exclamation triangle icon"></i>
                    Circular Reporting
                </a>

            </div>
            <div class="item right">
                <div class="ui action input" id="url-search-box">
                    <input type="text" placeholder="Navigate to...">
                    <div class="ui button">Annotate</div>
                </div>
            </div>
            <div class="item">
                <div class="ui button">Help</div>
            </div>
        </div>
    </div>
    <div class="lower-section">
        <div class="sidenav">
            <div class="ui raised segment" id="annotation-tray">
                <a class="ui teal ribbon label" id="annotation-label">Annotations</a>


                <div class="ui cards" id="annotation-column">


<#--                    <div class="card" id="card-1">-->
<#--                        <div class="content">-->
<#--                            <div class="header">-->
<#--                                <span>Citation 1</span>-->
<#--                                <a class="ui src green circular label">3</a>-->
<#--                            </div>-->
<#--                            <div class="meta">-->
<#--                                <a href="google.com">Webpage</a>-->
<#--                            </div>-->
<#--                        </div>-->
<#--                        <div class="content">-->
<#--                            <h4 class="ui sub header">Generating Sources:</h4>-->
<#--                            <div class="ui ordered list">-->
<#--                                <a class="item" href="">Src 1</a>-->
<#--                                <a class="item" href="">Src 2</a>-->
<#--                            </div>-->
<#--                            <h5 class="ui red header">Circular reporting found.</h5>-->
<#--                            <button class="ui small violet inverted right labeled icon button graph-redirect">-->
<#--                                <i class="right arrow icon"></i>-->
<#--                                <span>See Graph</span>-->
<#--                            </button>-->
<#--                        </div>-->
<#--                    </div>-->




                </div>

            </div>


        </div>

        <div class="extracted-wiki-page">
            <div id="content" class="mw-body" role="main">

            </div>
        </div>
    </div>

    <script src="/js/jquery-2.1.1.js"></script>
    <script src="/js/new_annotation.js"></script>
    <script src="/js/annotate-socket.js"></script>
</body>
<!-- See http://html5boilerplate.com/ for a good place to start
     dealing with real world issues like old browsers.  -->
</html>