package net.contargo.iris.address.nominatim.service;

/**
 * @author  Oliver Messner - messner@synyx.de
 */
public enum OsmType {

    WAY {

        @Override
        public String getKey() {

            return "W";
        }
    },

    NODE {

        @Override
        public String getKey() {

            return "N";
        }
    },

    RELATION {

        @Override
        public String getKey() {

            return "R";
        }
    };

    public abstract String getKey();
}
