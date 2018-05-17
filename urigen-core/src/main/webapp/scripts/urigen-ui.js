/**
 * Created by IntelliJ IDEA.
 * User: jupp
 * Date: 06/01/2012
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */

var users;
var userTable;

var prefs;
var prefsTable;

var urisTable;


function initUI() {

    autoUserLogin();
    $(document).ready(function() {

        $("#tabs").tabs( {
            "show": function(event, ui) {
                if ( userTable != undefined && userTable.length > 0 ) {
                    userTable.fnAdjustColumnSizing();
                }
                if ( prefsTable != undefined && prefsTable.length > 0 ) {
                    prefsTable.fnAdjustColumnSizing();
                }
                if ( urisTable != undefined && urisTable.length > 0 ) {
                    urisTable.fnAdjustColumnSizing();
                }
            }
        } );

        $( "#dialog:ui-dialog" ).dialog( "destroy" );

        // setting return value on checkbox to true/false..
        $('#register-admin').change(function(){
            if($(this).attr('checked')){
                $(this).val('true');
            }else{
                $(this).val('false');
            }
        });
        configureUI();
    });
}


function configureUI() {

    initialiseUserTable();
    initialisePrefsTable();
    initialiseUrisTable();
    loadPrefTypes();

}

function loadPrefTypes() {

    $.ajax({
        url: 'api/preferences/types',
        dataType: 'json',
        success: function(json) {
            for (var x = 0 ; x < json.length ; x++) {
                $("#pref-type").append($("<option/>", {
                    value: json[x].classid,
                    text: json[x].description
                }));
            }
        },
        error: function() {
            alert("Failed to retrieve users");
        }
    });
}

function initialiseUrisTable() {
    urisTable = $("#uri-table").dataTable({
        "bServerSide": true,
        "sAjaxSource": "api/uris/jdatatable",
        "bProcessing": true,
        "bJQueryUI": true,
        "bAutoWidth": false,
        "aoColumns" : [
            { sWidth: '200px' },
            { sWidth: '60px' },
            { sWidth: '40px' },
            { sWidth: '40px' },
            { sWidth: '150px' }
        ],
        "bSort": false,
        "bPaginate": true,
        "bFilter": false,
        "bInfo": true

    });
//    requestUris();


}

function initialisePrefsTable() {
    prefsTable = $("#prefs-table").dataTable({
        "aaSorting": [
            [ 0, "desc" ]
        ],
        "bPaginate": false,
        "bStateSave": true,
        "bFilter": false,
        "bSort": true,
        "bInfo": false,
//        "fnDrawCallback": redrawPublicationsTableLater,
//        "sScrollY": "200px",
        "bAutoWidth": false,
        "aoColumns" : [
            { sWidth: '10px' },
            { sWidth: '150px' },
            { sWidth: '280px' },
            { sWidth: '13px' },
            { sWidth: '13px' },
            { sWidth: '100px' }
        ]
    });
    requestPrefs();
}


function initialiseUserTable() {
    userTable = $("#user-table").dataTable({
        "aaSorting": [
            [ 1, "desc" ]
        ],
        "bPaginate": false,
        "bStateSave": true,
        "bSort": true,
        "bInfo": false,
        "bFilter": false,

//        "fnDrawCallback": redrawPublicationsTableLater,
//        "sScrollY": "200px",
        "bAutoWidth": false,
        "aoColumns" : [
            { sWidth: '10px' },
            { sWidth: '300px' },
            { sWidth: '300px' },
            { sWidth: '200px' },
            { sWidth: '150px' }
        ]
    });
    requestUsers();
}

//function requestUris() {
//
//    $.ajax({
//        url: 'api/uris',
//        dataType: 'json',
//        success: function(json) {
//            prefs = json;
//            displayUrisCallback()
//        },
//        error: function() {
//            alert("Failed to retrieve uris");
//        }
//    });
//}
//function displayUrisCallback() {
//
//    for (var i = 0; i < prefs.length && i < 200; i++) {
//
//        var id = prefs[i].preferenceId;
//        var ontologyName = prefs[i].ontologyName;
//        var ontologyUri = prefs[i].ontologyUri;
////        var idType = pref[i].autoIdGenerator;
//
//        var img_element = document.createElement("img");
//        img_element.src = "images/edit.png";
//        img_element.id = "pref-edit-" + id;
//        var dataItem = [
//            id,
//            ontologyName,
//            ontologyUri,
//            "<img onclick=\"prefEdit(" +id+")\" src=\"images/edit.png\"/>",
//            "<img id=\"pref-delete-" + id + "\" src=\"images/delete.png\"/>"];
//
//        prefsTable.fnAddData(dataItem);
//
//    }
//    prefsTable.fnAdjustColumnSizing();
//    prefsTable.fnStandingRedraw();
//}

