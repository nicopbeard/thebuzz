(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['ElementList.hb'] = template({"1":function(container,depth0,helpers,partials,data) {
    var alias1=container.lambda, alias2=container.escapeExpression;

  return "            <tr>\r\n                <td>Author: "
    + alias2(alias1((depth0 != null ? depth0.userId : depth0), depth0))
    + "</td>\r\n                <td>"
    + alias2(alias1((depth0 != null ? depth0.message : depth0), depth0))
    + "</td>\r\n                <td>Votes: "
    + alias2(alias1((depth0 != null ? depth0.vote : depth0), depth0))
    + "</td>\r\n                <td><button type=\"button\" class=\"ElementList-upvotebtn btn-primary\" data-value=\""
    + alias2(alias1((depth0 != null ? depth0.idM : depth0), depth0))
    + "\">\r\n                    <span class=\"glyphicon glyphicon-thumbs-up\"></span>\r\n                    Like</button></td>\r\n                <td><button class=\"ElementList-downvotebtn btn-danger\" data-value=\""
    + alias2(alias1((depth0 != null ? depth0.idM : depth0), depth0))
    + "\">\r\n                    <span class=\"glyphicon glyphicon-thumbs-down\"></span>\r\n                    Dislike</button></td>\r\n            </tr>\r\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1;

  return "<div class=\"panel panel-default\" id=\"ElementList\">\r\n    <div class=\"panel-heading\">\r\n        <h3 class=\"panel-title\">All Messages</h3>\r\n    </div>\r\n    <table class=\"table\">\r\n        <tbody>\r\n"
    + ((stack1 = helpers.each.call(depth0 != null ? depth0 : (container.nullContext || {}),depth0,{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "        </tbody>\r\n    </table>\r\n</div>\r\n";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['NewEntryForm.hb'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<div id=\"NewEntryForm\" class=\"modal fade\" role=\"dialog\">\r\n    <div class=\"modal-dialog\">\r\n        <div class=\"modal-content\">\r\n            <div class=\"modal-header\">\r\n                <h4 class=\"modal-title\">Add a New Message</h4>\r\n            </div>\r\n            <div class=\"modal-body\">\r\n                <label for=\"NewEntryForm-message\">Message</label>\r\n                <textarea class=\"form-control\" id=\"NewEntryForm-message\"></textarea>\r\n            </div>\r\n            <div class=\"modal-footer\">\r\n                <button type=\"button\" class=\"btn btn-default\" id=\"NewEntryForm-OK\">OK</button>\r\n                <button type=\"button\" class=\"btn btn-default\" id=\"NewEntryForm-Close\">Close</button>\r\n            </div>\r\n        </div>\r\n    </div>\r\n</div>\r\n";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['Navbar.hb'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<nav class=\"navbar navbar-default\">\r\n    <div class=\"container-fluid\">\r\n        <!-- Brand and toggle get grouped for better mobile display -->\r\n        <div class=\"navbar-header\">\r\n            <button type=\"button\" class=\"navbar-toggle collapsed\" data-toggle=\"collapse\" \r\n                data-target=\"#bs-example-navbar-collapse-1\" aria-expanded=\"false\">\r\n              <span class=\"sr-only\">Toggle navigation</span>\r\n              <span class=\"icon-bar\"></span>\r\n              <span class=\"icon-bar\"></span>\r\n              <span class=\"icon-bar\"></span>\r\n            </button>\r\n            <!-- Clicking the brand refreshes the page -->\r\n            <a class=\"navbar-brand\" href=\"/\">Logout</a>\r\n            \r\n        </div>\r\n\r\n        <!-- Collect the nav links, forms, and other content for toggling -->\r\n        <div class=\"collapse navbar-collapse\" id=\"bs-example-navbar-collapse-1\">\r\n            <ul class=\"nav navbar-nav\">\r\n                <li>\r\n                    <a class=\"btn btn-link\" id=\"Navbar-add\">\r\n                        Add Message\r\n                        <span class=\"glyphicon glyphicon-plus\"></span><span class=\"sr-only\">Show Trending Posts</span>\r\n                    </a>\r\n                        \r\n                </li>\r\n                <li>\r\n                    <a class=\"btn btn-link\" id=\"Navbar-viewProfile\">\r\n                        View Profile\r\n                        <span class=\"glyphicon glyphicon-plus\"></span><span class=\"sr-only\">Show Trending Posts</span>\r\n                    </a>\r\n                        \r\n                </li>\r\n                \r\n            </ul>\r\n        </div>\r\n        \r\n    </div>\r\n</nav>\r\n";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['Login.hb'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<div id=\"Login\">\r\n    <div class=\"modal-dialog>\r\n        <div class = \"modal-content\">\r\n            <div class=\"modal-header\">\r\n                <h4 class=\"modal-title\">Login</h4>\r\n            </div>\r\n            <div class=\"modal-body\">\r\n                <label for=\"Login-username\">Username</label>\r\n                <textarea class=\"form-control\" id=\"Login-username\"></textarea>\r\n\r\n                <label for=\"Login-password\">Password</label>\r\n                <textarea class=\"form-control\" id=\"Login-password\"></textarea>\r\n            </div>\r\n            <div class=\"modal-footer\">\r\n                <button type=\"button\" class=\"btn btn-link\" id=\"Login-LoginSubmit\">Login</button>\r\n                <button type=\"button\" class=\"btn btn-link\" id=\"Login-Register\">Register</button>   \r\n            </div>\r\n        </div>\r\n    </div>\r\n\r\n</div>\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['Register.hb'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<div id=\"Register\">\r\n    <div class=\"modal-dialog\">\r\n        <div class=\"modal-content\">\r\n            <div class=\"modal-header\">\r\n                <h4 class=\"modal-title\">Register New User</h4>\r\n            </div>\r\n            <div class=\"modal-body\">\r\n                <label for=\"Register-email\">Email Address</label>\r\n                <textarea class=\"form-control\" id=\"Register-email\"></textarea>\r\n\r\n                <label for=\"Register-first_name\">First Name</label>\r\n                <textarea class=\"form-control\" id=\"Register-first_name\"></textarea>\r\n\r\n                <label for=\"Register-last_name\">Last Name</label>\r\n                <textarea class=\"form-control\" id=\"Register-last_name\"></textarea>\r\n\r\n                <label for=\"Register-password\">Password</label>\r\n                <textarea class=\"form-control\" id=\"Register-password\"></textarea>\r\n\r\n                <label for=\"Register-password\">Confirm Password</label>\r\n                <textarea class=\"form-control\" id=\"Register-password_confirm\"></textarea>\r\n            </div>\r\n            <div class=\"modal-footer\">\r\n                \r\n                <a type=\"button\" class=\"btn btn-default\" href=\"/\" id=\"Register-submit\">Register</a>\r\n                <a type=\"button\" class=\"btn btn-default\" href=\"/\" id=\"Register-cancel\">Cancel</a>\r\n                \r\n            </div>\r\n        </div>\r\n    </div>\r\n</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['MyProfile.hb'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<div id=\"MyProfile\" class=\"modal fade\" role=\"dialog\">\r\n    <div class=\"modal-dialog\">\r\n        <div class=\"modal-content\">\r\n            <div class=\"modal-header\">\r\n                <h4 class=\"modal-title\">Profile</h4>\r\n            </div>\r\n            <div class=\"modal-body\">\r\n                <label for=\"MyProfile-name\">Name</label>\r\n                <input type=\"text\" class=\"form-control\" id=\"MyProfile-name\" readonly />\r\n            \r\n                <label for=\"MyProfile-email\">Email</label>\r\n                <input type=\"text\" class=\"form-control\" id=\"MyProfile-email\" readonly />\r\n\r\n                <label for=\"MyProfile-password\">Update Password</label>\r\n                <textarea class=\"form-control\" id=\"MyProfile-password\"></textarea>\r\n\r\n                <label for=\"MyProfile-password\">Confirm New Password</label>\r\n                <textarea class=\"form-control\" id=\"MyProfile-password_confirm\"></textarea>\r\n\r\n                <label for=\"MyProfile-comment\">Enter A New Comment</label>\r\n                <textarea class=\"form-control\" id=\"MyProfile-comment\"></textarea>\r\n\r\n\r\n            </div>\r\n            <div class=\"modal-footer\">\r\n                <button type=\"button\" class=\"btn btn-default\" id=\"MyProfile-update\">Update</button>\r\n                <button type=\"button\" class=\"btn btn-default\" id=\"MyProfile-cancel\">Cancel</button>\r\n            </div>\r\n        </div>\r\n    </div>\r\n</div>";
},"useData":true});
})();
