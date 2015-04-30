/** install dummy console for headless mode? */
if (window.console == undefined) {
    window.console = {
        log: function (title) {
        }
    };
}

function getTemplate(name) {
    if (typeof globalTemplates != "undefined") {
        return Handlebars.compile(globalTemplates['src/main/webapp/client/js/routing/templates/' + name + '.html']);
    }
    else {
        var templateManager = new TemplateManager({
            prefix: "src/routing/templates/"
        });

        try {
            templateManager.getTemplate("Terminal");
        } catch (e) {
            templateManager.set("prefix", "src/main/webapp/client/js/routing/templates/");
        }

        return templateManager.getTemplate(name);
    }
}

/**
 * adjust fixture-path for jasmine-jquery to work in headless and head-mode
 */
try {
    jasmine.getFixtures().fixturesPath = 'spec/html';
    loadFixtures('test.html');
} catch (e) {
    jasmine.getFixtures().fixturesPath = 'src/test/javascript/html';
}