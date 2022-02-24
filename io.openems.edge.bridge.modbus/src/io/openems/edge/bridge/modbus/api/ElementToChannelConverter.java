package io.openems.edge.bridge.modbus.api;

import io.openems.edge.bridge.modbus.api.element.ElementToChannelValueReplacer;
import io.openems.edge.common.converter.StaticConverters;

import java.util.function.Function;

/**
 * Provides Functions to convert from Element to Channel and back. Also has some
 * static convenience functions to facilitate conversion.
 */
public class ElementToChannelConverter {

	/**
	 * Converts directly 1-to-1 between Element and Channel.
	 */
	public static final ElementToChannelConverter DIRECT_1_TO_1 = new ElementToChannelConverter(//
			// element -> channel
			value -> value, //
			// channel -> element
			value -> value);

	/**
	 * Applies a scale factor of -1. Converts value [1] to [0.1].
	 * 
	 * @see ElementToChannelScaleFactorConverter
	 */
	public static final ElementToChannelConverter SCALE_FACTOR_MINUS_1 = new ElementToChannelScaleFactorConverter(-1);

	/**
	 * Applies a scale factor of -2. Converts value [1] to [0.01].
	 * 
	 * @see ElementToChannelScaleFactorConverter
	 */
	public static final ElementToChannelConverter SCALE_FACTOR_MINUS_2 = new ElementToChannelScaleFactorConverter(-2);

	/**
	 * Applies a scale factor of -3. Converts value [1] to [0.001].
	 * 
	 * @see ElementToChannelScaleFactorConverter
	 */
	public static final ElementToChannelConverter SCALE_FACTOR_MINUS_3 = new ElementToChannelScaleFactorConverter(-3);

	/**
	 * Applies a scale factor of -4. Converts value [1] to [0.0001].
	 *
	 * @see ElementToChannelScaleFactorConverter
	 */
	public static final ElementToChannelConverter SCALE_FACTOR_MINUS_4 = new ElementToChannelScaleFactorConverter(-4);

	/**
	 * Applies a scale factor of -5. Converts value [1] to [0.00001].
	 *
	 * @see ElementToChannelScaleFactorConverter
	 */
	public static final ElementToChannelConverter SCALE_FACTOR_MINUS_5 = new ElementToChannelScaleFactorConverter(-5);

	/**
	 * Applies a scale factor of 1. Converts value [1] to [10].
	 * 
	 * @see ElementToChannelScaleFactorConverter
	 */
	public static final ElementToChannelConverter SCALE_FACTOR_1 = new ElementToChannelScaleFactorConverter(1);

	/**
	 * Applies a scale factor of 2. Converts value [1] to [100].
	 * 
	 * @see ElementToChannelScaleFactorConverter
	 */
	public static final ElementToChannelConverter SCALE_FACTOR_2 = new ElementToChannelScaleFactorConverter(2);

	/**
	 * Applies a scale factor of 3. Converts value [1] to [1000].
	 * 
	 * @see ElementToChannelScaleFactorConverter
	 */
	public static final ElementToChannelConverter SCALE_FACTOR_3 = new ElementToChannelScaleFactorConverter(3);

	/**
	 * Applies a scale factor of 4. Converts value [1] to [10000].
	 *
	 * @see ElementToChannelScaleFactorConverter
	 */
	public static final ElementToChannelConverter SCALE_FACTOR_4 = new ElementToChannelScaleFactorConverter(4);

	/**
	 * Applies a scale factor of 5. Converts value [1] to [100000].
	 *
	 * @see ElementToChannelScaleFactorConverter
	 */
	public static final ElementToChannelConverter SCALE_FACTOR_5 = new ElementToChannelScaleFactorConverter(5);

	/**
	 * If the value of the element is 0XFFFF, writes null in the channel.
	 */
	public static final ElementToChannelConverter REPLACE_WITH_MINUS_ZERO_IF_0XFFFF = new ElementToChannelValueReplacer(65535, -1);

