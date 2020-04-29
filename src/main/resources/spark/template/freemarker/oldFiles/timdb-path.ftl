<link rel="stylesheet" href="/css/timdbmain.css">

<#assign content>
    <h1 class="header">${fromName} to ${toName}</h1>
    <p>Path: </p>
    <ul>${path}</ul>

</#assign>
<#include "timdb-result.ftl">