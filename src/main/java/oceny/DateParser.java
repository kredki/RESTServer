package oceny;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {
    private static SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
    Date date;

    public DateParser() { }

    public DateParser(String dateString) {
        Date t;
        try {
            t = ft.parse(dateString);
            this.date = t;
            System.out.println("new date: " + t);
        } catch (ParseException e) {
            System.out.println("Unparseable using " + ft);
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDate(String dateString) {
        Date t;
        try {
            t = ft.parse(dateString);
            this.date = t;
            System.out.println("new date: " + t);
        } catch (ParseException e) {
            System.out.println("Unparseable using " + ft);
        }
    }
}
