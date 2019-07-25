<#-- @ftlvariable name="receiver" type="java.lang.String" -->
<!doctype html>
<html lang="en">
<head>
    <title>Temperatures</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
</head>
<body>
<input type="hidden" id="receiver" value="${receiver}"/>

<p><span id="alertCount">0</span> alerts arrived</p>
<ul id="alerts">

</ul>
<!-- JavaScript -->
<script>
    <#noparse>
    const receiver = document.getElementById('receiver').value;
    let es = new EventSource(`/redis/alerts/${receiver}/subscribe`);
    const alerts = [];
    const $alertCount = document.getElementById('alertCount');
    const $alerts = document.getElementById('alerts');

    es.onopen = e => console.log('Event source opened');

    es.onmessage = function (event) {
        console.log('Event source on Message ', event);
    };


    es.addEventListener(`redis-alert-${receiver}`, function (event) {
        const data = JSON.parse(event.data);
        alerts.push(data);
        console.log(event);

        $alertCount.innerText = alerts.length;
        const $li = document.createElement('li');
        $li.appendChild(document.createTextNode(`${data.sender} send message [${data.message}]`));
        $alerts.appendChild($li)
    }, false);

    /**
     * EventSource.CONNECTING = 0
     * EventSource.OPEN = 1
     * EventSource.CLOSED = 2
     */
    es.addEventListener('error', function (e) {
        if (es.readyState === 2) {
            es.close();
            console.log(`Event source was closed : ${e}, ${es}}`);
            es = new EventSource(`/alerts/${receiver}/subscribe`);

            console.log(`And trying to reconnect by creating new EventSource ${es}`);
        } else {
            console.log(`Event source error event listener : ${e}, ${es}`);
        }
    }, false);

    window.onbeforeunload = function(e){
        es.close();
        console.log('check close call');
        return null;
    };
    </#noparse>
</script>
</body>
</html>