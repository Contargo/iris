function getTransportChain(routePairs) {

    var transportChain = [];

    for (var i = 1; i < routePairs.length; i++) {

        var stop = routePairs.at(i);

        var fromLat = stop.get("from").get("latitude");
        var fromLon = stop.get("from").get("longitude");

        var toLat = stop.get("to").get("latitude");
        var toLon = stop.get("to").get("longitude");

        var segment = {
            from: {
                type: "ADDRESS",
                lon: fromLon,
                lat: fromLat
            },
            to: {
                type: "ADDRESS",
                lon: toLon,
                lat: toLat
            },
            loadingState: "FULL",
            unitAvailable: false,
            modeOfTransport: "ROAD"
        };

        transportChain.push(segment);
    }
    return transportChain;
}

function transformToD3Objects(elevations) {

    return elevations.map(function (elePoint) {
        return {
            xData: elePoint.summedDistance.value / 1000,
            yData: elePoint.elevation.value
        };
    });
}

function hideLoadingAnimation() {
    $('#loadingAnimation').addClass('hide');
}

function showLoadingAnimation() {
    console.log("GEh an!")
    $('#loadingAnimation').removeClass('hide');
}

function removeGraph() {
    $('#graph').empty();
}

function drawGraph(elevationData) {

    var margins = [40, 40, 40, 80];
    var width = 1050 - margins[1] - margins[3];
    var height = 500 - margins[0] - margins[2];

    var x = d3.scaleLinear().domain(d3.extent(elevationData, function (d) {
        return d.xData
    })).range([0, width]);
    var y = d3.scaleLinear().domain(d3.extent(elevationData, function (d) {
        return d.yData
    })).range([height, 0]);

    var line = d3.line()
        .x(function (d) {
            return x(d.xData);
        })
        .y(function (d) {
            return y(d.yData);
        })
        .curve(d3.curveMonotoneX);

    var graph = d3.select("#graph").append("svg:svg")
        .attr("preserveAspectRatio", "xMinYMin meet")
        .attr("viewBox", "-50 0 1000 2000")
        .classed("svg-content", true);

    var xAxis = d3.axisBottom(x);
    graph.append("svg:g")
        .attr("class", "x axis")
        .attr("transform", "translate(0," + height + ")")
        .call(xAxis);

    //x-Achsen-Beschriftung
    graph.append("text")
        .attr("transform", "translate(" + (width / 2) + " ," + (height + margins[2] + 5) + ")")
        .style("text-anchor", "middle")
        .text("zurückgelegte Strecke in km");

    var yAxisLeft = d3.axisLeft(y);
    graph.append("svg:g")
        .attr("class", "y axis")
        .attr("transform", "translate(0,0)")
        .call(yAxisLeft);

    //y-Achsen-Beschriftung
    graph.append("text")
        .attr("transform", "rotate(-90)")
        .attr("y", 0 - 50)
        .attr("x", 0 - (height / 2))
        .attr("dy", "1em")
        .style("text-anchor", "middle")
        .text("Höhe über NN in m");

    graph.append("svg:path")
        .attr("class", "line")
        .attr("d", line(elevationData));

}

var ElevationsView = Backbone.View.extend({

    templateName: 'ElevationsView',


    initialize: function (options) {

        Helper.isDefined(options, 'options');
        Helper.isDefined(options.model, 'options.model');

        this.template = getTemplate(this.templateName);

        _.bindAll(this, 'render');

        this.model.bind('add', this.render);
        this.model.bind('reset', this.render);
        this.model.bind('remove', this.render);
        this.$el.html(this.template(this.model));
        // this.render();
    },

    getTransportDescription: function () {

        removeGraph();
        if (this.model.length >= 2) {

            var transportDescription = {transportChain: getTransportChain(this.model.pairs)};

            showLoadingAnimation();

            jQuery.ajax({
                url: "/api/transport/elevations",
                type: "POST",
                data: JSON.stringify(transportDescription),
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                success: function (responseData) {
                    var elevationData = transformToD3Objects(responseData.elevations);
                    hideLoadingAnimation();
                    drawGraph(elevationData);
                }
            });

        }
    },

    render: function () {
        this.model.elevationRequestRunning = true;
        this.getTransportDescription();
    }
});