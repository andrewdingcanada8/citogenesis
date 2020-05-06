const CITATION_TITLE_LENGTH = 45;
let deprecatedTitles = new Set([
    "301 Moved Permanently",
    "302 Moved Permanently",
    "308 Permanent Redirect",
    "404 Not Found",
    "Handle Redirect"
]);

function new_annotation(data) {

    // Local card object to make data easier to access
    let card = {
        citeId: "",
        name: "",
        metaLink: "",
    };

    // Extract Payload Properties
    card.citeId = data.payload.citeId;
    card.name = data.payload.citeRefText;
    card.metaLink = data.payload.citeURL;

    // Shortens string to fit within card
    if (card.name.length > CITATION_TITLE_LENGTH) {
        card.name = card.name.substr(0, CITATION_TITLE_LENGTH);
        card.name = card.name.concat('...');
    }

    // Variables that help determine what type of card is made
    let citeType = data.payload.citeType;
    let hasCycles = data.payload.hasCycles;
    let srcList = data.payload.jGenSources;

    // Logic for selecting card type
    switch (citeType) {
        default:
            console.log("Error: Card " + card.citeId + " not created properly");
            break;
        case "Self":
            citeTypeText = "Other";
            break;
        case "Other":
            citeTypeText = "Other";
            break;
        case "Time Out":
            citeTypeText = "Other";
            break;
        case "Non-HTML": // TODO: confirm if this is correct
            citeTypeText = "Other";
            break;

        case "Web":
            if ((srcList !== null) && (srcList.length > 0)) {
                if (!isDeprecated(srcList)) {
                    regularCard(card, data);
                } else {
                    deprecatedCard(card, data);
                }
            } else {
                nullListCard(card, data);
            }
            break;
    }
}

function isDeprecated (list) {
    for (let i = 0; i < list.length; i++) {
        let title = list[i].title;
        if (deprecatedTitles.has(title)) {
            return true;
        }
    }
    return false;
}
