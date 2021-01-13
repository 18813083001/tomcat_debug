<div id="header">
    <h2>FreeMarker Spring MVC Hello World</h2>
</div>
<script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js">
</script>
<script src="../js/test.js">
</script>
<div id="content">
    <fieldset>
        <legend>Add Car</legend>
        <form name="car" action="http://10.250.102.42:9002/iam/v1/login" method="post">
            邮箱 : <input type="text" name="email" /><br/>
            密码: <input type="text" name="password" /><br/>
            clientId: <input type="text" name="clientId" /><br/>
            <input type="submit" value="登录" />
        </form>
    </fieldset>
    <br/>
    <table class="datatable">
        <tr>
            <th>Make</th>
            <th>Model</th>
        </tr>
        <#list model["carList"] as car>
            <tr>
                <td>${car.make}</td>
                <td>${car.model}</td>
            </tr>
        </#list>
    </table>
    <button id="captcha">获取验证码</button>
    <button id="captcha1" οnclick="sendJson()">请求json格式数据</button>
    <script type="text/javascript">
        $(document).ready(function(){
            $("#captcha").click(function(){

                // $.get("http://172.17.10.13:9359/cityBasicApi/getCityList",function(data,status){
                //     alert("数据: " + data + "\n状态: " + status);
                // });
                $.ajax({
                    type: 'GET',
                    url: 'http://gateway10.51gofunev.com/api/juhe//cityBasicApi/getCityList',
                    headers: {
                        "X-API-TOKEN":"024b68d7b0a07b8a16db13dc601c648b"
                    }
                    //OR
                    //beforeSend: function(xhr) {
                    //  xhr.setRequestHeader("My-First-Header", "first value");
                    //  xhr.setRequestHeader("My-Second-Header", "second value");
                    //}
                }).done(function(data) {
                    alert(data);
                });
            });
        });
        // $(document).ready(function(){
        //     $("#captcha1").click(function(){
        //         $.get("http://chenlinsong.com:9002/iam/v1/image/captcha/get",function(data,status){
        //             alert("数据: " + data + "\n状态: " + status);
        //         });
        //     });
        // });

        $(document).ready(function(){
            $("#captcha1").click(function(){
                $.ajax({
                    url : "http://10.250.102.42:9002/iam/v1/image/captcha/get",
                    type : 'PUT',
                    data : "{\"a\":\"c\"}",
                    dataType : 'json',
                    contentType : 'application/json',
                    headers:{token:'a52cb1d0-30b4-4b35-abc4-534915342506'},
                    success : function(data, status, xhr) {
                        alert("数据: " + data + "\n状态: " + status);
                    },
                    Error : function(xhr, error, exception) {
                        // handle the error.
                        alert(exception.toString());
                    }
                });
            });


        });

        // function sendJson(){
        //     $.ajax({
        //         url : "http://chenlinsong.com:9002/iam/v1/image/captcha/get",
        //         type : 'POST',
        //         data : "{\"a\":\"c\"}",
        //         dataType : 'json',
        //         //contentType : 'application/json',
        //         success : function(data, status, xhr) {
        //             alert("数据: " + data + "\n状态: " + status);
        //         },
        //         Error : function(xhr, error, exception) {
        //             // handle the error.
        //             alert(exception.toString());
        //         }
        //     });
        // }


    </script>
</div>