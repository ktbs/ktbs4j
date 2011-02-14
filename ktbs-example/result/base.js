/**
 * 
 */
function init(evt) {
    	
        if ( window.svgDocument == null )
            svgDocument = evt.target.ownerDocument;
    }

    var click       = 0;
    var mouseDown   = 0;
    var mouseUp     = 0;
    var mouseOver   = 0;
    var mouseMove   = 0;
    var mouseOut    = 0;
    
    function updateStats() {
        svgDocument.getElementById("clicks").firstChild.data = "onclick = " + click;
        svgDocument.getElementById("mousedowns").firstChild.data = "onmousedown = " + mouseDown;
        svgDocument.getElementById("mouseups").firstChild.data = "onmouseup = " + mouseUp;
        svgDocument.getElementById("mouseovers").firstChild.data = "onmouseover = " + mouseOver;
        svgDocument.getElementById("mousemoves").firstChild.data = "onmousemove = " + mouseMove;
        svgDocument.getElementById("mouseouts").firstChild.data = "onmouseout = " + mouseOut;
    }
    
    function msClick (evt) {
        click++;
        updateStats();
    }

    function msDown (evt) {
        mouseDown++;
        updateStats();
    }
    
    function msUp (evt) {
        mouseUp++;
        updateStats();
    }
    
    function msOver (evt) {
        mouseOver++;
        updateStats();
    }
    
    function msMove (evt) {
        mouseMove++;
        updateStats();
    }

    function msOut (evt) {
        mouseOut++;
        updateStats();
    }
    
    function affiche(id, x, y){
    	alert(id + x + y);
    	alert(document.getElementById(id));
    	getElementById(id).style = "left: 400px; top: 720px; position: absolute; display: block;";
    }