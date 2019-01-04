package io.openems.common.utils;

import java.net.Inet4Address;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import io.openems.common.exceptions.NotImplementedException;
import io.openems.common.exceptions.OpenemsError;
import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.exceptions.OpenemsException;

public class JsonUtils {

	private final static Logger log = LoggerFactory.getLogger(JsonUtils.class);

	public static boolean getAsBoolean(JsonElement jElement) throws OpenemsNamedException {
		JsonPrimitive jPrimitive = getAsPrimitive(jElement);
		if (!jPrimitive.isBoolean()) {
			throw OpenemsError.JSON_NO_BOOLEAN.exception(jPrimitive);
		}
		return jPrimitive.getAsBoolean();
	};

	public static boolean getAsBoolean(JsonElement jElement, String memberName) throws OpenemsNamedException {
		JsonPrimitive jPrimitive = getAsPrimitive(jElement, memberName);
		if (!jPrimitive.isBoolean()) {
			throw OpenemsError.JSON_NO_BOOLEAN_MEMBER.exception(memberName, jPrimitive);
		}
		return jPrimitive.getAsBoolean();
	};

	public static int getAsInt(JsonElement jElement, String memberName) throws OpenemsNamedException {
		JsonPrimitive jPrimitive = getAsPrimitive(jElement, memberName);
		if (jPrimitive.isNumber()) {
			return jPrimitive.getAsInt();
		} else if (jPrimitive.isString()) {
			String string = jPrimitive.getAsString();
			return Integer.parseInt(string);
		}
		throw OpenemsError.JSON_NO_INTEGER_MEMBER.exception(memberName, jPrimitive);
	}

	public static JsonArray getAsJsonArray(JsonElement jElement) throws OpenemsNamedException {
		if (!jElement.isJsonArray()) {
			throw OpenemsError.JSON_NO_ARRAY.exception(jElement);
		}
		return jElement.getAsJsonArray();
	};

	public static JsonArray getAsJsonArray(JsonElement jElement, String memberName) throws OpenemsNamedException {
		JsonElement jSubElement = getSubElement(jElement, memberName);
		if (!jSubElement.isJsonArray()) {
			throw OpenemsError.JSON_NO_ARRAY_MEMBER.exception(memberName, jSubElement);
		}
		return jSubElement.getAsJsonArray();
	};

	public static JsonElement getAsJsonElement(Object value) {
		// null
		if (value == null) {
			return JsonNull.INSTANCE;
		}
		// optional
		if (value instanceof Optional<?>) {
			if (!((Optional<?>) value).isPresent()) {
				return null;
			} else {
				value = ((Optional<?>) value).get();
			}
		}
		if (value instanceof Number) {
			/*
			 * Number
			 */
			return new JsonPrimitive((Number) value);
		} else if (value instanceof String) {
			/*
			 * String
			 */
			return new JsonPrimitive((String) value);
		} else if (value instanceof Boolean) {
			/*
			 * Boolean
			 */
			return new JsonPrimitive((Boolean) value);
		} else if (value instanceof Inet4Address) {
			/*
			 * Inet4Address
			 */
			return new JsonPrimitive(((Inet4Address) value).getHostAddress());
		} else if (value instanceof JsonElement) {
			/*
			 * JsonElement
			 */
			return (JsonElement) value;
		} else if (value instanceof Long[]) {
			/*
			 * Long-Array
			 */
			JsonArray js = new JsonArray();
			for (Long l : (Long[]) value) {
				js.add(new JsonPrimitive((Long) l));
			}
			return js;
		} else if (value instanceof String[]) {
			/*
			 * String-Array
			 */
			JsonArray js = new JsonArray();
			for (String s : (String[]) value) {
				js.add(new JsonPrimitive((String) s));
			}
			return js;
		} else if (value instanceof Object[]) {
			/*
			 * Object-Array
			 */
			JsonArray js = new JsonArray();
			for (Object o : (Object[]) value) {
				js.add(JsonUtils.getAsJsonElement(o));
			}
			return js;
		} else {
			/*
			 * Use toString()-method
			 */
			log.warn("Converter for [" + value + "]" + " of type [" + value.getClass().getSimpleName()
					+ "] to JSON is not implemented.");
			return new JsonPrimitive(value.toString());
		}
	};

