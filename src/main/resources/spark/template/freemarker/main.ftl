<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/css/normalize.css">
    <link rel="stylesheet" href="/css/html5bp.css">
    <link rel="stylesheet" type="text/css" href="/semantic/dist/semantic.min.css">
    <link rel="stylesheet" href="/css/main.css">
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@300&display=swap" rel="stylesheet">

    <script>
        function annotate(){
            let url = $('#pageURL').val();
            url = url.substr(url.lastIndexOf("/") - 4, url.length);
            if (url !== "") {
                // TODO: Modify this when using deployed
                window.location.href = "http://localhost:4567/" + url;
            }
            // TODO: add some string cutting here so only the important wikipedia page info is shown
            // It's actually only the last word that is the article title. everything after the last /

        }
    </script>
</head>

<body>
    <div class="side-column">
        <ul>
            <li class="link"><a href="#landing-block"> Search |</a></li>
            <li class="link"><a href="#faq-block"> FAQ |</a></li>
            <li class="link"><a href="#about-block"> About |</a></li>
        </ul>
    </div>
    <div class="main-column">

<#--        HTML FOR LANDING SECTION-->
        <div class="content-block" id="landing-block">
            <div>
                <div class="title-box">
                    <h1>CITO -</h1>
                    <h1>GENESIS</h1>
                </div>

                <div class="ui mini action input" id="search-bar">
                    <input type="text" placeholder="Search..." id="pageURL">
                    <button class="ui icon button" onclick="annotate()">
                        <i class="search icon"></i>
                    </button>
                </div>

            </div>
            <h2>The annotation tool of the 21st century</h2>
            <div class="info-box">
                <div class="ui statistic">
                    <div class="text value">
                        Three<br>
                        Thousand
                    </div>
                    <div class="label">
                        Signups
                    </div>
                </div>
                <div id="contact-buttons">
                    <button class="ui small social secondary github button"
                            onclick="location.href='https://github.com/cs032-2020/term-project-ading6-cshi18-jgong15-sshaw4-1';">
                        <i class="github icon"></i>
                        Repository
                    </button>
                    <button class="ui small social blue email button"
                            onclick="location.href='mailto:andrew_ding@brown.edu?cc=seiji_shaw@brown.edu, jason_gong@brown.edu, charles_shi@brown.edu&subject=Inquiry%20Regarding%20Citogenesis';">
                        <i class="envelope icon"></i>
                        Email Us
                    </button>
                    <div class="ui labeled button" tabindex="0">
                        <div class="ui small red button">
                            <i class="heart icon"></i> Like
                        </div>
                        <a class="ui basic red left pointing label">
                            31
                        </a>
                    </div>
                </div>
        </div>




        </div>





<#--        HTML FOR FAQ SECTION-->
        <div class="content-block" id="faq-block">
            <h2>Frequently Asked</h2>
            <div class="ui divider"></div>
            <h3>Q: How do I use this?</h3>
            <p>We use this by blah blah blah</p>
            <h3>Q: How do I use this?</h3>
            <p>We use this by blah blah blah</p>
            <h3>Q: How do I use this?</h3>
            <p>We use this by blah blah blah</p>
        </div>



<#--        HTML FOR ABOUT SECTION-->
        <div class="content-block" id="about-block">
            <h2>About Us</h2>
            <div class="ui divider"></div>
            <h3>Project</h3>
            <p>We use this by blah blah blah</p>
            <h3>Background</h3>
            <p>We use this by blah blah blah</p>
            <h3>Future Interests</h3>
            <p>We use this by blah blah blah</p>
        </div>

    </div>
    <div class="footer">

    </div>



    <script src="/js/jquery-2.1.1.js"></script>
</body>
<!-- See http://html5boilerplate.com/ for a good place to start
     dealing with real world issues like old browsers.  -->