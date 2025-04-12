package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class ByteSize implements Token {
  private final String original;
  private final long bytes;

  public ByteSize(String value) {
    this.original = value;
    this.bytes = parseByteSize(value);
  }

  private long parseByteSize(String input) {
    input = input.trim().toUpperCase();
    if (input.endsWith("KB")) return (long)(Double.parseDouble(input.replace("KB", "")) * 1024);
    if (input.endsWith("MB")) return (long)(Double.parseDouble(input.replace("MB", "")) * 1024 * 1024);
    if (input.endsWith("GB")) return (long)(Double.parseDouble(input.replace("GB", "")) * 1024 * 1024 * 1024);
    if (input.endsWith("TB")) return (long)(Double.parseDouble(input.replace("TB", "")) * 1024L * 1024L * 1024L * 1024L);
    if (input.endsWith("B"))  return Long.parseLong(input.replace("B", ""));
    throw new IllegalArgumentException("Invalid byte size format: " + input);
  }

  public long getBytes() {
    return bytes;
  }

  @Override
  public Object value() {
    return bytes;
  }

  @Override
  public TokenType type() {
    return TokenType.BYTE_SIZE;
  }

  @Override
  public JsonElement toJson() {
    return new JsonPrimitive(bytes);
  }

  @Override
  public String toString() {
    return original;
  }
}
