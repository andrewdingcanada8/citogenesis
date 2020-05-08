/**
 * NOTE: These js lines have a custom code structure where each new indentation
 * describes the properties of the html tag above it.
 */
function nullListCard (data) {
    console.log("nullListCard is being created...");
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
                    bubble.className = "ui src blue circular label";
                    bubble.innerText = "1";


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
                sourceTitle.innerText = "No further connections found.";
            cardBottom.appendChild(sourceTitle);
        card.appendChild(cardBottom);
    column.appendChild(card);
}