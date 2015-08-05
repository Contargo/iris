var DetailledAddressView = Backbone.View.extend({

    templateName: "DetailedAddress",

    initialize: function () {
        _.bindAll(this, "render");

        this.template = getTemplate(this.templateName);

        this.model.bind("change", this.render);

        this.render();
    },

    create: function (options) {
        'use strict';
        return new DetailledAddressView(options);
    },

    render: function () {
        var model = this.model.toJSON();

        var details = "";
        if (model.address) {
            _.each(model.address, function (e, f) {
                details = details + "<strong>" + f + ":</strong> " + e + "<br />";
            });
        }

        details = details + "<strong>latitude:</strong> " + model.latitude + "<br />";
        details = details + "<strong>longitude:</strong> " + model.longitude + "<br />";

        model.hasDetails = (details.length > 0);
        model.details = details;

        this.$el.html(this.template(model));

        this.$(".info").popover();

        Helper.setPopoverOnClickCloseEvent(this);
    }
});

var AddressView = Backbone.View.extend({

    tagName: "tr",
    className: "address",
    templateName: "Address",

    initialize: function (options) {
        _.bindAll(this, "render", "selectAddress");

        Helper.isDefined(options, "options");
        Helper.isDefined(this.model, "model");

        this.template = getTemplate(this.templateName);

        this.model.bind("change", this.render);

        this.showdetails = true;
    },

    events: {
        "click .selectAddressRow": "selectAddress"
    },

    render: function () {
        this.$el.html(this.template(this.model.toJSON()));

        if (this.showdetails) {
            DetailledAddressView.prototype.create({
                el: this.$(".address"),
                model: this.model
            });
        }
    },

    selectAddress: function () {
        this.model.set("selected", !this.model.get("selected"));
    }
});

var AddressListView = Backbone.View.extend({

    templateName: "AddressList",

    events: {
        "click .selectParentAddress": "selectParentAddress"
    },

    initialize: function (options) {

        Helper.isDefined(options, "options");
        Helper.isDefined(options.model, "options.model");
        this.template = getTemplate(this.templateName);

        if (this.model === undefined) {
            throw "model is undefined";
        }

        _.bindAll(this, "render", "addOne", "addAll", "selectParentAddress",
            "expand");
        this.model.get("addresses").bind("add", this.render);
        this.model.get("addresses").bind("remove", this.render);
        this.model.get("addresses").bind("reset", this.render);

        this.render();
    },

    selectParentAddress: function () {
        var first = this.model.get("addresses").at(0);

        var newvalue = !first.get("expanded");
        this.expand(newvalue);
        first.set("expanded", newvalue);

        return false;
    },

    render: function () {

        if (this.model.get("addresses").size() === 0) {
            this.$el.html("<strong>No results found.</strong>");
        } else {

            var model = this.model.toJSON();
            model.size = this.model.get("addresses").size();
            model.moreThanOne = model.size > 1;
            model.more = model.size - 1;

            model.cid = this.model.cid;

            this.$el.html(this.template(model));

            this.addAll();
        }

        this.collapsables();
    },

    collapsables: function () {
        this.$('.collapsableindicator').on('hide', function (e) {

            var $header = $(e.target).parent(".accordion-group").find(".collapsable-indicator");
            $header.removeClass("accordion-heading-open");
        });

        this.$('.collapsableindicator').on('show', function (e) {
            var $header = $(e.target).parent(".accordion-group").find(".collapsable-indicator");
            $header.addClass("accordion-heading-open");
        });
    },

    addOne: function (address) {
        var view = new AddressView({
            model: address
        });
        view.render();
        var node = view.el;
        this.$(".address_table").append(node);
    },

    addAll: function () {
        this.model.get("addresses").forEach(this.addOne);
    },

    expand: function (value) {
        if (value) {
            $(this.$(".accordion-body")).addClass('in');
            $(this.$(".accordion-toggle")).addClass('accordion-heading-open');
            $(this.$(".accordion-body")).css('height', 'auto');
        } else {
            $(this.$(".accordion-body")).removeClass('in');
            $(this.$(".accordion-toggle")).removeClass('accordion-heading-open');
            $(this.$(".accordion-body")).css('height', '0px');
        }
    }
});

var AddressCategoryView = Backbone.View.extend({

    templateName: "AddressCategoryView",

    initialize: function (options) {
        Helper.isDefined(options.searchStatus, "searchStatus");
        this.searchStatus = options.searchStatus;

        this.template = getTemplate(this.templateName);

        _.bindAll(this, "render", "selectedChanged");
        this.model.bind("add", this.render);
        this.model.bind("remove", this.render);

        this.model.bind("reset", this.render);
        this.model.bind("change:selectedAddressOfAddressListList",
            this.selectedChanged);

        this.render();
    },

    render: function () {
        this.$el.html("");

        if (this.model.length === 0 && this.searchStatus.haveSearched()) {
            this.$el.html("<strong>No results found.</strong>");
        } else if (this.searchStatus.haveSearched()) {
            // get combined address count
            var count = this.model.reduce(function (memo, el) {
                return memo + el.get("addresses").size();
            }, 0);

            var model = {
                length: count
            };

            this.$el.html(this.template(model));

            var addressListHasNonEmptyAddresses = false;

            this.model.each(function (addressList) {
                if (typeof addressList.get("addresses").first() !== "undefined") {
                    addressListHasNonEmptyAddresses = true;
                }
            });
            var expand = (this.model.size() === 1);
            var that = this;
            this.model.each(function (addressList) {
                if (!(typeof addressList.get("addresses").first() === "undefined" && addressListHasNonEmptyAddresses)) {
                    var view = new AddressListView({
                        model: addressList
                    });

                    if (expand) {
                        view.expand(true);
                    }

                    that.$("#addressCategoryAccordion").append(view.el);
                }
            });
        }
    },

    selectedChanged: function (address) {
        var oneElement = (this.model.size() === 1);

        this.model.each(function (addressList) {

            var expand = false;

            addressList.get("addresses").each(function (addr) {
                if (addr === address) {
                    expand = true;
                }
            });

            if (expand || oneElement) {
                this.$("#collapse" + addressList.cid).addClass('in');
                this.$("#collapse" + addressList.cid).addClass('accordion-heading-open');
                this.$("#collapse" + addressList.cid).css('height', 'auto');
            } else {
                this.$("#collapse" + addressList.cid).removeClass('in');
                this.$("#collapse" + addressList.cid).removeClass('accordion-heading-open');
                this.$("#collapse" + addressList.cid).css('height', '0px');
            }
        });
    }
});
