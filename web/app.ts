/// <reference path="ts/NewEntryForm.ts"/>
/// <reference path="ts/ElementList.ts"/>
/// <reference path="ts/Navbar.ts"/>

// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
let $: any;

/// This constant indicates the path to our backend server
const backendUrl = "https://clowns-who-code.herokuapp.com";

// Prevent compiler errors when using Handlebars
let Handlebars: any;

let ID: any;
let userName: any;

// Run some configuration code when the web page loads
$.ajax({
    type: "POST",
    url: backendUrl + "/user",
    dataType: "json",
    data: JSON.stringify({ name: "Nico", password: "Beard"}),
    success: function(data){
        ID = data.userId;
        userName = data.name;
    }
});
$(document).ready(function () {
    Navbar.refresh();
    NewEntryForm.refresh();
    ElementList.refresh();
});