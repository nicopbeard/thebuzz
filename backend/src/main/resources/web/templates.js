(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['ElementList.hb'] = template({"1":function(container,depth0,helpers,partials,data) {
    var alias1=container.lambda, alias2=container.escapeExpression;

  return "            <tr>\n                <td>"
    + alias2(alias1((depth0 != null ? depth0.text : depth0), depth0))
    + "</td>\n                <td><button type=\"button\" class=\"ElementList-upvotebtn btn-primary\" data-value=\""
    + alias2(alias1((depth0 != null ? depth0.id : depth0), depth0))
    + "\">\n                    <span class=\"glyphicon glyphicon-thumbs-up\"></span>\n                    Like</button></td>\n                <td><button class=\"ElementList-downvotebtn btn-danger\" data-value=\""
    + alias2(alias1((depth0 != null ? depth0.id : depth0), depth0))
    + "\">\n                    <span class=\"glyphicon glyphicon-thumbs-down\"></span>\n                    Dislike</button></td>\n            </tr>\n";
},"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1;

  return "<div class=\"panel panel-default\" id=\"ElementList\">\n    <div class=\"panel-heading\">\n        <h3 class=\"panel-title\">Messages</h3>\n    </div>\n    <table class=\"table\">\n        <tbody>\n"
    + ((stack1 = helpers.each.call(depth0 != null ? depth0 : (container.nullContext || {}),(depth0 != null ? depth0.mData : depth0),{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "        </tbody>\n    </table>\n</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['NewEntryForm.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<div id=\"NewEntryForm\" class=\"modal fade\" role=\"dialog\">\n    <div class=\"modal-dialog\">\n        <div class=\"modal-content\">\n            <div class=\"modal-header\">\n                <h4 class=\"modal-title\">Add a New Entry</h4>\n            </div>\n            <div class=\"modal-body\">\n                <label for=\"NewEntryForm-message\">Message</label>\n                <textarea class=\"form-control\" id=\"NewEntryForm-message\"></textarea>\n            </div>\n            <div class=\"modal-footer\">\n                <button type=\"button\" class=\"btn btn-default\" id=\"NewEntryForm-OK\">OK</button>\n                <button type=\"button\" class=\"btn btn-default\" id=\"NewEntryForm-Close\">Close</button>\n            </div>\n        </div>\n    </div>\n</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['Navbar.hb'] = template({"compiler":[8,">= 4.3.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<nav class=\"navbar navbar-default\">\n    <div class=\"container-fluid\">\n        <!-- Brand and toggle get grouped for better mobile display -->\n        <div class=\"navbar-header\">\n            <button type=\"button\" class=\"navbar-toggle collapsed\" data-toggle=\"collapse\" \n                data-target=\"#bs-example-navbar-collapse-1\" aria-expanded=\"false\">\n              <span class=\"sr-only\">Toggle navigation</span>\n              <span class=\"icon-bar\"></span>\n              <span class=\"icon-bar\"></span>\n              <span class=\"icon-bar\"></span>\n            </button>\n            <!-- Clicking the brand refreshes the page -->\n            <a class=\"navbar-brand\" href=\"/\">The Buzz</a>\n        </div>\n\n        <!-- Collect the nav links, forms, and other content for toggling -->\n        <div class=\"collapse navbar-collapse\" id=\"bs-example-navbar-collapse-1\">\n            <ul class=\"nav navbar-nav\">\n                <li>\n                    <a class=\"btn btn-link\" id=\"Navbar-add\">\n                        Add Entry\n                        <span class=\"glyphicon glyphicon-plus\"></span><span class=\"sr-only\">Show Trending Posts</span>\n                    </a>\n                </li>\n            </ul>\n        </div>\n    </div>\n</nav>";
},"useData":true});
})();
