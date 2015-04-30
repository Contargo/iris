describe("Helper test suite", function () {

    describe("formatKM", function () {

        it("exists", function () {

            expect(Helper.formatKM).toBeDefined();
        });

        it("returns undefined if the argument is undefined", function () {
            var result = Helper.formatKM(undefined);
            expect(result).toBeUndefined();
        });

        it("returns 0 min if the argument is 0", function () {
            var result = Helper.formatKM(0);
            expect(result).toEqual("0 km");
        });

        it("formats natural numbers correctly", function () {
            var result = Helper.formatKM(42);
            expect(result).toEqual("42 km");
        });

        it("formats non-natural numbers correctly", function () {
            var result = Helper.formatKM(59.0232);
            expect(result).toEqual("59 km");
        });

    });

    describe("formatting duration in detail", function () {

        it("returns '-' if duration is not defined", function () {

            expect(Helper.formatDurationInDetail()).toEqual("-");

        });

        it("returns formatted duration for 800 min", function () {

            expect(Helper.formatDurationInDetail(800)).toEqual("13.20hrs");

        });

        it("returns formatted duration for 8000 min", function () {

            expect(Helper.formatDurationInDetail(8000)).toEqual("05ds 13.20hrs");

        });

        it("returns formatted duration for 70 min", function () {

            expect(Helper.formatDurationInDetail(70)).toEqual("01.10hrs");

        });

        it("returns formatted duration for 5 min", function () {

            expect(Helper.formatDurationInDetail(5)).toEqual("00.05hrs");

        });

    });

    describe("closePopups", function () {

        var popover;

        beforeEach(function () {
            $div = $('div').addClass("blub").append($('<span class="info"></span>'));
            $div.append($('<div class="popover"></div>'));
            popover = new Backbone.View({
                el: ".blub"
            });

        });

        it("for all popups", function () {
            Helper.closeAllPopover();
            expect($('.popover').length).toEqual(0);
        });
                
    });

});