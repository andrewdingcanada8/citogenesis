const MESSAGE_TYPE = {
    CONNECT: 0,
    URLSUBMISSION: 1,
    HTML: 2,
    CITATION: 3
};

let conn = null;
let myId = -1;
let graphs = {};

// Fires on page load.
$(document).ready(() => {
    setup_socket();
});

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
                console.log('ERROR: Unknown message type: ', data.type);
                break;
            case MESSAGE_TYPE.CONNECT:
                myId = parseInt(data.payload.id, 10);
                console.log("Connect Session ID: " + myId);
                urlSubmit();
                break;
            case MESSAGE_TYPE.HTML:
                insertHTML(data);
                break;
            case MESSAGE_TYPE.CITATION:
                new_annotation(data);
                let id = data.payload.citeId;
                let graph = data.payload.jGraph;
                graphs[id] = graph;
                break;
        }
    };
}

// Called when a user clicks the annotate button
function urlSubmit() {
    let submitURL = window.location.href;
    submitURL = submitURL.substr(submitURL.lastIndexOf("/")-4, submitURL.length);
    conn.send(JSON.stringify({type: MESSAGE_TYPE.URLSUBMISSION, payload: {
            id: myId,
            url: "https://en.wikipedia.org/" + submitURL
        }}));
}

/**
 * Helper function that inserts the shortened content HTML of the wikipedia
 * article. HTML should be pre-shortened and have its tags already added.
 * @param data
 */
function insertHTML(data) {
    let html = data.payload.html;
    let div = document.getElementById("wiki-page");
    div.insertAdjacentHTML("beforeend", html);
}