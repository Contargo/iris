describe('RouteTypes', function () {

    var sut;
    var routeType;
    var routeType2;

    beforeEach(function () {
        routeType = new RouteType({
            value: 'BARGE'
        });
        routeType2 = new RouteType({
            value: 'RAIL'
        });

        sut = new RouteTypes([routeType, routeType2]);
    });

    it('sets Selected silent', function () {

       sut.trigger = function (event) {
           expect(event).not.toBe('selectionChange');
       };

        sut.setSelected('RAIL', true);

        expect(routeType2.get('selected')).toBe(true);
    });

    it('sets Selected', function () {

        var selectionChangeEventTriggered = false;
        sut.trigger = function (event) {
            if(event === 'selectionChange'){
                selectionChangeEventTriggered = true;
            }
        };
        sut.setSelected('RAIL');

        expect(routeType2.get('selected')).toBe(true);
        expect(selectionChangeEventTriggered).toBe(true);
    });

    it('sets Selected undefined silent', function () {

       sut.trigger = function (event) {
           expect(event).not.toBe('selectionChange');
       };

        sut.setSelected(undefined, true);

        expect(routeType.get('selected')).toBe(true);
    });

    it('sets undefined Selected', function () {

        var selectionChangeEventTriggered = false;
        sut.trigger = function (event) {
            if(event === 'selectionChange'){
                selectionChangeEventTriggered = true;
            }
        };
        sut.setSelected(undefined);

        expect(routeType.get('selected')).toBe(true);
        expect(selectionChangeEventTriggered).toBe(true);
    });

    it('gets selected element', function () {
        sut.setSelected(undefined);
        expect(sut.getSelected()).toBe(routeType);
    });
});