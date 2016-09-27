
describe("TrianglePoints", function() {

    beforeEach(function() {

        this.serverMock = {
            getRouteDetails : jasmine.createSpy("getRouteDetails")
        };
       
        this.points = new TrianglePoints([],{server: this.serverMock});
    });

    it("can be instatiated", function() {

        expect(this.points).toBeDefined();
    });


    it("accepts addresses", function() {

        var address = new Address({name: "address"});
        this.points.add(address);

        expect(this.points.size()).toBe(1);
        expect(this.points.at(0).get("name")).toBe(address.get("name"));
    });

    it("accepts terminals", function() {

        var terminal = new Terminal({name: "DIT"});
        this.points.add(terminal);

        expect(this.points.size()).toBe(1);
        expect(this.points.at(0).get("name")).toBe(terminal.get("name"));
    });


    it("accepts terminals and addresses at once", function() {

        this.points.add(new Terminal({name: "DIT"}));
        this.points.add(new Address({name: "address"}));
        this.points.add(new Terminal({name: "DIT"}));

        expect(this.points.size()).toBe(3);
    });
    
    it("accepts seaports", function() {

        var seaport = new Seaport({name: "Rotterdam"});
        this.points.add(seaport);

        expect(this.points.size()).toBe(1);
        expect(this.points.at(0).get("name")).toBe(seaport.get("name"));
    });
    
    it("accepts seaports, terminals and addresses at once", function() {

        this.points.add(new Terminal({name: "DIT"}));
        this.points.add(new Address({name: "address"}));
        this.points.add(new Seaport({name: "Antwerpen"}));
        this.points.add(new Terminal({name: "DIT"}));
        this.points.add(new Seaport({name: "Rotterdam"}));

        expect(this.points.size()).toBe(5);
    });

    it("has getRoutePairs function", function() {

        expect(this.points.getRoutePairs).toBeDefined();

    });

    it("returns correct route pairs", function() {

        this.points.add(new Terminal({name: "DIT"}));
        this.points.add(new Address({name: "address"}));
        this.points.add(new Terminal({name: "DIT2"}));

        var pairs = this.points.getRoutePairs();
        expect(pairs.size()).toBe(3);

        var start = pairs.at(0);
        var firstroute = pairs.at(1);
        var secondroute = pairs.at(2);

        expect(start.get("from")).toBeUndefined();
        expect(start.get("to").get("name")).toBe("DIT");

        expect(firstroute.get("from").get("name")).toBe("DIT");
        expect(firstroute.get("to").get("name")).toBe("address");

        expect(secondroute.get("from").get("name")).toBe("address");
        expect(secondroute.get("to").get("name")).toBe("DIT2");
    });


    it("calls server for route-details on getRoutePairs", function() {
        this.points.add(new Terminal({name: "DIT", latitude: 1, longitude: 2}));
        this.points.add(new Address({name: "address", latitude: 11, longitude: 12}));
        this.points.add(new Terminal({name: "DIT2", latitude: 21, longitude: 22}));

        var pairs = this.points.getRoutePairs();

        expect(this.serverMock.getRouteDetails).toHaveBeenCalled();


        expect(pairs.size()).toEqual(3);
    });

    it("sets totals and part-details correctly upon getRoutePairs", function() {
        this.points.add(new Terminal({name: "DIT", latitude: 1, longitude: 2}));
        this.points.add(new Address({name: "address", latitude: 11, longitude: 12}));
        this.points.add(new Terminal({name: "DIT2", latitude: 21, longitude: 22}));

        var pairs = this.points.getRoutePairs();

        var status = this.points.totals;
        
        
        
        expect(status).toBeDefined();
        expect(status.get("size")).toEqual(3);
        expect(status.get("distance")).toBeUndefined();
        expect(status.get("toll")).toBeUndefined();
        expect(status.get("duration")).toBeUndefined();

        var parts = this.serverMock.getRouteDetails.calls.mostRecent().args[0];
        expect(parts.size()).toEqual(3);

        var callback = this.serverMock.getRouteDetails.calls.mostRecent().args[1];

        expect(callback).toBeDefined();

        var result = {
            data : {
                totalDistance: 1,
                totalTollDistance : 2,
                totalDuration : 3,
                parts : [
                    {
                        data : {
                            distance : 4,
                            tollDistance: 5,
                            duration: 6
                        }
                    },
                    {
                        data : {
                            distance : 7,
                            tollDistance: 8,
                            duration: 9
                        }
                    }
                ]
            }
        };
        
        // calling the callback
        callback(result);

        var statusAfterCallback = this.points.totals;

        expect(statusAfterCallback).toBeDefined();
        expect(statusAfterCallback.get("size")).toEqual(3);
        expect(statusAfterCallback.get("distance")).toEqual(Helper.formatKM(1));
        expect(statusAfterCallback.get("toll")).toEqual(Helper.formatKM(2));
        expect(statusAfterCallback.get("duration")).toEqual(Helper.formatDurationInDetail(3));


        var part1 = parts.at(1);
        expect(part1.get("distance")).toEqual(Helper.formatKM(4));
        expect(part1.get("toll")).toEqual(Helper.formatKM(5));
        expect(part1.get("duration")).toEqual(Helper.formatDurationInDetail(6));

        var part2 = parts.at(2);
        expect(part2.get("distance")).toEqual(Helper.formatKM(7));
        expect(part2.get("toll")).toEqual(Helper.formatKM(8));
        expect(part2.get("duration")).toEqual(Helper.formatDurationInDetail(9));


    });
    
    


    it("removes item when routepart is destroyed", function() {

        this.points.add(new Terminal({name: "DIT"}));
        this.points.add(new Address({name: "address"}));
        this.points.add(new Terminal({name: "DIT2"}));

        var pairs = this.points.getRoutePairs();
        expect(this.points.pluck("name")).toEqual(["DIT", "address", "DIT2"]);
        expect(pairs.size()).toBe(3);
        // remove route 2 which means "address" should be removed
        pairs.at(1).destroy();

        var pairs = this.points.getRoutePairs();
        expect(pairs.size()).toBe(2);
        expect(this.points.pluck("name")).toEqual(["DIT", "DIT2"]);

    });


    it("moves items up and down on two", function() {

        this.points.add(new Terminal({name: "A"}));
        this.points.add(new Terminal({name: "B"}));

        expect(this.points.pluck("name")).toEqual(["A", "B"]);

        var start_a = this.points.getRoutePairs().at(0);
        start_a.trigger("down", start_a);

        // index   from    to
        // 0                C
        // 1          C     A
        // 2          A     B

        expect(this.points.pluck("name")).toEqual(["B", "A"]);


    });

    it("moves items up and down", function() {

        this.points.add(new Terminal({name: "A"}));
        this.points.add(new Terminal({name: "B"}));
        this.points.add(new Terminal({name: "C"}));

        expect(this.points.pluck("name")).toEqual(["A", "B", "C"]);

        // index   from    to
        // 0                A
        // 1          A     B
        // 2          B     C
        var btoc = this.points.getRoutePairs().at(2);
        btoc.trigger("up", btoc);

        // index   from    to
        // 0                A
        // 1          A     C
        // 2          C     B
        expect(this.points.pluck("name")).toEqual(["A", "C", "B"]);


        var start_a = this.points.getRoutePairs().at(0);
        start_a.trigger("down", start_a);

        // index   from    to
        // 0                C
        // 1          C     A
        // 2          A     B

        expect(this.points.pluck("name")).toEqual(["C", "A", "B"]);
    });
});
