package io.openems.common.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.BiFunction;

import io.openems.common.exceptions.OpenemsException;

public class DateUtils {

	private DateUtils() {
	}

	/**
	 * Asserts that both dates are in the same timezone.
	 *
	 * @param date1 the first Date
	 * @param date2 the second Date
	 * @throws OpenemsException if dates are not in the same timezone
	 */
	public static void assertSameTimezone(ZonedDateTime date1, ZonedDateTime date2) throws OpenemsException {
		if (ZoneOffset.from(date1).getTotalSeconds() != ZoneOffset.from(date2).getTotalSeconds()) {
			throw new OpenemsException("FromDate and ToDate need to be in the same timezone!");
		}
	}

	/**
	 * Parses a string to an {@link ZonedDateTime} or returns null.
	 * 
	 * <p>
	 * See {@link ZonedDateTime#parse(CharSequence)}
	 * 
	 * @param date the string value
	 * @return an {@link ZonedDateTime} or null
	 */
	public static ZonedDateTime parseZonedDateTimeOrNull(String date) {
		return parseZonedDateTimeOrNull(date, DateTimeFormatter.ISO_ZONED_DATE_TIME);
	}

	/**
	 * Parses a string to an {@link ZonedDateTime} or returns null.
	 * 
	 * <p>
	 * See {@link ZonedDateTime#parse(CharSequence, DateTimeFormatter)}
	 * 
	 * @param date      the string value
	 * @param formatter the formatter to use, not null
	 * @return an {@link ZonedDateTime} or null
	 */
	public static ZonedDateTime parseZonedDateTimeOrNull(String date, DateTimeFormatter formatter) {
		return parseDateOrNull(ZonedDateTime::parse, date, formatter);
	}

	/**
	 * Parses a string to an {@link LocalDate} or returns null.
	 * 
	 * <p>
	 * See {@link LocalDate#parse(CharSequence)}
	 * 
	 * @param date the string value
	 * @return an {@link LocalDate} or null
	 */
	public static LocalDate parseLocalDateOrNull(String date) {
		return parseLocalDateOrNull(date, DateTimeFormatter.ISO_LOCAL_DATE);
	}

	/**
	 * Parses a string to an {@link LocalDate} or returns null.
	 * 
	 * <p>
	 * See {@link LocalDate#parse(CharSequence, DateTimeFormatter)}
	 * 
	 * @param date      the string value
	 * @param formatter the formatter to use, not null
	 * @return an {@link LocalDate} or null
	 */
	public static LocalDate parseLocalDateOrNull(String date, DateTimeFormatter formatter) {
		return parseDateOrNull(LocalDate::parse, date, formatter);
	}

	/**
	 * Parses a string to an {@link LocalTime} or returns null.
	 * 
	 * <p>
	 * See {@link LocalTime#parse(CharSequence)}
	 * 
	 * @param time the string value
	 * @return an {@link LocalTime} or null
	 */
	public static LocalTime parseLocalTimeOrNull(String time) {
		return parseLocalTimeOrNull(time, DateTimeFormatter.ISO_LOCAL_TIME);
	}

	/**
	 * Parses a string to an {@link LocalTime} or returns null.
	 * 
	 * <p>
	 * See {@link LocalTime#parse(CharSequence, DateTimeFormatter)}
	 * 
	 * @param time      the string value
	 * @param formatter the formatter to use, not null
	 * @return an {@link LocalTime} or null
	 */
	public static LocalTime parseLocalTimeOrNull(String time, DateTimeFormatter formatter) {
		return parseDateOrNull(LocalTime::parse, time, formatter);
	}

	private static final <T> T parseDateOrNull(//
			BiFunction<String, DateTimeFormatter, T> parser, //
			String value, //
			DateTimeFormatter formatter //
	) {
		if (value == null || value.isBlank()) {
			return null;
		}
		try {
			return parser.apply(value, formatter);
		} catch (DateTimeParseException e) {
			// unabel to parse date
		} catch (RuntimeException e) {
			// unexpected error
			e.printStackTrace();
		}
		return null;
	}

}
