"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
var NewEntryForm = /** @class */ (function () {
    function NewEntryForm() {
    }
    /**
     * Initialize the NewEntryForm by creating its element in the DOM and
     * configuring its buttons.  This needs to be called from any public static
     * method, to ensure that the Singleton is initialized before use
     */
    NewEntryForm.init = function () {
        if (!NewEntryForm.isInit) {
            $("#input-container").append(Handlebars.templates[NewEntryForm.NAME + ".hb"]());
            $("#" + NewEntryForm.NAME + "-OK").click(NewEntryForm.submitForm);
            $("#" + NewEntryForm.NAME + "-CLEAR").click(NewEntryForm.clear);
            NewEntryForm.isInit = true;
        }
    };
    /**
     * Refresh() doesn't really have much meaning, but just like in sNavbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    NewEntryForm.refresh = function () {
        NewEntryForm.init();
    };
    /**
     * Hide the NewEntryForm.  Be sure to clear its fields first
     */
    NewEntryForm.hide = function () {
        $("#input-container").hide();
    };
    NewEntryForm.show = function () {
        $("#input-container").show();
    };
    NewEntryForm.clear = function () {
        $("#NewEntryForm-message").val("");
    };
    /**
     * Send data to submit the form only if the fields are both valid.
     * Immediately hide the form when we send data, so that the user knows that
     * their click was received.
     */
    NewEntryForm.submitForm = function () {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        var msg = "" + $("#" + NewEntryForm.NAME + "-message").val();
        var id = $(this).data("value");
        var lat;
        var long;
        navigator.geolocation.getCurrentPosition(function (position) {
            lat = position.coords.latitude;
            long = position.coords.longitude;
            console.log(id);
            console.log(lat);
            console.log(long);
            //formData.append('senderId', id);
            //formData.append('text', msg);
            //formData.append('nUpVotes', '0');
            //formData.append('nDownVotes', '0');
            if (msg === "") {
                window.alert("Error: title or message is not valid");
                return;
            }
            $("#NewEntryForm-message").val("");
            // set up an AJAX post.  When the server replies, the result will go to
            // onSubmitResponse
            var fileData = null;
            if (document.getElementById('docpicker').files[0] != null) {
                var reader = new FileReader();
                reader.readAsDataURL(document.getElementById('docpicker').files[0]);
                reader.onload = function () {
                    console.log("The data in the file is:");
                    //                console.log(reader.result);
                    $.ajax({
                        type: "POST",
                        url: backendUrl + "/messages",
                        dataType: "json",
                        data: JSON.stringify({
                            senderId: '8',
                            text: msg,
                            nUpVotes: 0,
                            nDownVotes: 0,
                            fileName: document.getElementById('docpicker').value,
                            file: reader.result,
                            latitude: lat,
                            longitude: long
                        }),
                        processData: false,
                        success: NewEntryForm.onSubmitResponse
                    });
                    console.log(JSON.stringify({
                        senderId: '7',
                        text: msg,
                        nUpVotes: 0,
                        nDownVotes: 0,
                        fileName: document.getElementById('docpicker').value,
                        file: reader.result,
                        latitude: lat,
                        longitude: long
                    }));
                    document.getElementById('docpicker').value = '';
                };
            }
            else {
                $.ajax({
                    type: "POST",
                    url: backendUrl + "/messages",
                    dataType: "json",
                    data: JSON.stringify({
                        senderId: '8',
                        text: msg,
                        nUpVotes: 0,
                        nDownVotes: 0,
                        file: 'null',
                        latitude: lat,
                        longitude: long
                    }),
                    processData: false,
                    success: NewEntryForm.onSubmitResponse
                });
                console.log(JSON.stringify({
                    senderId: '8',
                    text: msg,
                    nUpVotes: 0,
                    nDownVotes: 0,
                    latitude: lat,
                    longitude: long
                }));
            }
        }); // end of lambda for geolocation
    };
    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a
     * result.
     *
     * @param data The object returned by the server
     */
    NewEntryForm.onSubmitResponse = function (data) {
        // If we get an "ok" message, clear the form and refresh the main 
        // listing of messages
        if (data.mStatus === "ok") {
            ElementList.refresh();
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    };
    /**
     * The name of the DOM entry associated with NewEntryForm
     */
    NewEntryForm.NAME = "NewEntryForm";
    /**
     * Track if the Singleton has been initialized
     */
    NewEntryForm.isInit = false;
    return NewEntryForm;
}());
/**
 * The ElementList Singleton provides a way of displaying all of the data
 * stored on the server as an HTML table.
 */
var ElementList = /** @class */ (function () {
    function ElementList() {
    }
    /**
     * Initialize the ElementList singleton.
     * This needs to be called from any public static method, to ensure that the
     * Singleton is initialized before use.
     */
    ElementList.init = function () {
        if (!ElementList.isInit) {
            ElementList.isInit = true;
        }
    };
    /**
 * refresh() is the public method for updating the ElementList
 */
    ElementList.refresh = function () {
        // Make sure the singleton is initialized
        ElementList.init();
        // Issue a GET, and then pass the result to update()
        $.ajax({
            type: "GET",
            url: backendUrl + "/messages",
            dataType: "json",
            success: function (data) {
                ElementList.update(data);
            }
        });
    };
    ElementList.hide = function () {
        $("#message-container").hide();
    };
    ElementList.show = function () {
        $("#message-container").show();
    };
    /**
     * update() is the private method used by refresh() to update the
     * ElementList
     */
    ElementList.update = function (data) {
        // Remove the table of data, if it exists
        console.log("Data is");
        console.log(data);
        for (var i = 0; i < data.mData.length; i++) {
            var text = data.mData[i].text;
            ElementList.map[data.mData[i].id] = data.mData[i];
            var number = -1;
            for (var j = 0; j < text.length; j++) {
                data.mData[i].linkMsg = number;
                if (text[j] == '/') {
                    console.log("Found forward slash");
                    number = 0;
                    for (var k = j + 1; k < text.length; k++) {
                        if (text[k] >= '0' && text[k] <= '9') {
                            number *= 10;
                            number += parseInt(text[k]);
                            console.log("number is: " + number);
                        }
                        else {
                            break;
                        }
                    }
                    data.mData[i].linkMsg = number;
                    console.log("Found hashtag -- number is " + number);
                }
            }
        }
        $("#" + ElementList.NAME).remove();
        // Use a template to re-generate the table, and then insert it
        $("#message-container").append(Handlebars.templates[ElementList.NAME + ".hb"](data));
        // Find all of the Upvote buttons, and set their behavior
        $("." + ElementList.NAME + "-upvotebtn").click(ElementList.clickUpVote);
        // Find all of the Downvote buttons, and set their behavior
        $("." + ElementList.NAME + "-downvotebtn").click(ElementList.clickDownVote);
        $("." + ElementList.NAME + "-linkMsgButton").click(ElementList.clickMsgLink);
        $("." + ElementList.NAME + "-fileButton").click(ElementList.clickFile);
    };
    /**
     * buttons() creates 'edit' and 'delete' buttons for an id, and puts them in
     * a TD
     */
    ElementList.buttons = function (id) {
        return "<td><button class='" + ElementList.NAME +
            "-upvotebtn' data-value='" + id + "'>Like</button></td>" +
            "<td><button class='" + ElementList.NAME +
            "-downvotebtn' data-value='" + id + "'>Dislike</button></td>";
    };
    /**
     * clickUpvote is the code we run in response to a click of a upvote button
    */
    ElementList.clickUpVote = function () {
        var msgId = $(this).data("value");
        $.ajax({
            type: "PUT",
            url: backendUrl + "/like",
            dataType: "json",
            data: JSON.stringify({
                userId: ID,
                msgId: msgId
            }),
            success: ElementList.refresh,
            error: function (e) {
                console.info(e);
            }
        });
    };
    /**
     * clickDownVote is the code we run in response to a click of a downvote button
    */
    ElementList.clickDownVote = function () {
        return __awaiter(this, void 0, void 0, function () {
            var msgId;
            return __generator(this, function (_a) {
                msgId = $(this).data("value");
                $.ajax({
                    type: "PUT",
                    url: backendUrl + "/dislike",
                    dataType: "json",
                    data: JSON.stringify({
                        userId: ID,
                        msgId: msgId
                    }),
                    success: ElementList.refresh,
                    error: function (e) {
                        console.info(e);
                    }
                });
                return [2 /*return*/];
            });
        });
    };
    ElementList.clickMsgLink = function () {
        console.log("Length of map is: " + Object.keys(ElementList.map).length);
        var msgId = $(this).data("value");
        console.log(JSON.stringify(ElementList.map[msgId]));
        if (ElementList.map[msgId] == null) {
            window.alert("No message linked in this message.");
        }
        else {
            window.alert(JSON.stringify(ElementList.map[msgId]));
        }
    };
    ElementList.clickFile = function () {
        console.log("in click file");
    };
    /**
     * The name of the DOM entry associated with ElementList
     */
    ElementList.NAME = "ElementList";
    /**
     * Track if the Singleton has been initialized
     */
    ElementList.isInit = false;
    ElementList.map = {};
    return ElementList;
}());
/**
 * The Navbar Singleton is the navigation bar at the top of the page.  Through
 * its HTML, it is designed so that clicking the "brand" part will refresh the
 * page.  Apart from that, it has an "add" button, which forwards to
 * NewEntryForm
 */
var Navbar = /** @class */ (function () {
    function Navbar() {
    }
    /**
     * Initialize the Navbar Singleton by creating its element in the DOM and
     * configuring its button.  This needs to be called from any public static
     * method, to ensure that the Singleton is initialized before use.
     */
    Navbar.init = function () {
        if (!Navbar.isInit) {
            $("#navbar-container").append(Handlebars.templates[Navbar.NAME + ".hb"]());
            $("#" + Navbar.NAME + "-Profile").click(Navbar.viewProfile);
            Navbar.isInit = true;
        }
    };
    /**
     * Refresh() doesn't really have much meaning for the navbar, but we'd
     * rather not have anyone call init(), so we'll have this as a stub that
     * can be called during front-end initialization to ensure the navbar
     * is configured.
     */
    Navbar.viewProfile = function () {
        MyProfile.refresh();
        ElementList.hide();
        NewEntryForm.hide();
    };
    Navbar.refresh = function () {
        Navbar.init();
    };
    Navbar.welcomeUser = function (name) {
        Navbar.init();
        $("#" + Navbar.NAME + "-Name").text(name);
    };
    /**
     * Track if the Singleton has been initialized
     */
    Navbar.isInit = false;
    /**
     * The name of the DOM entry associated with Navbar
     */
    Navbar.NAME = "Navbar";
    return Navbar;
}());
/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
var ValidationForm = /** @class */ (function () {
    function ValidationForm() {
    }
    /**
     * Initialize the NewEntryForm by creating its element in the DOM and
     * configuring its buttons.  This needs to be called from any public static
     * method, to ensure that the Singleton is initialized before use
     */
    ValidationForm.init = function () {
        if (!ValidationForm.isInit) {
            $("#login-container").append(Handlebars.templates[ValidationForm.NAME + ".hb"]());
            $("#" + ValidationForm.NAME + "-Register").click(ValidationForm.register);
            $("#" + ValidationForm.NAME + "-Login").click(ValidationForm.login);
            //$("#" + ValidationForm.NAME + "-google").success(ValidationForm.onSignIn);
            ValidationForm.isInit = true;
        }
    };
    /**
     * Refresh() doesn't really have much meaning, but just like in sNavbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    ValidationForm.refresh = function () {
        ValidationForm.init();
    };
    /**
     * Hide the NewEntryForm.  Be sure to clear its fields first
     */
    ValidationForm.hide = function () {
        $("#" + ValidationForm.NAME + "-username").val("");
        $("#" + ValidationForm.NAME + "-password").val("");
        $("#" + ValidationForm.NAME).hide();
    };
    /**
     * Show the NewEntryForm.  Be sure to clear its fields, because there are
     * ways of making a Bootstrap modal disapper without clicking Close, and
     * we haven't set up the hooks to clear the fields on the events associated
     * with those ways of making the modal disappear.
     */
    ValidationForm.show = function () {
        $("#" + ValidationForm.NAME + "-username").val("");
        $("#" + ValidationForm.NAME + "-password").val("");
        $("#" + ValidationForm.NAME).show();
    };
    /**
     * Send data to submit the form only if the fields are both valid.
     * Immediately hide the form when we send data, so that the user knows that
     * their click was received.
     */
    ValidationForm.register = function () {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        var username = $("#" + ValidationForm.NAME + "-username").val();
        var password = $('#' + ValidationForm.NAME + "-password").val();
        if (username === "" || password === "") {
            window.alert("Error: Username or Password field incomplete");
            return;
        }
        $("#" + ValidationForm.NAME + "-username").val("");
        $("#" + ValidationForm.NAME + "-password").val("");
        $("#" + ValidationForm.NAME + "-name").val("");
        //ValidationForm.hide();
        // Unknown call here, have to wait for backend 
        // $.ajax({
        //     type: "POST",
        //     url: backendUrl + "/messages",
        //     dataType: "json",
        //     data: JSON.stringify({ 
        //                             senderId: id, 
        //                             text: msg, 
        //                             nUpVotes: 0,
        //                             nDownVotes: 0}
        //                         ),
        //     success: ValidationForm.onSubmitResponse
        // });
        var data = {
            userid: "Test_Register123",
            session_token: "abc123zyz"
        };
        ValidationForm.onSubmitResponse(data);
    };
    ValidationForm.login = function () {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        var username = $("#" + ValidationForm.NAME + "-username").val();
        var password = $('#' + ValidationForm.NAME + "-password").val();
        if (username === "" || password === "") {
            window.alert("Error: Username or Password field incomplete");
            return;
        }
        $("#" + ValidationForm.NAME + "-username").val("");
        $("#" + ValidationForm.NAME + "-password").val("");
        //ValidationForm.hide();
        // Unknown call here, have to wait for backend 
        // $.ajax({
        //     type: "POST",
        //     url: backendUrl + "/messages",
        //     dataType: "json",
        //     data: JSON.stringify({ 
        //                             senderId: id, 
        //                             text: msg, 
        //                             nUpVotes: 0,
        //                             nDownVotes: 0}
        //                         ),
        //     success: ValidationForm.onSubmitResponse
        // });
        var data = {
            userid: "Test_Login123",
            session_token: "abc123zyz"
        };
        ValidationForm.onSubmitResponse(data);
    };
    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a
     * result.
     *
     * @param data The object returned by the server
     */
    ValidationForm.onSubmitResponse = function (data) {
        var sucess = true;
        // If we get an "ok" message, clear the form and refresh the main 
        // listing of messages
        // if (data.mStatus === "ok") {
        //     ElementList.refresh();
        // }
        // // Handle explicit errors with a detailed popup message
        // else if (data.mStatus === "error") {
        //     window.alert("The server replied with an error:\n" + data.mMessage);
        // }
        // // Handle other errors with a less-detailed popup message
        // else {
        //     window.alert("Unspecified error");
        // }
        if (sucess) {
            ValidationForm.hide();
            ElementList.refresh();
            NewEntryForm.refresh();
            Navbar.welcomeUser("Nicoooo");
            console.log("Sucessfully Verified " + data.userid + " in...\n Current Session Token: " + data.session_token);
        }
    };
    //placeholder for google login functions until i figure out wtf is going on
    // If there's an access token, try an API request.
    // Otherwise, start OAuth 2.0 flow.
    ValidationForm.trySampleRequest = function () {
        console.log("Hello");
        var params = JSON.parse(localStorage.getItem('oauth2-test-params'));
        if (params && params['access_token']) {
            var xhr = new XMLHttpRequest();
            xhr.open('GET', 'https://www.googleapis.com/drive/v3/about?fields=user&' +
                'access_token=' + params['access_token']);
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    console.log(xhr.response);
                }
                else if (xhr.readyState === 4 && xhr.status === 401) {
                    // Token invalid, so prompt for user permission.
                    oauth2SignIn();
                }
            };
            xhr.send(null);
        }
        else {
            oauth2SignIn();
        }
    };
    /*
     * Create form to request access token from Google's OAuth 2.0 server.
     */
    ValidationForm.oauth2SignIn = function () {
        // Google's OAuth 2.0 endpoint for requesting an access token
        var oauth2Endpoint = 'https://accounts.google.com/o/oauth2/v2/auth';
        // Create element to open OAuth 2.0 endpoint in new window.
        var form = document.createElement('form');
        form.setAttribute('method', 'GET'); // Send as a GET request.
        form.setAttribute('action', oauth2Endpoint);
        // Parameters to pass to OAuth 2.0 endpoint.
        var params = {
            'client_id': '755594120508-vajdss1hrkqi335sukhur5qaa70s5jji.apps.googleusercontent.com',
            'redirect_uri': 'https://clowns-who-code.herokuapp.com',
            'scope': 'https://www.googleapis.com/auth/drive.metadata.readonly',
            'state': 'try_sample_request',
            'include_granted_scopes': 'true',
            'response_type': 'token'
        };
        // Add form parameters as hidden input values.
        for (var p in params) {
            var input = document.createElement('input');
            input.setAttribute('type', 'hidden');
            input.setAttribute('name', p);
            input.setAttribute('value', params[p]);
            form.appendChild(input);
        }
        // Add form to page and submit it to open the OAuth 2.0 endpoint.
        document.body.appendChild(form);
        form.submit();
    };
    ValidationForm.onSignIn = function (googleUser) {
        // Useful data for your client-side scripts:
        console.log(googleUser);
        var profile = googleUser.getBasicProfile();
        console.log("ID: " + profile.getId()); // Don't send this directly to your server!
        console.log('Full Name: ' + profile.getName());
        console.log('Given Name: ' + profile.getGivenName());
        console.log('Family Name: ' + profile.getFamilyName());
        console.log("Image URL: " + profile.getImageUrl());
        console.log("Email: " + profile.getEmail());
        // The ID token you need to pass to your backend:
        var id_token = googleUser.getAuthResponse().id_token;
        console.log("ID Token: " + id_token);
    };
    ValidationForm.onFailure = function () { console.error('Sign in has failed!'); };
    ValidationForm.NAME = "ValidationForm";
    /**
     * Track if the Singleton has been initialized
     */
    ValidationForm.isInit = false;
    return ValidationForm;
}());
/**
 * The ElementList Singleton provides a way of displaying all of the data
 * stored on the server as an HTML table.
 */
