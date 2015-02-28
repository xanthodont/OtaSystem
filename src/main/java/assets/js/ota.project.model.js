var ProjectAdd = function () {
	var handleSelec2Init = function () {
		function queryFilter(hId, results) {
			$(hId).select2({
	            allowClear: true,
	            minimumInputLength: 0,
	            query: function (query) {
	            	var data = {
	                    results: []
	                };
	            	data.results.push({id: query.term, text: query.term});
	            	var rs = results.filter(function(x) {return x.text.match(query.term);});
	            	for (var i = 0, len = rs.length;  i < len; i++) {
	            		data.results.push(rs[i]);
	            	}
	                query.callback(data);
	            }
	        });
			
		}
		function ajaxResults(propertyName, results) {
			$.ajax({
				url: 'ota/project/getProperty',
				type: 'GET',
				data: {propertyName: propertyName},
				dataType: 'JSON',
				success: function(resp) {
					for (var i = 0, len = resp.length; i < len; i++) {
						results.push({id: resp[i], text: resp[i]});
					}
					
				}
			});
		}
		var	oemResults = [], productResults = [], languageResults = [], operatorResults = [];
		ajaxResults('oem', oemResults);
		ajaxResults('product', productResults);
		ajaxResults('language', languageResults);
		ajaxResults('operator', operatorResults);
		
		queryFilter('#select2_oem', oemResults);
		queryFilter('#select2_product', productResults);
		queryFilter('#select2_language', languageResults);
		queryFilter('#select2_operator', operatorResults);
	}

	
	function formInit() {
		var form = $('#form_model');
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
                 var projectName = $(form).find('input[name="projectName"]').val();
                 var oem = $(form).find('input[name="oem"]').val();
                 var product = $(form).find('input[name="product"]').val();
                 var language = $(form).find('input[name="language"]').val();
                 var operator = $(form).find('input[name="operator"]').val();
                 $.ajax({
                 	url: 'ota/project/save',
                 	type: 'POST',
                 	data: {projectName: projectName, oem: oem, product: product, language: language, operator: operator},
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

    return {
        //main function to initiate the module
        init: function () {
            handleSelec2Init();
            formInit();
        }

    };

}();