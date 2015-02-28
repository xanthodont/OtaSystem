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
			
			function queryFilter(hId, results) {
				$(hId).select2({
		            allowClear: true,
		            minimumInputLength: 0,
		            query: function (query) {
		            	var data = {
		                    results: [{id: 0, text: '=显示全部='}]
		                };
		            	var rs = results.filter(function(x) {return x.text.match(query.term);});
		            	for (var i = 0, len = rs.length;  i < len; i++) {
		            		data.results.push(rs[i]);
		            	}
		                query.callback(data);
		            }
		        });
			}
			function ajaxResults(results) {
				$.ajax({
					url: 'ota/project/getList',
					type: 'GET',
					data: {},
					dataType: 'JSON',
					success: function(resp) {
						var list = resp.projects;
						for (var i = 0, len = list.length; i < len; i++) {
							results.push({id: list[i].id, text: list[i].projectName});
						}
					}
				});
			}
			var	projectResults = [];
			ajaxResults(projectResults);
			queryFilter('#select2_projectId', projectResults);
			$('#select2_projectId').click(function(){
				$('#search_form').submit();
			});
		}
	};
})();