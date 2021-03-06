package se.kotlinski.imagesort.forecaster.date;

import com.google.inject.Inject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateToFileRenamer {
  private final Calendar calendar;

  @Inject
  public DateToFileRenamer(final Calendar calendar) {
    this.calendar = calendar;
  }

  public String formatPathDate(Date date) {
    calendar.setTime(date);
    SimpleDateFormat format = new SimpleDateFormat("yyyy" + File.separator + "MM");
    return format.format(calendar.getTime());
  }


  public String formatFileDate(Date date) {
    calendar.setTime(date);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
    format.setTimeZone(calendar.getTimeZone());
    return format.format(calendar.getTime());
  }
}
