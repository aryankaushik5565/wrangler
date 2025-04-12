package io.cdap.directives.aggregates;

//import com.google.common.collect.ImmutableList;
import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.wrangler.api.Arguments;
import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.DirectiveExecutionException;
import io.cdap.wrangler.api.DirectiveParseException;
import io.cdap.wrangler.api.EntityCountMetric;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.annotations.Categories;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.parser.UsageDefinition;

import java.util.Collections;
import java.util.List;

/**
 * Aggregates byte size and time duration across all rows.
 */
@Plugin(type = Directive.TYPE)
@Name("aggregate-stats")
@Categories(categories = {"aggregate"})
@Description("Aggregates byte size and time duration columns into total values.")
public class AggregateStats implements Directive {

  private String byteSizeCol;
  private String timeDurationCol;
  private String targetByteCol;
  private String targetTimeCol;

  private long totalBytes = 0;
  private long totalMs = 0;

  @Override
  public UsageDefinition define() {
    UsageDefinition.Builder builder = UsageDefinition.builder("aggregate-stats");

    builder.define("byteColumn", TokenType.COLUMN_NAME);
    builder.define("timeColumn", TokenType.COLUMN_NAME);
    builder.define("targetByteCol", TokenType.COLUMN_NAME);
    builder.define("targetTimeCol", TokenType.COLUMN_NAME);

    return builder.build();
  }

  @Override
  public void initialize(Arguments args) throws DirectiveParseException {
    byteSizeCol = ((ColumnName) args.value("byteColumn")).value();
    timeDurationCol = ((ColumnName) args.value("timeColumn")).value();
    targetByteCol = ((ColumnName) args.value("targetByteCol")).value();
    targetTimeCol = ((ColumnName) args.value("targetTimeCol")).value();
  }

  @Override
  public List<Row> execute(List<Row> rows, ExecutorContext context) throws DirectiveExecutionException {
    for (Row row : rows) {
      Object byteValue = row.getValue(byteSizeCol);
      Object timeValue = row.getValue(timeDurationCol);

      if (byteValue != null) {
        ByteSize size = new ByteSize(byteValue.toString());
        totalBytes += size.getBytes();
      }

      if (timeValue != null) {
        TimeDuration duration = new TimeDuration(timeValue.toString());
        totalMs += duration.getMilliseconds();
      }
    }

    Row result = new Row();
    double totalMB = totalBytes / (1024.0 * 1024);
    double totalSec = totalMs / 1000.0;

    result.add(targetByteCol, totalMB);
    result.add(targetTimeCol, totalSec);

    return Collections.singletonList(result);
  }

  @Override
  public void destroy() {
    // no-op
  }

  @Override
  public List<EntityCountMetric> getCountMetrics() {
    return null;
  }
}
