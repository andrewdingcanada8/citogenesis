function annotate() {
    let url = $('#pageURL').val();

    // check if input url matches wikipedia format

    if (!/https:\/\/en\.wikipedia\.org\/wiki\/([A-Z][\w%]*)/g.test(url)) {
        alert("Invalid Wikipedia URL");
        return;
    } else if (url.includes('#')) {
        alert("Invalid Wikipedia URL");
        return;
    }



    url = url.substr(url.lastIndexOf("/") - 4, url.length);
    if (url !== "") {
        // TODO: Modify this when using deployed
        window.location.href = "http://localhost:4567/" + url;
    }
    // TODO: add some string cutting here so only the important wikipedia page info is shown
    // It's actually only the last word that is the article title. everything after the last /

}