function requestPrefs() {

    $.ajax({
        url: 'api/preferences',
        dataType: 'json',
        success: function(json) {
            prefs = json;
            displayPrefsCallback()
        },
        error: function() {
            alert("Failed to retrieve prefs");
        }
    });
}

function requestUsers() {

    var priorVisit = getCookie("urigen-manager-cookie");
    $.ajax({
        url: 'api/users?restApiKey=' + priorVisit,
        dataType: 'json',
        success: function(json) {
            users = json;
            displayUserCallback();
            // autoUserLogin();
        },
        error: function() {
            alert("Failed to retrieve users");
            // autoUserLogin();
        }
    });
}



function displayPrefsCallback() {

    prefsTable.fnClearTable(false);


    for (var i = 0; i < prefs.length && i < 200; i++) {

        var id = prefs[i].preferenceId;
        var ontologyName = prefs[i].ontologyName;
        var ontologyUri = prefs[i].ontologyUri;
//        var idType = pref[i].autoIdGenerator;

        var img_element = document.createElement("img");
        img_element.src = "images/edit.png";
        img_element.id = "pref-edit-" + id;
        var dataItem = [
            id,
            ontologyName,
            ontologyUri,
            "<img onclick=\"prefEdit(" + id + ")\" src=\"images/edit.png\"/>",
            "<img onclick=\"removePref(" + id + ")\" onclick=\"prefEdit(" +id+")\" id=\"pref-delete-" + id + "\"  src=\"images/delete.png\"/>",
            "<button class=\"ui-button-text\" onclick=\"newUri(" + id + ")\">Generate URI</button>"
        ];

        prefsTable.fnAddData(dataItem);

    }
    prefsTable.fnAdjustColumnSizing();
    prefsTable.fnStandingRedraw();
}

function removePref(id) {

    var priorVisit = getCookie("urigen-manager-cookie");
    if (priorVisit == undefined || priorVisit == "") {
        alert("Sorry, only administrators can remove preferences")
    }
    else {
        $("#dialog-confirm" ).dialog({
            resizable: false,
            height:140,
            modal: true,
            buttons: {
                "Delete all items": function() {
                    $.ajax({
                        type:   'DELETE',
                        url:    'api/preferences/' + id + '?restApiKey=' + getCookie("urigen-manager-cookie"),
                        contentType:    'application/json',
                        processData:    false,
                        success:        function (data) {
                            // hide the registration overlay
                            if (data.success == true) {
                                requestPrefs();
                                prefsTable.fnStandingRedraw();
                                $( this ).dialog( "close" );
                            }
                            else {
                                alert("Error: " + data.message);
                            }
                        },
                        error:          function(request, status, error) {
                            alert("Error: " + error);
                        }
                    });
                    $( this ).dialog( "close" );
                },
                Cancel: function() {
                    $( this ).dialog( "close" );
                }
            }
        });
    }
}

function newUri(prefid) {

    var json = "{" +
        "\"preferencesId\":" + prefid + "}";

    $.ajax({
        type:           'POST',
        url:            'api/uris?restApiKey=' + getCookie("urigen-manager-cookie"),
        contentType:    'application/json',
        data:           json,
        processData:    false,
        success:        function (data) {
            // hide the registration overlay
            if (data.statusOK == true) {
                alert("New URI created: " + data.generatedUri);
                urisTable.fnAdjustColumnSizing();
                prefsTable.fnStandingRedraw();
            }
            else {
                alert("Error: " + data.errorMessage);
            }
        },
        error:          function(request, status, error) {
            alert("Error: " + error);
        }
    });
}

function displayUserCallback() {

//    var userTable = $("#user-table").dataTable();
    userTable.fnClearTable(false);

    for (var i = 0; i < users.length && i < 200; i++) {

        var id = users[i].id;
        var userName = users[i].userName;
        var email = users[i].email;
        var apiKey = users[i].apiKey;
        var admin = users[i].admin;

        var dataItem = [
            id,
            userName,
            email,
            apiKey,
            admin];

        userTable.fnAddData(dataItem);

    }

    userTable.fnAdjustColumnSizing();
    userTable.fnStandingRedraw();


}

