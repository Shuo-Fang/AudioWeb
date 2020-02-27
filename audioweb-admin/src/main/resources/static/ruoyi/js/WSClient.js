(function(window) {
    Blob.prototype.appendAtFirst = function(blob) {
        return new Blob([blob, this]);
    };
    var WS_Open = 1,
        WS_Close = 2,
        WS_MsgToAll = 3,
        WS_MsgToPoints = 4,
        WS_RequireLogin = 5,
        WS_setName = 6,
        types = ["文本", "语音"],
        getWebSocket = function(host) {
            var socket;
            if ('WebSocket' in window) {
                socket = new WebSocket(host);
            } else if ('MozWebSocket' in window) {
                socket = new MozWebSocket(host);
            }
            return socket;
        },
        WSClient = function(option) {
            var isReady = false,
                init = function(client, option) {
                    client.socket = null;
                    client.online = false;
                    client.isUserClose = false;
                    client.option = option || {};
                };

            this.connect = function(host) {
                var client = this,
                    socket = getWebSocket(host);

                if (socket == null) {
                    console.log('错误: 当前浏览器不支持WebSocket，请更换其他浏览器', true);
                    alert('错误: 当前浏览器不支持WebSocket，请更换其他浏览器');
                    return;
                }

                socket.onopen = function() {
                    var onopen = client.option.onopen,
                        type = types[client.option.type];
                    console.log('WebSocket已连接.');
                    console.log("%c类型：" + type, "color:rgb(228, 186, 20)");
                    onopen && onopen();
                };

                socket.onclose = function() {
                    var onclose = client.option.onclose,
                        type = types[client.option.type];
                    client.online = false;
                    console.error('WebSocket已断开.');
                    console.error("%c类型：" + type, "color:rgb(228, 186, 20)");
                    onclose && onclose();
                    if (!client.isUserClose) {
                        client.initialize();
                    }
                };

                socket.onmessage = function(message) {
                    var option = client.option;
                    if (typeof(message.data) == "string") {
                        var msg = JSON.parse(message.data);
                        switch (msg.type) {
                        case WS_Open:
                            option.wsonopen && option.wsonopen(msg);
                            break;
                        case WS_Close:
                            option.wsonclose && option.wsonclose(msg);
                            break;
                        case WS_MsgToAll:
                        case WS_MsgToPoints:
                            option.wsonmessage && option.wsonmessage(msg);
                            break;
                        case WS_RequireLogin:
                            option.wsrequirelogin && option.wsrequirelogin();
                            break;
                        case WS_setName:
                            option.userName = msg.host;
                            option.wssetname && option.wssetname(msg);
                            break;
                        }
                    } else if (message.data instanceof Blob) {
                        option.wsonblob && option.wsonblob(message);
                    }

                };

                isReady = true;
                this.socket = socket;
                return this;
            };

            this.initialize = function(param) {
                return this.connect(this.option.host + (param ? "?" + param : ""));
            };

            this.sendString = function(message) {// 向服务端发送给字符串
                return isReady && this.socket.send(message);
            };

            this.sendBlob = function(blob) {// 向服务端发送二进制数据
                return isReady && this.socket.send(blob.appendAtFirst(this.option.userName));
            };

            this.close = function() {
                this.isReady = false;
                this.online = false;
                this.isUserClose = true;
                this.socket.close();
                return true;
            };

            this.isMe = function(name) {
                return this.option.userName == name;
            }

            init(this, option);
        };

    window.WSClient = WSClient;

})(window);