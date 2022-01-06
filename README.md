# Citogenesis

*From our initial project outline*

Circular reporting, informally known as citogenesis, occurs when a work cites multiple sources that ultimately stem from a single source. While often used as a technique to justify and spread fake news and propaganda, this phenomenon also occurs naturally due to a lack of quality-control of sources on the internet. In particular, Wikipedia has exhibited a cyclical sort of circular reporting that occurs in two stages: 1. A user (regardless of intent) writes a false statement on Wikipedia 2. Another external source reads Wikipedia entry and writes an article without citing Wikipedia 3. Moderator on Wikipedia attaches source produced in step (2) to false statement produced in step (1)

Misinformation introduced this way has spread to major news outlets, popular television channels, and even academic publications. We intend to develop a tool that will (1) combat citogenesis on Wikipedia and (2) evaluate the overall reliability of a citation.

Our tool will be a proxy Wikipedia webpage that will annotate each cited statement with a rating of the strength of its sources. To assess the viability of a statement, we intend to run a search on its citations to understand the total number unique supporting sources linked to the statement (including the sources of cited citations as well). This will amount to a graph-search on web pages, where each node is represented by a source page, and each edge established by a source citation. We also intend to leverage the Wikipedia API to access the editing history of the user’s desired Wikipedia article. By ensuring a sufficient number of cited sources are published before the editing/insertion date of the statement on Wikipedia, we can ensure report any instances of the cyclical circular reporting discussed above.

Main challenges: There may be a chance that citations may not provide specific-enough information to uniquely identify the document if there is no hyperlink attached. This may be solved by using Google’s Search Engine API to make our best guess at the citation’s referenced source.

Working with citations only may cut off available relevant sources to a particular statement. Using a common text-matching search engine (again, Google’s API) may be helpful, but it will be difficult to determine the reliability of a source that is delivered by a search algorithm.

### **Critical Features for MF**

**Proxy webpage:** Since we do not intend to modify nor access Wikipedia’s codebase, we must find an alternate way to annotate Wikipedia’s webpages. While developing a browser plugin is a possibility, we would like this project to be platform-independent to be as accessible as possible to all users. Thus, we decided implementing this project as a proxy-webpage that can annotate Wikipedia pages would be best.

Challenges:

- learning Wikipedia’s page API
- figuring out how to change webpage HTML to support statement annotations

These problems can be both solved by dedicating the time to learn Wikipedia’s interface.

**Filter out false statements:** Users have indicated that they would like to read a version of a Wikipedia page that is totally factually correct. Thus, this feature involves setting an “original source threshold” to filter out poorly-supported statements.

Challenges:

- Filtering out statements without compromising typical Wikipedia page format

We are not bound to Wikipedia’s page layout to present the filtered information. Thus, designing a flexible page format that can is readable regardless of the number of statements may be a solution to this problem.

**Highlight statements based-on how well they are supported by their sources:** Users indicate that they would not like to mouse over every statement to check the validity of their sources. Highlighting is a quick way of giving visual feedback without any cumbersome mouse movements and/or clicks.

Challenges:

- The algorithm must be fast enough to annotate each statement by the time the user has to read it. Since many users skim Wikipedia articles quickly, this must be within the first few seconds of page load.

This is heavily dependent on the speed of the internet connection of the server, as well as efficiency of the algorithm implementation. Writing in a timeout for long-pending HTTP requests may be necessary to achieve the response times needed to make this annotation usable.

### Contributing Members

Andrew Ding – @andrewdingcanada8

Charles Shi - @winterhy

Seiji Shaw – @sageshoyu

Jason Gong – @jgong15
