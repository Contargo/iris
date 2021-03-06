package net.contargo.iris.transport;

import net.contargo.iris.FlowDirection;
import net.contargo.iris.co2.Co2CalculationParams;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.service.MainRunConnectionService;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.transport.api.ModeOfTransport;
import net.contargo.iris.transport.api.StopType;
import net.contargo.iris.transport.api.TransportResponseDto;
import net.contargo.iris.transport.co2.Co2CalculationRailParams;
import net.contargo.iris.transport.co2.Co2CalculationWaterParams;
import net.contargo.iris.transport.util.TransportDescriptionUtils;
import net.contargo.iris.units.Distance;
import net.contargo.iris.units.Duration;
import net.contargo.iris.units.Weight;

import java.math.BigDecimal;
import java.math.BigInteger;

import static net.contargo.iris.co2.Co2Calculator.rail;
import static net.contargo.iris.co2.Co2Calculator.water;
import static net.contargo.iris.transport.api.StopType.SEAPORT;
import static net.contargo.iris.transport.api.StopType.TERMINAL;
import static net.contargo.iris.units.LengthUnit.KILOMETRE;
import static net.contargo.iris.units.MassUnit.KILOGRAM;
import static net.contargo.iris.units.TimeUnit.MINUTE;

import static java.math.RoundingMode.UP;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class TransportDescriptionMainRunExtender {

    // average speeds in km/h
    private static final BigDecimal AVERAGE_SPEED_BARGE_UPSTREAM = new BigDecimal("10.0");
    private static final BigDecimal AVERAGE_SPEED_BARGE_DOWNSTREAM = new BigDecimal("18.0");
    private static final BigDecimal AVERAGE_SPEED_RAIL = new BigDecimal("45.0");
    private static final BigDecimal MINUTES_PER_HOUR = new BigDecimal("60.0");

    private static final int DIGITS_TO_ROUND = 0;
    private static final int SCALE = 2;

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
                throw new IllegalArgumentException(modeOfTransport
                    + " is not a valid mode of transport for a main run segment");
        }
    }


    private MainRunConnection getMainRunConnection(TransportResponseDto.TransportResponseSegment segment) {

        BigInteger terminalUuid = getUuid(segment, TERMINAL);
        BigInteger seaportUuid = getUuid(segment, SEAPORT);

        RouteType routeType = segment.modeOfTransport.getRouteType();

        return mainRunConnectionService.getConnectionByTerminalUidAndSeaportUidAndType(terminalUuid, seaportUuid,
                routeType);
    }


    private static void extendRailSegment(TransportResponseDto.TransportResponseSegment segment,
        MainRunConnection mainRunConnection) {

        BigDecimal dieselDistance = mainRunConnection.getRailDieselDistance();
        BigDecimal electricDistance = mainRunConnection.getRailElectricDistance();
        BigDecimal railDistance = dieselDistance.add(electricDistance);

        segment.distance = new Distance(railDistance.intValue(), KILOMETRE);
        segment.duration = calculateDuration(railDistance, AVERAGE_SPEED_RAIL);

        Co2CalculationParams.Rail params = new Co2CalculationRailParams(mainRunConnection, segment);
        segment.co2 = new Weight(rail(params), KILOGRAM);
    }


    private static void extendWaterSegment(TransportResponseDto.TransportResponseSegment segment,
        MainRunConnection mainRunConnection) {

        FlowDirection flowDirection = TransportDescriptionUtils.getFlowDirection(segment);
        BigDecimal divisor;

        switch (flowDirection) {
            case UPSTREAM:
                divisor = AVERAGE_SPEED_BARGE_UPSTREAM;
                break;

            case DOWNSTREAM:
                divisor = AVERAGE_SPEED_BARGE_DOWNSTREAM;
                break;

            default:
                throw new IllegalArgumentException("Unknown flow direction: " + flowDirection);
        }

        BigDecimal bargeDistance = mainRunConnection.getBargeDieselDistance().setScale(SCALE, UP);
        int convertedBargeDistance = bargeDistance.intValue();

        segment.distance = new Distance(convertedBargeDistance, KILOMETRE);
        segment.duration = calculateDuration(bargeDistance, divisor);

        Co2CalculationParams.Water params = new Co2CalculationWaterParams(mainRunConnection, segment);
        segment.co2 = new Weight(water(params), KILOGRAM);
    }


    static Duration calculateDuration(BigDecimal distance, BigDecimal divisor) {

        return new Duration(distance.multiply(MINUTES_PER_HOUR).divide(divisor, DIGITS_TO_ROUND, UP).intValue(),
                MINUTE);
    }


    private static BigInteger getUuid(TransportResponseDto.TransportResponseSegment segment, StopType stopType) {

        if (segment.from.type == stopType) {
            return new BigInteger(segment.from.uuid);
        } else if (segment.to.type == stopType) {
            return new BigInteger(segment.to.uuid);
        } else {
            return null;
        }
    }
}
