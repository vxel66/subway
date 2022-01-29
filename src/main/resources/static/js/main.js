
function search(){
    var inputtext = $("#inputtext").val();
    if(inputtext == ""){
        alert("값이 올바르지 않습니다.");
        return false;
    }
    location.href = "/searchcontroller/"+inputtext;
}

function setClock(){
    var dateinfo = new Date();
    var hour = modifyNumber(dateinfo.getHours());
    var min = modifyNumber(dateinfo.getMinutes());
    var sec = modifyNumber(dateinfo.getSeconds());
    var year = dateinfo.getFullYear();
    var month = dateinfo.getMonth()+1;
    var date = dateinfo.getDate();

    document.getElementById("time").innerHTML = "🕓"+ hour + ":" + min +":"+ sec;
    document.getElementById("date").innerHTML = year + "년" + month +"월"+ date+"일";
}

function modifyNumber(time){
    if(parseInt(time)<10){
        return "0"+time;
     }else{
        return time;
     }
}

window.onload = function(){
    setClock();
    //

    setInterval(setClock,1000);
}