var MyProfile = /** @class */ (function () {
    function MyProfile() {
    }
    /**
     * Initialize the ElementList singleton.
     * This needs to be called from any public static method, to ensure that the
     * Singleton is initialized before use.
     */
    MyProfile.init = function () {
        if (!MyProfile.isInit) {
            MyProfile.isInit = true;
            $("#profile-container").append(Handlebars.templates[MyProfile.NAME + ".hb"]());
            $("#Profile-Back-Button").click(MyProfile.returnToMain);
        }
    };
    /**
 * refresh() is the public method for updating the ElementList
 */
    MyProfile.refresh = function () {
        // Make sure the singleton is initialized
        MyProfile.init();
        //AJAX CALL TO GET MY USERDATA
        // $.ajax({
        //     type: "GET",
        //     url: backendUrl + "/messages",
        //     dataType: "json",
        //     success: function(data) {
        //         MyProfile.update(data);
        //     }
        // });
        var data = {
            username: "Test_Username",
            email: "Test_Email",
            comment: "Test Comment"
        };
        $("#" + MyProfile.NAME).remove();
        $("#profile-container").append(Handlebars.templates[MyProfile.NAME + ".hb"](data));
        $("#Profile-Back-Button").click(MyProfile.returnToMain);
    };
    MyProfile.hide = function () {
        $("#" + MyProfile.NAME).hide();
    };
    /**
     * update() is the private method used by refresh() to update the
     * ElementList
     */
    MyProfile.update = function (data) {
        // Remove the table of data, if it exists
        $("#" + MyProfile.NAME).remove();
        // Use a template to re-generate the table, and then insert it
        $("#profile-container").append(Handlebars.templates[MyProfile.NAME + ".hb"](data));
        // Find all of the Upvote buttons, and set their behavior
    };
    MyProfile.returnToMain = function () {
        MyProfile.hide();
        ElementList.show();
        NewEntryForm.show();
    };
    /**
     * The name of the DOM entry associated with ElementList
     */
    MyProfile.NAME = "MyProfile";
    /**
     * Track if the Singleton has been initialized
     */
    MyProfile.isInit = false;
    return MyProfile;
}());
/// <reference path="ts/NewEntryForm.ts"/>
/// <reference path="ts/ElementList.ts"/>
/// <reference path="ts/Navbar.ts"/>
/// <reference path="ts/ValidationForm.ts"/>
/// <reference path="ts/MyProfile.ts"/>
// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $;
/// This constant indicates the path to our backend server
var backendUrl = "https://clowns-who-code.herokuapp.com";
// Prevent compiler errors when using Handlebars
var Handlebars;
var ID;
var userName;
var validated = false;
// Run some configuration code when the web page loads
$(document).ready(function () {
    ValidationForm.refresh();
});
