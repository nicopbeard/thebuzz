/// <reference path="ts/NewEntryForm.ts"/>
/// <reference path="ts/ElementList.ts"/>
/// <reference path="ts/Navbar.ts"/>
/// <reference path="ts/ValidationForm.ts"/>
/// <reference path="ts/MyProfile.ts"/>

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

let validated:boolean = false; 

// Run some configuration code when the web page loads
$(document).ready(function () {
    ValidationForm.refresh();
});