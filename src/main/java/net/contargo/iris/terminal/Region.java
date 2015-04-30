package net.contargo.iris.terminal;

/**
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public enum Region {

    NIEDERRHEIN("region.niederrhein"),
    OBERRHEIN("region.oberrhein"),
    SCHELDE("region.schelde"),
    NOT_SET("region.no");

    private final String messageKey;

    private Region(String messageKey) {

        this.messageKey = messageKey;
    }

    public String getMessageKey() {

        return messageKey;
    }
}
