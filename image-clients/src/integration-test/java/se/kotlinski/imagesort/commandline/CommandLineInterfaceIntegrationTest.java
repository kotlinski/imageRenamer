package se.kotlinski.imagesort.commandline;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.kotlinski.imagesort.commandline.argument.Interpreter;
import se.kotlinski.imagesort.commandline.argument.Transformer;
import se.kotlinski.imagesort.executor.FileMover;
import se.kotlinski.imagesort.forecaster.MediaFileOutputForecaster;
import se.kotlinski.imagesort.forecaster.date.DateToFileRenamer;
import se.kotlinski.imagesort.forecaster.date.FileDateInterpreter;
import se.kotlinski.imagesort.main.ImageSorter;
import se.kotlinski.imagesort.mapper.MediaFileDataMapper;
import se.kotlinski.imagesort.mapper.OutputMapper;
import se.kotlinski.imagesort.resolver.OutputConflictResolver;
import se.kotlinski.imagesort.utils.MediaFileHashGenerator;
import se.kotlinski.imagesort.utils.MediaFileTestUtil;
import se.kotlinski.imagesort.utils.MediaFileUtil;

import java.util.GregorianCalendar;

import static org.mockito.Mockito.spy;

public class CommandLineInterfaceIntegrationTest {
  private CommandLineInterface commandLineInterface;
  private MediaFileTestUtil mediaFileTestUtil;

  @Before
  public void setUp() throws Exception {
    MediaFileUtil mediaFileUtil = spy(new MediaFileUtil());
    mediaFileTestUtil = new MediaFileTestUtil(mediaFileUtil);
    MediaFileHashGenerator MediaFileHashGenerator = new MediaFileHashGenerator();

    HelpFormatter formatter = new HelpFormatter();
    CommandLineParser parser = new GnuParser();
    MediaFileUtil fileUtil = new MediaFileUtil();
    Transformer transformer = new Transformer(formatter, parser, fileUtil);
    Interpreter interpreter = spy(new Interpreter(transformer));
    DateToFileRenamer dateToFileRenamer = new DateToFileRenamer(new GregorianCalendar());
    FileDateInterpreter fileDateInterpreter = new FileDateInterpreter();

    OutputConflictResolver outputConflictResolver = new OutputConflictResolver(new MediaFileHashGenerator(),
                                                                               mediaFileUtil);
    FileMover fileMover = new FileMover();

    MediaFileDataMapper mediaFileHashMapTransformer = new MediaFileDataMapper(MediaFileHashGenerator);
    MediaFileOutputForecaster mediaFileOutputForecaster = new MediaFileOutputForecaster(
        dateToFileRenamer,
        fileDateInterpreter);
    OutputMapper mediaOutputCalculator = new OutputMapper(mediaFileOutputForecaster);
    ImageSorter imageSorter = new ImageSorter(mediaFileUtil,
                                              mediaFileHashMapTransformer,
                                              mediaOutputCalculator,
                                              outputConflictResolver,
                                              fileMover);

    commandLineInterface = new CommandLineInterface(interpreter, imageSorter);
  }

  @After
  public void tearDown() throws Exception {
    mediaFileTestUtil.cleanRestoreableMasterFolder();
  }

  @Test
  public void testRunCommandLine() throws Exception {
    mediaFileTestUtil.cleanRestoreableMasterFolder();
    mediaFileTestUtil.copyTestFilesToRestoreableDirectory();
    String[] arguments = new String[]{"-s", mediaFileTestUtil.getRestorableTestMasterPath()};

    commandLineInterface.runCommandLine(arguments);
  }
}