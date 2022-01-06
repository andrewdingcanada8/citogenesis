/**
 * NOTE: These js lines have a custom code structure where each new indentation
 * describes the properties of the html tag above it.
 */
function regularCard (data) {
    console.log("regularCard is being created...");
    const LIST_LENGTH = 5;
    let column = document.getElementById("annotation-column");
    let srcList = data.payload.jGenSources;

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
                    cName.innerText = data.payload.citeTitle;
                header.appendChild(cName);

                let bubble =
                    document.createElement("a");
                    // TODO: CODE FOR BUBBLE DATA
                    if (srcList.length === 1) {
                        bubble.className = "ui src blue circular label";
                        bubble.innerText = srcList.length;
                    } else if (srcList.length > 1) {
                        bubble.className = "ui src green circular label";
                        bubble.innerText = srcList.length;
                    } else {
                        bubble.className = "ui src circular label";
                        bubble.innerText = srcList.length;
                    }


                header.appendChild(bubble);
            cardTop.appendChild(header);

            let meta =
                document.createElement("div");
                meta.className = "meta";

                let a =
                    document.createElement("a");
                    // TODO: CODE FOR META DATA
                    a.innerText = "Webpage";
                    a.href =  data.payload.citeURL;
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
                sourceTitle.innerText = "Generating Sources:";
            cardBottom.appendChild(sourceTitle);

            // TODO: Maybe don't include the following:
            let oList =
                document.createElement("div");
                oList.className = "ui ordered list";
                for (let i = 0; i < srcList.length; i++) {
                    if (i > LIST_LENGTH) {break;}
                    let a = document.createElement("a");
                    a.className = "item";
                    a.href = srcList[i].url;
                    a.innerText = srcList[i].title;
                    oList.appendChild(a);
                }
            cardBottom.appendChild(oList);
            if ((data.payload.hasCycles) && (srcList.length > 1)){
                let crLabel =
                    document.createElement("h5");
                crLabel.className = "ui red header";
                crLabel.innerText = "Circular reporting found.";
                cardBottom.appendChild(crLabel);
            }

            // TODO: might delete this section:
            if (srcList.length > 1) {
                let graphButton =
                    document.createElement("button");
                    graphButton.className = "ui small violet inverted right labeled icon button graph";
                    graphButton.onclick = function() {graph(data.payload.citeId)};

                    let rightArrow =
                        document.createElement("i");
                        rightArrow.className = "right arrow icon";
                    graphButton.appendChild(rightArrow);

                    let graphButtonText =
                        document.createElement("span");
                        graphButtonText.innerText = "See Graph";
                    graphButton.appendChild(graphButtonText);
                cardBottom.appendChild(graphButton);
            }
        card.appendChild(cardBottom);
    column.appendChild(card);
}