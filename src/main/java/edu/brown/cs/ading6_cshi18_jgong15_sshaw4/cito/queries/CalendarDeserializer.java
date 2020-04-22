package edu.brown.cs.ading6_cshi18_jgong15_sshaw4.cito.queries;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarDeserializer implements JsonDeserializer<Calendar> {
  @Override
  public Calendar deserialize(JsonElement jsonElement,
                              Type type,
                              JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    // Wayback CRX API outputs as multidimensional arrays
    JsonArray entryArray = jsonElement.getAsJsonArray();
    if (entryArray.size() == 0) {
      return null; // if there is no entry, return null
    }
    // query was constructed so that timestamp is only returned
    String strTime = entryArray.get(1).getAsJsonArray().get(0).getAsString();

    // parse string (follows colon-less ISO-8601 standard, YYYYMMDDhhmmss)
    int year = Integer.parseInt(strTime.substring(0, 4));
    int month = Integer.parseInt(strTime.substring(4, 6));
    int day = Integer.parseInt(strTime.substring(6, 8));
    int hour = Integer.parseInt(strTime.substring(8, 10));
    int minute = Integer.parseInt(strTime.substring(10, 12));
    int second = Integer.parseInt(strTime.substring(12, 14));

    // construct and return calendar
    return new GregorianCalendar(year, month, day, hour, minute, second);
  }
}
