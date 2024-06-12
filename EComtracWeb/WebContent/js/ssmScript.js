function removeRequiredCss(el){
	if('INPUT'==el.tagName){
		if(el.value.length>0){
			el.className = el.className.replace("formcomponentReqHigh", "");
		}else{
			el.className = el.className + "formcomponentReqHigh"; 
		}
	}
	 
}

function showModal(modalId){
	$('#'+modalId).modal('show');
	$('#'+modalId).css({top:'40%',left:'50%',margin:'-'+($('#'+modalId).height() / 2)+'px 0 0 -'+($('#'+modalId).width() / 2)+'px'});
}

function showModalParentID(parentId){
	var modalUI = $('#'+parentId).find('.modal');
	modalUI.modal('show');
	modalUI.css({top:'50%',left:'50%',margin:'-'+(modalUI.height() / 2)+'px 0 0 -'+(modalUI.width() / 2)+'px'});
	
}

function setSizeFullscreen(id){
	var docH = $(document).height();
	//alert(docH);
	$("#"+id).height(docH);
}

function validateUploadSize(file) {
    var FileSize = file.files[0].size / 1024 / 1024; // in MiB
    if (FileSize > 3) {
        alert('File size exceeds 3 MiB');
        $(file).val(''); //for clearing with Jquery
    } else {

    }
}

function prepareLoadingVeil(){
	
	 $( document ).ajaxStart(function() {
		   //console.log('ajaxStart:');
		  	showBusysign();
	  }).ajaxStop(function() {
		  	//console.log('ajaxStop:');
		  	hideBusysign();
	  });
	  
	  document.getElementsByTagName('body')[0].onclick = nonAjaxClick;
	  
	  $.when(nonAjaxClick).done(function() { 
    		hideBusysign();
	  });  
}


function nonAjaxClick(eventData) {
   var clickedElement = (window.event) ? event.srcElement : eventData.target;
   if (clickedElement.tagName.toUpperCase() == 'BUTTON' || clickedElement.tagName.toUpperCase() == 'IMG' || clickedElement.tagName.toUpperCase() == 'A' || clickedElement.parentNode.tagName.toUpperCase() == 'A'
     || (clickedElement.tagName.toUpperCase() == 'INPUT' && (clickedElement.type.toUpperCase() == 'BUTTON' || clickedElement.type.toUpperCase() == 'SUBMIT'))) {
     
	/*
	 console.log('id : '+clickedElement.id );
	 console.log('tagName : '+clickedElement.tagName );
     console.log('wicket:id : '+clickedElement.getAttribute("wicket:id") );
     console.log('class : '+clickedElement.className );
     console.log('tagName : '+clickedElement.tagName.className );
     console.log('parent ID : '+clickedElement.parentNode.id );
     console.log('parent tag : '+clickedElement.parentNode.tagName );
     console.log('parent class : '+clickedElement.parentNode.className );
     */
     
     if(clickedElement.tagName.toUpperCase() == 'A'){
    	if(String(clickedElement.getAttribute("href")).indexOf('fileAttach')!=-1){
	 		return;
	 	}  
	 } 
     
     if(clickedElement.className.indexOf('ajs')!=-1){
	 	return;
	 } 
     
     if(String(clickedElement.getAttribute("wicket:id")).indexOf('download')!=-1){
	 	return;
	 } 
    
      if(clickedElement.getAttribute("wicket:id")===null && clickedElement.parentNode.className.indexOf('field')!=-1){
	 	return;
	 } 
     
     if(String(clickedElement.getAttribute("wicket:id"))==null){
	 	return;
	 } 
     
      if(clickedElement.parentNode.className.indexOf('browse item')!=-1){
	 	return;
	 } 
      
      
	  if(clickedElement.className.indexOf('browse item')!=-1 || clickedElement.className.indexOf('calnav')!=-1 ){
	 	return;
	 }
	  
	  
      if(clickedElement.parentNode.className.indexOf('calcell')!=-1){
	 	return;
	 } 
      if(clickedElement.parentNode.className.indexOf('yui')!=-1){
	 	return;
	 } 
      
	// console.log('onclick show Ajax');
	 //console.log('Start not AJAX:');
	 showBusysign();
     
   }
}
	 
function hideBusysign() {
	 $('#ajaxveil').hide();
}
	 
function showBusysign() {
	 $('#ajaxveil').height($(document).height());
	 $('#ajaxveil').show();
}
	 