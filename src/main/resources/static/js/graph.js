let dummy = '{"vertices":[{"title":"NOT HTML PAGE","url":"http://www.npr.org/sections/thetwo-way/2016/12/19/506134622/scientists-blast-antimatter-atoms-with-a-laser-for-the-first-time"},{"title":"NOT HTML PAGE","url":"http://journals.aps.org/prd/abstract/10.1103/PhysRevD.94.023528"},{"title":"NOT HTML PAGE","url":"http://www.eurekalert.org/pub_releases/2015-11/dnl-pmf103015.php"},{"title":"Scientists Measure Force Binding Antiprotons Together To Probe Why Antimatter Is Extremely Rare | Tech Times","url":"http://www.techtimes.com/articles/103499/20151105/scientists-measure-force-binding-antiprotons-together-to-probe-why-antimatter-is-extremely-rare.htm"},{"title":"Universe Should Not Actually Exist: Big Bang Produced Equal Amounts Of Matter And Antimatter | Tech Times","url":"http://www.techtimes.com/articles/214821/20171025/universe-should-not-actually-exist-big-bang-produced-equal-amounts-of-matter-and-antimatter.htm"},{"title":"NOT HTML PAGE","url":"https://www.nature.com/nature/journal/v550/n7676/full/nature24048.html"},{"title":"NOT HTML PAGE","url":"http://www.nature.com/nature/journal/vaop/ncurrent/full/nature15724.html"},{"title":"CERN Scientists Use Laser To Observe Light Spectrum Of Antimatter [Video] | Tech Times","url":"http://www.techtimes.com/articles/189426/20161219/cern-scientists-use-laser-to-observe-light-spectrum-of-antimatter.htm"},{"title":"NOT HTML PAGE","url":"http://www.uni-mainz.de/presse/aktuell/3027_ENG_HTML.php"},{"title":"Universe Lost Up To 5 Percent Of Dark Matter Since Big Bang | Tech Times","url":"http://www.techtimes.com/articles/190898/20161231/universe-lost-up-to-5-percent-of-dark-matter-since-big-bang.htm"},{"title":"NOT HTML PAGE","url":"http://www.livescience.com/32387-what-is-antimatter.html"},{"title":"NOT HTML PAGE","url":"http://home.cern/about/updates/2016/12/alpha-observes-light-spectrum-antimatter-first-time"}],"map":{"http://www.npr.org/sections/thetwo-way/2016/12/19/506134622/scientists-blast-antimatter-atoms-with-a-laser-for-the-first-time":[],"http://journals.aps.org/prd/abstract/10.1103/PhysRevD.94.023528":[],"http://www.eurekalert.org/pub_releases/2015-11/dnl-pmf103015.php":[],"http://www.techtimes.com/articles/103499/20151105/scientists-measure-force-binding-antiprotons-together-to-probe-why-antimatter-is-extremely-rare.htm":[{"title":"NOT HTML PAGE","url":"http://www.eurekalert.org/pub_releases/2015-11/dnl-pmf103015.php"},{"title":"NOT HTML PAGE","url":"http://www.nature.com/nature/journal/vaop/ncurrent/full/nature15724.html"}],"http://www.techtimes.com/articles/214821/20171025/universe-should-not-actually-exist-big-bang-produced-equal-amounts-of-matter-and-antimatter.htm":[{"title":"NOT HTML PAGE","url":"https://www.nature.com/nature/journal/v550/n7676/full/nature24048.html"},{"title":"NOT HTML PAGE","url":"http://www.uni-mainz.de/presse/aktuell/3027_ENG_HTML.php"},{"title":"Universe Lost Up To 5 Percent Of Dark Matter Since Big Bang | Tech Times","url":"http://www.techtimes.com/articles/190898/20161231/universe-lost-up-to-5-percent-of-dark-matter-since-big-bang.htm"},{"title":"CERN Scientists Use Laser To Observe Light Spectrum Of Antimatter [Video] | Tech Times","url":"http://www.techtimes.com/articles/189426/20161219/cern-scientists-use-laser-to-observe-light-spectrum-of-antimatter.htm"}],"https://www.nature.com/nature/journal/v550/n7676/full/nature24048.html":[],"http://www.nature.com/nature/journal/vaop/ncurrent/full/nature15724.html":[],"http://www.techtimes.com/articles/189426/20161219/cern-scientists-use-laser-to-observe-light-spectrum-of-antimatter.htm":[{"title":"Scientists Measure Force Binding Antiprotons Together To Probe Why Antimatter Is Extremely Rare | Tech Times","url":"http://www.techtimes.com/articles/103499/20151105/scientists-measure-force-binding-antiprotons-together-to-probe-why-antimatter-is-extremely-rare.htm"},{"title":"NOT HTML PAGE","url":"http://www.npr.org/sections/thetwo-way/2016/12/19/506134622/scientists-blast-antimatter-atoms-with-a-laser-for-the-first-time"},{"title":"NOT HTML PAGE","url":"http://home.cern/about/updates/2016/12/alpha-observes-light-spectrum-antimatter-first-time"},{"title":"NOT HTML PAGE","url":"http://www.livescience.com/32387-what-is-antimatter.html"}],"http://www.uni-mainz.de/presse/aktuell/3027_ENG_HTML.php":[],"http://www.techtimes.com/articles/190898/20161231/universe-lost-up-to-5-percent-of-dark-matter-since-big-bang.htm":[{"title":"NOT HTML PAGE","url":"http://journals.aps.org/prd/abstract/10.1103/PhysRevD.94.023528"}],"http://www.livescience.com/32387-what-is-antimatter.html":[],"http://home.cern/about/updates/2016/12/alpha-observes-light-spectrum-antimatter-first-time":[]}}';

function graph(id) {
    $('.ui.inverted.dimmer').dimmer('show');
    console.log("Graph Showed up");

    //let jGraph = graphs[id];
    let jGraph = JSON.parse(dummy);



    // EXAMPLE CODE
    /*let node1 = {
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
    ]);*/

    let nodes = new vis.DataSet(jGraph.vertices.map(src => {
        return {id: src.url, label: src.title}
    }));

    let edges = new vis.DataSet(
        Object.getOwnPropertyNames(jGraph.map).flatMap(url => {
            // obtain all adjacent urls to the current one in map
            let adjUrls = jGraph.map[url].map(src => src.url);
            // construct edge array
            return adjUrls.map(adjUrl => {
                return {from: adjUrl, to: url}
            });
        }));

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
                color: '#000000'
            },
            borderWidth: 2
        },
        edges: {
            width: 2,
            arrows: 'to'
        },
        layout: {
            /*hierarchical: {
                sortMethod: 'directed',
                direction: 'RL',
                edgeMinimization: true
            },*/
            improvedLayout: true
        }
    };

    // initialize your network!
    let network = new vis.Network(container, data, options);
}

//