(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['ElementList.hb'] = template({"1":function(container,depth0,helpers,partials,data) {
    var alias1=container.lambda, alias2=container.escapeExpression;

  return "            <tr>\n                <td>"
    + alias2(alias1((depth0 != null ? depth0.senderId : depth0), depth0))
    + "</td>\n                <td>"
    + alias2(alias1((depth0 != null ? depth0.id : depth0), depth0))
    + "</td>}\n                <td>"
    + alias2(alias1((depth0 != null ? depth0.text : depth0), depth0))
    + "</td>\n                <td><button type=\"button\" class=\"ElementList-upvotebtn btn-primary\" data-value=\""
    + alias2(alias1((depth0 != null ? depth0.id : depth0), depth0))
    + "\">\n                    <span class=\"glyphicon glyphicon-thumbs-up\"></span>\n                    Like</button></td>\n                <td><button class=\"ElementList-downvotebtn btn-danger\" data-value=\""
    + alias2(alias1((depth0 != null ? depth0.id : depth0), depth0))
    + "\">\n                    <span class=\"glyphicon glyphicon-thumbs-down\"></span>\n                    Dislike</button></td>\n                <td><button class=\"ElementList-linkMsgButton\" data-value=\""
    + alias2(alias1((depth0 != null ? depth0.linkMsg : depth0), depth0))
    + "\">\n                    <span class=\"glyphicon glyphicon-thumbs-down\"></span>\n                    Link</button></td>\n                <td><button class=\"ElementList-fileButton\" data-value=\""
    + alias2(alias1((depth0 != null ? depth0.linkMsg : depth0), depth0))
    + "\">\n                    <span class=\"glyphicon glyphicon-thumbs-down\"></span>\n                    File</button></td>\n            </tr>\n";
},"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1;

  return "<div class=\"panel panel-default\" id=\"ElementList\">\n    <div class=\"panel-heading\">\n        <h3 class=\"panel-title\"></h3>\n    </div>\n    <table class=\"table\" id = message-table>\n        <tbody>\n"
    + ((stack1 = helpers.each.call(depth0 != null ? depth0 : (container.nullContext || {}),(depth0 != null ? depth0.mData : depth0),{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data,"loc":{"start":{"line":7,"column":12},"end":{"line":25,"column":21}}})) != null ? stack1 : "")
    + "        </tbody>\n    </table>\n</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['NewEntryForm.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<div id=\"NewEntryForm\">\n            <div id = \"new-entry-input-field\">\n                <label for=\"NewEntryForm-message\">Message</label>\n                <textarea class=\"form-control\" id=\"NewEntryForm-message\"></textarea>\n            </div>\n            <div id =\"new-entry-input-footer\">\n                <button type=\"button\" class=\"btn btn-default\" id=\"NewEntryForm-OK\">OK</button>\n                <button type=\"button\" class=\"btn btn-default\" id=\"NewEntryForm-CLEAR\">CLEAR</button>\n                <input type=\"file\" id=\"docpicker\" accept=\".pdf\">\n            </div>\n</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['Navbar.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<nav class=\"navbar navbar-default\">\n    <div class=\"container-fluid\">\n        <h3> Welcome to the Buzz <span id = \"Navbar-Name\"> </span> </h3>\n        <button type=\"button\" class=\"btn btn-default\" id=\"Navbar-Profile\">My Profile</button>\n\n    </div>\n</nav>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['ValidationForm.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<div id=\"ValidationForm\">  \n\n    <script>\n        public static onSignIn(googleUser) {\n        // Useful data for your client-side scripts:\n        console.log(googleUser);\n        var profile = googleUser.getBasicProfile();\n        console.log(\"ID: \" + profile.getId()); // Don't send this directly to your server!\n        console.log('Full Name: ' + profile.getName());\n        console.log('Given Name: ' + profile.getGivenName());\n        console.log('Family Name: ' + profile.getFamilyName());\n        console.log(\"Image URL: \" + profile.getImageUrl());\n        console.log(\"Email: \" + profile.getEmail());\n\n        // The ID token you need to pass to your backend:\n        var id_token = googleUser.getAuthResponse().id_token;\n        console.log(\"ID Token: \" + id_token);\n    </script>\n    <div id=\"ValidationForm-google\" class=\"g-signin2\" data-onsuccess=\"onSignIn\" data-onfailure=\"onFailure\"\" \n    data-redirecturi=\"https://clowns-who-code.herokuapp.com\"\n    data-theme=\"dark\"></div>\n\n\n\n    <div class=\"form-group\">\n        <label for=\"loginUsername\">Username</label>\n        <input type=\"username\" class=\"form-control\" id=\"ValidationForm-username\" aria-describedby=\"emailHelp\" placeholder=\"Enter username\">\n    </div>\n    <div class=\"form-group\">\n        <label for=\"loginPassword\">Password</label>\n        <input type=\"password\" class=\"form-control\" id=\"ValidationForm-password\" placeholder=\"Password\">\n    </div>\n    <div class=\"form-group\">\n            <label for=\"loginPassword\">Email Address (Only for registration)</label>\n            <input type=\"text\" class=\"form-control\" id=\"ValidationForm-name\" placeholder=\"Email\">\n        </div>\n\n    <button type=\"button\" class=\"btn btn-default\" id=\"ValidationForm-Register\">Register</button>\n    <button type=\"button\" class=\"btn btn-default\" id=\"ValidationForm-Login\">Login</button>\n\n</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['MyProfile.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    var alias1=container.lambda, alias2=container.escapeExpression;

  return "<div class = \"container\" id = \"MyProfile\">\n    <button type=\"button\" class=\"btn btn-default\" id=\"Profile-Back-Button\">Back</button>\n\n    <div class = \"row\" id = \"username-row\"> \n        <div class = \"col-2\">  Username:  </div>\n        <div class = \"col-2\"> "
    + alias2(alias1((depth0 != null ? depth0.username : depth0), depth0))
    + " </div>\n        <div class = \"col-8\"> </div>\n    </div>\n\n    <div class = \"row\" id = \"email-row\"> \n            <div class = \"col-2\">Email:</div>\n            <div class = \"col-2\"> "
    + alias2(alias1((depth0 != null ? depth0.email : depth0), depth0))
    + " </div>\n            <div class = \"col-8\"> </div>\n    </div>\n\n    <div class = \"row\" id = \"comment-row\"> \n            <div class = \"col-2\">Comment:</div>\n            <div class = \"col-2\"> <input id=\"comment-field\" value = \""
    + alias2(alias1((depth0 != null ? depth0.comment : depth0), depth0))
    + "\"> </input> </div>\n            <div class = \"col-2\"> <button> Change Comment </button> </div>\n            <div class = \"col-6\"> </div>\n    </div>\n\n    <div class = \"row\" id = \"password-row\"> \n            <div class = \"col-2\">Password:</div>\n            <div class = \"col-2\"> \n                <input id=\"password-field\" placeholder = \"Enter new password\"> </input>\n                <input id=\"password-confirm-field\" placeholder = \"Confirm Password\"> </input>\n            \n            </div>\n            <div class = \"col-2\"> <button> Change Password </button> </div>\n            <div class = \"col-6\"> </div>\n    </div>\n\n</div>\n";
},"useData":true});
})();
