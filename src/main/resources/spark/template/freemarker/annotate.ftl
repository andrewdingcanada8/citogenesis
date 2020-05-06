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
    <script src="/js/jquery-2.1.1.js"></script>
    <script src="/semantic/dist/semantic.min.js"></script>
    <script src="/js/new_annotation.js"></script>
    <script src="/js/cards/regular_card.js"></script>
    <script src="/js/cards/null_list_card.js"></script>
    <script src="/js/cards/deprecated_card.js"></script>
    <script src="/js/cards/not_a_webpage_card.js"></script>
    <script src="/js/cards/other_card.js"></script>
    <script src="/js/cards/timeout_card.js"></script>
    <script src="/js/annotate-socket.js"></script>
    <script src="/js/search.js"></script>
    <script src="/js/graph.js"></script>
</head>

<body>
    <div class="upper-section">
        <div class="ui menu">
            <div class="header item" id="logo">
                <a href="http://localhost:4567/main" id="logo-text">CITOGENESIS</a>
            </div>
            <a class="item" href="javascript:history.back()"><i class="arrow left icon"></i></a>
            <a class="item" href="javascript:history.forward()"><i class="arrow right icon"></i></a>
            <div class="item" id="article-labels">
                <a class="ui blue label" id="menu-citation-stat"><i class="globe icon"></i>50</a>
                <a class="ui red label" id="menu-cr-stat"><i class="exclamation triangle icon"></i>CR Found</a>
            </div>

            <div class="right item">
                <div class="ui action input">
                    <input type="text" placeholder="Navigate to..." id="pageURL">
                    <button class="ui teal button" onclick="annotate()">Annotate</button>
                </div>
                <div class="ui basic button" id="help-button" onclick="show_help()">Help</div>
            </div>
        </div>
    </div>
    <div class="ui page dimmer">
        <script>
            function show_help() {$('.ui.page.dimmer').dimmer('show');}
        </script>
        <div class="help overlay content">

            <div class="help top">
                <p><i class="arrow up icon"></i>Menu, Backwards/Forwards, #Sources, Circular Reporting Label, Search Bar, Help Section</p>
            </div>

            <div class="help left">
                <i class="arrow left icon"></i>
                <p>Each Wikipedia citation is displayed as a card.</p>
                <a class="ui green circular label">3</a>
                <p style="display: inline;">There's </p>
                <br>
                <a class="ui green circular label">3</a>
                <p style="display: inline;">There's </p>
                <br>
                <a class="ui green circular label">3</a>
                <p style="display: inline;">There's </p>
            </div>
            <div class="help center" style="float: right;">
                <p>To use Citogenesis, simply input the url of an existing Wikipedia article and click annotate.</p>
                <p>Citogenesis will go and help you discover all of the generating sources for each Wikipedia citation.</p>
                <button class="ui mini purple inverted button">
                    See Graph
                </button>

                <p><i style="display: inline;" class="arrow up icon"></i>Click on this button in the sidebar to see a internet graph with the generating sources of a given citation.</p>
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
<#--                                <span class="citation-title">Citation 1</span>-->
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
<#--                            <button class="ui small violet inverted right labeled icon button graph">-->
<#--                                <i class="right arrow icon"></i>-->
<#--                                <span>See Graph</span>-->
<#--                            </button>-->
<#--                        </div>-->
<#--                    </div>-->




                </div>

            </div>


        </div>

        <div class="extracted-wiki-page" id="wiki-page">
            <div class="ui inverted dimmer graph">
                <i class="close icon"></i>
                <div>
                    <h1>SAMPLE GRAPH DISPLAY OUTPUT</h1>
                </div>
            </div>
        </div>
    </div>
</body>
<!-- See http://html5boilerplate.com/ for a good place to start
     dealing with real world issues like old browsers.  -->
</html>