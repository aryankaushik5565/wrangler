package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class TimeDuration implements Token {
  private final String original;
  private final long milliseconds;

  public TimeDuration(String value) {
    this.original = value;
    this.milliseconds = parseDuration(value);
  }

  private long parseDuration(String input) {
    input = input.trim().toLowerCase();
    if (input.endsWith("ms")) return (long)(Double.parseDouble(input.replace("ms", "")));
    if (input.endsWith("s"))  return (long)(Double.parseDouble(input.replace("s", "")) * 1000);
    if (input.endsWith("sec")) return (long)(Double.parseDouble(input.replace("sec", "")) * 1000);
    if (input.endsWith("seconds")) return (long)(Double.parseDouble(input.replace("seconds", "")) * 1000);
    if (input.endsWith("m"))  return (long)(Double.parseDouble(input.replace("m", "")) * 60000);
    if (input.endsWith("min")) return (long)(Double.parseDouble(input.replace("min", "")) * 60000);
    if (input.endsWith("minutes")) return (long)(Double.parseDouble(input.replace("minutes", "")) * 60000);
    if (input.endsWith("h"))  return (long)(Double.parseDouble(input.replace("h", "")) * 3600000);
    throw new IllegalArgumentException("Invalid time duration format: " + input);
  }

  public long getMilliseconds() {
    return milliseconds;
  }

  @Override
  public Object value() {
    return milliseconds;
  }

  @Override
  public TokenType type() {
    return TokenType.TIME_DURATION;
  }

  @Override
  public JsonElement toJson() {
    return new JsonPrimitive(milliseconds);
  }

  @Override
  public String toString() {
    return original;
  }
}
