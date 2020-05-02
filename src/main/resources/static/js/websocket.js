const MESSAGE_TYPE = {

  SUBMIT1: 0,
  // SUBMIT1: when the user clicks on 'Annotate'
  // payload: id, url

  SUBMIT2: 1,
  // SUBMIT2: when the user clicks on 'Graph'
  // payload: id, url

  ANNOTATION: 2,
  // ANNOTATION: Server sent annotation information
  // payload: TODO: tbd

  GRAPH: 3
  // GRAPH: Server sent graphh information
  // payload: TODO: tbd
};

let conn = null;
let myId = -1;

$(document).ready(() => {
  setup_click();
  setup_socket();
});




function setup_click() {
  // For Annotation Submit Button
  $('#annotateSubmitButton').click(function(evt) {
    evt.preventDefault();
    let url = $('#pageURL').val();
    annotate(url);
  });
  // For Graph Submit Button
  $('#graphSubmitButton').click(function(evt) {
    evt.preventDefault();
    let url = $('#pageURL').val();
    graph(url);
  });
}

// Setup the WebSocket connection for live updating of scores.
function setup_socket () {
  conn = new WebSocket('ws://' + window.location.host + '/socket-process');

  conn.onerror = err => {
    console.log('Connection error:', err);
  };

  conn.onmessage = msg => {
    const data = JSON.parse(msg.data);
    switch (data.type) {
      default:
        console.log('Unknown message type!', data.type);
        break;
      case MESSAGE_TYPE.ANNOTATION:
        const url = data.payload.url;
        console.log(url);
        $('#result').text(url);
        break;
      case MESSAGE_TYPE.GRAPH:
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