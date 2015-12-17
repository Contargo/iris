package net.contargo.iris.routedatarevision.service;

import net.contargo.iris.routedatarevision.ValidityRange;

import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;


/**
 * Unit test of {@link ValidityRange}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class ValidityRangeUnitTest {

    @Test(expected = IllegalArgumentException.class)
    public void createWithValidFromNull() {

        new ValidityRange(null, null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void createFromAfterAfter() {

        new ValidityRange(LocalDate.now().plusDays(1), LocalDate.now());
    }


    @Test
    public void createWithValidArguments() {

        new ValidityRange(LocalDate.now(), LocalDate.now().plusDays(1));
    }


    @Test
    public void createWithOpenEnd() {

        new ValidityRange(LocalDate.now(), null);
    }


    @Test
    public void overlapWithBothOpenEnded() {

        ValidityRange first = new ValidityRange(LocalDate.now(), null);
        ValidityRange second = new ValidityRange(LocalDate.now(), null);

        assertThat(first.overlapWith(second), is(true));
    }


    @Test
    public void overlapFirstOpenEndedSecondBefore() {

        ValidityRange first = new ValidityRange(LocalDate.now(), null);
        ValidityRange second = new ValidityRange(LocalDate.now().minusDays(2), LocalDate.now().minusDays(1));

        assertThat(first.overlapWith(second), is(false));
    }


    @Test
    public void overlapFirstOpenEndedSecondNotBefore() {

        ValidityRange first = new ValidityRange(LocalDate.now(), null);
        ValidityRange second = new ValidityRange(LocalDate.now().minusDays(2), LocalDate.now());

        assertThat(first.overlapWith(second), is(true));
    }


    @Test
    public void overlapFirstBeforeSecondAndSecondOpenEnded() {

        ValidityRange first = new ValidityRange(LocalDate.now().minusDays(2), LocalDate.now().minusDays(1));
        ValidityRange second = new ValidityRange(LocalDate.now(), null);

        assertThat(first.overlapWith(second), is(false));
    }


    @Test
    public void overlapFirstNotBeforeSecondAndSecondOpenEnded() {

        ValidityRange first = new ValidityRange(LocalDate.now().minusDays(2), LocalDate.now());
        ValidityRange second = new ValidityRange(LocalDate.now(), null);

        assertThat(first.overlapWith(second), is(true));
    }


    @Test
    public void overlapFirstBeforeSecond() {

        ValidityRange first = new ValidityRange(LocalDate.now().minusDays(2), LocalDate.now().minusDays(1));
        ValidityRange second = new ValidityRange(LocalDate.now(), LocalDate.now().plusDays(1));

        assertThat(first.overlapWith(second), is(false));
    }


    @Test
    public void overlapFirstAfterSecond() {

        ValidityRange first = new ValidityRange(LocalDate.now(), LocalDate.now().plusDays(1));
        ValidityRange second = new ValidityRange(LocalDate.now().minusDays(2), LocalDate.now().minusDays(1));

        assertThat(first.overlapWith(second), is(false));
    }


    @Test
    public void overlapFirstOverlapSecond() {

        ValidityRange first = new ValidityRange(LocalDate.now().minusDays(1), LocalDate.now());
        ValidityRange second = new ValidityRange(LocalDate.now(), LocalDate.now().plusDays(1));

        assertThat(first.overlapWith(second), is(true));
    }


    @Test
    public void overlapFirstOverlapSecondReverse() {

        ValidityRange first = new ValidityRange(LocalDate.now(), LocalDate.now().plusDays(1));
        ValidityRange second = new ValidityRange(LocalDate.now().minusDays(1), LocalDate.now());

        assertThat(first.overlapWith(second), is(true));
    }
}