function showAddUserPanel() {

    var priorVisit = getCookie("urigen-manager-cookie");
    if (priorVisit == undefined || priorVisit == "") {
        alert("Sorry, only administrators can add new users")
    }
    else {

        $.ajax({
            url: 'api/users/query?restApiKey=' + priorVisit,
            dataType: 'json',
            success: function(jsonData) {

                if (jsonData.admin) {
                    $("#backgroundOverlay").css({
                        "opacity": "0.7"
                    });
                    $("#backgroundOverlay").fadeIn("slow");
                    $("#register-user-popup").fadeIn("slow");
                }
                else {
                    alert("Sorry, only administrators can add new users")
                }
            }
        });
    }
}


function clearErrorsFromAddPrefsPanel() {
    $("#pref-name-missing").hide();

    $("#base-uri-error").hide();
    $("#prefix-error").hide();
    $("#ontology-uri-error").hide();
    $("#ontology-url-error").hide();
    $("#suffix-error").hide();

    $("#digit-error").hide();
    $("#start-error").hide();
    $("#end-error").hide();
    $('#prefs-submit-image').hide();
    $("#duplicate-user-error").hide();
    $("#user-range-error").hide();
    $('#serverside-error').hide();
    $("#missing-user-error").hide();
}

function closePrefPanel () {
    resetPrefsPanel();
    clearErrorsFromAddPrefsPanel();
    $("#backgroundOverlay").fadeOut("fast");
    $('#prefs-panel-popup').hide();

}

function showAddPrefsPanel () {

    $('#prefs-submit-image').hide();

    var priorVisit = getCookie("urigen-manager-cookie");
    if (priorVisit == undefined || priorVisit == "") {
        alert("Sorry, only registered users can access this panel")
    }
    else {

        $.ajax({
            url: 'api/users/query?restApiKey=' + priorVisit,
            dataType: 'json',
            success: function(jsonData) {

                if (jsonData.admin) {
                    $("#backgroundOverlay").css({
                        "opacity": "0.7"
                    });
                    $("#backgroundOverlay").fadeIn("slow");
                    $("#prefs-panel-popup").fadeIn("slow");
                    selectionHandler();
                }
                else {
                    alert("Sorry, only admins can access this panel")
                }
            }
        });
    }
}

function getUserSelectBox(element1) {

    $.ajax({
        url: 'api/users',
        dataType: 'json',
        success: function(json) {
            for (var x = 0 ; x < json.length ; x++) {

                var selector = document.getElementById(element1);
                selector.append($("<option/>", {
                    value: json[x].id,
                    text: json[x].userName
                }));
            }
        },
        error: function() {
            alert("Failed to retrieve users");
            // autoUserLogin();

        }
    });
}

