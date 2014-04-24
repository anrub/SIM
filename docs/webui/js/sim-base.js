var sim = {

    /**
     * all received and sent chat messages
     */
    messages: false,

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
        });
    },
    
    
    /**
     * Emoticons initialisieren
     */
    initEmoticons: function() {
        var emotbox = $('#message-emoticons');
        var lastEmot = "";
        $.each(emoticons, function(shortcut, emoticon) {
            if(lastEmot != emoticon) {
                emotbox.append('<img src="'+ emoticon +'" title="' + shortcut + '"/>');
            }
            lastEmot = emoticon;
        });
    },
    
    
    /**
     * initialize events (clicks, ...)
     */
    initEvents: function() {
        // toggle emoticons
        $('#message-toggleemots').click(function() {
            $(this).toggleClass('active');
            if($(this).hasClass('active'))
                $('#message').addClass('message-emoticions-visible');
            else 
                $('#message').removeClass('message-emoticions-visible');
        });
        
        // emoticons click
        $('#message-emoticons').delegate("img", "click", function() {
            $('#message-input-textfield').val(
                $('#message-input-textfield').val() + " " + $(this).attr('title')
            );
        });
        
        // switch to user
        $('.contacts').delegate("li", "click", function() {
            var user = $(this).find('.contacts-name').html();
            $('.rooms li, .contacts li').removeClass('active');
            $(this).addClass('active');
            
            // ToDo
            console.info("open chat user " + user); // highlite new messages, set unreadcounter to 0
        });
        
        // enter room click
        $('.rooms').delegate("li .name", "click", function() {
            var room = $(this).parent().find('.name').html();
            $('.rooms li, .contacts li').removeClass('active');
            $(this).parent().parent().addClass('active');
            
            // ToDo
            console.info("enter and open room " + room);
        });
        
        // leave room click
        $('.rooms').delegate("li .rooms-leave", "click", function(e) {
            var room = $(this).parent().find('.name').html();
            
            // ToDo
            console.info("leave room " + room);
        });
        
        // send message
        $('#message-send').click(function() {
            var message = $('#message-input-textfield').val();
            
            // ToDo
            console.info("send message " + message);
            
            // scroll 2 bottom
            $("#content-wrapper").mCustomScrollbar("scrollTo","bottom");
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
    }
}