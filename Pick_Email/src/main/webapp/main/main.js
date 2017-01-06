$(function() {
	var $tiles = $('#tiles'),
	$handler = $('li', $tiles), 
	$main = $('#main'), $window = $(window), 
	$document = $(document), 
	options = {
		autoResize : true, // This will auto-update the layout when the browser window is resized.
		container : $main, // Optional, used for some extra CSS styling
		offset : 15, // Optional, the distance between grid items
		itemWidth : 200, // Optional, the width of a grid item
	};
	/**
	 * Reinitializes the wookmark handler after all images have loaded
	 */
	function applyLayout() {
		$tiles.imagesLoaded(function() {
			// Destroy the old handler
			if ($handler.wookmarkInstance) {
				$handler.wookmarkInstance.clear();
			}
			// Create a new layout handler.
			$handler = $('li', $tiles);
			$handler.wookmark(options);
		});
	} 

	// Call the layout function for the first time
	applyLayout();
	// Capture scroll event.
	
});
$(function() {

	
	$('.post-module').hover(function() {
		$(this).find('.description').stop().animate({
			height : "toggle",
			opacity : "toggle"
		}, 230);
	});
	$('.button-collapse').sideNav({
		menuWidth : 230, // Default is 240
		clorseOnClick : true, // Closes side-nav on <a> clicks, useful for Angular/Meteor
		draggable : true
	
	// Choose whether you can drag to open on touch screens
	});
	$('.modal').modal();

	/*팝업창*/

	$('#pick10000').on("click", function() {
		$.colorbox({

			iframe : "true",
			href : "../pick/pickOne.jsp",
			width : "800px",
			height : "900px"
		});

	});
	$('#pick10001').on("click", function() {
		$.colorbox({

			iframe : "true",
			href : "../pick/pickMulti.jsp",
			width : "800px",
			height : "900px"
		});

	});

	/*관심사 드롭다운*/
	$('select').material_select();

	/*add btn*/
	$("#add_btn").on("click", function() {
		$.colorbox({

			iframe : "true",
			href : "../voteRegistration/voteRegistration.jsp",
			width : "500px",
			height : "900px",
				scrolling: true
		});

	});

	

	

});