function submitNewPrefs () {

    $('#prefs-submit-image').show();
    var allSupplied = true;


    var prefId = $("#pref-hidden-id").val();
    var lastId = $("#hidden-last-id").val();
    var ontologyName = $("#pref-name").val().trim();
    var ontologyUri = $('#ontology-uri').val();
    var checkSource = false;
    var ontologyPhysicalUri = $('#ontology-url').val().trim();
    var defaultBaseUri = $("#base-uri").val().trim();
    var separator = "/";

    if (ontologyName == undefined || ontologyName == "") {
        $("#pref-name-missing").show();
        allSupplied = false;
    }
    if (ontologyUri == undefined || ontologyUri == "") {
        $("#ontology-uri-error").show();
        allSupplied = false;
    }
//    if (document.getElementById("check-source").checked) {
//        checkSource = true;
//
//        if (ontologyPhysicalUri == undefined || ontologyPhysicalUri == "") {
//            $("#ontology-url-error").show();
//            allSupplied = false;
//        }
//    }

//    if (document.getElementById("same-base-uri").checked) {
//        defaultBaseUri = ontologyUri;
//    }
    if (defaultBaseUri == undefined || defaultBaseUri == "") {
        $("#base-uri-error").show();
        allSupplied = false;
    }

    for (var j = 0; j < document.getElementsByName("separator").length; j++) {
        if (document.getElementsByName("separator")[j].checked) {
            separator = document.getElementsByName("separator")[j].value;
        }
    }

    var autoIdGenerator = $('#pref-type').val();
    var prefix = $('#prefix').val().trim();
    var suffix = $('#suffix').val().trim();

    if (/\s+/.test(prefix)) {
        $("#prefix-error").show();
        allSupplied = false;
    }
    if (/\s+/.test(suffix)) {
        $("#suffix-error").show();
        allSupplied = false;
    }

    var autoIdDigitCount = $("#digits").val().trim();
    if (autoIdDigitCount == "" || !(/^\d+$/.test(autoIdDigitCount))) {
        $("#digit-error").show();
        allSupplied = false;
    }

    var autoIdDigitStart = 0;
    var autoIdDigitEnd = -1;
    if (autoIdGenerator == "uk.ac.ebi.fgpt.urigen.dao.IterativeIdGenerator") {
        autoIdDigitStart = $("#start").val().trim();
        if (autoIdDigitStart =="" || !(/^\d+$/.test(autoIdDigitStart))) {
            $("#start-error").show();
            allSupplied = false;
        }
        autoIdDigitEnd = $("#end").val().trim();
        if (autoIdDigitEnd == "") {
            $("#end-error").show();
            allSupplied = false;
        }
        if (autoIdDigitEnd != -1 && autoIdDigitStart > autoIdDigitEnd) {
            $("#end-error").show();
            allSupplied = false;
        }
    }

    var prefsJson = "{";
    if (prefId > -1) {
        prefsJson = prefsJson +
            "\"preferenceId\":\"" + prefId + "\", ";
    }
    if (lastId > -1) {
        prefsJson = prefsJson +
            "\"lastIdInSequence\":\"" + lastId + "\", ";
    }

    prefsJson = prefsJson + "\"ontologyName\":\"" + ontologyName + "\", " +
        "\"ontologyUri\":\"" + ontologyUri + "\", " +
        "\"ontologyPhysicalUri\":\"" + ontologyPhysicalUri + "\", " +
        "\"checkSource\":\"" + checkSource + "\", " +
        "\"baseUri\":\"" + defaultBaseUri + "\", " +
        "\"separator\":\"" + separator + "\", " +
        "\"prefix\":\"" + prefix + "\", " +
        "\"suffix\":\"" + suffix + "\", " +
        "\"autoIdDigitCount\":\"" + autoIdDigitCount + "\", " +
        "\"autoIdStart\":\"" + autoIdDigitStart + "\", " +
        "\"autoIdEnd\":\"" + autoIdDigitEnd + "\", " +
        "\"autoIdGenerator\":\"" + autoIdGenerator + "\", " +
        "\"allUserRange\": [";

    var rangeJson ="";

    if (autoIdGenerator == "uk.ac.ebi.fgpt.urigen.dao.UserRangeGenerator") {

        var tableBody = document.getElementById("user-range-prefs-table");
        var rows = tableBody.getElementsByTagName("tr");
        var users = new Array(rows.length);
        if (rows.length == 1) {
            $("#missing-user-error").show();
            allSupplied = false;
        }

        for (var i = 1 ; i < rows.length ; i++) {

            if (i>1) {
                rangeJson = rangeJson + ",";
            }
            var cells = [];
            cells = rows[i].getElementsByTagName("td");

            var selectedBox = cells[0].firstChild;
            var uid = selectedBox.value;
            if (users[uid] ==1) {
                allSupplied = false;
                $("#duplicate-user-error").show();
            }
            users[uid] = 1;
            var ur_start = cells[1].firstChild.value;
            var ur_end = cells[2].firstChild.value;
            if (ur_start =="" || !(/^\d+$/.test(ur_start))) {
                $("#user-range-error").show();
                allSupplied = false;
            }
            if (ur_end == "" || (ur_end != -1 && ur_start > ur_end)) {
                $("#user-range-error").show();
                allSupplied = false;
            }

            var lastIdInSequence = cells[5].firstChild.value;
            if (lastIdInSequence == undefined || lastIdInSequence == "") {
                lastIdInSequence = -1;
            }
            rangeJson = rangeJson + "{" +
                "\"userId\":\"" + uid + "\", " +
                "\"autoIdDigitStart\":\"" + ur_start + "\", " +
                "\"autoIdDigitEnd\":\"" + ur_end + "\", " +
                "\"lastIdInSequence\":\"" + lastIdInSequence +

                "\"}";
        }
    }

    prefsJson = prefsJson + rangeJson +
        " ]" +
        "}";

    if (allSupplied) {
        if (prefId == undefined || prefId == "-1") {
            $.ajax({
                type:           'POST',
                url:            'api/preferences?restApiKey=' + getCookie("urigen-manager-cookie"),
                contentType:    'application/json',
                data:           prefsJson,
                processData:    false,
                success:        function (data) {
                    // hide the registration overlay
                    if (data.success == undefined)  {
                        closePrefPanel();
                        requestPrefs();
                    }
                    else {
                        $('#prefs-submit-image').hide();
                        $('#serverside-error').text(data.message);
                        $('#serverside-error').show();
                    }
                },
                error:          function(request, status, error) {
                    // inform user by showing the dialog
                    $('#prefs-submit-image').hide();
                    $('#serverside-error').text(error);
                    $('#serverside-error').show();
                    alert("Oops, failed to create preference! Check server logs");

                }
            });
        }
        else {
            $.ajax({
                type:           'PUT',
                url:            'api/preferences?restApiKey=' + getCookie("urigen-manager-cookie"),
                contentType:    'application/json',
                data:           prefsJson,
                processData:    false,
                success:        function (data) {
                    // hide the registration overlay
                    if (data.success == undefined)  {
                        closePrefPanel();
                        requestPrefs();
                    }
                    else {
                        $('#prefs-submit-image').hide();
                        $('#serverside-error').text(data.message);
                        $('#serverside-error').show();
                    }
                },
                error:          function(request, status, error) {
                    // inform user by showing the dialog
                    $('#prefs-submit-image').hide();
                    $('#serverside-error').text(error);
                    $('#serverside-error').show();
                    alert("Oops, failed to update preference! Check server logs");
                }
            });
        }
    }
    else {
        $('#prefs-submit-image').hide();
    }
}

