<link rel="stylesheet" href="/css/timdbmain.css">

<#assign content>
  <h1 class="header">${aname}</h1>
  <p>Appeared in movies: </p>
  <ul>${movies}</ul>

</#assign>
<#include "timdb-result.ftl">