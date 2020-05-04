const MESSAGE_TYPE = {
    CONNECT: 0,
    URLSUBMISSION: 1,
    GRAPH: 2
};

let conn = null;
let myId = -1;

$(document).ready(() => {
    console.log("hahahahhaha");
    // setup_hover();
    setup_socket();
});

// function newAnnotation(data) {
//     let id = data.payload.id;
//     let hasCycles = data.payload.hasCycles;
//     let citeSource = data.payload.citeSource;
//     let list = data.genSources;
//     console.log(id + hasCycles + citeSource + list);
//     $('#result').text(id + hasCycles + citeSource + list);
//     // let newDiv = document.createElement("div");
//
//     // document.body.appendChild(newDiv);
// }
//
// function setup_hover () {
//     $(".a-link1").mouseover(function (evt) {
//         evt.preventDefault();
//         console.log("hey");
//         // window.location.href = "#"+anchor;
//         window.location.href = "#annotation0";
//     });
// }

// Setup the WebSocket connection for live updating of scores.
function setup_socket () {
    conn = new WebSocket('ws://' + window.location.host + '/graph-socket');

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
                urlSubmit();
                break;
            case MESSAGE_TYPE.GRAPH:
                // newAnnotation(data);
                console.log(data.payload);
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
    console.log(submitURL);
    conn.send(JSON.stringify({type: MESSAGE_TYPE.URLSUBMISSION, payload: {
            id: myId,
            url: "https://en.wikipedia.org/" + submitURL
        }}));
}