	public static JsonObject getAsJsonObject(JsonElement jElement) throws OpenemsNamedException {
		if (!jElement.isJsonObject()) {
			throw OpenemsError.JSON_NO_OBJECT.exception(jElement);
		}
		return jElement.getAsJsonObject();
	}

	public static JsonObject getAsJsonObject(JsonElement jElement, String memberName) throws OpenemsNamedException {
		JsonElement subElement = getSubElement(jElement, memberName);
		if (!subElement.isJsonObject()) {
			throw OpenemsError.JSON_NO_OBJECT_MEMBER.exception(memberName, subElement);
		}
		return subElement.getAsJsonObject();
	}

	public static long getAsLong(JsonElement jElement, String memberName) throws OpenemsNamedException {
		JsonPrimitive jPrimitive = getAsPrimitive(jElement, memberName);
		if (jPrimitive.isNumber()) {
			return jPrimitive.getAsLong();
		} else if (jPrimitive.isString()) {
			String string = jPrimitive.getAsString();
			return Long.parseLong(string);
		}
		throw OpenemsError.JSON_NO_NUMBER.exception(jPrimitive);
	}

	public static Optional<Integer> getAsOptionalInt(JsonElement jElement, String memberName) {
		try {
			return Optional.of(getAsInt(jElement, memberName));
		} catch (OpenemsNamedException e) {
			return Optional.empty();
		}
	}

	public static Optional<JsonArray> getAsOptionalJsonArray(JsonElement jElement, String memberName) {
		try {
			return Optional.of(getAsJsonArray(jElement, memberName));
		} catch (OpenemsNamedException e) {
			return Optional.empty();
		}
	}

	public static Optional<JsonObject> getAsOptionalJsonObject(JsonElement jElement) {
		try {
			return Optional.of(getAsJsonObject(jElement));
		} catch (OpenemsNamedException e) {
			return Optional.empty();
		}
	}

	public static Optional<JsonObject> getAsOptionalJsonObject(JsonElement jElement, String memberName) {
		try {
			return Optional.of(getAsJsonObject(jElement, memberName));
		} catch (OpenemsNamedException e) {
			return Optional.empty();
		}
	}

	public static Optional<Long> getAsOptionalLong(JsonElement jElement, String memberName) {
		try {
			return Optional.of(getAsLong(jElement, memberName));
		} catch (OpenemsNamedException e) {
			return Optional.empty();
		}
	}

	public static Optional<String> getAsOptionalString(JsonElement jElement, String memberName) {
		try {
			return Optional.of(getAsString(jElement, memberName));
		} catch (OpenemsNamedException e) {
			return Optional.empty();
		}
	}

	public static Object getAsBestType(JsonElement j) throws OpenemsNamedException {
		try {
			if (j.isJsonArray()) {
				JsonArray jA = (JsonArray) j;
				// identify the array type (boolean, int or String)
				boolean isBoolean = true;
				boolean isInt = true;
				for (JsonElement jE : jA) {
					if (jE.isJsonPrimitive()) {
						JsonPrimitive jP = jE.getAsJsonPrimitive();
						if (isBoolean && !jP.isBoolean()) {
							isBoolean = false;
						}
						if (isInt && !jP.isNumber()) {
							isInt = false;
						}
					} else {
						isBoolean = false;
						isInt = false;
						break;
					}
				}
				if (isBoolean) {
					// convert to boolean array
					boolean[] result = new boolean[jA.size()];
					for (int i = 0; i < jA.size(); i++) {
						result[i] = jA.get(i).getAsBoolean();
					}
					return result;
				} else if (isInt) {
					// convert to int array
					int[] result = new int[jA.size()];
					for (int i = 0; i < jA.size(); i++) {
						result[i] = jA.get(i).getAsInt();
					}
					return result;
				} else {
					// convert to string array
					String[] result = new String[jA.size()];
					for (int i = 0; i < jA.size(); i++) {
						JsonElement jE = jA.get(i);
						if (!jE.isJsonPrimitive()) {
							result[i] = jE.toString();
						} else {
							result[i] = jE.getAsString();
						}
					}
					return result;
				}
			}
			if (!j.isJsonPrimitive()) {
				return j.toString();
			}
			JsonPrimitive jP = j.getAsJsonPrimitive();
			if (jP.isBoolean()) {
				return jP.getAsBoolean();
			}
			if (jP.isNumber()) {
				Number n = jP.getAsNumber();
				return n.intValue();
			}
			return j.getAsString();
		} catch (Exception e) {
			throw OpenemsError.JSON_PARSE_ELEMENT_FAILED.exception(j, e.getClass().getSimpleName(), e.getMessage());
		}
	}

