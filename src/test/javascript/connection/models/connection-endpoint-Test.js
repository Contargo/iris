describe('ConnectionEndpoints', function () {

    var sut;
    var endpoint;
    var endpoint2;

    beforeEach(function () {
        endpoint = new ConnectionEndpoint({
            uniqueId: '42'
        });
        endpoint2 = new ConnectionEndpoint({
            uniqueId: '23'
        });

        sut = new ConnectionEndpoints([endpoint, endpoint2]);
    });

    it('sets Selected silent', function () {

       sut.trigger = function (event) {
           expect(event).not.toBe('selectionChange');
       };

        sut.setSelected('23', true);

        expect(endpoint2.get('selected')).toBe(true);
    });

    it('sets Selected', function () {

        var selectionChangeEventTriggered = false;
        sut.trigger = function (event) {
            if(event === 'selectionChange'){
                selectionChangeEventTriggered = true;
            }
        };
        sut.setSelected('23');

        expect(endpoint2.get('selected')).toBe(true);
        expect(selectionChangeEventTriggered).toBe(true);
    });

    it('sets Selected undefined silent', function () {

       sut.trigger = function (event) {
           expect(event).not.toBe('selectionChange');
       };

        sut.setSelected(undefined, true);

        expect(endpoint.get('selected')).toBe(true);
    });

    it('sets undefined Selected', function () {

        var selectionChangeEventTriggered = false;
        sut.trigger = function (event) {
            if(event === 'selectionChange'){
                selectionChangeEventTriggered = true;
            }
        };
        sut.setSelected(undefined);

        expect(endpoint.get('selected')).toBe(true);
        expect(selectionChangeEventTriggered).toBe(true);
    });

    it('gets selected name', function () {
        endpoint.set('name', 'endpoint');
        sut.setSelected(undefined);
        expect(sut.getSelectedName()).toBe('endpoint');
    });

    it('gets selected element', function () {
        sut.setSelected(undefined);
        expect(sut.getSelected()).toBe(endpoint);
    });

    it('gets element by its unique id', function () {
        expect(sut.getByUniqueId('23')).toBe(endpoint2);
    });
});