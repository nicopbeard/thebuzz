(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['ElementList.hb'] = template({"1":function(container,depth0,helpers,partials,data) {
    var alias1=container.propertyIsEnumerable, alias2=container.lambda, alias3=container.escapeExpression;

<<<<<<< HEAD
  return "            <tr>\r\n                <td class = \"ElementList-user-profile-button\" data-id = \""
    + alias3(alias2((depth0 != null ? depth0.senderId : depth0), depth0))
    + "\">"
=======
  return "            <tr>\n                <td>"
>>>>>>> parent of 07726df... Fixed user profiles, waiting for API call to check
    + alias3(alias2((depth0 != null ? depth0.senderId : depth0), depth0))
    + "</td>\r\n                <td>"
    + alias3(alias2((depth0 != null ? depth0.text : depth0), depth0))
<<<<<<< HEAD
    + "</td>\r\n                <td>\r\n                    <button type=\"button\" class=\"ElementList-upvotebtn btn-primary\" data-value=\""
    + alias3(alias2((depth0 != null ? depth0.id : depth0), depth0))
    + "\">\r\n                        <span class=\"glyphicon glyphicon-thumbs-up\"></span>\r\n                        Like\r\n                    </button>\r\n                </td>\r\n                <td>\r\n                    <button class=\"ElementList-downvotebtn btn-danger\" data-value=\""
    + alias3(alias2((depth0 != null ? depth0.id : depth0), depth0))
    + "\">\r\n                        <span class=\"glyphicon glyphicon-thumbs-down\"></span>\r\n                        Dislike\r\n                    </button>\r\n                </td>\r\n            </tr>\r\n";
=======
    + "</td>\n                <td><button type=\"button\" class=\"ElementList-upvotebtn btn-primary\" data-value=\""
    + alias3(alias2((depth0 != null ? depth0.id : depth0), depth0))
    + "\">\n                    <span class=\"glyphicon glyphicon-thumbs-up\"></span>\n                    Like</button></td>\n                <td><button class=\"ElementList-downvotebtn btn-danger\" data-value=\""
    + alias3(alias2((depth0 != null ? depth0.id : depth0), depth0))
    + "\">\n                    <span class=\"glyphicon glyphicon-thumbs-down\"></span>\n                    Dislike</button></td>\n            </tr>\n";
>>>>>>> parent of 07726df... Fixed user profiles, waiting for API call to check
},"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1, alias1=container.propertyIsEnumerable;

  return "<div class=\"panel panel-default\" id=\"ElementList\">\r\n    <div class=\"panel-heading\">\r\n        <h3 class=\"panel-title\"></h3>\r\n    </div>\r\n    <table class=\"table\" id = message-table>\r\n        <tbody>\r\n"
    + ((stack1 = helpers.each.call(depth0 != null ? depth0 : (container.nullContext || {}),(depth0 != null ? depth0.mData : depth0),{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data,"loc":{"start":{"line":7,"column":12},"end":{"line":24,"column":21}}})) != null ? stack1 : "")
    + "        </tbody>\r\n    </table>\r\n</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['NewEntryForm.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<div id=\"NewEntryForm\">\r\n            <div id = \"new-entry-input-field\">\r\n                <label for=\"NewEntryForm-message\">Message</label>\r\n                <textarea class=\"form-control\" id=\"NewEntryForm-message\"></textarea>\r\n            </div>\r\n            <div id =\"new-entry-input-footer\">\r\n                <button type=\"button\" class=\"btn btn-default\" id=\"NewEntryForm-OK\">OK</button>\r\n                <button type=\"button\" class=\"btn btn-default\" id=\"NewEntryForm-CLEAR\">CLEAR</button>\r\n            </div>\r\n</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['Navbar.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<nav class=\"navbar navbar-default\">\r\n    <div class=\"container-fluid\">\r\n        <h3> Welcome to the Buzz <span id = \"Navbar-Name\"> </span> </h3>\r\n        <button type=\"button\" class=\"btn btn-default\" id=\"Navbar-Profile\">My Profile</button>\r\n\r\n    </div>\r\n</nav>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['ValidationForm.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<div id=\"ValidationForm\">\r\n    <div class=\"form-group\">\r\n        <label for=\"loginUsername\">Username</label>\r\n        <input type=\"username\" class=\"form-control\" id=\"ValidationForm-username\" aria-describedby=\"emailHelp\" placeholder=\"Enter username\">\r\n    </div>\r\n    <div class=\"form-group\">\r\n        <label for=\"loginPassword\">Password</label>\r\n        <input type=\"password\" class=\"form-control\" id=\"ValidationForm-password\" placeholder=\"Password\">\r\n    </div>\r\n    <div class=\"form-group\">\r\n            <label for=\"loginPassword\">Email Address (Only for registration)</label>\r\n            <input type=\"text\" class=\"form-control\" id=\"ValidationForm-name\" placeholder=\"Email\">\r\n        </div>\r\n\r\n    <button type=\"button\" class=\"btn btn-default\" id=\"ValidationForm-Register\">Register</button>\r\n    <button type=\"button\" class=\"btn btn-default\" id=\"ValidationForm-Login\">Login</button>\r\n\r\n</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['MyProfile.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    var alias1=container.propertyIsEnumerable, alias2=container.lambda, alias3=container.escapeExpression;

  return "<div class = \"container\" id = \"MyProfile\">\r\n    <button type=\"button\" class=\"btn btn-default\" id=\"Profile-Back-Button\">Back</button>\r\n\r\n    <div class = \"row\" id = \"username-row\"> \r\n        <div class = \"col-2\">  Username:  </div>\r\n        <div class = \"col-2\"> "
    + alias3(alias2((depth0 != null ? depth0.username : depth0), depth0))
    + " </div>\r\n        <div class = \"col-8\"> </div>\r\n    </div>\r\n\r\n    <div class = \"row\" id = \"email-row\"> \r\n            <div class = \"col-2\">Email:</div>\r\n            <div class = \"col-2\"> "
    + alias3(alias2((depth0 != null ? depth0.email : depth0), depth0))
    + " </div>\r\n            <div class = \"col-8\"> </div>\r\n    </div>\r\n\r\n    <div class = \"row\" id = \"comment-row\"> \r\n            <div class = \"col-2\">Comment:</div>\r\n            <div class = \"col-2\"> <input id=\"comment-field\" value = \""
    + alias3(alias2((depth0 != null ? depth0.comment : depth0), depth0))
    + "\"> </input> </div>\r\n            <div class = \"col-2\"> <button> Change Comment </button> </div>\r\n            <div class = \"col-6\"> </div>\r\n    </div>\r\n\r\n    <div class = \"row\" id = \"password-row\"> \r\n            <div class = \"col-2\">Password:</div>\r\n            <div class = \"col-2\"> \r\n                <input id=\"password-field\" placeholder = \"Enter new password\"> </input>\r\n                <input id=\"password-confirm-field\" placeholder = \"Confirm Password\"> </input>\r\n            \r\n            </div>\r\n            <div class = \"col-2\"> <button> Change Password </button> </div>\r\n            <div class = \"col-6\"> </div>\r\n    </div>\r\n\r\n</div>\r\n";
},"useData":true});
})();
<<<<<<< HEAD
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['UserProfile.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    var alias1=container.propertyIsEnumerable, alias2=container.lambda, alias3=container.escapeExpression;

  return "<div class = \"container\" id = \"UserProfile\">\r\n    <button type=\"button\" class=\"btn btn-default\" id=\"Profile-Back-Button\">Back</button>\r\n\r\n    <div class = \"row\" id = \"username-row\"> \r\n        <div class = \"col-2\">  Username:  </div>\r\n        <div class = \"col-2\"> "
    + alias3(alias2((depth0 != null ? depth0.username : depth0), depth0))
    + " </div>\r\n        <div class = \"col-8\"> </div>\r\n    </div>\r\n\r\n    <div class = \"row\" id = \"email-row\"> \r\n            <div class = \"col-2\">Email:</div>\r\n            <div class = \"col-2\"> "
    + alias3(alias2((depth0 != null ? depth0.email : depth0), depth0))
    + " </div>\r\n            <div class = \"col-8\"> </div>\r\n    </div>\r\n\r\n    <div class = \"row\" id = \"comment-row\"> \r\n            <div class = \"col-2\">Comment:</div>\r\n            <div class = \"col-2\"> <input id=\"comment-field\" value = \""
    + alias3(alias2((depth0 != null ? depth0.comment : depth0), depth0))
    + "\"> </input> </div>\r\n            <div class = \"col-2\"> <button> Change Comment </button> </div>\r\n            <div class = \"col-6\"> </div>\r\n    </div>\r\n\r\n    <div class = \"row\" id = \"password-row\"> \r\n            <div class = \"col-2\">Password:</div>\r\n            <div class = \"col-2\"> \r\n                <input id=\"password-field\" placeholder = \"Enter new password\"> </input>\r\n                <input id=\"password-confirm-field\" placeholder = \"Confirm Password\"> </input>\r\n            \r\n            </div>\r\n            <div class = \"col-2\"> <button> Change Password </button> </div>\r\n            <div class = \"col-6\"> </div>\r\n    </div>\r\n\r\n</div>\r\n";
},"useData":true});
})();
=======
>>>>>>> parent of 07726df... Fixed user profiles, waiting for API call to check
