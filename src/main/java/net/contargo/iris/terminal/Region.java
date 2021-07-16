package net.contargo.iris.terminal;

/**
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Bjoern Martin - martin@synyx.de
 */
public enum Region {

    MAIN("region.main"),
    MITTELRHEIN("region.mittelrhein"),
    NIEDERRHEIN("region.niederrhein"),
    OBERRHEIN("region.oberrhein"),
    SCHELDE("region.schelde"),
    NOT_SET("region.no");

    private final String messageKey;

    Region(String messageKey) {

        this.messageKey = messageKey;
    }

    public String getMessageKey() {

        return messageKey;
    }
}
