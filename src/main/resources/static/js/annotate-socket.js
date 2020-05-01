const MESSAGE_TYPE = {
    CONNECT: 0,
    URLSUBMISSION: 1,
    HTML: 2,
    CITATION: 3
};

let conn = null;
let myId = -1;

$(document).ready(() => {
    console.log("hahahahhaha"); // TODO: Delete Later
    setup_hover();
    setup_socket();
});


function insertHTML(data) {
    let html = data.payload.html;
    // html = "<p> just a simple farmer</p>";
    // console.log(html);
    let div = document.getElementById("content");
    div.insertAdjacentHTML("beforeend", html);
}

function newAnnotation(data) {
    console.log("NEW ANNOTATION!"); // TODO: Delete Later
    let citeRefText = data.payload.citeRefText;
    let citeId = data.payload.citeId;
    let citeTitle = data.payload.citeTitle;
    let citeType = data.payload.citeType;
    let citeURL = data.payload.citeURL;
    let hasCycles = data.payload.hasCycles;
    let genSrcList = data.payload.jGenSources;

    let column = document.getElementById("annotationColumn");

    let card = document.createElement("div");
    card.className = "annotationCard";
    card.id = citeId;
    column.appendChild(card);

    let citeLink = document.createElement("a");
    citeLink.href = citeURL;
    citeLink.innerText = citeRefText;
    card.appendChild(citeLink);

    let genP = document.createElement("p");
    genP.innerText = "Generating Sources (" + genSrcList.length + "):";
    card.appendChild(genP);

    let genList = document.createElement("ol");
    card.appendChild(genList);
    let counter = 0;
    for (let source in genSrcList) {
        if (counter > 5) {
            break;
        }
        counter++;
        let li = document.createElement("li");
        let a = document.createElement("a");
        a.innerText = source.title;
        a.href = source.url;
        li.appendChild(a);
        genList.appendChild(li);
    }

    let circularReport = document.createElement("p");
    circularReport.innerText = "Circular Reporting: " + hasCycles;
    card.appendChild(circularReport);
}


function setup_hover () {
    $(".a-link1").mouseover(function (evt) {
        evt.preventDefault();
        console.log("hey");
        // window.location.href = "#"+anchor;
        window.location.href = "#annotation0";
    });
}


// Setup the WebSocket connection for live updating of scores.
function setup_socket () {
    conn = new WebSocket('ws://' + window.location.host + '/citation-socket');

    conn.onerror = err => {
        console.log('Connection error:', err);
    };

    conn.onmessage = msg => {
        const data = JSON.parse(msg.data);
        switch (data.type) {
            default:
                console.log('Unknown message type!', data.type);
                break;
            case MESSAGE_TYPE.CONNECT:
                myId = parseInt(data.payload.id, 10);
                console.log("CONNECT MESSAGE RECIEVED: " + myId); // TODO: Delete Later
                urlSubmit();
                break;
            case MESSAGE_TYPE.HTML:
                console.log("HTML MESSAGE RECIEVED"); // TODO: Delete Later
                insertHTML(data);
                break;
            case MESSAGE_TYPE.CITATION:
                console.log("CITATION MESSAGE RECIEVED"); // TODO: Delete Later
                newAnnotation(data);
                break;

        }
    };
}

/**
 * Called when a user clicks the annotate button
 * @param url - a string that contains the website url to be annotated
 */
function urlSubmit() {
    let submitURL = window.location.href;
    submitURL = submitURL.substr(submitURL.lastIndexOf("/")-4, submitURL.length);
    console.log("Short wiki url sent: " + submitURL); // TODO: Delete Later
    conn.send(JSON.stringify({type: MESSAGE_TYPE.URLSUBMISSION, payload: {
            id: myId,
            url: "https://en.wikipedia.org/" + submitURL
        }}));
}
