package se.kotlinski.imagesort.feedback;

import se.kotlinski.imagesort.data.MediaFileDataHash;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface FindDuplicatesFeedbackInterface {

  void masterFolderSuccessfulParsed(final Map<MediaFileDataHash, List<File>> mediaFilesInFolder);

  void startGroupFilesByContent();

  void groupFilesByContentProgress(final int total, final int progress);

}
