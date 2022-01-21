function search(){
    var inputtext = $("#inputtext").val();
    alert(inputtext);
    $.ajax({
        url:"http://swopenapi.seoul.go.kr/api/subway/6f58714f6c64686534356359587644/json/realtimeStationArrival/0/8/%ED%95%9C%EB%8C%80%EC%95%9E",
        success: function(result){
             console.log(result);
        }
    })


}