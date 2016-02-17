package se.kotlinski.imagesort.javafx.controllers.tabs;

import com.brsanthu.googleanalytics.PageViewHit;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import se.kotlinski.imagesort.data.RelativeMediaFolderOutput;
import se.kotlinski.imagesort.data.SortSettings;
import se.kotlinski.imagesort.feedback.FindDuplicatesFeedbackInterface;
import se.kotlinski.imagesort.feedback.PreMoveFeedbackInterface;
import se.kotlinski.imagesort.feedback.ReadFilesFeedbackInterface;
import se.kotlinski.imagesort.javafx.controllers.TabSwitcher;
import se.kotlinski.imagesort.main.ImageSorter;
import se.kotlinski.imagesort.module.ImageModule;

import java.io.File;
import java.util.List;
import java.util.Map;

public class SelectFolderTabController {

  public final Button selectFolderButton;
  public final Button continueButton;
  private final PreMoveFeedbackInterface preMoveFeedback;
  private final ReadFilesFeedbackInterface readFilesFeedbackInterface;
  private final FindDuplicatesFeedbackInterface findDuplicatesFeedbackInterface;
  private final TabSwitcher tabSwitcher;
  private final Text selectedFolderPathText;
  private final Button findDuplicatesContinueButton;
  private final ImageSorter imageSorter;
  private File selectedFolder;


  public SelectFolderTabController(final PreMoveFeedbackInterface preMoveFeedback,
                                   final ReadFilesFeedbackInterface readFilesFeedbackInterface,
                                   final FindDuplicatesFeedbackInterface findDuplicatesFeedbackInterface,
                                   final TabSwitcher tabSwitcher,
                                   final Button selectFolderButton,
                                   final Button continueButton,
                                   final Text selectedFolderPathText,
                                   final Button findDuplicatesContinueButton) {
    this.preMoveFeedback = preMoveFeedback;
    this.readFilesFeedbackInterface = readFilesFeedbackInterface;
    this.findDuplicatesFeedbackInterface = findDuplicatesFeedbackInterface;
    this.tabSwitcher = tabSwitcher;
    this.selectFolderButton = selectFolderButton;
    this.continueButton = continueButton;
    this.selectedFolderPathText = selectedFolderPathText;
    this.findDuplicatesContinueButton = findDuplicatesContinueButton;

    Injector injector = Guice.createInjector(new ImageModule());
    imageSorter = injector.getInstance(ImageSorter.class);

    setUpListeners();
  }

  private void setUpListeners() {
    continueButton.setOnAction((event) -> {
      if (selectedFolder != null) {
        tabSwitcher.setTabsInPreMoveMode();
        runPreMovePhase(selectedFolder);
      }
    });
    findDuplicatesContinueButton.setOnAction((event) -> {
      if (selectedFolder != null) {
        tabSwitcher.setTabsInFindDuplicatesMode();
        runFindDuplicates(selectedFolder);
      }
    });
  }

  public void setStageAndSetupListeners(final Stage primaryStage) {
    inputFolderSetup(primaryStage);
  }

  private void inputFolderSetup(final Stage primaryStage) {
    Injector injector = Guice.createInjector(new ImageModule());
    DirectoryChooser directoryChooser = injector.getInstance(DirectoryChooser.class);
    EventHandler<ActionEvent> selectInputEvent = e -> {
      tabSwitcher.resetTabs();
      File folder = directoryChooser.showDialog(primaryStage);
      selectedFolder = folder;
      if (folder != null) {
        selectedFolderPathText.setText(folder.getAbsolutePath());
        continueButton.setDisable(false);
        findDuplicatesContinueButton.setDisable(false);
      }
      else {
        selectedFolderPathText.setText("");
        continueButton.setDisable(true);
        findDuplicatesContinueButton.setDisable(true);
      }
    };
    selectFolderButton.setOnAction(selectInputEvent);
  }

  private void runPreMovePhase(final File folder) {
    SortSettings sortSettings = new SortSettings();
    sortSettings.masterFolder = folder;

    Task<Integer> task = new Task<Integer>() {
      @Override
      protected Integer call() throws Exception {
        preMoveFeedback.preMovePhaseInitiated();

        Map<List<File>, RelativeMediaFolderOutput> listRelativeMediaFolderOutputMap;
        listRelativeMediaFolderOutputMap = imageSorter.analyzeImages(readFilesFeedbackInterface,
                                                                     preMoveFeedback,
                                                                     sortSettings);

        preMoveFeedback.preMovePhaseComplete(listRelativeMediaFolderOutputMap, sortSettings);

        return 0;
      }
    };

    new Thread(task).start();
  }

  private void runFindDuplicates(final File folder) {
    SortSettings sortSettings = new SortSettings();
    sortSettings.masterFolder = folder;

    Task<Integer> task = new Task<Integer>() {
      @Override
      protected Integer call() throws Exception {
        imageSorter.findDuplicatedFileStatsInFolder(sortSettings,
                                                    readFilesFeedbackInterface,
                                                    findDuplicatesFeedbackInterface);

        return 0;
      }
    };

    new Thread(task).start();

  }

}