	/**
	 * Converts only positive values from Element to Channel.
	 */
	public static final ElementToChannelConverter KEEP_POSITIVE = new ElementToChannelConverter(//
			// element -> channel
			value -> StaticConverters.KEEP_POSITIVE, //
			// channel -> element
			value -> value);

	/**
	 * Inverts the value from Element to Channel.
	 */
	public static final ElementToChannelConverter INVERT = new ElementToChannelConverter(//
			// element -> channel
			StaticConverters.INVERT, //
			// channel -> element
			StaticConverters.INVERT);

	/**
	 * Sets the value to 'zero' if parameter is true; otherwise
	 * {@link #DIRECT_1_TO_1}.
	 * 
	 * <ul>
	 * <li>true: set zero
	 * <li>false: apply {@link #DIRECT_1_TO_1}
	 * </ul>
	 * 
	 * @param setZero true to set to null
	 * @return the {@link ElementToChannelConverter}
	 */
	// CHECKSTYLE:OFF
	public static ElementToChannelConverter SET_ZERO_IF_TRUE(boolean setZero) {
		// CHECKSTYLE:ON
		if (setZero) {
			return new ElementToChannelConverter(//
					// element -> channel
					value -> 0, //
					// channel -> element
					value -> 0);
		} else {
			return DIRECT_1_TO_1;
		}
	}

	/**
	 * Converts depending on the given parameter.
	 * 
	 * <ul>
	 * <li>true: invert value
	 * <li>false: keep value (1-to-1)
	 * </ul>
	 * 
	 * @param invert true if Converter should invert
	 * @return the {@link ElementToChannelConverter}
	 */
	// CHECKSTYLE:OFF
	public static ElementToChannelConverter INVERT_IF_TRUE(boolean invert) {
		// CHECKSTYLE:ON
		if (invert) {
			return INVERT;
		} else {
			return DIRECT_1_TO_1;
		}
	}

	/**
	 * Converts only negative values from Element to Channel and inverts them (makes
	 * the value positive).
	 */
	public static final ElementToChannelConverter KEEP_NEGATIVE_AND_INVERT = new ElementToChannelConverterChain(INVERT,
			KEEP_POSITIVE);

	/**
	 * Applies {@link ElementToChannelConverter#SCALE_FACTOR_1} and
	 * CONVERT_POSITIVE.
	 */
	public static final ElementToChannelConverter SCALE_FACTOR_1_AND_KEEP_POSITIVE = new ElementToChannelConverterChain(
			SCALE_FACTOR_1, KEEP_POSITIVE);

	/**
	 * Applies {@link ElementToChannelConverter#SCALE_FACTOR_2} and INVERT.
	 */
	public static final ElementToChannelConverter SCALE_FACTOR_2_AND_INVERT = new ElementToChannelConverterChain(
			SCALE_FACTOR_2, INVERT);

	/**
	 * Applies {@link ElementToChannelConverter#SCALE_FACTOR_1} and
	 * CONVERT_NEGATIVE_AND_INVERT.
	 */
	public static final ElementToChannelConverter SCALE_FACTOR_1_AND_KEEP_NEGATIVE_AND_INVERT = new ElementToChannelConverterChain(
			SCALE_FACTOR_1, KEEP_NEGATIVE_AND_INVERT);

	/**
	 * Applies {@link ElementToChannelConverter#SCALE_FACTOR_2} and
	 * CONVERT_POSITIVE.
	 */
	public static final ElementToChannelConverter SCALE_FACTOR_2_AND_KEEP_POSITIVE = new ElementToChannelConverterChain(
			SCALE_FACTOR_2, KEEP_POSITIVE);

	/**
	 * Applies {@link ElementToChannelConverter#SCALE_FACTOR_2} and @see
	 * {@link ElementToChannelConverter#KEEP_NEGATIVE_AND_INVERT}.
	 */
	public static final ElementToChannelConverter SCALE_FACTOR_2_AND_KEEP_NEGATIVE_AND_INVERT = new ElementToChannelConverterChain(
			SCALE_FACTOR_2, KEEP_NEGATIVE_AND_INVERT);

