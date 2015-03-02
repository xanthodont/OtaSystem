/**
 * 
 */
var DeltaList = (function(){
	return {
		init: function() {
			var dialog = $("#dialog-form").dialog({
				autoOpen: false,
				width: 650,
				modal: true,
				buttons: {
					'上传文件': uploadFile,
					'取消': function() {
						dialog.dialog( "close" );
					}
				},
				close: function() {
					form[0].reset();
					$('#message').html('');
				}
		    });
			var form = dialog.find("form");
			
			function validUpload(formData, jqForm, options){
				$('#message').html(lang.sys.file_uploading);
				return true;
			}
			function processReturnJson(json, statusText){
				var resp = $.parseJSON(json);
				if (resp.code == constants.code.success) {
					$('#message').html(lang.sys.file_upload_success);
					setTimeout(function() {
						dialog.dialog('close');
						window.location.href = '';
					}, 3000);
					//dialog.dialog('close');
				} else {
					$('#message').html(resp.msg);
				}
			}
			var uploadOptions = {
					beforeSubmit: validUpload,
					success: processReturnJson,
					dataType: 'text'
			};
			$('#fileupload').ajaxForm(uploadOptions);
			
			function loadDelta() {
				
			}
			function uploadFile() {
				
				form.submit();
			}
			$('.fileupload-exists').click(function(){
				$('#message').html('');
			});
			
			
			$('.upload,.reupload').click(function(){
				var deltaId = $(this).attr('key');
				form.attr('action', 'ota/delta/uploadfile/'+deltaId);
				form.find('input[name="deltaId"]').val(deltaId);
				dialog.dialog("open");
			});
			
			$('.pass').click(function(){
				if (confirm(lang.delta.sure_pass)) {
					$.ajax({
						url: 'ota/delta/setStatus',
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
				if (confirm(lang.delta.sure_nopass)) {
					$.ajax({
						url: 'ota/delta/setStatus',
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
						url: 'ota/delta/setStatus',
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
						url: 'ota/delta/setStatus',
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
			
			
			
			$('#select2_versionId').select2({
				placeholder: "=请输入版本名称=",
			    minimumInputLength: 2,
			    ajax: {
			    	url: 'ota/version/getList',
					type: 'GET',
					dataType: 'JSON',
					data: function (term, page) {
			            return {
			                q: term, // search term
			            };
			        },
			        results: function (data, page) { // parse the results into the format expected by Select2.
			            // since we are using custom formatting functions we do not need to alter the remote JSON data
			        	var results = [{id: 0, text: '=显示全部='}];
			        	var list = data.versions;
						for (var i = 0, len = list.length; i < len; i++) {
							results.push({id: list[i].id, text: list[i].versionName});
						}
			            return { results: results };
			        },
			        cache: true
			    }
			});
			$('#select2_versionId').click(function(){
				$('#search_form').submit();
			});
		}
	};
})();