$(function() {
	$('form').submit(function(e) {
		var form = $(this);
		$.post('/data/feedback', form.serialize(), function() {
			form.hide();
			$('img').show();
		});
		e.preventDefault();  // necessary?
	});
});
