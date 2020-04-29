<#assign head>
    <link rel="stylesheet" href="css/socket-demo.css">
</#assign>

<#assign body>

<h1>Sockets Demo</h1>
<div id="content">
    <input id="socket-form" name="socket-form" class="u-full-width"
           type="text"
           placeholder="What you say here should appear below when you press the button:">
<#--        <form method="GET" id="socket-form" action="/socket-demo">-->
<#--        <label for="urlbox" class="input-label">What you say here should appear below when you press the button:</label><br>-->
<#--        <textarea name="urlbox" id="urlbox"></textarea><br>-->
<#--    </form>-->
    <button id="submit">Submit</button>
    <p class="hover-demo">Hover on me!</p>
    <p id="result"></p>
</div>

</#assign>
<#include "main.ftl">


<#--<script>-->
<#--    $(document).ready(function() {-->
<#--        setup_click();-->
<#--        setup_socket();-->
<#--    });-->
<#--</script>-->
