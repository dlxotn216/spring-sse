<!doctype html>
<html lang="en">
<head>
    <title>Temperatures</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
</head>
<body>

<!-- JavaScript -->
<script>
    var es = new EventSource('/simple-event-streams');

    es.onopen = e => console.log('Event source opened');

    es.onmessage = function (event) {
        // 이벤트 설정이안된 기본 데이터 처리
        console.log(event);
    };
    es.addEventListener('periodic-event', function(event) {
        // 'periodic-event' 이벤트의 데이터 처리
        console.log(event);
    }, false);
</script>
</body>
</html>