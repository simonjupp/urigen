/**
 * Created by IntelliJ IDEA.
 * User: jupp
 * Date: 05/01/2012
 * Time: 11:02
 */


var usersURL = "api/users";

function autoUserLogin() {

    // do a cleanup of existing forms
    clearLoginForms()

    // see if there is a cookie

    $("#tabs").hide();

    var priorVisit = getCookie("urigen-manager-cookie");

    $.getJSON(usersURL, function(json) {

        if (json == undefined) {
            Alert.error("Can't access URI manager API! Please contact an admin.");
        }
        else if (json.length == 0) {
            $("#first-login").show();
        }
        else {
            // run as guest user
            if (priorVisit == undefined || priorVisit == "") {

                $("#user-full-name").html("Guest");
                $("#first-login").hide();
                $("#user-greeting").hide();
                $("#tabs").show();
                $("#guest-greeting").show();

            }
            else {

                // log in by reading cookie and getting the user
                $.getJSON("api/users/query?restApiKey=" + priorVisit, function(json) {

                    if (json == undefined) {
                        $("#user-full-name").html("Guest");
                        $("#first-login").hide();
                        $("#user-greeting").hide();
                        $("#guest-greeting").show();
                        $("#tabs").show();

                    }
                    else {
                        // init
                        finalizeUserLogin(json);
                    }
                });
            }
        }
    });
}

function registerNewUserOnEnter(e) {
    var key = e.keyCode || e.which;
    if (key == 13) {
        registerNewUser();
    }
}

function userLogin() {

    navigator.id.get(function(assertion) {
        if (assertion) {
            // This code will be invoked once the user has successfully
            // selected an email address they control to sign in with.

            $.ajax({
                url: 'api/users/query?browserid=' + assertion,
                dataType: 'json',
                success: function(jsonData) {
                    finalizeUserLogin(jsonData)
                }                ,
                error: function() {
                    alert("unkown user, guest login")

                }
            });

        } else {
            // something went wrong!  the user isn't logged in.
            alert("unkown user, guest login")
        }
    });



}

function firstTimeAdminCreation () {

    $("#user-greeting").hide();
    $("#backgroundOverlay").css({
        "opacity": "0.7"
    });
    $("#backgroundOverlay").fadeIn("slow");
    $("#register-user-popup").fadeIn("slow");

    $("#first-login").hide();

//    $("#register-username").val("admin");
//    $("#register-username").attr("disabled", true);

    $("#register-admin").attr('checked', true);
    $("#register-admin").val(true);
    $("#register-admin").attr("disabled", true);
}


function registerNewUser() {

    var username = $("#register-username").val();
    var email = $("#register-email").val();
    var admin = $("#register-admin").val();

    var allSupplied = true;

    if (username == undefined || username == "") {
        allSupplied = false;
        $("#username-missing").show();
    }
    else {
        $("#username-missing").hide();
    }
    if (email == undefined || email == "") {
        allSupplied = false;
        $("#email-missing").show();
    }
    else {
        $("#email-missing").hide();
    }

    // all required data supplied?
    if (!allSupplied) {
        $("#missing-data-error-message").show();
    }
    else {
        $("#username-missing").hide();
        $("#email-missing").hide();
        $("#missing-data-error-message").hide();

        var emailStr = encodeURIComponent(email);
        // send email with ajax request, but register error handler to detect failure to communicate with server
        $.ajax({
            url: 'api/users/query?email=' + emailStr,
            dataType: 'json',
            success: createUserIfNoneExists

        });
    }
}



function createUserIfNoneExists(userJson) {
    if (userJson == undefined) {
        // ok to create
        var jsonString = "{" +
            "\"userName\":\"" + $("#register-username").val() + "\", " +
            "\"email\":\"" + $("#register-email").val() + "\", " +
            "\"admin\":\"" + $("#register-admin").val() + "\"}";

        $.ajax({
            type:           'POST',
            url:            'api/users?restApiKey=' + getCookie("urigen-manager-cookie"),
            contentType:    'application/json',
            data:           jsonString,
            processData:    false,
            success:        function (data) {

                // hide the registration overlay
                clearLoginForms()

                // if admin creation, then also log him in
//                if (data.userName == "admin") {
//                    finalizeUserLogin(data);
//                }
                // and update users list

                requestUsers();

            },
            error:          function(request, status, error) {
                // inform user by showing the dialog
                alert("User with that username already exists!");
            }
        });
    }
    else {
        // already newUriExists
        $("#email-missing").show();
        $("#email-newUriExists-error-message").show();
    }
}


function finalizeUserLogin(userJson) {
    // display the users name at the top
    $("#backgroundOverlay").css({
        "opacity": "0.7"
    });
    $("#backgroundOverlay").fadeOut("slow");
    $("#login-user-popup").fadeOut("slow");
    $("#user-full-name").html(userJson.userName);
    $("#first-login").hide();
    $("#guest-greeting").hide();
    $("#user-greeting").show();
    $("#tabs").show();

    // set rest api cookie
    var now = new Date();
    now.setFullYear(now.getFullYear() + 1);
    setCookie("urigen-manager-cookie", userJson.apiKey, now);
}



/**
 * Gets a cookie set on the users system with the supplied name
 *
 * @param cookieName the name of the cookie
 */
function getCookie(cookieName) {
    if (document.cookie.length > 0) {
        c_start = document.cookie.indexOf(cookieName + "=");
        if (c_start != -1) {
            c_start = c_start + cookieName.length + 1;
            c_end = document.cookie.indexOf(";", c_start);
            if (c_end == -1) {
                c_end = document.cookie.length;
            }
            return document.cookie.substring(c_start, c_end);
        }
    }
    return undefined;
}

function setCookie(cookieName, cookieValue, expires) {
    document.cookie =
        cookieName + "=" + cookieValue + ";" +
            "expires=" + expires + ";";
}


/**
 * Removes a cookie that has previously been set on the users system with the supplied name
 *
 * @param cookieName the name of the cookie to remove
 */
function removeCookie(cookieName) {
    if (getCookie(cookieName) != undefined) {
        setCookie(cookieName, "", -1)

    }
    return false;
}


/**
 * Displays an error box if we got a failure response from a pipelines request from the server.
 */
function serverCommunicationFail() {
    // couldn't find the server, so all ajax content will be empty.  To stop it looking ugly and show whats wrong,
    // show an error instead of empty pipeline dropdowns
    alert("Failed to communicate with server.  Some details may be missing from the interface.  Please notify the system administrator.");
    autoUserLogin();
}


function clearLoginForms () {

    $("#backgroundOverlay").fadeOut("slow");
    $("#register-user-popup").fadeOut("slow");
    $("#register-username").val("");
    $("#register-email").val("");
    $("#register-admin").attr("checked", false);
    $("#register-admin").val(false);
    $("#username-missing").hide();
    $("#email-missing").hide();
    $("#missing-data-error-message").hide();

}
