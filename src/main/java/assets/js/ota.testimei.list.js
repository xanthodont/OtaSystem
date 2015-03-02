/**
 * 
 */
var TestimeiList = (function(){
	return {
		init: function() {
			$('.delete').click(function(){
				if (confirm(lang.sys.sure_delete)) {
					$.ajax({
						url: 'ota/testimei/delete',
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
			
			function setStatus(id, status) {
				$.ajax({
					url: 'ota/testimei/setStatus',
					type: 'POST',
					data: {id: id, status: status},
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
			$('.active').click(function(){
				if (confirm(lang.testimei.sure_active)) {
					setStatus($(this).attr('key'), 1);
				}
			});
			$('.forbidden').click(function(){
				if (confirm(lang.testimei.sure_forbidden)) {
					setStatus($(this).attr('key'), 0);
				}
			});
			
			$('#select2_imei').select2({
				placeholder: "=请输入IMEI号=",
			    minimumInputLength: 2,
			    ajax: {
			    	url: 'ota/testimei/getList',
					type: 'GET',
					dataType: 'JSON',
					data: function (term, page) {
			            return {
			                q: term, // search term
			            };
			        },
			        results: function (data, page) { // parse the results into the format expected by Select2.
			            // since we are using custom formatting functions we do not need to alter the remote JSON data
			        	var results = [];
			        	var list = data.imeis;
			        	//results.push({id: query.term, text: query.term});
						for (var i = 0, len = list.length; i < len; i++) {
							results.push({id: list[i].imei, text: list[i].imei});
						}
			            return { results: results };
			        }
			    }
			});
		}
	};
})();