	@Deprecated
	public static Object getAsType(Class<?> type, JsonElement j) throws NotImplementedException {
		try {
			if (Integer.class.isAssignableFrom(type)) {
				/*
				 * Asking for an Integer
				 */
				return j.getAsInt();

			} else if (Long.class.isAssignableFrom(type)) {
				/*
				 * Asking for an Long
				 */
				return j.getAsLong();
			} else if (Boolean.class.isAssignableFrom(type)) {
				/*
				 * Asking for an Boolean
				 */
				return j.getAsBoolean();
			} else if (Double.class.isAssignableFrom(type)) {
				/*
				 * Asking for an Double
				 */
				return j.getAsDouble();
			} else if (String.class.isAssignableFrom(type)) {
				/*
				 * Asking for a String
				 */
				return j.getAsString();
			} else if (JsonObject.class.isAssignableFrom(type)) {
				/*
				 * Asking for a JsonObject
				 */
				return j.getAsJsonObject();
			} else if (JsonArray.class.isAssignableFrom(type)) {
				/*
				 * Asking for a JsonArray
				 */
				return j.getAsJsonArray();
			} else if (type.isArray()) {
				/**
				 * Asking for Array
				 */
				if (Long.class.isAssignableFrom(type.getComponentType())) {
					/**
					 * Asking for ArrayOfLong
					 */
					if (j.isJsonArray()) {
						JsonArray js = j.getAsJsonArray();
						Long[] la = new Long[js.size()];
						for (int i = 0; i < js.size(); i++) {
							la[i] = js.get(i).getAsLong();
						}
						return la;
					}

				}
			}
		} catch (IllegalStateException e) {
			throw new IllegalStateException("Failed to parse JsonElement [" + j + "]", e);
		}
		throw new NotImplementedException(
				"Converter for value [" + j + "] to class type [" + type + "] is not implemented.");
	}

	public static Object getAsType(Optional<Class<?>> typeOptional, JsonElement j) throws NotImplementedException {
		if (!typeOptional.isPresent()) {
			throw new NotImplementedException("Type of Channel was not set: " + j.getAsString());
		}
		Class<?> type = typeOptional.get();
		return getAsType(type, j);
	}

	public static JsonPrimitive getAsPrimitive(JsonElement jElement) throws OpenemsNamedException {
		if (!jElement.isJsonPrimitive()) {
			throw OpenemsError.JSON_NO_PRIMITIVE.exception(jElement);
		}
		return jElement.getAsJsonPrimitive();
	}

	public static JsonPrimitive getAsPrimitive(JsonElement jElement, String memberName) throws OpenemsNamedException {
		JsonElement jSubElement = getSubElement(jElement, memberName);
		return getAsPrimitive(jSubElement);
	}

	public static String getAsString(JsonElement jElement) throws OpenemsNamedException {
		JsonPrimitive jPrimitive = getAsPrimitive(jElement);
		if (!jPrimitive.isString()) {
			throw OpenemsError.JSON_NO_STRING.exception(jPrimitive);
		}
		return jPrimitive.getAsString();
	}

	public static String getAsString(JsonElement jElement, String memberName) throws OpenemsNamedException {
		JsonPrimitive jPrimitive = getAsPrimitive(jElement, memberName);
		if (!jPrimitive.isString()) {
			throw OpenemsError.JSON_NO_STRING_MEMBER.exception(memberName, jPrimitive);
		}
		return jPrimitive.getAsString();
	}

	/**
	 * Takes a json in the form 'YYYY-MM-DD' and converts it to a ZonedDateTime with
	 * hour, minute and second set to zero.
	 * 
	 * @param jElement
	 * @param memberName
	 * @param timezone
	 * @return
	 * @throws OpenemsException
	 */
	public static ZonedDateTime getAsZonedDateTime(JsonElement jElement, String memberName, ZoneId timezone)
			throws OpenemsNamedException {
		String[] date = JsonUtils.getAsString(jElement, memberName).split("-");
		try {
			int year = Integer.valueOf(date[0]);
			int month = Integer.valueOf(date[1]);
			int day = Integer.valueOf(date[2]);
			return ZonedDateTime.of(year, month, day, 0, 0, 0, 0, timezone);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw OpenemsError.JSON_NO_DATE_MEMBER.exception(memberName, jElement, e.getMessage());
		}
	}

