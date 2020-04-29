const MESSAGE_TYPE = {
  CONNECT: 0,
  VERTEX: 1,
  NEIGHBORS: 2
};

let conn = null;
let myId = -1;

$(document).ready(() => {
  console.log("READY");
  setup_click();
  setup_hover();
  setup_socket();
});

function setup_click() {
  $('#submit').click(function(evt) {
    evt.preventDefault();
    let url = $('#socket-form').val();
    neighbors(url);
  });
}

function setup_hover () {
  $('.hover-demo').mouseover(function(evt) {
    evt.preventDefault();
    let url = $('#socket-form').val();
    neighbors(url);
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
      case MESSAGE_TYPE.CONNECT:
        myId = parseInt(data.payload.id, 10);
        break;
      case MESSAGE_TYPE.NEIGHBORS:
        const url = data.payload.url;
        console.log(url);
        $('#result').text(url);
        break;
    }
  };
}

// Should be called when a user blips
// `guesses` should be the array of guesses the user has made so far.
const neighbors = url => {
  conn.send(JSON.stringify({type: MESSAGE_TYPE.VERTEX, payload: {
    id: myId,
    url: url
  }}));
}
