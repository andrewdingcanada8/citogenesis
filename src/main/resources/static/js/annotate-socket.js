const MESSAGE_TYPE = {
    CONNECT: 0,
    URLSUBMISSION: 1,
    HTML: 2,
    CITATION: 3
};

let conn = null;
let myId = -1;
const CITATION_TITLE_LENGTH = 60;

$(document).ready(() => {
    setup_hover();
    setup_socket();
});

/**
 * Helper function that inserts the shortened content HTML of the wikipedia
 * article. HTML should be pre-shortened and have its tags already added.
 * @param data
 */
function insertHTML(data) {
    let html = data.payload.html;
    let div = document.getElementById("content");
    div.insertAdjacentHTML("beforeend", html);
}

function newAnnotation(data) {
    let citeRefText = data.payload.citeRefText;
    let citeId = data.payload.citeId;
    let citeTitle = data.payload.citeTitle;
    let citeType = data.payload.citeType;
    let citeURL = data.payload.citeURL;
    let hasCycles = data.payload.hasCycles;
    let srcList;
    if (citeType === 'Web') {
        srcList = data.payload.jGenSources;
    } else {
        srcList = null;
    }

    let column = document.getElementById("annotationColumn");

    let card = document.createElement("div");
    card.className = "annotationCard";
    card.id = citeId;
    column.appendChild(card);

    let citeLink = document.createElement("a");
    citeLink.href = citeURL;
    if (citeRefText.length > CITATION_TITLE_LENGTH) {
        citeRefText = citeRefText.substr(0, CITATION_TITLE_LENGTH); // Shortens string to 50 chars
        citeRefText = citeRefText.concat('...');
    }
    citeLink.innerText = citeRefText;
    card.appendChild(citeLink);

    let genP = document.createElement("p");
    if (srcList !== null) {
        genP.innerText = "Generating Sources (" + srcList.length + "):";
    } else {
        genP.innerText = "No Generating Sources Found";
    }

    card.appendChild(genP);

    let genList = document.createElement("ol");
    card.appendChild(genList);

    if (srcList !== null) {
        for (let i = 0; i < srcList.length; i++) {
            if (i > 5) {
                break;
            }
            let li = document.createElement("li");
            let a = document.createElement("a");
            a.innerText = srcList[i].title;
            a.href = srcList[i].url;
            console.log(srcList[i].title + " and " + srcList[i].url);
            li.appendChild(a);
            genList.appendChild(li);
        }
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
                // console.log("HTML MESSAGE RECIEVED"); // TODO: Delete Later
                insertHTML(data);
                break;
            case MESSAGE_TYPE.CITATION:
                // console.log("CITATION MESSAGE RECIEVED"); // TODO: Delete Later
                newAnnotation(data);
                break;

        }
    };
}

/**
 * Called when a user clicks the annotate button
 */
function urlSubmit() {
    let submitURL = window.location.href;
    submitURL = submitURL.substr(submitURL.lastIndexOf("/")-4, submitURL.length);
    conn.send(JSON.stringify({type: MESSAGE_TYPE.URLSUBMISSION, payload: {
            id: myId,
            url: "https://en.wikipedia.org/" + submitURL
        }}));
}
