<#assign head>
<#--    <link rel="stylesheet" href="css/search.css">-->
    <script>
        function annotate(){
            let url = $('#pageURL').val();

            // check if input url matches wikipedia format
            if (/https:\/\/en\.wikipedia\.org\/wiki\/([\w%]+)/g.test(url)) {
                alert("Make sure to find a Wikipedia article first and copy its URL!");
                return;
            }

            url = url.substr(url.lastIndexOf("/") - 4, url.length);
            if (url !== "") {
                // TODO: Modify this when using deployed
                window.location.href = "http://localhost:4567/" + url;
            }
            // TODO: add some string cutting here so only the important wikipedia page info is shown
            // It's actually only the last word that is the article title. everything after the last /

            // window.location.href = "http://localhost:4567/socket-demo";

        }
    </script>
</#assign>
<#assign body>



    <div class="landing">
        <h1>DISCOVER THE SOURCE OF YOUR INFORMATION</h1>
        <p>./CITOGENESIS.ORG</p>
    </div>
    <div class="wrap">
        <div class="search">
            <input type="text" placeholder="enter web url here" id="pageURL">
        </div>
        <button id="annotateSubmitButton" onclick="annotate();">Annotate</button>
        <button id="graphSubmitButton" onclick="graph();">Graph</button>
    </div>

</#assign>