	public static Set<JsonElement> getMatchingElements(JsonElement j, String... paths) {
		Set<JsonElement> result = new HashSet<JsonElement>();
		if (paths.length == 0) {
			// last path element
			result.add(j);
			return result;
		}
		String path = paths[0];
		if (j.isJsonObject()) {
			JsonObject jO = j.getAsJsonObject();
			if (jO.has(path)) {
				List<String> nextPathsList = new ArrayList<String>(Arrays.asList(paths));
				nextPathsList.remove(0);
				String[] nextPaths = nextPathsList.toArray(new String[0]);
				result.addAll(getMatchingElements(jO.get(path), nextPaths));
			}
		} else if (j.isJsonArray()) {
			for (JsonElement jE : j.getAsJsonArray()) {
				result.addAll(getMatchingElements(jE, paths));
			}
		} else if (j.isJsonPrimitive()) {
			JsonPrimitive jP = j.getAsJsonPrimitive();
			if (jP.isString()) {
				if (jP.getAsString().equals(path)) {
					result.add(jP);
				}
			}
		}
		return result;
	}

	public static JsonElement getSubElement(JsonElement jElement, String memberName) throws OpenemsNamedException {
		JsonObject jObject = getAsJsonObject(jElement);
		if (!jObject.has(memberName)) {
			throw OpenemsError.JSON_HAS_NO_MEMBER.exception(jElement, memberName);
		}
		return jObject.get(memberName);
	}

	public static boolean hasElement(JsonElement j, String... paths) {
		return getMatchingElements(j, paths).size() > 0;
	}

	/**
	 * Parses a string to a JsonElement
	 * 
	 * @param string
	 * @return
	 * @throws OpenemsNamedException
	 */
	public static JsonElement parse(String string) throws OpenemsNamedException {
		try {
			JsonParser parser = new JsonParser();
			return parser.parse(string);
		} catch (JsonParseException e) {
			throw OpenemsError.JSON_PARSE_FAILED.exception(e.getMessage(), string);
		}
	}

	/**
	 * Pretty print a JsonElement
	 *
	 * @param j
	 */
	public static void prettyPrint(JsonElement j) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(j);
		System.out.println(json);
	}

	/**
	 * A temporary builder class for JsonObjects
	 */
	public static class JsonObjectBuilder {

		private final JsonObject j;

		protected JsonObjectBuilder() {
			this(new JsonObject());
		}

		protected JsonObjectBuilder(JsonObject j) {
			this.j = j;
		}

		public JsonObjectBuilder addProperty(String property, String value) {
			j.addProperty(property, value);
			return this;
		}

		public JsonObjectBuilder addProperty(String property, int value) {
			j.addProperty(property, value);
			return this;
		}

		public JsonObjectBuilder addProperty(String property, long value) {
			j.addProperty(property, value);
			return this;
		}

		public JsonObjectBuilder addProperty(String property, boolean value) {
			j.addProperty(property, value);
			return this;
		}

		public JsonObjectBuilder add(String property, JsonElement value) {
			j.add(property, value);
			return this;
		}

		public JsonObject build() {
			return this.j;
		}

	}

	/**
	 * Creates a JsonObject using a Builder.
	 * 
	 * @return
	 */
	public static JsonObjectBuilder buildJsonObject() {
		return new JsonObjectBuilder();
	}

	/**
	 * Creates a JsonObject using a Builder. Initialized from an existing
	 * JsonObject.
	 * 
	 * @param j
	 * @return
	 */
	public static JsonObjectBuilder buildJsonObject(JsonObject j) {
		return new JsonObjectBuilder(j);
	}

	/**
	 * Parses a string to a JsonObject
	 * 
	 * @param string
	 * @return
	 * @throws OpenemsNamedException
	 */
	public static JsonObject parseToJsonObject(String string) throws OpenemsNamedException {
		return JsonUtils.getAsJsonObject(JsonUtils.parse(string));
	}
}
