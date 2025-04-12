package io.cdap.directives.aggregates;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.TestingRig;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AggregateStatsTest {

  @Test
  public void testAggregateStats() throws Exception {
    List<Row> rows = Arrays.asList(
      new Row().add("data_size", "1MB").add("duration", "500ms"),
      new Row().add("data_size", "2MB").add("duration", "1500ms")
    );

    String[] recipe = {
      "aggregate-stats :data_size :duration total_size_mb total_time_sec"
    };

    List<Row> result = TestingRig.execute(recipe, rows);

    Assert.assertEquals(1, result.size());

    double size = (Double) result.get(0).getValue("total_size_mb");
    double time = (Double) result.get(0).getValue("total_time_sec");

    Assert.assertEquals(3.0, size, 0.001);
    Assert.assertEquals(2.0, time, 0.001);
  }
}
