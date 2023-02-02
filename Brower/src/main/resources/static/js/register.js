// function getUserName() {
//     // ${@registerMethod.getRegisterCode(\'getUserName()\')}
//     request.setQuery();
//     return
//     // var a="register(userEmail="+document.getElementById("userEmail").value+")";
//     // let b=window.location.href.toString();
//     // b=b.replace("register",a);
//     // window.location.replace(b);
// };

window.onload =function(){

    //获取验证码
    var code = document.getElementById("getcode");
    code.onclick=getcode;
    function getcode(){
        var userEmail = document.getElementById("userEmail").value;
        $.ajax({
            type: "POST",
            url:"http://localhost:8088/getcode",
            data:{
                "userEmail":userEmail,
            },
            success:function(res){
                document.write(res);
                document.close();
            }
        })
    }

    //检查注册信息
    var btn_sub = document.getElementById("submit");
    btn_sub.onclick=submit;

    function submit(){
        var userName = document.getElementById("userName").value;
        var userEmail = document.getElementById("userEmail").value;
        var userPassword = document.getElementById("userPassword").value;
        var userPasswordAgain = document.getElementById("userPasswordAgain").value;
        var code = document.getElementById("code").value;
        var user={
            "userName":userName,
            "userEmail":userEmail,
            "userPassword":userPassword,
            "userPasswordAgain":userPasswordAgain,
            "code":code,
        };
        $.ajax({
            type: "POST",
            url:"http://localhost:8088/registerConfirm",
            async:false,
            dataType: "json",
            contentType: "application/json",
            data:  JSON.stringify(user),
            success: function(res){
                var warn=res["res"];
                if( warn==null ||warn.length===0 ){
                    alert("register success!!!");
                    window.location.replace("http://localhost:8088/login");

                }else{
                    alert(warn);
                }
            }
        })
    }
    //重置所有信息
    var btn_res = document.getElementById("cancel");
    btn_res.onclick=reset;

    function reset(){
        var userName = document.getElementById("userName");
        var userEmail = document.getElementById("userEmail");
        var userPassword = document.getElementById("userPassword");
        var userPasswordAgain = document.getElementById("userPasswordAgain");
        var code = document.getElementById("code");

        userName.innerText="";
        userEmail.innerText="";
        userPassword.innerText="";
        userPasswordAgain.innerText="";
        code.innerText="";
    }


}

