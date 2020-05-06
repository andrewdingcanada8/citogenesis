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
    <script src="/js/search.js"></script>
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
            <h2>An annotation tool for the 21st century</h2>
            <div class="info-box">
                <div class="ui statistic">
                    <div class="text value">
                        Three<br>
                        Thousand
                    </div>
                    <div class="label">
                        Searches
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
            <h3>Q: Where's the lamb sauce?</h3>
            <p>The <b><i>magic</i></b> of Citogenesis is achieved through our two main our algorithms of citation website exploration and generating-source detection.</p>
            <h4>Citation Wesbite Exploration?</h4>
            <p>When our server receives a citation URL from the Wikipedia page, it starts browsing the current citation and all of its linked web pages (and the links of those links, etc).To ensure the web pages are feasible sources for our original cited Wikipedia text, we run an algorithm that scores the relevance of a web page based on the frequency of common words to the original text. On all of the web pages that pass the relevancy score, we build a network (or graph) that represents the sourcing relationships between each of the pages.</p>
            <h4>Generating-Source wuh?</h4>
            <p>Once we compute the citation's network of source pages, we can then look for circular reporting and find the citation's generating sources. We first run an algorithm to segment the network into cycles of pages. Typically, those "cycles" only contain one web page (which is good), but if there is a cycle present with more than one website, then we know there is some form of circular reporting happening. Our server will let you know if this is true.</p>
            <p>We then examine each cycle possible generating sources, with a maximum of one per cycle. We call a candidate web site a generating source if it satisfies the following criteria:</p>
            <div class="ui list">
                <div class="item">- the candidate source is the oldest source in its cycle</div>
                <div class="item">- the candidate source's linked sources are present in the candidate's cycle</div>
            </div>
            <p>After computing these generating sources, our server will compile them in a list and send them back to you!</p></p>
        </div>



<#--        HTML FOR ABOUT SECTION-->
        <div class="content-block" id="about-block">
            <h2>About Us</h2>
            <div class="ui divider"></div>
            <h3>Project</h3>
            <p>We use this by blah blah blah</p>
            <h3>Background</h3>
            <p>We are the <i>Pasta Coders</i>, a team of CS undergraduate students from Brown University. This project was initially created as our <i>CSCI0320: Software Eng.</i> term project.</p>
            <h3>Future Plans</h3>
            <p>Having a nice relaxing summer. Coding this project during the covid-19 outbreak was not without challenges. 10 hour zoom calls not recommended.</p>
            <p>See you on the beach,</p>
            <p>Pasta Coders Dev. Team</p>
            <img src="assets/flamingo.svg" alt="Imaginary Vacations in Quarantine Times" style="float: right; width: 200px; margin-right: 50px;">
        </div>

    </div>
    <div class ="content-block footer">

    </div>



    <script src="/js/jquery-2.1.1.js"></script>
</body>
<!-- See http://html5boilerplate.com/ for a good place to start
     dealing with real world issues like old browsers.  -->