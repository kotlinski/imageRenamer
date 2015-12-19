package se.kotlinski.imagesort;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.kotlinski.imagesort.data.SortSettings;
import se.kotlinski.imagesort.parser.MediaFileParser;
import se.kotlinski.imagesort.transformer.MediaFileTransformer;
import se.kotlinski.imagesort.utils.DateToFileRenamer;
import se.kotlinski.imagesort.utils.FileDateInterpreter;
import se.kotlinski.imagesort.utils.MD5Generator;
import se.kotlinski.imagesort.utils.MediaFileTestUtil;
import se.kotlinski.imagesort.utils.MediaFileUtil;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FileExecutorTest {

  private static final Logger LOGGER = LogManager.getLogger(FileExecutorTest.class);
  private MediaFileParser mediaFileParser;
  private SortSettings sortSettings;
  private MD5Generator MD5Generator;
  private FileDateInterpreter fileDateInterpreter;
  private DateToFileRenamer dateToFileRenamer;
  private MediaFileTestUtil mediaFileTestUtil;


  @Before
  public void setUp() throws Exception {
    MediaFileUtil mediaFileUtil = new MediaFileUtil();
    mediaFileTestUtil = new MediaFileTestUtil(mediaFileUtil);

    sortSettings = new SortSettings();
    File file = new File(mediaFileTestUtil.getTestInputPath());
    sortSettings.masterFolder = new File(mediaFileTestUtil.getRestorableTestMasterPath());
    Calendar calendar = new GregorianCalendar();
    MD5Generator = spy(new MD5Generator());
    fileDateInterpreter = mock(FileDateInterpreter.class);
    dateToFileRenamer = mock(DateToFileRenamer.class);
    MediaFileTransformer mediaFileTransformer = mock(MediaFileTransformer.class);
    mediaFileParser = new MediaFileParser(mediaFileUtil, MD5Generator);

    File outputFolder = new File(mediaFileTestUtil.getRestorableTestMasterPath());
    deleteFolderContent(outputFolder);
  }

  @After
  public void tearDown() throws Exception {
    File outputFolder = new File(mediaFileTestUtil.getRestorableTestMasterPath());
    deleteFolderContent(outputFolder);
  }

  @Test
  public void testCopyFiles() throws Exception {
/*    FileExecutor fileExecutor = spy(new FileExecutor());
    doThrow(new IOException()).when(fileExecutor).createNewFile(any(File.class), any(String.class));
    DeprecatedExportFileDataMap exportFileDataMap = mediaFileParser.transformFilesToMediaFiles(sortSettings);
    DeprecatedFileCopyReport fileCopyReport = fileExecutor.copyFiles(exportFileDataMap, sortSettings);
    assertThat(fileCopyReport, is(nullValue()));
    Assert.assertEquals(0, fileCopyReport.getNumberOfFilesCopied());
    Assert.assertEquals(9, fileCopyReport.getFilesNotCopied().size());

    File outputFolder = new File(new MediaFileUtil().getRestorableTestMasterPath());
    deleteFolderContent(outputFolder);
    DeprecatedExportFileDataMap exportFileDataMap2 = mediaFileParser.transformFilesToMediaFiles(sortSettings);
    fileExecutor.copyFiles(exportFileDataMap2, sortSettings);
    String[] list = outputFolder.list();
    for (String file : list) {
      LOGGER.debug(file);
    }
    Assert.assertEquals(5, list.length);*/
  }

  @Test
  public void sunShineTest() throws Exception {

    File outputFolder = new File(mediaFileTestUtil.getRestorableTestMasterPath());
    deleteFolderContent(outputFolder);
/*
    DeprecatedExportFileDataMap exportFileDataMap = mediaFileParser.transformFilesToMediaFiles(sortSettings);
    fileExecutor.copyFiles(exportFileDataMap, sortSettings);
    String[] list = outputFolder.list();
    for (String file : list) {
      LOGGER.debug(file);
    }
    Assert.assertEquals(1, list.length);*/
  }

  private void deleteFolderContent(File folder) {
    File[] files = folder.listFiles();
    if (files != null) { //some JVMs return null for empty dirs
      for (File file : files) {
        if (file.isDirectory()) {
          deleteFolderContent(file);
        }
        else {
          if (!".gitignore".equals(file.getName())) {
            boolean delete = file.delete();
            if (delete) {
              LOGGER.error("Delete: " + file.getName());
            }
            else {
              LOGGER.error("Could not delete: " + file.getName());
            }

          }
        }
      }
    }
  }
}