<link rel="stylesheet" href="css/stars.css">

<#assign content>

    <h1>Stars</h1>
    <div id="content">
        <form method="POST" id="stars-form" action="/command">
            <div id="command-selector">
                <input type="radio" id="neighbors" name="command type" value="neighbors">
                <label for="neighbors" id="neighbors-label">
                    <div class="search-type">Neighbors</div>
                </label>
                <input type="radio" id="radius" name="command type" value="radius">
                <label for="radius" id="radius-label">
                    <div class="search-type">Radius</div>
                </label></div><br>
            <label for="number" class="input-label">Enter the number of neighbors to find or the radius to search within:</label><br>
            <textarea name="number" id="number"></textarea><br>
            <label for="text" class="input-label">Enter the coordinates of a point or the name of a star to search around:</label><br>
            <textarea name="text" id="text"></textarea><br>
            <button type="submit" id="submit">Submit</button>
        </form>
    ${result}
    ${errors}

    <p id="timdb"><a href="/timdb" id="timdb-link">tIMDb →</a></p>
    </div>

</#assign>
<#include "timdb-main.ftl">