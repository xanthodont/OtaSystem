/**
 * 
 */
var VersionList = (function(){
	return {
		init: function() {
			$('.delete').click(function(){
				if (confirm(lang.sys.sure_delete)) {
					$.ajax({
						url: 'ota/version/delete',
						type: 'POST',
						data: {id: $(this).attr('key')},
						dataType: 'JSON',
						error: function() {},
						success: function(resp) {
							if (resp.code == constants.code.success) {
                                window.location.href = resp.msg;
                            } else {
                            	alert(resp.msg);
                            }
						}
					});
				}
			});
		}
	};
})();