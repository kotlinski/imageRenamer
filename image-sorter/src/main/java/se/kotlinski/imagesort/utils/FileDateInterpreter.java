package se.kotlinski.imagesort.utils;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.kotlinski.imagesort.exception.CouldNotParseDateException;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

public class FileDateInterpreter {

  private static final Logger LOGGER = LogManager.getLogger(FileDateInterpreter.class);

  Date getImageDate(File file) throws Exception {
    try {
      Metadata metadata = ImageMetadataReader.readMetadata(file);
      ExifSubIFDDirectory exifSubIFDDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
      int tagDatetimeOriginal = ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL;

      ExifIFD0Directory exifIFD0Directory;
      exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
      int tagDatetime = ExifIFD0Directory.TAG_DATETIME;
      if (exifSubIFDDirectory != null &&
          exifSubIFDDirectory.getDate(tagDatetimeOriginal, TimeZone.getDefault()) != null) {
        return exifSubIFDDirectory.getDate(tagDatetimeOriginal, TimeZone.getDefault());
      }
      if (exifIFD0Directory != null &&
          exifIFD0Directory.getDate(tagDatetime, TimeZone.getDefault()) != null) {
        return exifIFD0Directory.getDate(tagDatetime, TimeZone.getDefault());
      }
      throw new CouldNotParseDateException();
    }
    catch (ImageProcessingException | IOException e) {
      System.out.println("Failure in getImageDate(file)");
      throw new CouldNotParseDateException("File: " + file.getName());
    }
  }

  public Date getDate(final File file) throws Exception {
    try {
      return getImageDate(file);
    }
    catch (CouldNotParseDateException e) {
      LOGGER.error("File is not an image with meta data, " + file);
    }
    catch (Exception e) {
      System.err.println("Can't get date for : " + file);
    }
    try {
      return getVideoDate(file);
    }
    catch (CouldNotParseDateException e) {
      LOGGER.error("File is not an video with meta data, " + file);
    }
    catch (Exception e) {
      System.err.println("Can't get date for : " + file);
    }
    throw new CouldNotParseDateException("Could not Parse: " + file);
  }

  private Date getVideoDate(File videoFile) throws Exception {
    TimeZone.setDefault(TimeZone.getTimeZone("CET"));
    try {
      IsoFile isoFile = new IsoFile(videoFile.getAbsolutePath());
      MovieBox movieBox = isoFile.getMovieBox();
      MovieHeaderBox movieHeaderBox = movieBox.getMovieHeaderBox();
      System.out.println(movieHeaderBox);
      Date creationTime = movieHeaderBox.getCreationTime();
      System.out.println(creationTime);
      return creationTime;
    }
    catch (IOException | NullPointerException e) {
      LOGGER.error("File is not a parcelable mp4");
      throw new CouldNotParseDateException();
    }
  }
}
