window.onload =function(){
    var search_btn = document.getElementById("search-button");
    search_btn.onclick=getsearch;
    function getsearch(){
        var search_input = document.getElementById("search-input").value;
        $.ajax({
            type: "GET",
            url:"http://localhost:8088/search/q/{"+search_input+"}",
            success:function(res){
                document.write(res);
                document.close();
            }
        })
    }
}