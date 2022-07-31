package util;

import java.time.Duration;
import java.time.LocalDateTime;

public class FormatDate {

	public static String relativize(LocalDateTime yourDate) {
		final int SECOND = 1;
		final int MINUTE = 60 * SECOND;
		final int HOUR = 60 * MINUTE;
		final int DAY = 24 * HOUR;
		final int MONTH = 30 * DAY;

		Duration ts = Duration.between(yourDate, LocalDateTime.now());
		long delta = ts.abs().getSeconds();

		if (delta < 1 * MINUTE)
			return ts.getSeconds() == 1 ? "1 phút trước" : ts.getSeconds() + " giây trước";

		else if (delta < 2 * MINUTE)
			return "1 phút trước";

		else if (delta < 45 * MINUTE)
			return ts.toMinutes() + " phút trước";

		else if (delta < 120 * MINUTE)
			return "1 giờ trước";

		else if (delta < 24 * HOUR) {
			return ts.toHours() + " giờ trước";
		}

		else if (delta < (24 + LocalDateTime.now().getHour() + LocalDateTime.now().getMinute() / 60) * HOUR) {
			if (yourDate.getHour() < 10) {
				if (yourDate.getMinute()<10) 
					return "Hôm qua lúc 0" + yourDate.getHour() + ":0" + yourDate.getMinute();
				return "Hôm qua lúc 0" + yourDate.getHour() + ":" + yourDate.getMinute();
			}	
			return "Hôm qua lúc " + yourDate.getHour() + ":" + yourDate.getMinute();
		}

		else if (delta < 30 * DAY) {
			if (yourDate.getHour() < 10) {
				if (yourDate.getMinute()<10) 
					return yourDate.getDayOfMonth() + " tháng " + yourDate.getMonth().getValue() + " lúc 0" + yourDate.getHour()
					+ ":0" + yourDate.getMinute();
				return yourDate.getDayOfMonth() + " tháng " + yourDate.getMonth().getValue() + " lúc 0" + yourDate.getHour()
				+ ":" + yourDate.getMinute();
			}
			return yourDate.getDayOfMonth() + " tháng " + yourDate.getMonth().getValue() + " lúc " + yourDate.getHour()
					+ ":" + yourDate.getMinute();
		}
		
		else if (delta < 12 * MONTH) {
			return yourDate.getDayOfMonth() + " tháng " + yourDate.getMonth().getValue();
		}

		else {
			return yourDate.getDayOfMonth() + " tháng " + yourDate.getMonthValue() + ", " + yourDate.getYear();
		}
	}
}