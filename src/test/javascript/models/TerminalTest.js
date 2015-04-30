/**
 * Author: Sven Mueller - mueller@synyx.de
 * Date: 02.07.12
 * Time: 18:00
 */

describe("Terminal test suite", function() {

    it("can be instatiated", function() {

        var terminal = new Terminal();

        expect(terminal).toBeDefined();
        expect(terminal.name).toBeUndefined();
    });
});

describe("TerminalList test suite", function() {

    it("can be instantiated", function() {
         var list = new TerminalList();
         expect(list).toBeDefined();
         expect(list.at).toBeDefined();

        
    });

});