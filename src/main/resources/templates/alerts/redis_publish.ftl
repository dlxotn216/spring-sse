<#-- @ftlvariable name='receiver' type='java.lang.String' -->
<!doctype html>
<html lang='en'>
<head>
    <title>Publish message to User</title>
    <meta charset='utf-8'>
    <meta name='viewport' content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no'>
</head>
<body>
<p><label for='sender'>From</label></p>
<input type='text' id='sender'/>

<p><label for='receiver'>Send to</label></p>
<input type='text' id='receiver'/>

<p><label for='message'>Message</label></p>
<textarea id='message'></textarea>

<button id='send'>Send</button>

<p><span id='subscriberCount'>0</span> Subscriber get Message</p>

<!-- JavaScript -->
<script>
    <#noparse>
    const $sender = document.getElementById('sender');
    const $receiver = document.getElementById('receiver');
    const $subscriberCount = document.getElementById('subscriberCount');
    const $message = document.getElementById('message');

    document.getElementById('send').addEventListener('click', function (e) {
        const sender = $sender.value;
        const receiver = $receiver.value;
        const message = $message.value;
        if(!message){
            return;
        }

        fetch(`/redis/alerts`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json; charset=utf-8'},
            body: JSON.stringify({
                sender,
                receiver,
                message,
            })
        }).then(res => res.json()) // parse response as JSON (can be res.text() for plain response)
                .then(response => {
                    $message.value = '';
                    $subscriberCount.innerText = response['subscriberCount'];
                    $subscriberCount.focus();
                })
                .catch(err => {
                    console.error('error occure', err);
                    alert('sorry, there are no results for your search')
                });
    });
    </#noparse>
</script>
</body>
</html>