package rs.raf.demo.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
public class DateUtil {

        public static Optional<Date> convertStringToDate(Optional<String> dateString) {
            return dateString.map(DateUtil::parseDate);
        }
        public static Date convertStringToDate(String dateString) {
            return parseDate(dateString);
        }

        private static Date parseDate(String dateString) {
            if (dateString.isEmpty()) return null;
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // Adjust the date format as needed
                return dateFormat.parse(dateString);
            } catch (ParseException e) {
                // Handle parsing exception if needed
                throw new IllegalArgumentException("Invalid date format: " + dateString, e);
            }
        }
        public static Date parseDate2(String dateString) { //za search
            if(dateString==null || dateString.isEmpty() || dateString.equalsIgnoreCase("null"))return null;
            // Parse the input date
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDateTime dateTime = LocalDate.parse(dateString, inputFormatter).atStartOfDay();

            // Format the date in the desired output format "yyyy-MM-dd HH:mm:ss"
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateTime.format(outputFormatter);
            System.out.println(formattedDate); // prints 2019-01-01 10:10:00
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); // Adjust the date format as needed
            try {
                return dateFormat.parse(formattedDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }


}
