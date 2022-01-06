const CITATION_TITLE_LENGTH = 45;
let deprecatedTitles = new Set([
    "301 Moved Permanently",
    "302 Moved Permanently",
    "308 Permanent Redirect",
    "404 Not Found",
    "Handle Redirect"
]);

function new_annotation(data) {
    console.log('Citation rcvd (' + data.payload.citeType + '). ID = ' + data.payload.citeId);
    // Shortens string to fit within card
    if (data.payload.citeTitle.length > CITATION_TITLE_LENGTH) {
        data.payload.citeTitle = data.payload.citeTitle.substr(0, CITATION_TITLE_LENGTH);
        data.payload.citeTitle = data.payload.citeTitle.concat('...');
    }

    // Variables that help determine what type of card is made
    let citeType = data.payload.citeType;
    let hasCycles = data.payload.hasCycles;
    let srcList = data.payload.jGenSources;

    // Logic for selecting card type
    switch (citeType) {
        case "Self":
            console.log("***** NOTE: WIKIPEDIA HAS CITED ITSELF *****");
            break;
        case "Other":
            otherCard(data);
            break;
        case "Time Out":
            timeoutCard(data);
            break;
        case "Non-HTML":
            deprecatedCard(data);
            break;
        case "Web":
            if ((srcList !== null) && (srcList.length > 0)) {
                if (!isDeprecated(srcList)) {
                    regularCard(data);
                } else {
                    deprecatedCard(data);
                }
            } else {
                nullListCard(cardData, data);
            }
            break;
        default:
            console.log("ERROR: Card " + data.payload.citeId + " not created properly");
            break;
    }
}

function isDeprecated (list) {
    for (let i = 0; i < list.length; i++) {
        let title = list[i].title;
        if (deprecatedTitles.has(title)) {
            return true;
        }
        if (title.includes('Page not found')
            || title.includes('page not found')
            || title.includes('Page Not Found')) {
            return true;
        }
    }
    return false;
}

function annotate_again(data) {
    // delete the old one
    // create a new one
}