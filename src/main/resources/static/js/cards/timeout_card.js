/**
 * NOTE: These js lines have a custom code structure where each new indentation
 * describes the properties of the html tag above it.
 */
function timeoutCard (data) {
    console.log("timeoutCard is being created...");
    let column = document.getElementById("annotation-column");

    let card = // Card Container
        document.createElement("div");
        card.className = "card";
        card.id = data.payload.citeId;

        let cardTop = // Top Content Box
            document.createElement("div");
            cardTop.className = "content";

            let header =
                document.createElement("div");
                header.className = "header";

                let cName =
                    document.createElement("span");
                    cName.className = "citation-title";
                    cName.innerText = "Citation Timed Out: " + data.payload.citeId;
                header.appendChild(cName);

                let bubble =
                    document.createElement("a");
                    // TODO: CODE FOR BUBBLE DATA
                    bubble.className = "ui src red circular label";
                    bubble.innerText = "!";


                header.appendChild(bubble);
            cardTop.appendChild(header);

            let meta =
                document.createElement("div");
                meta.className = "meta";

                let a =
                    document.createElement("a");
                    // TODO: CODE FOR META DATA
                    a.innerText = "Query Timeout";
                    a.href =  "";
                meta.appendChild(a);
            cardTop.appendChild(meta);
        card.appendChild(cardTop);

        let cardBottom = // Bottom Content Box
            document.createElement("div");
            cardBottom.className = "content";

            let sourceTitle =
                document.createElement("h4");
                sourceTitle.className = "ui sub header";
                // TODO: Source Title specific text
                sourceTitle.innerText = "Could not load citation information.";
            cardBottom.appendChild(sourceTitle);

            // TODO: might delete this section:
            let graphButton =
                document.createElement("button");
                graphButton.className = "ui small orange right labeled icon button graph";
                graphButton.onclick = function() {annotate_again(data)};
                let rightArrow =
                    document.createElement("i");
                    rightArrow.className = "right arrow icon";
                graphButton.appendChild(rightArrow);

                let graphButtonText =
                    document.createElement("span");
                    graphButtonText.innerText = "Try Again";
                graphButton.appendChild(graphButtonText);
            cardBottom.appendChild(graphButton);
        card.appendChild(cardBottom);
    column.appendChild(card);
}