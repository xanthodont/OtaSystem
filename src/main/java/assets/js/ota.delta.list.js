/**
 * 
 */
var DeltaList = (function(){
	return {
		init: function() {
			$('.pass').click(function(){
				if (confirm(lang.delta.sure_publish)) {
					$.ajax({
						url: '/ota/delta/setStatus',
						type: 'POST',
						data: {id: $(this).attr('key'), status: 4},
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
			
			$('.nopass').click(function(){
				if (confirm(lang.delta.sure_publish)) {
					$.ajax({
						url: '/ota/delta/setStatus',
						type: 'POST',
						data: {id: $(this).attr('key'), status: 1},
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
			
			$('.publish').click(function(){
				if (confirm(lang.delta.sure_publish)) {
					$.ajax({
						url: '/ota/delta/setStatus',
						type: 'POST',
						data: {id: $(this).attr('key'), status: 5},
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
			
			$('.retest').click(function(){
				if (confirm(lang.delta.sure_retest)) {
					$.ajax({
						url: '/ota/delta/setStatus',
						type: 'POST',
						data: {id: $(this).attr('key'), status: 2},
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
			
			$('.undo').click(function(){
				if (confirm(lang.delta.sure_undo)) {
					$.ajax({
						url: '/ota/delta/setStatus',
						type: 'POST',
						data: {id: $(this).attr('key'), status: 4},
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