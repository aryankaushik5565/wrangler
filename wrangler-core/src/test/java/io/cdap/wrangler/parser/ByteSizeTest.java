package io.cdap.wrangler.parser;

import io.cdap.wrangler.api.parser.ByteSize;
import org.junit.Assert;
import org.junit.Test;

public class ByteSizeTest {

  @Test
  public void testBytes() {
    Assert.assertEquals(1024L, new ByteSize("1KB").getBytes());
    Assert.assertEquals(1048576L, new ByteSize("1MB").getBytes());
    Assert.assertEquals(1073741824L, new ByteSize("1GB").getBytes());
    Assert.assertEquals(512L, new ByteSize("512B").getBytes());
    Assert.assertEquals(1572864L, new ByteSize("1.5MB").getBytes());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInput() {
    new ByteSize("10XYZ");
  }
}
