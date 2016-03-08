package net.contargo.iris.address.nominatim.service;

/**
 * @author  Oliver Messner - messner@synyx.de
 */
enum OsmType {

    WAY {

        @Override
        String getKey() {

            return "W";
        }
    },

    NODE {

        @Override
        String getKey() {

            return "N";
        }
    };

    abstract String getKey();
}
