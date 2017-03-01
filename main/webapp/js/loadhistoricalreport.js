function onloadHistoricalReport() {
  jQuery.ajax({
	url: 'historicalReportAction.do',
        dataType: 'json',
        success:function(result){
        var histTable = "";
        var histCount = 1;
        var allCount = 1;
        histTable += '<table id="historicalReportsTable">';
        histTable += '<tr><th colspan="2">Historical Reports</th></tr>';

        var histreportarray = result['histReportList'];
        var downloadHistUrl = "/report-console/view/downloadHistReport.do?histReportID=";
        jQuery.each(histreportarray, function (data) {
			if ("statusCode" != data && "statusMessage" != data) {
				var id = histreportarray[data].id;
				var fileName = histreportarray[data].fileName;
				var fileDescription = histreportarray[data].description;
				var fileDescriptionRegexp = new RegExp("run (.*) (for|by) (.*) on (.*)", "i"); // e.g. "run Software Update Report by dmportal on Tue Jul 22 15:22:41 CDT 2014"
				var matchArray = fileDescription.match(fileDescriptionRegexp);
				var bIsNew = false;
				if (matchArray != null && matchArray.length >= 5) {
					var insertedDateStr = matchArray[4];
					var insertedDate = new Date(insertedDateStr);
					if (insertedDate != null) {
						var insertedTime = insertedDate.getTime();
						var nowTime = (new Date()).getTime();
						var diff = nowTime - insertedTime;
						var thresholdDiff = 10 * 60 * 1000; // 10 minutes
						if (diff <= thresholdDiff) {
							bIsNew = true;
						}
					}
				}
				var histReportIsVisible = 0;
				var roles = histreportarray[data].roles;
				var evenOddClass = "oddRow";
				if (allCount % 2 == 0) {
					evenOddClass = "evenRow";
				}
				if (bIsNew) {
					evenOddClass += " newRow";
				}
				histTable += '<tr class="' + evenOddClass + '">';
				histTable += '<td style="position: relative;">';
				histTable += '<img src="../images/report2.gif"/>';
				if (bIsNew) {
					histTable += '<span class="newIcon"></span>';
				}
				histTable += '</td>';
				histTable += '<td><a href=\"javascript:launchHistoricalReport(\'' + downloadHistUrl + id + '\');\">'+ fileName + '</a><br/>';
				histTable += '<span class="historicalReportsSubline">' + fileDescription + '</span>';
				histTable += '</td> </tr>';

				histCount = histCount + 1;
				allCount = allCount + 1;
			}
		});
        if ( histCount == 1 ) {
        	histTable += '<tr><td>No Historical Reports avialable for this Group.</td></tr>';
        }
        histTable += '</table>';
   jQuery('#historicalReportsScrollingDiv').html(histTable);
    },
    error: function (jqXHR, textStatus, errorThrown){
      if(errorThrown.indexOf("login") != -1){
        // location.href = 'login.vm'
        location.reload();
      } else{
        alert("Exception occured " + textStatus);
      }
    }
  });
}


/**
 * Converts an object to string
 */
function object2String(obj) {
    var val, output = "";
    if (obj) {
        output += "{";
        for (var i in obj) {
            val = obj[i];
            switch (typeof val) {
                case ("object"):
                    if (val[0]) {
                        output += i + ":" + array2String(val) + ",";
                    } else {
                        output += i + ":" + object2String(val) + ",";
                    }
                    break;
                case ("string"):
                    output += i + ":'" + escape(val) + "',";
                    break;
                case ("function"):
                	break;
                default:
                    output += i + ":" + val + ",";
            }
        }
        output = output.substring(0, output.length-1) + "}";
    }
    return output;
}

/**
 * Converts an array to string
 */
function array2String(array) {
    var output = "";
    if (array) {
        output += "[";
        for (var i in array) {
            val = array[i];
            switch (typeof val) {
                case ("object"):
                    if (val[0]) {
                        output += array2String(val) + ",";
                    } else {
                        output += object2String(val) + ",";
                    }
                    break;
                case ("string"):
                    output += "'" + escape(val) + "',";
                    break;
                case ("function"):
                	break;
                default:
                    output += val + ",";
            }
        }
        output = output.substring(0, output.length-1) + "]";
    }
    return output;
}

/**
 * Converts a string to an object
 */
function string2Object(string) {
    eval("var result = " + string);
    return result;
}

/**
 * Converts a string to an array
 */
function string2Array(string) {
    eval("var result = " + string);
    return result;
}
