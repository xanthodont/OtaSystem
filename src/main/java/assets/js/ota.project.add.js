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
				url: '/ota/project/getProperty',
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

    return {
        //main function to initiate the module
        init: function () {
            handleSelec2Init();
        }

    };

}();