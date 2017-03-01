function PopupScreenDynamicTableHandler(screen, maxNumRows) {
	var _screen;
	var _maxNumRows;

	_init(screen, maxNumRows);

	function _init(screen, maxNumRows) {
		_screen = screen;
		_maxNumRows = (maxNumRows == null) ? 5 : maxNumRows;
	}

	function _handleButtonClick(jqButton) {
		var jqNewButtons = null;
		if (jqButton.hasClass("addDynamicRow")) {
			jqNewButtons = _addDynamicRow(jqButton);
		} else if (jqButton.hasClass("removeDynamicRow")) {
			jqNewButtons = _removeDynamicRow(jqButton);
		}
		return jqNewButtons;
	}

	function _addDynamicRow(jqAddRowButton) {
		// Add row button -> Get HTML for the new row
		var rowHTML = _getNewRowHTML(jqAddRowButton);
		// Add this new row to dynamic table
		var dynamicTableId = jqAddRowButton.attr("dynamicTableId");
		var jqScreenDiv = _screen.getScreenDiv();
		var jqDynamicTable = $('#'+dynamicTableId, jqScreenDiv);
		jqDynamicTable.append(rowHTML);
		// Disable the Add More button if necessary
		if (jqDynamicTable.find('tr.dynamicRow').length >= _maxNumRows) {
			jqAddRowButton.attr('disabled', 'disabled');
			var attr = jqAddRowButton.attr('tieToRadioId');
			if (attr != null && attr != "")	{
				jqAddRowButton.attr('tieToRadioId', '');
			}
		}
		// Update visibility of remove buttons
		_updateRemoveButtonVisibility(jqDynamicTable);
		// Return the new buttons
		return jqDynamicTable.find('button.removeDynamicRow');
	}

	function _removeDynamicRow(jqRemoveRowButton) {
		// Define the dynamic table reference
		var jqDynamicTable = jqRemoveRowButton.closest('table.dynamicTable');
		// Enable the Add More button
		var jqAddRowButton = jqRemoveRowButton.closest('form').find('button.addDynamicRow'); // there's probably a better way to do this
		jqAddRowButton.removeAttr('disabled');
		if (jqAddRowButton.attr('tieToRadioId') == "") {
			var tieToRadioId = jqRemoveRowButton.attr("tieToRadioId");
			jqAddRowButton.attr('tieToRadioId', tieToRadioId);
		}
		// Remove the row
		jqRemoveRowButton.closest('tr.dynamicRow').remove();
		// Update visibility of remove buttons
		_updateRemoveButtonVisibility(jqDynamicTable);
	}

	// ******************************************* //
	// *** UTILITY FUNCTIONS ********************* //
	// ******************************************* //

	function _getNewRowHTML(jqButton) {
		var dynamicRowTemplate = jqButton.attr("dynamicRowTemplate");
		var uniqueId = (new Date()).getTime();
		var renderData = { "uniqueRowId": uniqueId };
		var tieToRadioId = jqButton.attr("tieToRadioId");
		if (tieToRadioId != null) renderData.tieToRadioId = tieToRadioId;
		var rowHTML = _screen.renderContent(dynamicRowTemplate, renderData);
		return rowHTML;
	}

	function _updateRemoveButtonVisibility(jqDynamicTable) {
		var jqRemoveRowButtons = jqDynamicTable.find('button.removeDynamicRow');
		jqRemoveRowButtons.css('visibility', (jqRemoveRowButtons.length > 1) ? 'visible' : 'hidden');
	}

	// ******************************************* //
	// *** PUBLIC METHODS ************************ //
	// ******************************************* //

	return {
		handleButtonClick: _handleButtonClick,
		addDynamicRow: _addDynamicRow,
		removeDynamicRow: _removeDynamicRow
	}

}