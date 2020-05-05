function newAnnotation(data) {
    // Extract Payload Properties
    let citeRefText = data.payload.citeRefText;
    let citeId = data.payload.citeId;
    let citeTitle = data.payload.citeTitle;
    let citeType = data.payload.citeType;
    let citeURL = data.payload.citeURL;
    let hasCycles = data.payload.hasCycles;
    let srcList;
    if (citeType === 'Web') {
        srcList = data.payload.jGenSources;
    } else {
        srcList = null;
    }


    // Building Annotation Card HTML

    let column = document.getElementById("annotation-column");

    let card = document.createElement("div");
    card.className = "card";
    card.id = citeId;
    column.appendChild(card);

    let content1 = document.createElement("div");
    content1.className = "content";
    card.appendChild(content1);
    let header = document.createElement("div");
    header.className = "header";
    content1.appendChild(header);
    let span = document.createElement("span");
    span.innerText = citeRefText;
    header.appendChild(span);



    let content2 = document.createElement("div");
    content2.className = "content";
    card.appendChild(content2);


    let citeLink = document.createElement("a");
    citeLink.href = citeURL;
    if (citeRefText.length > CITATION_TITLE_LENGTH) {
        citeRefText = citeRefText.substr(0, CITATION_TITLE_LENGTH); // Shortens string to 50 chars
        citeRefText = citeRefText.concat('...');
    }
    citeLink.innerText = citeRefText;
    card.appendChild(citeLink);

    let genP = document.createElement("p");
    if (srcList !== null) {
        genP.innerText = "Generating Sources (" + srcList.length + "):";
    } else {
        genP.innerText = "No Generating Sources Found";
    }

    card.appendChild(genP);

    let genList = document.createElement("ol");
    card.appendChild(genList);

    if (srcList !== null) {
        for (let i = 0; i < srcList.length; i++) {
            if (i > 5) {
                break;
            }
            let li = document.createElement("li");
            let a = document.createElement("a");
            a.innerText = srcList[i].title;
            a.href = srcList[i].url;
            // console.log(srcList[i].title + " and " + srcList[i].url); // TODO: Delete Later
            li.appendChild(a);
            genList.appendChild(li);
        }
    }

    let circularReport = document.createElement("p");
    circularReport.innerText = "Circular Reporting: " + hasCycles;
    card.appendChild(circularReport);
}