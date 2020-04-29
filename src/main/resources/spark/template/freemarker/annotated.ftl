<#assign body>
    <div class="sidenav">


    </div>
    <div class="content">
        <form method="GET" action="/search" class="search">
            <label> URL
                <input type="text" name="url">
            </label>
            <input type="submit">
        </form>
        ${content}
    </div>
</#assign>
<#include "main.ftl">