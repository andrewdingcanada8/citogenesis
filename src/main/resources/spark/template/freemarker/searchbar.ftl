<#assign head>
    <script>
        function myFunction(){
            document.getElementById("searchText").placeholder = "Button A";
        }
        function myFunctionTwo(){
            document.getElementById("searchText").placeholder = "Button B";
        }
    </script>
    <link rel="stylesheet" href="css/searchbar.css">
</#assign>

<div class="wrap">
    <div class="search">
        <input type="text" placeholder="Wikipedia URL" id="searchText">
        <button type="submit" class="searchButton">
            <i class="fa fa-search"></i>
        </button>
    </div>
    <button onclick="myFunction()">a</button>
    <button onclick="myFunctionTwo()">b</button>
</div>