	/**
	 * Applies {@link ElementToChannelConverter#SCALE_FACTOR_2_AND_KEEP_NEGATIVE}
	 * and @see {@link ElementToChannelConverter#INVERT}.
	 */
	public static ElementToChannelConverter SCALE_FACTOR_2_AND_KEEP_NEGATIVE = new ElementToChannelConverterChain(
			SCALE_FACTOR_2_AND_KEEP_NEGATIVE_AND_INVERT, INVERT);

	/**
	 * Applies {@link ElementToChannelConverter#SCALE_FACTOR_1} and INVERT_IF_TRUE.
	 */
	public static final ElementToChannelConverter SCALE_FACTOR_1_AND_INVERT_IF_TRUE(boolean invert) {
		return new ElementToChannelConverterChain(SCALE_FACTOR_1, INVERT_IF_TRUE(invert));
	}

	/**
	 * Applies {@link ElementToChannelConverter#SCALE_FACTOR_2} and INVERT_IF_TRUE.
	 */
	public static final ElementToChannelConverter SCALE_FACTOR_2_AND_INVERT_IF_TRUE(boolean invert) {
		return new ElementToChannelConverterChain(SCALE_FACTOR_2, INVERT_IF_TRUE(invert));
	}

	/**
	 * Applies {@link ElementToChannelConverter#SCALE_FACTOR_3} and INVERT_IF_TRUE.
	 */
	public static final ElementToChannelConverter SCALE_FACTOR_3_AND_INVERT_IF_TRUE(boolean invert) {
		return new ElementToChannelConverterChain(SCALE_FACTOR_3, INVERT_IF_TRUE(invert));
	}

	/**
	 * Applies {@link ElementToChannelConverter#SCALE_FACTOR_MINUS_1} and
	 * INVERT_IF_TRUE.
	 */
	public static final ElementToChannelConverter SCALE_FACTOR_MINUS_1_AND_INVERT_IF_TRUE(boolean invert) {
		return new ElementToChannelConverterChain(SCALE_FACTOR_MINUS_1, INVERT_IF_TRUE(invert));
	}

	/**
	 * If the value of the element is 0X8000H, writes null in the channel.
	 */
	public static final ElementToChannelConverter REPLACE_WITH_NULL_IF_0X8000H = new ElementToChannelValueReplacer(-32768);


	private final Function<Object, Object> elementToChannel;
	private final Function<Object, Object> channelToElement;

	/**
	 * This constructs and back-and-forth converter from Element to Channel and
	 * back.
	 * 
	 * @param elementToChannel from Element to Channel
	 * @param channelToElement from Channel to Element
	 */
	public ElementToChannelConverter(Function<Object, Object> elementToChannel,
			Function<Object, Object> channelToElement) {
		this.elementToChannel = elementToChannel;
		this.channelToElement = channelToElement;
	}

	/**
	 * This constructs a forward-only converter from Element to Channel.
	 * Back-conversion throws an Exception.
	 * 
	 * @param elementToChannel Element to Channel
	 */
	public ElementToChannelConverter(Function<Object, Object> elementToChannel) {
		this.elementToChannel = elementToChannel;
		this.channelToElement = (value) -> {
			throw new IllegalArgumentException("Backwards-Conversion for [" + value + "] is not implemented.");
		};
	}

	/**
	 * Convert an Element value to a Channel value. If the value can or should not
	 * be converted, this method returns null.
	 * 
	 * @param value the Element value
	 * @return the converted value or null
	 */
	public Object elementToChannel(Object value) {
		return this.elementToChannel.apply(value);
	}

	/**
	 * Convert a Channel value to an Element value. If the value can or should not
	 * be converted, this method returns null.
	 * 
	 * @param value the Channel value
	 * @return the converted value or null
	 */
	public Object channelToElement(Object value) {
		return this.channelToElement.apply(value);
	}
}
