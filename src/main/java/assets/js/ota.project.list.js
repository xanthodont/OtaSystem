/**
 * 
 */
var ProjectList = (function(){
	return {
		init: function() {
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
			
			$('.delete').click(function(){
				if (confirm(lang.sys.sure_delete)) {
					$.ajax({
						url: 'ota/project/delete',
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
			
			var editDialog = $('#edit_dialog').dialog({
				autoOpen: false,
				title: '编辑项目',
				width: 750,
				modal: true,
				buttons: {
					'保存': function() {
						$('#form_edit').submit();
					},
					'取消': function() {
						$(this).dialog('close');
					}
				}
			});
			
			$('.edit').click(function(){
				var id = $(this).attr('key');
				$.ajax({
					url: 'ota/project/getProject',
					type: 'get',
					dataType: 'json',
					data: {id: id},
					error: function() {
						alert('服务器错误，请刷新重试!');
					},
					success: function(resp) {
						if (resp.r.code == "0") {
							alert(resp.r.msg);
						} else {
							var p = resp.project;
							var f = $('#form_edit');
							f.find('input[name="id"]').val(p.id);
							f.find('input[name="projectName"]').val(p.projectName);
							f.find('input[name="oem"]').val(p.oem);
			                f.find('input[name="product"]').val(p.product);
			                f.find('input[name="language"]').val(p.language);
			                f.find('input[name="operator"]').val(p.operator);
							editDialog.dialog("open");
						}
					}
				});
				
			});
			
			/*
			$('#example').dataTable( {
				"bServerSide": true,
				"sPaginationType": "bootstrap",
		        "sAjaxSource": "ota/project/list",
		        "aoColumns": [
		            { "mDataProp": "projectName" },
		            { "mDataProp": "oem" },
		            { "mDataProp": "product" },
		            { "mDataProp": "language" },
		            { "mDataProp": "operator" },
		            { "mDataProp": "updateTime" },
		            {
		            	"mDataProp": "id",
		            	"fnRender": function(obj) {
		            		return '<a href="javascript:;" class="btn mini purple edit" key="1"><i class="icon-edit"></i> 编辑</a> ' +
							' <a href="javascript:;" class="btn mini black delete" key="1"><i class="icon-trash"></i> 删除</a>';
		            	}
		            }
		        ]
		    } );*/
			
			function formInit() {
				var form = $('#form_edit');
				var error = $('.alert-error', form);
		        var success = $('.alert-success', form);
		        
		        form.validate({
		        	errorElement: 'span', //default input error message container
		            errorClass: 'help-inline', // default input error message class
		            focusInvalid: false, // do not focus the last invalid input
		            ignore: "",
		            rules: {
		            	projectName: {
		            		required: true
		            	},
		                oem: {
		                    required: true
		                }, 
		                product: {
		                	required: true
		                },
		                language: {
		                	required: true
		                }
		            },
		            messages: { // custom messages for radio buttons and checkboxes
		                username: {
		                    required: lang.account.requiredUsername
		                }
		            },

		            errorPlacement: function (error, element) { 
		                error.insertAfter(element); // for other inputs, just perform default behavoir
		            },
		            
		            invalidHandler: function (event, validator) { //display error alert on form submit   
		                success.hide();
		                error.show();
		                App.scrollTo(error, -200);
		            },
		            success: function (label) {
		                if (label.attr("for") == "service" || label.attr("for") == "roleId") { // for checkboxes and radip buttons, no need to show OK icon
		                    label.closest('.control-group').removeClass('error').addClass('success');
		                    label.remove(); // remove error label here
		                } else { // display success icon for other inputs
		                    label.addClass('valid').addClass('help-inline ok') // mark the current input as valid and display OK icon
		                    .closest('.control-group').removeClass('error').addClass('success'); // set success class to the control group
		                }
		            },
		            highlight: function (element) { // hightlight error inputs
		                $(element)
		                    .closest('.help-inline').removeClass('ok'); // display OK icon
		                $(element)
		                    .closest('.control-group').removeClass('success').addClass('error'); // set error class to the control group
		            },
		            submitHandler: function (form) {
		            	 success.show();
		                 error.hide();
		                 var id = $(form).find('input[name="id"]').val();
		                 var projectName = $(form).find('input[name="projectName"]').val();
		                 var oem = $(form).find('input[name="oem"]').val();
		                 var product = $(form).find('input[name="product"]').val();
		                 var language = $(form).find('input[name="language"]').val();
		                 var operator = $(form).find('input[name="operator"]').val();
		                 $.ajax({
		                 	url: 'ota/project/save',
		                 	type: 'POST',
		                 	data: {id: id, projectName: projectName, oem: oem, product: product, language: language, operator: operator},
		                 	dataType: 'JSON',
		                 	error: function() {},
		                 	success: function(resp) {
		                 		if (resp.code == constants.code.success) {
		                             window.location.href = resp.msg;
		                         } else {
		                         	success.hide();
		                         	$('.alert-error', form).children('span').html(resp.msg);
		                         	$('.alert-error', form).show();
		                         }
		                 	}
		                 });
		            }
		        });
		        
		        $('.chosen, .chosen-with-diselect', form).change(function () {
		            form.validate().element($(this)); //revalidate the chosen dropdown value and show error or success message for the input
		        });

		         //apply validation on select2 dropdown value change, this only needed for chosen dropdown integration.
		        $('.select2', form).change(function () {
		            form.validate().element($(this)); //revalidate the chosen dropdown value and show error or success message for the input
		        });
			}
			formInit();
		}
	};
})();