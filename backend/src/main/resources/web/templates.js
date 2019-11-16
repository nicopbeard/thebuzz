(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['ElementList.hb'] = template({"1":function(container,depth0,helpers,partials,data) {
    var alias1=container.propertyIsEnumerable, alias2=container.lambda, alias3=container.escapeExpression;

  return "            <tr>\r\n                <td class = \"ElementList-user-profile-button\" data-id = \""
    + alias3(alias2((depth0 != null ? depth0.senderId : depth0), depth0))
    + "\">"
    + alias3(alias2((depth0 != null ? depth0.senderId : depth0), depth0))
    + "</td>\r\n                <td>"
    + alias3(alias2((depth0 != null ? depth0.text : depth0), depth0))
    + "</td>\r\n                <td>\r\n                    <button type=\"button\" class=\"ElementList-upvotebtn btn-primary\" data-value=\""
    + alias3(alias2((depth0 != null ? depth0.id : depth0), depth0))
    + "\">\r\n                        <span class=\"glyphicon glyphicon-thumbs-up\"></span>\r\n                        Like\r\n                    </button>\r\n                </td>\r\n                <td>\r\n                    <button class=\"ElementList-downvotebtn btn-danger\" data-value=\""
    + alias3(alias2((depth0 != null ? depth0.id : depth0), depth0))
    + "\">\r\n                        <span class=\"glyphicon glyphicon-thumbs-down\"></span>\r\n                        Dislike\r\n                    </button>\r\n                </td>\r\n            </tr>\r\n";
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