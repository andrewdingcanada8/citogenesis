<link rel="stylesheet" href="/css/timdbmain.css">

<#assign querycontent>

    <form method="POST" action="/timdb/connect">
        <label for="start-actor">Start actor's name: </label>
        <input name="start-actor" id="start-actor" required>

        <label for="end-actor">End actor's name:</label>
        <input name="end-actor" id="end-actor" required>
        <input type="submit">
    </form>

</#assign>
<#include "timdb-query.ftl">