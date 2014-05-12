var sim = {

    /**
     * counts unread messages for every room/person
     */
    unreadMessagesCounter: {},
    
    /**
     * id of current visible conversation
     */
    currentConversation: false,
    
    /**
     * initialize application
     */
    init: function() {
        jQuery(document).ready(function() {          
            // load emoticons
            sim.initEmoticons();
            
            // initialize all events
            sim.initEvents();
            
            // initialize window resize handler
            $(window).bind("resize", sim.resize);
            sim.resize();
            
            // initialize div inline scroller
            $("#contacts-wrapper, #rooms-wrapper, #content-wrapper").mCustomScrollbar({
                advanced:{
                    updateOnContentResize: true,
                }
            });
            
            // initialize backend callbacks
            sim.initBackendCallbacks();
            
            // Userliste und Rooms updaten
            sim.backend.updateUserlist();
            sim.backend.updateRoomlist();
        });
    },
    
    
    /**
     * set callbacks which will update the frontend on backend action (e.g. receiving a new message)
     */
    initBackendCallbacks: function() {
        // alertify about new user state
        var userOnlineOffline = function(text) {
            alertify.log(text);
            sim.backend.updateUserlist();
            
            // update messagelist for updating online/offline state
            sim.backend.getConversation(sim.currentConversation);
        }
        
        // user is now online
        sim.backend.onUserOnlineNotice(userOnlineOffline);
        
        // register callback for a user goes offline
        sim.backend.onUserOfflineNotice(userOnlineOffline);
        
        // register callback for user changes his state
        sim.backend.onUserUpdateNotice(function(id, text) {
            alertify.log("Status: " + id + " " + text);
            sim.backend.updateUserlist();
        });
        
        // register callback for incoming new message
        sim.backend.onNewMessage(function(message) {
            if(sim.currentConversation == message.receiver)
                sim.backend.getConversation(sim.currentConversation);
            else {
                if (typeof sim.unreadMessagesCounter[message.receiver] == "undefined") {
                    sim.unreadMessagesCounter[message.receiver] = 0;
                }
                sim.unreadMessagesCounter[message.receiver]++;
                sim.backend.updateUserlist();
                sim.backend.updateRoomlist();
            }
        });
        
        // register callback for incoming file
        sim.backend.onFileSendRequest(function() {
            // ToDo
        });
        
        // register callback for new file progress state
        sim.backend.onFileSendProgress(function() {
            // ToDo
        });
        
        // register callback for user enters room
        sim.backend.onRoomEnterNotice(function(room, user) {
            alertify.log("Benutzer: " + user + " hat den Raum " + room + " betreten");
            sim.backend.updateUserlist();
        });
        
        // register callback for user leaves room
        sim.backend.onRoomLeaveNotice(function() {
            alertify.log("Benutzer: " + user + " hat den Raum " + room + " verlassen");
            sim.backend.updateUserlist();
        });
        
        // register callback for new room was opened
        sim.backend.onRoomOpened(function(room) {
            alertify.log("Neuer Raum " + room + " wurde eröffnet");
            sim.backend.updateRoomlist();
        });
        
        // register callback for room list update
        sim.backend.onGetRoomlistResponse(function(rooms) {
            sim.updateRoomlist(rooms);
        });
        
        // register callback for user list update
        sim.backend.onGetUserlistResponse(function(users) {
            sim.updateUserlist(users);
        });
        
        // register callback for getting conversation
        sim.backend.onGetContentResponse(function(id, messages) {
            if (id==sim.currentConversation)
                sim.updateConversation(messages);
        });
    },
    
    
    /**
     * load all available emoticons
     */
    initEmoticons: function() {
        var emotbox = $('#message-emoticons');
        var lastEmot = "";
        $.each(emoticons, function(shortcut, emoticon) {
            if(lastEmot != emoticon)
                emotbox.append('<img src="'+ emoticon +'" title="' + shortcut + '"/>');
            lastEmot = emoticon;
        });
    },
    
    
    /**
     * initialize events (clicks, ...)
     */
    initEvents: function() {
        // toggle emoticons
        $('#message-toggleemots').click(function() {
            var emoticonsPopup = $('#message-emoticons');
            $(this).toggleClass('active');
            if($(this).hasClass('active')) {
                emoticonsPopup.css('marginTop', -1 * emoticonsPopup.height() - parseInt(emoticonsPopup.css('padding')));
                emoticonsPopup.show();
            } else  {
                emoticonsPopup.hide();
            }
        });
        
        // emoticons click
        $('#message-emoticons').delegate("img", "click", function() {
            $('#message-input-textfield').val(
                $('#message-input-textfield').val() + " " + $(this).attr('title')
            );
            $('#message-toggleemots').click();
        });
        
        // switch to user
        $('.contacts').delegate("li", "click", function() {
            var user = $(this).find('.contacts-name').html();
            $('.rooms li, .contacts li').removeClass('active');
            $(this).addClass('active');
            sim.currentConversation = user;
            sim.backend.getConversation(user);
            $('#main-metadata').css('visibility', 'visible');
        });
        
        // enter room click
        $('.rooms').delegate("li .name", "click", function() {
            var room = $(this).parent().find('.name').html();
            $('.rooms li, .contacts li').removeClass('active');
            $(this).parent().parent().addClass('active');
            sim.currentConversation = room;
            sim.backend.joinRoom(room);
            sim.backend.updateRoomlist();
            sim.backend.getConversation(sim.currentConversation);
            $('#main-metadata').css('visibility', 'visible');
        });
        
        // leave room click
        $('.rooms').delegate("li .rooms-leave", "click", function(e) {
            var room = $(this).parent().find('.name').html();
            sim.backend.leaveRoom(room);
        });
        
        // send message
        $('#message-send').click(function() {
            var message = $('#message-input-textfield').val();
            if (message.trim().length==0) {
                alertify.error('bitte eine Nachricht eingeben');
                return;
            }
            
            if (sim.currentConversation==false) {
                alertify.error('bitte einen Chat Kanal ausw&auml;hlen');
                return;
            }
            $('#message-input-textfield').val("");
            sim.backend.sendMessage(sim.currentConversation, message);
        });
    },
    
    
    
    // ui update helpers
    
    
    /**
     * update current userlist
     */
    updateUserlist: function(users) {
        // save scroll state
        var scrollPosition = $("#contacts-wrapper").scrollTop();
        
        // update userlist
        $('.contacts').html('');
        $.each(users, function(index, user) {
            // unread
            var unread = "";
            if (typeof sim.unreadMessagesCounter[user.username] != "undefined")
                unread = '<div class="contacts-unread">' + sim.unreadMessagesCounter[user.username] + '</div>';
            
            // online state
            var state = 'online';
            if(user.state.toLowerCase()=='not_available')
                state = 'offline';
            if(user.state.toLowerCase()=='busy')
                state = 'notavailable';
            
            // avatar url
            var avatar = sim.gravatarUrl(user.email);
            
            // active
            var active = '';
            if(sim.currentConversation==user.username)
                active = 'class="active"';
            
            $('.contacts').append('<li ' + active + '>\
                <div class="' + state + ' contacts-state"></div>\
                <img src="' + avatar + '" class="contacts-avatar avatar" />\
                <div class="contacts-name">' + sim.escape(user.username) + '</div>\
                ' + unread + '\
            </li>');
        });
        
        // restore scroll state
        $("#contacts-wrapper").mCustomScrollbar("scrollTo", scrollPosition);
    },
    
    
    /**
     * update roomlist
     */
    updateRoomlist: function(rooms) {
        // save scroll state
        var scrollPosition = $("#rooms-wrapper").scrollTop();
        
        // update roomlist
        $('.rooms').html('');
        $.each(rooms, function(index, room) {
            // state
            var state = 'rooms-outside';
            var leave = '';
            if($.inArray(room, rooms.usersrooms)) {
                state = 'rooms-inside';
                leave = '<span class="rooms-leave ion-log-out"></span>';
            }
            
            // unread
            var unread = "";
            if (typeof sim.unreadMessagesCounter[room] != "undefined")
                unread = '<div class="contacts-unread">' + sim.unreadMessagesCounter[room] + '</div>';

            // active
            var active = '';
            if(sim.currentConversation==room)
                active = 'class="active"';
                
            $('.rooms').append('<li ' + active + '>\
                <div class="' + state + '"></div> \
                <div class="rooms-name"><span class="name">' + sim.escape(room) + '</span> ' + leave + ' </div>\
                ' + unread + '\
            </li>');
        });
        
        // restore scroll state
        $("#rooms-wrapper").mCustomScrollbar("scrollTo", scrollPosition);
    },
    
    
    /**
     * update current conversation
     */
    updateConversation: function(messages) {
        // set unreadcounter to 0
        var unread = sim.unreadMessagesCounter[sim.currentConversation];
        delete sim.unreadMessagesCounter[sim.currentConversation];
        sim.backend.updateUserlist();
        sim.backend.updateRoomlist();
        
        // set metadata: avatar
        var avatar = 'group.png';
        if($('.contacts .active').length > 0)
            avatar = $('.contacts .active .avatar').attr('src');
        avatar = '<img src="' + avatar + '" class="avatar" />';
        
        // set metadata: state
        var state = 'online';
        var stateElement = $('.active > div:first');
        if(stateElement.length > 0) {
            if(stateElement.hasClass('offline')) {
                state = 'offline';
            } else if(stateElement.hasClass('notavailable')) {
                state = 'notavailable';
            }
        }
        
        // write metadata
        $('#main-metadata').html(avatar + '<span>' + sim.currentConversation + '</span><span class="' + state + '"></span>');
        
        // show messages (highlite new messages)
        $('#content').html('');
        $.each(messages, function(index, message) {
            $('#content').append('<li class="entry">\
                <div class="entry-metadata">\
                    <img src="' + sim.gravatarUrl(message.email) + '" class="avatar" />\
                    <span>' + sim.escape(message.sender) + '</span>\
                    <span class="entry-datetime">' + sim.dateAgo(message.datetime) + '</span>\
                </div>\
                <div class="entry-content">\
                    ' + sim.emoticons(sim.escape(message.text)) + '\
                </div>\
            </li>');
            
            // scroll 2 bottom
            if(index==messages.length-1) {
                window.setTimeout(function() { $("#content-wrapper").mCustomScrollbar("scrollTo","bottom"); }, 500);
            }
        });
        
    },
    
        
    /**
     * set automatically the height of the tags and set scrollbar for div scrolling
     */
    resize: function() {
        var start = $('#contacts-wrapper').position().top;
        var windowHeight = $(window).height();
        var roomsHeight = $('#rooms').outerHeight();
        $('#contacts-wrapper').height(windowHeight - start - roomsHeight);
        $("#contacts-wrapper").mCustomScrollbar("update");
        $('#contacts-wrapper').show();
        
        var headerHeight = $('#main-header').outerHeight();
        var messageHeight = $('#message').outerHeight();
        var padding = parseInt($('#content').css('padding-bottom')) * 2;
        
        $('#content-wrapper').height(windowHeight - headerHeight - messageHeight - padding);
    },
        
    
    
    // private helpers
    
    /**
     * return gravatar image if available
     */
    gravatarUrl: function(email) {
        return "http://www.gravatar.com/avatar/" + CryptoJS.MD5(email);
    },
    
    
    /**
     * convert date in vor n Minuten
     */
    dateAgo: function(date) {
        var now = new Date().getTime() / 1000;
        
        var ageInSeconds = now - date;
        var ageInMinutes = ageInSeconds / 60;
        var ageInHours = ageInMinutes / 60;
        var ageInDays = ageInHours / 24;
        
        if(ageInMinutes<1)
            return 'vor ' + Math.floor(ageInSeconds) + ' Sekunden';
        if(ageInHours<1)
            return 'vor ' + Math.floor(ageInMinutes) + ' Minuten';
        if(ageInDays<1)
            return 'vor ' + Math.floor(ageInHours) + ' Stunden';
        
        var dateObj = new Date(date*1000);
        return dateObj.getHours() + ':' + dateObj.getMinutes() + ':' + dateObj.getSeconds();
    },
    
    
    /**
     * escape html
     */
    escape: function(string) {
        var entityMap = {
            "&": "&amp;",
            "<": "&lt;",
            ">": "&gt;",
            '"': '&quot;',
            "'": '&#39;',
            "/": '&#x2F;'
          };

        return String(string).replace(/[&<>"'\/]/g, function (s) {
          return entityMap[s];
        });
    },
    
    
    /**
     * emoticons einfuegen
     */
    emoticons: function(text) {
        $.each(emoticons, function(shortcut, emoticon) {
            // escape shortcut
            shortcut = shortcut.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
            
            var re = new RegExp(shortcut, 'g');
            text = text.replace(re, '<img src="'+ emoticon +'" title="' + shortcut + '"/>');
        });
        return text;
    }
}