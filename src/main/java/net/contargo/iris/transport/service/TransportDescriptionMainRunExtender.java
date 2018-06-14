package net.contargo.iris.transport.service;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.service.MainRunConnectionService;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.transport.api.ModeOfTransport;
import net.contargo.iris.transport.api.SiteType;
import net.contargo.iris.transport.api.TransportResponseDto;

import java.math.BigDecimal;
import java.math.BigInteger;

import static net.contargo.iris.transport.api.SiteType.SEAPORT;
import static net.contargo.iris.transport.api.SiteType.TERMINAL;
import static net.contargo.iris.transport.service.TransportDescriptionMainRunExtender.WaterDirection.DOWNSTREAM;
import static net.contargo.iris.transport.service.TransportDescriptionMainRunExtender.WaterDirection.UPSTREAM;

import static java.math.RoundingMode.UP;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class TransportDescriptionMainRunExtender {

    // average speeds in km/h
    private static final BigDecimal AVERAGE_SPEED_BARGE_UPSTREAM = new BigDecimal("10.0");
    private static final BigDecimal AVERAGE_SPEED_BARGE_DOWNSTREAM = new BigDecimal("18.0");
    private static final BigDecimal AVERAGE_SPEED_RAIL = new BigDecimal("45.0");
    private static final BigDecimal SECONDS_IN_AN_HOUR = new BigDecimal("60.0");

    private static final int DIGITS_TO_ROUND = 0;

    enum WaterDirection {

        UPSTREAM,
        DOWNSTREAM
    }

    private final MainRunConnectionService mainRunConnectionService;

    public TransportDescriptionMainRunExtender(MainRunConnectionService mainRunConnectionService) {

        this.mainRunConnectionService = mainRunConnectionService;
    }

    void with(TransportResponseDto.TransportResponseSegment segment) {

        MainRunConnection mainRunConnection = getMainRunConnection(segment);
        ModeOfTransport modeOfTransport = segment.modeOfTransport;

        switch (modeOfTransport) {
            case RAIL:
                extendRailSegment(segment, mainRunConnection);
                break;

            case WATER:
                extendWaterSegment(segment, mainRunConnection);
                break;

            default:
                throw new IllegalArgumentException("TODO: " + modeOfTransport
                    + " is not a valid mode of transport for a main run segment");
        }
    }


    private MainRunConnection getMainRunConnection(TransportResponseDto.TransportResponseSegment segment) {

        BigInteger terminalUuid = getUuid(segment, TERMINAL);
        Terminal terminal = new Terminal();
        terminal.setUniqueId(terminalUuid);

        BigInteger seaportUuid = getUuid(segment, SEAPORT);
        Seaport seaport = new Seaport();
        seaport.setUniqueId(seaportUuid);

        RouteType routeType = segment.modeOfTransport.getRouteType();

        return mainRunConnectionService.findRoutingConnectionBetweenTerminalAndSeaportByType(terminal, seaport,
                routeType, null);
    }


    private static void extendRailSegment(TransportResponseDto.TransportResponseSegment segment,
        MainRunConnection mainRunConnection) {

        BigDecimal railDistance;

        if (mainRunConnection.getRailDieselDistance() != null) {
            railDistance = mainRunConnection.getRailDieselDistance();
        } else if (mainRunConnection.getRailElectricDistance() != null) {
            railDistance = mainRunConnection.getRailElectricDistance();
        } else {
            throw new IllegalStateException("TODO: No rail distance specified");
        }

        segment.distance = railDistance.intValue();
        segment.duration = calculateDuration(railDistance, AVERAGE_SPEED_RAIL);
    }


    private static void extendWaterSegment(TransportResponseDto.TransportResponseSegment segment,
        MainRunConnection mainRunConnection) {

        BigDecimal bargeDistance = mainRunConnection.getBargeDieselDistance().setScale(2, UP);

        WaterDirection waterDirection = getWaterDirection(segment);
        BigDecimal divisor;

        switch (waterDirection) {
            case UPSTREAM:
                divisor = AVERAGE_SPEED_BARGE_UPSTREAM;
                break;

            case DOWNSTREAM:
                divisor = AVERAGE_SPEED_BARGE_DOWNSTREAM;
                break;

            default:
                throw new IllegalArgumentException("TODO: Unknown water direction: " + waterDirection);
        }

        segment.distance = bargeDistance.intValue();
        segment.duration = calculateDuration(bargeDistance, divisor);
    }


    private static WaterDirection getWaterDirection(TransportResponseDto.TransportResponseSegment segment) {

        if (segment.fromSite.type == SEAPORT && segment.toSite.type == TERMINAL) {
            return UPSTREAM;
        }

        if (segment.fromSite.type == TERMINAL && segment.toSite.type == SEAPORT) {
            return DOWNSTREAM;
        }

        throw new IllegalArgumentException("TODO: Water direction can not be determined");
    }


    static int calculateDuration(BigDecimal distance, BigDecimal divisor) {

        return distance.multiply(SECONDS_IN_AN_HOUR).divide(divisor, DIGITS_TO_ROUND, UP).intValue();
    }


    private static BigInteger getUuid(TransportResponseDto.TransportResponseSegment segment, SiteType siteType) {

        if (segment.fromSite.type == siteType) {
            return new BigInteger(segment.fromSite.uuid);
        } else if (segment.toSite.type == siteType) {
            return new BigInteger(segment.toSite.uuid);
        } else {
            return null;
        }
    }
}