function prefEdit(id) {

    var priorVisit = getCookie("urigen-manager-cookie");
    if (priorVisit == undefined || priorVisit == "") {
        alert("Sorry, only registered users can access this panel")
    }
    else {
        resetPrefsPanel();
        clearErrorsFromAddPrefsPanel();

        $("#backgroundOverlay").css({
            "opacity": "0.7"
        });
        $("#backgroundOverlay").fadeIn("slow");
        $("#prefs-panel-popup").fadeIn("slow");
        $('#prefs-submit-image').show();

        $.ajax({
            url: 'api/preferences/' + id,
            dataType: 'json',
            success: function(json) {
                setPrefsPanel(json);
                $('#prefs-submit-image').hide();

            },
            error: function() {
                alert("Failed to retrieve preferences");
                closePrefPanel()
            }
        });
    }
}



function addUserRangePrefsRow (rowData) {

    var table = document.getElementById("user-range-prefs-table");

    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);

    var cell1 = row.insertCell(0);
    var element1 = document.createElement("select");

    for (var i = 0; i < users.length && i < 200; i++) {
        var option = document.createElement('option');
        option.value = users[i].id;
        option.text = users[i].userName;

        if (rowData != undefined) {
            if (rowData.userId == users[i].id) {
                option.selected = true;
            }
        }
        element1.appendChild(option);
    }

    cell1.appendChild(element1);

    var cell2 = row.insertCell(1);
    var element2 = document.createElement("input");
    element2.type = "text";
    if (rowData != undefined) {
        element2.value = rowData.autoIdDigitStart;
    }

    cell2.appendChild(element2);

    var cell3 = row.insertCell(2);
    var element3 = document.createElement("input");
    element3.type = "text";
    if (rowData != undefined) {
        element3.value = rowData.autoIdDigitEnd;
    }

    cell3.appendChild(element3);

    var cell4 = row.insertCell(3);
    var element4 = document.createElement("img");
    element4.id = "remove-" + (rowCount);
    element4.src = "images/delete.png";
    element4.onclick = function (){
        removeUserRangeRow(element4);
    };

    cell4.appendChild(element4);

    var cell5 = row.insertCell(4);
    var element5 = document.createElement("p");
    element5.id = "p-error-" + (rowCount);
    cell5.appendChild(element5);

    var cell6 = row.insertCell(5);
    var element6 = document.createElement("input");
    element6.type = "hidden";
    if (rowData != undefined) {
        if (rowData.lastIdInSequence == "") {
            element6.value = -1;
        }
        else {
            element6.value = rowData.lastIdInSequence;
        }
    }
    cell6.appendChild(element6);

}

