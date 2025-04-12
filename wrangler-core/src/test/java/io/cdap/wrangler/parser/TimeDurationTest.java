package io.cdap.wrangler.parser;

import io.cdap.wrangler.api.parser.TimeDuration;
import org.junit.Assert;
import org.junit.Test;

public class TimeDurationTest {

  @Test
  public void testMilliseconds() {
    Assert.assertEquals(100L, new TimeDuration("100ms").getMilliseconds());
    Assert.assertEquals(2000L, new TimeDuration("2s").getMilliseconds());
    Assert.assertEquals(60000L, new TimeDuration("1m").getMilliseconds());
    Assert.assertEquals(3600000L, new TimeDuration("1h").getMilliseconds());
    Assert.assertEquals(90000L, new TimeDuration("1.5m").getMilliseconds());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInput() {
    new TimeDuration("5lightyears");
  }
}
