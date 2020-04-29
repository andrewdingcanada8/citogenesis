const MESSAGE_TYPE = {
    CONNECT: 0,
    URLSUBMISSION: 1,
    CITATION: 2
};

let conn = null;
let myId = -1;

$(document).ready(() => {
    console.log("hahahahhaha");
    setup_socket();
});

function newAnnotation() {
    let newDiv = document.createElement("div");

    document.body.appendChild(newDiv);
}

function setup_hover () {
    $(".a-link1").mouseover(function (evt) {
        evt.preventDefault();
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
                console.log(myId);
                break;
            case MESSAGE_TYPE.CITATION:
                let citation = data.payload.citation;
                let id = data.payload.id;
                const url = data.payload.url;
                console.log(url);
                $('#result').text(url);
                break;
        }
    };
}

/**
 * Called when a user clicks the annotate button
 * @param url - a string that contains the website url to be annotated
 */
const annotate = url => {
    conn.send(JSON.stringify({type: MESSAGE_TYPE.ANNOTATE, payload: {
            id: myId,
            url: url
        }}));
}

/**
 * Called when a user clicks the graph button
 * @param url - a string that contains the website url to be graphed
 */
const graph = url => {
    conn.send(JSON.stringify({type: MESSAGE_TYPE.GRAPH, payload: {
            id: myId,
            url: url
        }}));
}