function removeUserRangeRow(obj) {
    var oRow = obj.parentElement.parentElement;
    //once the row reference is obtained, delete it passing in its rowIndex
    document.all("user-range-prefs-table").deleteRow(oRow.rowIndex);
}

function setPrefsPanel(json) {

    $("#pref-hidden-id").val(json.preferenceId);
    $("#hidden-last-id").val(json.lastIdInSequence);
    $("#pref-name").val(json.ontologyName);
    $('#ontology-uri').val(json.ontologyUri);
    $('#ontology-url').val(json.ontologyPhysicalUri);
    $("#base-uri").val(json.baseUri);
//    document.getElementById("check-source").checked = json.checkSource;

    for (var i = 0 ; i < document.getElementsByName("separator").length; i++) {
        if (document.getElementsByName("separator")[i].value == json.separator) {
            document.getElementsByName("separator")[i].checked = true;
        }
    }

    $('#prefix').val(json.prefix);
    $('#suffix').val(json.suffix);

    $("#digits").val(json.autoIdDigitCount);

    var typeSelBox = document.getElementById("pref-type");

    for (var j = 0 ; j < typeSelBox.options.length; j++) {
        if (typeSelBox.options[j].value == json.autoIDGenerator.id ) {
            typeSelBox.options[j].selected = true;
        }
    }
    selectionHandler();

    $("#start").val(json.autoIdStart);
    $("#end").val(json.autoIdEnd);

    var userRange = json.allUserRange;
    for (var k=0; k<userRange.length;k++) {
        addUserRangePrefsRow(userRange[k]);
    }
}

function resetPrefsPanel() {

    $("#pref-hidden-id").val("-1");
    $("#hidden-last-id").val("-1");

    $("#pref-name").val("");
    $('#ontology-uri').val("");
    $('#ontology-url').val("");
    $("#base-uri").val("");
//    document.getElementById("check-source").checked = true;
    document.getElementsByName("separator")[0].checked = true;

//    $('#pref-type').val();

    $('#prefix').val("");
    $('#suffix').val("");

    $("#digits").val("7");
    $("#start").val("1");
    $("#end").val("-1");

    var table = document.getElementById("user-range-prefs-table");
    var rowCount = table.rows.length;
    for(var i=rowCount -1; i >0; i--) {
        table.deleteRow(i);
    }

}

function selectionHandler() {

    $("#selected-users-start-end-div").hide();
    $("#all-users-start-end-div").hide();
    var selectVal = $("#pref-type :selected").val();

    if (selectVal == "uk.ac.ebi.fgpt.urigen.dao.IterativeIdGenerator") {
        $("#all-users-start-end-div").show();
        $("#start").val("");
        $("#end").val("");
    }
    else if (selectVal == "uk.ac.ebi.fgpt.urigen.dao.UserRangeGenerator") {
        $("#selected-users-start-end-div").show();
        var table = document.getElementById("user-range-prefs-table");
        var rowCount = table.rows.length;
        for(var i=rowCount -1; i >0; i--) {
            table.deleteRow(i);
        }
    }
}

/**
 * DataTables fsStandingRedraw API plugin
 *
 * @author Jonathan Hoguet
 * @param oSettings
 */
$.fn.dataTableExt.oApi.fnStandingRedraw = function(oSettings) {
    //redraw to account for filtering and sorting
    // concept here is that (for client side) there is a row got inserted at the end (for an add)
    // or when a record was modified it could be in the middle of the table
    // that is probably not supposed to be there - due to filtering / sorting
    // so we need to re process filtering and sorting
    // BUT - if it is server side - then this should be handled by the server - so skip this step
    if (oSettings.oFeatures.bServerSide === false) {
        var before = oSettings._iDisplayStart;
        oSettings.oApi._fnReDraw(oSettings);
        //iDisplayStart has been reset to zero - so lets change it back
        oSettings._iDisplayStart = before;
        oSettings.oApi._fnCalculateEnd(oSettings);
    }

    //draw the 'current' page
    oSettings.oApi._fnDraw(oSettings);
};