const MESSAGE_TYPE = {
  CONNECT: 0,
  VERTEX: 1
};

let conn = null;
let myId = -1;

// Setup the WebSocket connection for live updating of scores.
setup_live_scores = () => {
  conn = new WebSocket('ws://' + window.location.host + '/test');

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
      case MESSAGE_TYPE.VERTEX:
        const score = parseInt(data.payload.url, 10);
        $('#url').html(score);
        break;
    }
  };
}

// Should be called when a user blips
// `guesses` should be the array of guesses the user has made so far.
const new_guess = guesses => {
  const text = guesses.join(' ');
  conn.send(JSON.stringify({type: MESSAGE_TYPE.VERTEX, payload: {
    id: myId,
    url: "fillerurl.com"
  }}));
}
