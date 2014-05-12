var messages = [
                {
                    "datetime": "1399892528",
                    "sender": "Zeising.Tobias",
                    "email": "tobias.zeising@aditu.de",
                    "receiver" : "receiver",
                    "text": "ubergren, no sea takimata ;) sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."
                },
                {
                    "datetime": "1399892286",
                    "sender": "Zeising.Tobias",
                    "email": "tobias.zeising@aditu.de",
                    "receiver" : "er9wer",
                    "text": "ipsum dolor sit amet. :pirat: :clown: Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."
                },
                {
                    "datetime": "1399892046",
                    "sender": "Zeising.Tobias",
                    "email": "tobias.zeising@aditu.de",
                    "receiver" : "er9wer",
                    "text": "ipsum dolor sit amet. :pirat: :clown: Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."
                },
                {
                    "datetime": "1399632845",
                    "sender": "Zeising.Tobias",
                    "email": "tobias.zeising@aditu.de",
                    "receiver" : "er9wer",
                    "text": "ipsum dolor sit amet. :pirat: :clown: Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."
                },
                {
                    "datetime": "1399632845",
                    "sender": "Zeising.Tobias",
                    "email": "tobias.zeising@aditu.de",
                    "receiver" : "er9wer",
                    "text": "ipsum dolor sit amet. :pirat: :clown: Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."
                },
                {
                    "datetime": "1399632845",
                    "sender": "Zeising.Tobias",
                    "email": "tobias.zeising@aditu.de",
                    "receiver" : "er9wer",
                    "text": "ipsum dolor sit amet. :pirat: :clown: Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."
                },
                {
                    "datetime": "1399632845",
                    "sender": "Zeising.Tobias",
                    "email": "tobias.zeising@aditu.de",
                    "receiver" : "er9wer",
                    "text": "ipsum dolor sit amet. :pirat: :clown: Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."
                },
                {
                    "datetime": "1399632845",
                    "sender": "Zeising.Tobias",
                    "email": "tobias.zeising@aditu.de",
                    "receiver" : "er9wer",
                    "text": "ipsum dolor sit amet. :pirat: :clown: Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."
                }
            ];


sim.backend = {

    // register callbacks

    /**
     * register callback for a new online user
     */
    onUserOnlineNotice: function(callback) {
        sim.backend.userOnlineNoticeCallback = callback;
    },
    
    /**
     * register callback for a user goes offline
     */
    onUserOfflineNotice: function(callback) {
        sim.backend.userOfflineNoticeCallback = callback;
    },
    
    /**
     * register callback for user changes his state
     */
    onUserUpdateNotice: function(callback) {
        sim.backend.userUpdateNotice = callback;
    },
    
    /**
     * register callback for incoming new message
     */
    onNewMessage: function(callback) {
        sim.backend.newMessage = callback;
    },
    
    /**
     * register callback for incoming file
     */
    onFileSendRequest: function(callback) {
        sim.backend.fileSendRequest = callback;
    },
    
    /**
     * register callback for new file progress state
     */
    onFileSendProgress: function(callback) {
        sim.backend.fileSendProgress = callback;
    },
    
    /**
     * register callback for user enters room
     */
    onRoomEnterNotice: function(callback) {
        sim.backend.roomEnterNotice = callback;
    },
    
    /**
     * register callback for user leaves room
     */
    onRoomLeaveNotice: function(callback) {
        sim.backend.roomLeaveNotice = callback;
    },
    
    /**
     * register callback for new room was opened
     */
    onRoomOpened: function(callback) {
        sim.backend.roomOpened = callback;
    },
    
    /**
     * register callback for room list update
     */
    onGetRoomlistResponse: function(callback) {
        sim.backend.getRoomlistResponse = callback;
    },
    
    /**
     * register callback for user list update
     */
    onGetUserlistResponse: function(callback) {
        sim.backend.getUserlistResponse = callback;
    },
    
    /**
     * register callback for getting converstion
     */
    onGetContentResponse: function(callback) {
        sim.backend.getContentResponse = callback;
    },
    
    /**
     * register callback for error message
     */
    onError: function(callback) {
        sim.backend.error = callback;
    },
    
    
    
    // functions for frontend
    
    
    /**
     * prompt backend for new userlist. onGetUserlistRespons will be executed.
     */
    updateUserlist: function() {
        if(typeof sim.backend.getUserlistResponse != "undefined") {
            sim.backend.getUserlistResponse([
                {username: "Zeising.Tobias", rooms: ['Webteam', 'swm', 'Blub'], state: 'AVAILABLE', email: 'tobias.zeising@aditu.de'},
                {username: "Fuchs.Florian", rooms: ['Webteam'], state: 'NOT_AVAILABLE', email: 'fuchsi@bla.de'},
                {username: "Koslovski.Erich", rooms: [], state: 'AVAILABLE', email: 'erich@bla.de'}
            ]);
        }
    },
    
    
    /**
     * prompt backend for new roomlist. onGetRoomlistResponse will be executed.
     */
    updateRoomlist: function() {
        if(typeof sim.backend.getRoomlistResponse != "undefined") {
            sim.backend.getRoomlistResponse(['Webteam', 'swm', 'Blub']);
        }
    },
    
    
    /**
     * prompt backend for conversations. id is username or roomname. onGetContentResponse will be executed.
     */
    getConversation: function(id) {
        if(typeof sim.backend.getContentResponse != "undefined") {
            sim.backend.getContentResponse(id, messages);
        }
    },

    
    /**
     * receiving file will be rejected
     */
    fileSendReject: function(uuid) {
    
    },
    
    
    /**
     * receiving file will be accepted
     */
    fileSendAccept: function(uuid, port) {
    
    },
    
    
    /**
     * send new message. receiver is username or roomname.
     */
    sendMessage: function(receiver, text) {
        var now = new Date().getTime() / 1000;
        messages[messages.length] = {
            "datetime": now,
            "sender": "Zeising.Tobias",
            "email": "tobias.zeising@aditu.de",
            "receiver" : receiver,
            "text": text
        };
        sim.backend.getContentResponse(receiver, messages);
    },
    
    
    /**
     * update own status.
     * status = AVAILABLE oder BUSY oder NOT_AVAILABLE
     */
    updateStatus: function(status) {
    
    },
    
    
    /**
     * open new room
     */
    openRoom: function(name) {
    
    },
    
    
    /**
     * join room
     */
    joinRoom: function(name) {
    
    },
    
    
    /**
     * leave room
     */
    leaveRoom: function(name) {
    
    }

};






