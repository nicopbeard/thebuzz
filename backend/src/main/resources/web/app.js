"use strict";
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
            $("body").append(Handlebars.templates[NewEntryForm.NAME + ".hb"]());
            $("#" + NewEntryForm.NAME + "-OK").click(NewEntryForm.submitForm);
            $("#" + NewEntryForm.NAME + "-Close").click(NewEntryForm.hide);
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
        $("#" + NewEntryForm.NAME + "-title").val("");
        $("#" + NewEntryForm.NAME + "-message").val("");
        $("#" + NewEntryForm.NAME).modal("hide");
    };
    /**
     * Show the NewEntryForm.  Be sure to clear its fields, because there are
     * ways of making a Bootstrap modal disapper without clicking Close, and
     * we haven't set up the hooks to clear the fields on the events associated
     * with those ways of making the modal disappear.
     */
    NewEntryForm.show = function () {
        $("#" + NewEntryForm.NAME + "-title").val("");
        $("#" + NewEntryForm.NAME + "-message").val("");
        $("#" + NewEntryForm.NAME).modal("show");
    };
    /**
     * Send data to submit the form only if the fields are both valid.
     * Immediately hide the form when we send data, so that the user knows that
     * their click was received.
     */
    NewEntryForm.submitForm = function () {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        var title = "" + $("#" + NewEntryForm.NAME + "-title").val();
        var msg = "" + $("#" + NewEntryForm.NAME + "-message").val();
        if (title === "" || msg === "") {
            window.alert("Error: title or message is not valid");
            return;
        }
        NewEntryForm.hide();
        // set up an AJAX post.  When the server replies, the result will go to
        // onSubmitResponse
        $.ajax({
            type: "POST",
            url: "/messages",
            dataType: "json",
            data: JSON.stringify({ mTitle: title, mMessage: msg }),
            success: NewEntryForm.onSubmitResponse
        });
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
            url: "/messages",
            dataType: "json",
            success: ElementList.update
        });
    };
    /**
     * update() is the private method used by refresh() to update the
     * ElementList
     */
    ElementList.update = function (data) {
        // Remove the table of data, if it exists
        $("#" + ElementList.NAME).remove();
        // Use a template to re-generate the table, and then insert it
        $("body").append(Handlebars.templates[ElementList.NAME + ".hb"](data));
        // Find all of the Upvote buttons, and set their behavior
        $("." + ElementList.NAME + "-upvotebtn").click(ElementList.clickUpVote);
        // Find all of the Upvote buttons, and set their behavior
        $("." + ElementList.NAME + "-downvotebtn").click(ElementList.clickDownVote);
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
        // as in clickDelete, we need the ID of the row
        var id = $(this).data("value");
        $.ajax({
            type: "POST",
            url: "/messages/" + id,
            dataType: "json",
            data: JSON.stringify({ mId: id }),
            success: ElementList.refresh
        });
    };
    /**
     * clickDownVote is the code we run in response to a click of a downvote button
    */
    ElementList.clickDownVote = function () {
        // as in clickDelete, we need the ID of the row
        var id = $(this).data("value");
        $.ajax({
            type: "POST",
            url: "/messages/" + id,
            dataType: "json",
            data: JSON.stringify({ mId: id }),
            success: ElementList.refresh
        });
    };
    /**
     * The name of the DOM entry associated with ElementList
     */
    ElementList.NAME = "ElementList";
    /**
     * Track if the Singleton has been initialized
     */
    ElementList.isInit = false;
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
            $("body").prepend(Handlebars.templates[Navbar.NAME + ".hb"]());
            $("#" + Navbar.NAME + "-add").click(NewEntryForm.show);
            Navbar.isInit = true;
        }
    };
    /**
     * Refresh() doesn't really have much meaning for the navbar, but we'd
     * rather not have anyone call init(), so we'll have this as a stub that
     * can be called during front-end initialization to ensure the navbar
     * is configured.
     */
    Navbar.refresh = function () {
        Navbar.init();
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
/// <reference path="ts/NewEntryForm.ts"/>
/// <reference path="ts/ElementList.ts"/>
/// <reference path="ts/Navbar.ts"/>
// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $;
// Prevent compiler errors when using Handlebars
var Handlebars;
// Run some configuration code when the web page loads
$(document).ready(function () {
    Navbar.refresh();
    NewEntryForm.refresh();
    ElementList.refresh();
});
