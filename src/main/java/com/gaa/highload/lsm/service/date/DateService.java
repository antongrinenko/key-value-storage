package com.gaa.highload.lsm.service.date;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class DateService implements DateAPI {
    private static final int MONTHS_COUNT_TO_STORE_DATA = 6;

    @Override
    public String getCurrentDateLabel() {
        return createDateLabel(GregorianCalendar.getInstance());
    }

    @Override
    public List<String> getValidDateLabels() {
        List<String> result = new ArrayList<>();
        Calendar cal = GregorianCalendar.getInstance();
        for (int month=0; month<=MONTHS_COUNT_TO_STORE_DATA; month++) {
            cal.add(Calendar.MONTH, -1 * month);
            result.add(createDateLabel(cal));
        }
        return result;
    }

    private String createDateLabel(Calendar cal) {
        return String.format("%s_%s", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
    }
}
