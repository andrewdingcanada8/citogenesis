<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- In real-world webapps, css is usually minified and
         concatenated. Here, separate normalize from our code, and
         avoid minification for clarity. -->
    <link rel="stylesheet" href="/css/normalize.css">
    <link rel="stylesheet" href="/css/html5bp.css">
    <link rel="stylesheet" type="text/css" href="/semantic/dist/semantic.min.css">
    <link rel="stylesheet" href="/css/wiki1.css">
    <link rel="stylesheet" href="/css/wiki2.css">
    <link rel="stylesheet" href="/css/annotate.css">
    <script src="/js/jquery-2.1.1.js"></script>
    <script src="/semantic/dist/semantic.min.js"></script>
    <script type="text/javascript" src="https://unpkg.com/vis-network/standalone/umd/vis-network.min.js"></script>

    <script src="/js/graph-socket.js"></script>

    <style type="text/css">
        #mynetwork {
            width: 600px;
            height: 600px;
            border: 1px solid lightgray;
        }
    </style>
</head>

<body>
<div id="mynetwork"></div>

<script type="text/javascript">
    let node1 = {
        id: '1',
        label: 'AHHA',
        group: '5'
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
</script>
</body>
<!-- See http://html5boilerplate.com/ for a good place to start
     dealing with real world issues like old browsers.  -->
</html>
