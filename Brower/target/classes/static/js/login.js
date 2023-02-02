window.onload = function() {

    var user=checkCookie();
    if(user!=null){
        $.ajax({
            type: "POST",
            url:"http://localhost:8088/loginconfirm",
            contentType: "application/json",
            dataType: "json",
            async:false,
            data:JSON.stringify(user),
            success: function(res){
                var result=res["result"];
                if(result==null ||result.length===0){
                    alert("login in success!!!");
                    window.location.replace("http://localhost:8088/home_login_success")
                }else{
                    alert(result);
                }
            }
        })
    }

    var confirm = document.getElementById("submit");

    confirm.onclick = function() {



        var userEmail = document.getElementById("userEmail").value;
        var userPassword = document.getElementById("userPassword").value;
        user ={
                "userEmail":userEmail,
                "userPassword":userPassword,
        };

        $.ajax({
            type: "POST",
            url:"http://localhost:8088/loginconfirm",
            contentType: "application/json",
            dataType: "json",
            async:false,
            data:JSON.stringify(user),
            success: function(res){
                var result=res["result"];
                if(result==null ||result.length===0){
                    alert("login in success!!!");
                    setCookie(userEmail,userPassword,1);
                    window.location.replace("http://localhost:8088/home_login_success")
                }else{
                    alert(result);
                }
            }
        })
    }
    function checkCookie(){
        var userEmail =getCookie("userEmail");
        var userPassword=getCookie("userPassword");
        if(userEmail!="" && userPassword!=""){
            user ={
                "userEmail":userEmail,
                "userPassword":userPassword,
            };
            return user;
        }
        return null;
    }

    function setCookie(userEmail,userPassword, day){
        var d=new Date();
        d.setTime(new Date().getTime()+(day*24*60*60*1000));
        document.cookie="loginmessage="+"userEmail="+userEmail+",userPassword="+userPassword+";expires="+d.toLocaleTimeString();
    }

    function getCookie(name){
        var temp=document.cookie.replace("loginmessage=","")
        var info = temp.split(',');
        for(var i=0;i<info.length;i++){
            var c=info[i].trim();
            if(c.indexOf(name)==0){
                return c.substring(name.length+1,c.length);
            }
        }
        return "";
    }

}