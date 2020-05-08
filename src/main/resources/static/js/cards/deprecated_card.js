/**
 * NOTE: These js lines have a custom code structure where each new indentation
 * describes the properties of the html tag above it.
 */
function deprecatedCard (data) {
    console.log("deprecatedCard is being created...");
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
                    cName.innerText = data.payload.citeTitle ": " + data.payload.citeId;
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
                    a.innerText = "Deprecated Webpage";
                meta.appendChild(a);
            cardTop.appendChild(meta);
        card.appendChild(cardTop);

        let cardBottom = // Bottom Content Box
            document.createElement("div");
            cardBottom.className = "content";

            let crLabel =
                document.createElement("h5");
                crLabel.className = "ui red header";
                crLabel.innerText = "Warning: citation source " + data.citeId + " is deprecated.";
            cardBottom.appendChild(crLabel);
        card.appendChild(cardBottom);
    column.appendChild(card);
}