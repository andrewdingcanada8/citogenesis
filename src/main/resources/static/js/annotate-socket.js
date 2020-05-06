const MESSAGE_TYPE = {
    CONNECT: 0,
    URLSUBMISSION: 1,
    HTML: 2,
    CITATION: 3,
    GRAPH: 4
};

let conn = null;
let myId = -1;

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
                new_annotation(data);
                break;
            case MESSAGE_TYPE.GRAPH:
                console.log("GRAPH MESSAGE RECIEVED");
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
    let htm = html.substr(0, 200);
    console.log(htm);
    let div = document.getElementById("wiki-page");
    div.insertAdjacentHTML("beforeend", html);
}