<div class="card" id="card-1">
    <div class="content">
        <div class="header">
            <span class="citation-title">Citation 1</span>
            <a class="ui src green circular label">3</a>
        </div>
        <div class="meta">
            <a href="google.com">Webpage</a>
        </div>
    </div>
    <div class="content">
        <h4 class="ui sub header">Generating Sources:</h4>
        <div class="ui ordered list">
            <a class="item" href="">Src 1</a>
            <a class="item" href="">Src 2</a>
        </div>
        <h5 class="ui red header">Circular reporting found.</h5>
        <button class="ui small violet inverted right labeled icon button graph">
            <i class="right arrow icon"></i>
            <span>See Graph</span>
        </button>
    </div>
</div>



// Building Annotation Card HTML
// Annotation Column
let column = document.getElementById("annotation-column");
// Card Container
let card = document.createElement("div");
card.className = "card";
card.id = citeId;
column.appendChild(card);

// First Section of Card
let content1 = document.createElement("div");
content1.className = "content";
card.appendChild(content1);
// Citation Title
let header = document.createElement("div");
header.className = "header";
content1.appendChild(header);
let span = document.createElement("span");
span.innerText = citeRefText;
span.className = "citation-title";
header.appendChild(span);
let bubbleCount = document.createElement("a");
header.appendChild(bubbleCount); // The contents of bubbleCount are set up later in srcList section
// Citation Type
let meta = document.createElement("div");
meta.className = "meta";
content1.appendChild(meta);
let a = document.createElement("a"); // TODO: reassignment of a might cause issues.
a.innerText = citeTypeText;
a.href = citeURL;
meta.appendChild(a);


// Second Section of Card
let content2 = document.createElement("div");
content2.className = "content";
card.appendChild(content2);
// Generating Sources Subtitle
let h4 = document.createElement("h4");
h4.className = "ui sub header";
h4.innerText = "Generating Sources:";
content2.appendChild(h4);
// List
let htmlList = document.createElement("div");
htmlList.className = "ui ordered list";
content2.appendChild(htmlList);
// Finding All Generating Sources
if (srcList !== null) {
// Adding Generating Sources
for (let i = 0; i < srcList.length; i++) {
if (i > 5) {
break;
}
let a = document.createElement("a");
a.innerText = srcList[i].title;
a.className = "item";
a.href = srcList[i].url;
htmlList.appendChild(a);
}
// Changing Color of Bubble
if (srcList.length === 0) { // TODO: modifying a after a has already been added might cause issues
bubbleCount.className = "ui src orange circular label";
bubbleCount.innerText = "1";
} else if (srcList.length < 1) {
bubbleCount.className = "ui src yellow circular label";
bubbleCount.innerText = srcList.length + 1;
} else {
bubbleCount.className = "ui src green circular label";
bubbleCount.innerText = srcList.length + 1;
}
if (hasCycles === true) {
bubbleCount.className = "ui src red circular label";
let crWarning = document.createElement("h5");
// Circular Reporting Warning Label
crWarning.className = "ui red header";
crWarning.innerText = "Circular reporting found.";
content2.appendChild(crWarning);
}
} else {
bubbleCount.className = "ui src orange circular label";
bubbleCount.innerText = "1"; // TODO: not sure how we want to handle this behavior. 1 or 0?
let a = document.createElement("a");
a.innerText = 'Self Referenced.'; // TODO: not sure how we want to handle this behavior
a.className = "item";
htmlList.appendChild(a);
}
if (srcList !== null) {
// Graph Toggle Button
let graphButton = document.createElement("button");
graphButton.className = "ui small violet inverted right labeled icon button graph";
graphButton.onclick = function() {graph(citeId)};
content2.appendChild(graphButton);
let rightArrow = document.createElement("i");
rightArrow.className = "right arrow icon";
let graphButtonText = document.createElement("span");
graphButtonText.innerText = "See Graph";
graphButton.appendChild(rightArrow);
graphButton.appendChild(graphButtonText);
}