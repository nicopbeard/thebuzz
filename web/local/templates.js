(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['ElementList.hb'] = template({"1":function(container,depth0,helpers,partials,data) {
    var alias1=container.propertyIsEnumerable, alias2=container.lambda, alias3=container.escapeExpression;

  return "            <tr>\n                <td class = \"ElementList-user-profile-button\" data-id = \""
    + alias3(alias2((depth0 != null ? depth0.senderId : depth0), depth0))
    + "\">"
    + alias3(alias2((depth0 != null ? depth0.senderId : depth0), depth0))
    + "</td>\n                <td>"
    + alias3(alias2((depth0 != null ? depth0.text : depth0), depth0))
    + "</td>\n                <td>\n                    <button type=\"button\" class=\"ElementList-upvotebtn btn-primary\" data-value=\""
    + alias3(alias2((depth0 != null ? depth0.id : depth0), depth0))
    + "\">\n                        <span class=\"glyphicon glyphicon-thumbs-up\"></span>\n                        Like\n                    </button>\n                </td>\n                <td>\n                    <button class=\"ElementList-downvotebtn btn-danger\" data-value=\""
    + alias3(alias2((depth0 != null ? depth0.id : depth0), depth0))
    + "\">\n                        <span class=\"glyphicon glyphicon-thumbs-down\"></span>\n                        Dislike\n                    </button>\n                </td>\n            </tr>\n";
},"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1, alias1=container.propertyIsEnumerable;

  return "<div class=\"panel panel-default\" id=\"ElementList\">\n    <div class=\"panel-heading\">\n        <h3 class=\"panel-title\"></h3>\n    </div>\n    <table class=\"table\" id = message-table>\n        <tbody>\n"
    + ((stack1 = helpers.each.call(depth0 != null ? depth0 : (container.nullContext || {}),(depth0 != null ? depth0.mData : depth0),{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "        </tbody>\n    </table>\n</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['NewEntryForm.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<div id=\"NewEntryForm\">\n            <div id = \"new-entry-input-field\">\n                <label for=\"NewEntryForm-message\">Message</label>\n                <textarea class=\"form-control\" id=\"NewEntryForm-message\"></textarea>\n            </div>\n            <div id =\"new-entry-input-footer\">\n                <button type=\"button\" class=\"btn btn-default\" id=\"NewEntryForm-OK\">OK</button>\n                <button type=\"button\" class=\"btn btn-default\" id=\"NewEntryForm-CLEAR\">CLEAR</button>\n            </div>\n</div>";
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
    return "<div id=\"ValidationForm\">\n    <div class=\"form-group\">\n        <label for=\"loginUsername\">Username</label>\n        <input type=\"username\" class=\"form-control\" id=\"ValidationForm-username\" aria-describedby=\"emailHelp\" placeholder=\"Enter username\">\n    </div>\n    <div class=\"form-group\">\n        <label for=\"loginPassword\">Password</label>\n        <input type=\"password\" class=\"form-control\" id=\"ValidationForm-password\" placeholder=\"Password\">\n    </div>\n    <div class=\"form-group\">\n            <label for=\"loginPassword\">Email Address (Only for registration)</label>\n            <input type=\"text\" class=\"form-control\" id=\"ValidationForm-name\" placeholder=\"Email\">\n        </div>\n\n    <button type=\"button\" class=\"btn btn-default\" id=\"ValidationForm-Register\">Register</button>\n    <button type=\"button\" class=\"btn btn-default\" id=\"ValidationForm-Login\">Login</button>\n\n</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['MyProfile.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    var alias1=container.propertyIsEnumerable, alias2=container.lambda, alias3=container.escapeExpression;

  return "<div class = \"container\" id = \"MyProfile\">\n    <button type=\"button\" class=\"btn btn-default\" id=\"Profile-Back-Button\">Back</button>\n\n    <div class = \"row\" id = \"username-row\"> \n        <div class = \"col-2\">  Username:  </div>\n        <div class = \"col-2\"> "
    + alias3(alias2((depth0 != null ? depth0.username : depth0), depth0))
    + " </div>\n        <div class = \"col-8\"> </div>\n    </div>\n\n    <div class = \"row\" id = \"email-row\"> \n            <div class = \"col-2\">Email:</div>\n            <div class = \"col-2\"> "
    + alias3(alias2((depth0 != null ? depth0.email : depth0), depth0))
    + " </div>\n            <div class = \"col-8\"> </div>\n    </div>\n\n    <div class = \"row\" id = \"comment-row\"> \n            <div class = \"col-2\">Comment:</div>\n            <div class = \"col-2\"> <input id=\"comment-field\" value = \""
    + alias3(alias2((depth0 != null ? depth0.comment : depth0), depth0))
    + "\"> </input> </div>\n            <div class = \"col-2\"> <button> Change Comment </button> </div>\n            <div class = \"col-6\"> </div>\n    </div>\n\n    <div class = \"row\" id = \"password-row\"> \n            <div class = \"col-2\">Password:</div>\n            <div class = \"col-2\"> \n                <input id=\"password-field\" placeholder = \"Enter new password\"> </input>\n                <input id=\"password-confirm-field\" placeholder = \"Confirm Password\"> </input>\n            \n            </div>\n            <div class = \"col-2\"> <button> Change Password </button> </div>\n            <div class = \"col-6\"> </div>\n    </div>\n\n</div>\n";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['UserProfile.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    var alias1=container.propertyIsEnumerable, alias2=container.lambda, alias3=container.escapeExpression;

  return "<div class = \"container\" id = \"UserProfile\">\n    <button type=\"button\" class=\"btn btn-default\" id=\"Profile-Back-Button\">Back</button>\n\n    <div class = \"row\" id = \"username-row\"> \n        <div class = \"col-2\">  Username:  </div>\n        <div class = \"col-2\"> "
    + alias3(alias2((depth0 != null ? depth0.username : depth0), depth0))
    + " </div>\n        <div class = \"col-8\"> </div>\n    </div>\n\n    <div class = \"row\" id = \"email-row\"> \n            <div class = \"col-2\">Email:</div>\n            <div class = \"col-2\"> "
    + alias3(alias2((depth0 != null ? depth0.email : depth0), depth0))
    + " </div>\n            <div class = \"col-8\"> </div>\n    </div>\n\n    <div class = \"row\" id = \"comment-row\"> \n            <div class = \"col-2\">Comment:</div>\n            <div class = \"col-2\"> <input id=\"comment-field\" value = \""
    + alias3(alias2((depth0 != null ? depth0.comment : depth0), depth0))
    + "\"> </input> </div>\n            <div class = \"col-2\"> <button> Change Comment </button> </div>\n            <div class = \"col-6\"> </div>\n    </div>\n\n    <div class = \"row\" id = \"password-row\"> \n            <div class = \"col-2\">Password:</div>\n            <div class = \"col-2\"> \n                <input id=\"password-field\" placeholder = \"Enter new password\"> </input>\n                <input id=\"password-confirm-field\" placeholder = \"Confirm Password\"> </input>\n            \n            </div>\n            <div class = \"col-2\"> <button> Change Password </button> </div>\n            <div class = \"col-6\"> </div>\n    </div>\n\n</div>\n";
},"useData":true});
})();
