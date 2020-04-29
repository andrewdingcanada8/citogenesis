<link rel="stylesheet" href="/css/timdbmain.css">

<#assign content>
    <h1 class="header">tIMDb</h1>
    <form method="POST" action="/timdb/load">
        <label for="database-location">Path to database: </label>
        <input name="database-location" id="database-location" required>
        <input type="submit">
    </form>
    <br/>
    ${querycontent}
    <p id="output">${output}</p>
</#assign>
<#include "../timdb-main.ftl">