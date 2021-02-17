package application;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.prefs.Preferences;

public class UICreator {

    private ObservableList<Movie> movieData;
    private IHandler handler;
    private Preferences prefs;
    private File f;

    public UICreator(Stage stage) {
        prefs = Preferences.userNodeForPackage(this.getClass());

        create(stage);
    }

    public void createFileDialog(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        f = fileChooser.showSaveDialog(stage);
        try {
            prefs.putBoolean("FILE_SELECTED", true);
            prefs.put("PATH", f.getAbsolutePath());
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void create(Stage primaryStage) {
        GridPane grid = new GridPane();

        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(15);
        grid.getColumnConstraints().add(column);

        column = new ColumnConstraints();
        column.setPercentWidth(15);
        grid.getColumnConstraints().add(column);

        column = new ColumnConstraints();
        column.setPercentWidth(70);
        grid.getColumnConstraints().add(column);
        // grid.setPrefSize(1000, 400);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15, 15, 15, 15));

        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu("Загрузить");

        MenuItem fileMItem = new MenuItem("Из файла");
        menuFile.getItems().addAll(fileMItem);

        Menu menuHelp = new Menu("Помощь");
        MenuItem aboutMItem = new MenuItem("О программе");
        menuHelp.getItems().addAll(aboutMItem);

        menuBar.getMenus().addAll(menuFile, menuHelp);

        Button selectAllBtn = new Button("Выбрать все");
        selectAllBtn.setMaxWidth(Double.MAX_VALUE);
        grid.add(selectAllBtn, 0, 3);

        Button addBtn = new Button("Добавить фильм");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        grid.add(addBtn, 0, 4);

        Button delBtn = new Button("Удалить фильм");
        delBtn.setMaxWidth(Double.MAX_VALUE);
        grid.add(delBtn, 0, 5);

        Button editBtn = new Button("Редактировать фильм");
        editBtn.setMaxWidth(Double.MAX_VALUE);
        grid.add(editBtn, 0, 6);

        Button filterBtn = new Button("Фильтр");
        filterBtn.setMaxWidth(Double.MAX_VALUE);
        grid.add(filterBtn, 0, 7);

        Text infoTxt = new Text("Запущено");
        grid.add(infoTxt, 0, 12);

        Text numberOfRowsTxt = new Text();
        grid.add(numberOfRowsTxt, 1, 12);

        TableView<Movie> movieTable = new TableView<>();

        TableColumn nameCol = new TableColumn<>("Название");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.prefWidthProperty().bind(movieTable.widthProperty().multiply(0.18));

        TableColumn directorCol = new TableColumn<>("Режиссер");
        directorCol.setCellValueFactory(new PropertyValueFactory<>("director"));
        directorCol.prefWidthProperty().bind(movieTable.widthProperty().multiply(0.10));

        TableColumn countryCol = new TableColumn<>("Страна");
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));

        TableColumn yearCol = new TableColumn<>("Год");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn ratingCol = new TableColumn<>("Рейтинг");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        ratingCol.prefWidthProperty().bind(movieTable.widthProperty().multiply(0.05));

        TableColumn actorsCol = new TableColumn<>("Актеры");
        actorsCol.setCellValueFactory(new PropertyValueFactory<>("actors"));
        actorsCol.prefWidthProperty().bind(movieTable.widthProperty().multiply(0.30));

        TableColumn languageCol = new TableColumn<>("Язык");
        languageCol.setCellValueFactory(new PropertyValueFactory<>("language"));

        TableColumn durationCol = new TableColumn<>("Длительность");
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationCol.prefWidthProperty().bind(movieTable.widthProperty().multiply(0.05));

        TableColumn seenCol = new TableColumn<>("По произведению");
        seenCol.setCellValueFactory(new PropertyValueFactory<>("seen"));

        movieTable.getColumns().addAll(nameCol, directorCol, countryCol, yearCol, ratingCol, actorsCol, languageCol, durationCol, seenCol);

        grid.add(movieTable, 1, 2, 10, 8);

        fileMItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (prefs.getBoolean("FILE_SELECTED", false)) { // prompts the
                    // user for
                    // creating a
                    // file to save
                    // movies
                    f = new File("movies.csv");
                } else {
                    createFileDialog(primaryStage);
                }

                handler = new FileHandler(f);

                infoTxt.setText("Файловый режим.");
                selectAllBtn.fire();
            }
        });

        aboutMItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Alert aboutAlert = new Alert(AlertType.INFORMATION);
                aboutAlert.setTitle("О программе");
                aboutAlert.setHeaderText(null);
                aboutAlert.setContentText(
                        "Система запросов поиска фильмов\n\n2020 г.");

                aboutAlert.showAndWait();
            }
        });

        selectAllBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (handler == null)
                    infoTxt.setText("Ошибка - не подключено.");
                else {
                    movieData = FXCollections.observableArrayList(handler.selectAllMovies());
                    movieTable.setItems(movieData);
                    movieTable.refresh();

                    infoTxt.setText("Выбрано все.");
                    numberOfRowsTxt.setText(Integer.toString(movieTable.getItems().size()));
                }
            }
        });

        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Dialog<Movie> dialog = new Dialog<>();
                dialog.setTitle("Добавить название.");

                // Set the button types.
                ButtonType okBtn = new ButtonType("Добавить", ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(okBtn, ButtonType.CANCEL);

                // Create the username and password labels and fields.
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 20, 10, 10));

                TextField nameTxt = new TextField();
                TextField dirTxt = new TextField();
                TextField yearTxt = new TextField();
                TextField durTxt = new TextField();
                TextField counTxt = new TextField();
                TextField actTxt = new TextField();
                TextField langTxt = new TextField();
                TextField raitTxt = new TextField();
                CheckBox seenBox = new CheckBox();

                grid.add(new Label("Название:"), 0, 0);
                grid.add(nameTxt, 1, 0);
                grid.add(new Label("Режиссер:"), 0, 1);
                grid.add(dirTxt, 1, 1);
                grid.add(new Label("Страна:"), 0, 2);
                grid.add(counTxt, 1, 2);
                grid.add(new Label("Год:"), 0, 3);
                grid.add(yearTxt, 1, 3);
                grid.add(new Label("Рейтинг:"), 0, 4);
                grid.add(raitTxt, 1, 4);
                grid.add(new Label("Актеры:"), 0, 5);
                grid.add(actTxt, 1, 5);
                grid.add(new Label("Язык:"), 0, 6);
                grid.add(langTxt, 1, 6);
                grid.add(new Label("Длительность:"), 0, 7);
                grid.add(durTxt, 1, 7);
                grid.add(new Label("По произведению:"), 0, 8);
                grid.add(seenBox, 1, 8);

                dialog.getDialogPane().setContent(grid);

                // Request focus on the username field by default.
                Platform.runLater(() -> nameTxt.requestFocus());

                dialog.setResultConverter(dialogButton -> {
                    int i = 0;
                    if (seenBox.isSelected()) {
                        i = 1;
                    }
                    if (dialogButton == okBtn) {
                        return new Movie(nameTxt.getText(), dirTxt.getText(), counTxt.getText(), actTxt.getText(), langTxt.getText(), yearTxt.getText(), durTxt.getText(), i, raitTxt.getText()
                        );

                    }
                    return null;
                });

                Optional<Movie> result = dialog.showAndWait();

                result.ifPresent(m -> {
                    System.out.println("Name=" + m.getName() + " Dir=" + m.getDirector() + " Countr=" + m.getCountry() + " Year=" + m.getYear()
                            + " Rait=" + m.getRating()
                            + " Act=" + m.getActors()
                            + " lang=" + m.getActors()
                            + " Year=" + m.getYear()
                            + " Dur=" + m.getDuration()
                            + " Seen=" + m.getSeen());
                    String resultText = (handler.addMovie(m));

                    infoTxt.setText(resultText);
                    selectAllBtn.fire();
                    numberOfRowsTxt.setText(Integer.toString(movieTable.getItems().size()));

                });

            }
        });
        delBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Movie selMov = movieTable.getSelectionModel().getSelectedItem();
                System.out.println(
                        "DELETE FROM movie WHERE name=\"" + selMov.getName() + "\" AND year=" + selMov.getYear() + ";");
                handler.delMovie(selMov);

                /** if not exception raised **/
                infoTxt.setText("Фильм удален.");
                selectAllBtn.fire();
                numberOfRowsTxt.setText(Integer.toString(movieTable.getItems().size()));
            }
        });

        editBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Dialog<Movie> dialog = new Dialog<>();
                dialog.setTitle("Редактировать фильм.");

                Movie selMov = movieTable.getSelectionModel().getSelectedItem();
                if (selMov == null) {
                    infoTxt.setText("Фильм не выбран.");
                    return;
                }

                // Set the button types.
                ButtonType okBtn = new ButtonType("Редактировать", ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(okBtn, ButtonType.CANCEL);

                // Create the username and password labels and fields.
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 20, 10, 10));

                TextField nameTxt = new TextField(selMov.getName());
                TextField dirTxt = new TextField(selMov.getDirector());
                TextField yearTxt = new TextField(Integer.toString(selMov.getYear()));
                TextField durTxt = new TextField(Integer.toString(selMov.getDuration()));
                TextField counTxt = new TextField(selMov.getCountry());
                TextField actTxt = new TextField(selMov.getActors());
                TextField langTxt = new TextField(selMov.getLanguage());
                TextField raitTxt = new TextField(Float.toString(selMov.getRating()));
                CheckBox seenBox = new CheckBox();
                if (selMov.getSeen() == 1)
                    seenBox.setSelected(true);

                grid.add(new Label("Название:"), 0, 0);
                grid.add(nameTxt, 1, 0);
                grid.add(new Label("Режиссер:"), 0, 1);
                grid.add(dirTxt, 1, 1);
                grid.add(new Label("Страна:"), 0, 2);
                grid.add(counTxt, 1, 2);
                grid.add(new Label("Год:"), 0, 3);
                grid.add(yearTxt, 1, 3);
                grid.add(new Label("Рейтинг:"), 0, 4);
                grid.add(raitTxt, 1, 4);
                grid.add(new Label("Актеры:"), 0, 5);
                grid.add(actTxt, 1, 5);
                grid.add(new Label("Язык:"), 0, 6);
                grid.add(langTxt, 1, 6);
                grid.add(new Label("Длительность:"), 0, 7);
                grid.add(durTxt, 1, 7);
                grid.add(new Label("По произведению:"), 0, 8);
                grid.add(seenBox, 1, 8);

                dialog.getDialogPane().setContent(grid);

                // Request focus on the name field by default.
                Platform.runLater(() -> nameTxt.requestFocus());

                dialog.setResultConverter(dialogButton -> {
                    int i = 0;
                    if (seenBox.isSelected()) {
                        i = 1;
                    }
                    if (dialogButton == okBtn) {
                        return new Movie(nameTxt.getText(), dirTxt.getText(), counTxt.getText(), actTxt.getText(), langTxt.getText(), yearTxt.getText(), durTxt.getText(), i, raitTxt.getText()
                        );
                    }
                    return null;
                });

                Optional<Movie> result = dialog.showAndWait();

                result.ifPresent(m -> {
                    System.out.println("UPDATE movie SET name=\"" + m.getName() + "\", year=" + m.getYear() + ", seen="
                            + m.getSeen() + ", director=(SELECT dirKey FROM director WHERE name=\"" + m.getDirector()
                            + "\"), duration=" + m.getDuration() + " WHERE name=\"" + selMov.getName() + "\" AND year="
                            + selMov.getYear() + ";");
                    System.out.println("UPDATE director SET name=\"" + m.getDirector() + "\" WHERE name=\""
                            + selMov.getDirector() + "\";");
                    handler.editMovie(m, selMov);

                    infoTxt.setText("Фильм отредактирован.");
                    selectAllBtn.fire();
                    numberOfRowsTxt.setText(Integer.toString(movieTable.getItems().size()));

                });

            }
        });

        filterBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Dialog<Movie> dialog = new Dialog<>();
                dialog.setTitle("Фильтр");

                // Set the button types.
                ButtonType okBtn = new ButtonType("Фильтр", ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(okBtn, ButtonType.CANCEL);

                // Create the username and password labels and fields.
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 20, 10, 10));

                TextField nameTxt = new TextField();
                TextField dirTxt = new TextField();
                TextField yearTxt = new TextField();
                TextField durTxt = new TextField();
                TextField counTxt = new TextField();
                TextField actTxt = new TextField();
                TextField langTxt = new TextField();
                CheckBox seenBox = new CheckBox();

                grid.add(new Label("Название:"), 0, 0);
                grid.add(nameTxt, 1, 0);
                grid.add(new Label("Режиссер:"), 0, 1);
                grid.add(dirTxt, 1, 1);
                grid.add(new Label("Страна:"), 0, 2);
                grid.add(counTxt, 1, 2);
                grid.add(new Label("Год:"), 0, 3);
                grid.add(yearTxt, 1, 3);
                grid.add(new Label("Актеры:"), 0, 4);
                grid.add(actTxt, 1, 4);
                grid.add(new Label("Язык:"), 0, 5);
                grid.add(langTxt, 1, 5);
                grid.add(new Label("Длительность:"), 0, 6);
                grid.add(durTxt, 1, 6);
                grid.add(new Label("По произведению:"), 0, 7);
                grid.add(seenBox, 1, 7);

                dialog.getDialogPane().setContent(grid);

                // Request focus on the username field by default.
                Platform.runLater(() -> nameTxt.requestFocus());

                dialog.setResultConverter(dialogButton -> {
                    int i = 0;
                    if (dialogButton == okBtn) {
                    	//set defaults for filtering
                    	String name = "null";
                    	String dir = "null";
                    	String country = "null";
                    	String actors = "null";
                    	String lang = "null";
                    	String year = "0";
                    	String dur = "0";
                    	String rait = "0.0";
                        if (nameTxt.getText().length() != 0)
							name = nameTxt.getText();

                            if (dirTxt.getText().length() != 0)
                            	dir = dirTxt.getText();

                            	if (counTxt.getText().length() != 0)
                            		country = counTxt.getText();

                                    if (actTxt.getText().length() != 0)
                                    	actors = actTxt.getText();

                                        if (langTxt.getText().length() != 0)
                                        	lang = langTxt.getText();

                                            if (yearTxt.getText().length() != 0)
                                            	year = yearTxt.getText();

                                                if (durTxt.getText().length() != 0)
                                                	dur = durTxt.getText();

                                                    if (seenBox.isSelected())
                                                          i = 1;

                                                    return new Movie(name, dir, country, actors, lang, year, dur, i, rait
                                                    );
                    }
                    return null;
                });

                Optional<Movie> result = dialog.showAndWait();

                result.ifPresent(m -> {
                    movieData = FXCollections.observableArrayList(handler.filterMovies(m));
                    movieTable.setItems(movieData);
                    movieTable.refresh();

                    infoTxt.setText("Фильмы отфильтрованы.");
                    numberOfRowsTxt.setText(Integer.toString(movieTable.getItems().size()));
                });

            }
        });

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if (handler != null)
                    handler.closeConn();
            }
        });

        Scene scene = new Scene(new VBox(), 1200, 500);
        ((VBox) scene.getRoot()).getChildren().addAll(menuBar, grid);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Система запросов поиска фильмов");

        primaryStage.show();
    }
}
