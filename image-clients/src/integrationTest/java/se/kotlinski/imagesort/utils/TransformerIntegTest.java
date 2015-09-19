package se.kotlinski.imagesort.utils;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;
import se.kotlinski.imagesort.commandline.argument.Transformer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TransformerIntegTest {

  private Transformer transformer;
  private HelpFormatter formatter;

  @Before
  public void setUp() throws Exception {
    SortMasterFileUtil sortMasterFileUtil = new SortMasterFileUtil();
    CommandLineParser parser = new GnuParser();
    formatter = mock(HelpFormatter.class);
    transformer = new Transformer(formatter, parser, sortMasterFileUtil);
  }

  @Test
  public void testPrintHelp() throws Exception {
    Options options = mock(Options.class);
    transformer.printHelp(options);
    verify(formatter).printHelp("MainRenamer", options);
    assertThat("jul", is("jufdgl"));
  }
}