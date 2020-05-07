function graph(id) {
    $('.ui.inverted.dimmer').dimmer('show');
    console.log("Graph Showed up");

    let jGraph = graphs[id];

    // This is the object structure for nodes
    let node1 = {
        id: '', // URL
        label: 'AHHA', // TITLE
        group: '5' // COLORS
    };

    // create an array with nodes
    let nodes = new vis.DataSet([
        node1,
        {id: 2, label: 'Node 2'},
        {id: 3, label: 'Node 3'},
        {id: 4, label: 'Node 4'},
        {id: 5, label: 'Node 5'}
    ]);

    // create an array with edges
    let edges = new vis.DataSet([
        {from: 1, to: 3},
        {from: 1, to: 2},
        {from: 2, to: 4},
        {from: 2, to: 5}
    ]);

    // create a network
    let container = document.getElementById('mynetwork');

    // provide the data in the vis format
    let data = {
        nodes: nodes,
        edges: edges
    };
    let options = {
        nodes: {
            shape: 'dot',
            size: 30,
            font: {
                size: 32,
                color: '#ffffff'
            },
            borderWidth: 2
        },
        edges: {
            width: 2
        }
    };

    // initialize your network!
    let network = new vis.Network(container, data, options);
}

//