package evaluator.ui;

import evaluator.controller.DashboardController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;

public class Dashboard {

    private final BorderPane root;

    public Dashboard() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        // TOP BAR
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(14, 20, 14, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #12122a; -fx-border-color: #2a2a4a; "
                + "-fx-border-width: 0 0 1 0;");

        Text algoText = new Text("Algorithm ");
        algoText.setFill(Color.WHITE);
        algoText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

        Text perfText = new Text("Performance ");
        perfText.setFill(Color.web("#00d4ff"));
        perfText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

        Text evalText = new Text("Evaluator");
        evalText.setFill(Color.WHITE);
        evalText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

        TextFlow titleFlow = new TextFlow(algoText, perfText, evalText);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ToggleGroup modeGroup = new ToggleGroup();
        ToggleButton manualBtn = new ToggleButton("Manual");
        ToggleButton autoBtn   = new ToggleButton("Auto");
        manualBtn.setToggleGroup(modeGroup);
        autoBtn.setToggleGroup(modeGroup);
        manualBtn.setSelected(true);
        styleToggleButton(manualBtn);
        styleToggleButton(autoBtn);

        modeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) oldVal.setSelected(true);
        });

        Button runBtn = new Button("▶  Run Analysis");
        runBtn.setStyle(
                "-fx-background-color: #00d4ff; -fx-text-fill: #0d0d1a; "
                + "-fx-font-weight: bold; -fx-font-size: 13px; "
                + "-fx-padding: 8 20 8 20; -fx-background-radius: 6;");
        runBtn.setOnMouseEntered(e -> runBtn.setStyle(
                "-fx-background-color: #00b8d9; -fx-text-fill: #0d0d1a; "
                + "-fx-font-weight: bold; -fx-font-size: 13px; "
                + "-fx-padding: 8 20 8 20; -fx-background-radius: 6;"));
        runBtn.setOnMouseExited(e -> runBtn.setStyle(
                "-fx-background-color: #00d4ff; -fx-text-fill: #0d0d1a; "
                + "-fx-font-weight: bold; -fx-font-size: 13px; "
                + "-fx-padding: 8 20 8 20; -fx-background-radius: 6;"));

        topBar.getChildren().addAll(titleFlow, spacer, manualBtn, autoBtn, runBtn);

        // LEFT PANEL
        VBox inputPanel = new VBox(10);
        inputPanel.setPadding(new Insets(16));
        inputPanel.setStyle("-fx-background-color: #12122a;");

        HBox editorHeader = new HBox(10);
        editorHeader.setAlignment(Pos.CENTER_LEFT);

        Label editorTitle = new Label("CODE EDITOR");
        editorTitle.setStyle("-fx-text-fill: #8888aa; -fx-font-size: 11px; "
                + "-fx-font-weight: bold;");

        Label pythonBadge = new Label("Python");
        pythonBadge.setStyle(
                "-fx-background-color: #2a2a4a; -fx-text-fill: #00d4ff; "
                + "-fx-font-size: 11px; -fx-padding: 2 8 2 8; "
                + "-fx-background-radius: 4;");

        editorHeader.getChildren().addAll(editorTitle, pythonBadge);

        TextArea codeArea = new TextArea(
                "# Write your algorithm inside this function\n"
                + "def my_algorithm(arr):\n"
                + "    n = len(arr)\n"
                + "    for i in range(n):\n"
                + "        for j in range(i+1, n):\n"
                + "            if arr[i] > arr[j]:\n"
                + "                arr[i], arr[j] = arr[j], arr[i]\n"
                + "    return arr\n\n"
                + "#\n"
                + "# arr is injected automatically");
        codeArea.setStyle(
                "-fx-control-inner-background: #0d0d1a; "
                + "-fx-text-fill: #e0e0ff; "
                + "-fx-font-family: 'Courier New'; "
                + "-fx-font-size: 13px; "
                + "-fx-background-color: #0d0d1a; "
                + "-fx-border-color: #2a2a4a; -fx-border-radius: 6; "
                + "-fx-background-radius: 6;");
        VBox.setVgrow(codeArea, Priority.ALWAYS);

        // Bottom Bar
        HBox bottomBar = new HBox(10);
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setPadding(new Insets(8, 0, 0, 0));

        Label arrayLabel = new Label("Array:");
        arrayLabel.setStyle("-fx-text-fill: #8888aa; -fx-font-size: 12px;");

        TextField arrayField = new TextField();
        arrayField.setPromptText("1 5 3 2 9");
        arrayField.setPrefWidth(130);
        arrayField.setMaxWidth(130);
        arrayField.setStyle(
                "-fx-control-inner-background: #1e1e3a; -fx-text-fill: #e0e0ff; "
                + "-fx-prompt-text-fill: #555577; "
                + "-fx-border-color: #2a2a4a; -fx-border-radius: 4; "
                + "-fx-background-radius: 4;");

        Label sizeLabel = new Label("Size (n):");
        sizeLabel.setStyle("-fx-text-fill: #8888aa; -fx-font-size: 12px;");

        TextField sizeField = new TextField();
        sizeField.setPromptText("e.g. 9");
        sizeField.setPrefWidth(70);
        sizeField.setMaxWidth(70);
        sizeField.setStyle(
                "-fx-control-inner-background: #1e1e3a; -fx-text-fill: #e0e0ff; "
                + "-fx-prompt-text-fill: #555577; "
                + "-fx-border-color: #2a2a4a; -fx-border-radius: 4; "
                + "-fx-background-radius: 4;");

        Label nLabel = new Label("n = 0");
        nLabel.setStyle(
                "-fx-background-color: #1e1e3a; -fx-text-fill: #00d4ff; "
                + "-fx-font-size: 13px; -fx-padding: 4 14 4 14; "
                + "-fx-background-radius: 4;");

        sizeField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty())
                nLabel.setText("n = 0");
            else {
                try {
                    int n = Integer.parseInt(newVal.trim());
                    nLabel.setText("n = " + n);
                } catch (NumberFormatException ex) {
                    nLabel.setText("n = ?");
                }
            }
        });

        bottomBar.getChildren().addAll(arrayLabel, arrayField, sizeLabel, sizeField, nLabel);

        inputPanel.getChildren().addAll(editorHeader, codeArea, bottomBar);
        HBox.setHgrow(inputPanel, Priority.ALWAYS);
        

        Label bestLabel  = new Label("O(n)");
        Label avgLabel   = new Label("O(n²)");
        Label worstLabel = new Label("O(n²)");

        // RIGHT PANEL
        VBox resultPanel = new VBox(16);
        resultPanel.setPadding(new Insets(16));
        resultPanel.setPrefWidth(340);
        resultPanel.setStyle("-fx-background-color: #12122a; "
                + "-fx-border-color: #2a2a4a; -fx-border-width: 0 0 0 1;");

        Label resultsTitle = new Label("ANALYSIS RESULTS");
        resultsTitle.setStyle("-fx-text-fill: #8888aa; -fx-font-size: 11px; "
                + "-fx-font-weight: bold;");

        VBox complexityCard = new VBox(6);
        complexityCard.setPadding(new Insets(12));
        complexityCard.setStyle(
                "-fx-background-color: #1a1a2e; -fx-background-radius: 8; "
                + "-fx-border-color: #2a2a4a; -fx-border-radius: 8;");

        Label detectedLabel = new Label("DETECTED COMPLEXITY");
        detectedLabel.setStyle("-fx-text-fill: #8888aa; -fx-font-size: 10px; "
                + "-fx-font-weight: bold;");

        Label complexityValue = new Label("O(n²)");
        complexityValue.setStyle("-fx-text-fill: #00d4ff; -fx-font-size: 28px; "
                + "-fx-font-weight: bold;");

        Label complexityDesc = new Label("Quadratic — nested loop pattern detected");
        complexityDesc.setStyle("-fx-text-fill: #aaaacc; -fx-font-size: 11px;");

        Label confidenceLabel = new Label("Confidence");
        confidenceLabel.setStyle("-fx-text-fill: #8888aa; -fx-font-size: 10px;");

        ProgressBar confidenceBar = new ProgressBar(0.92);
        confidenceBar.setPrefWidth(Double.MAX_VALUE);
        confidenceBar.setStyle("-fx-accent: #00d4ff;");

        complexityCard.getChildren().addAll(
                detectedLabel, complexityValue, complexityDesc,
                confidenceLabel, confidenceBar);

        Label candidateLabel1 = new Label("O(n²)");
        Label candidateLabel2 = new Label("O(n² log n)");
        Label candidateLabel3 = new Label("O(n³)");

        ProgressBar candidateBar1 = new ProgressBar(0.90);
        ProgressBar candidateBar2 = new ProgressBar(0.30);
        ProgressBar candidateBar3 = new ProgressBar(0.10);

        // Candidate Card
        VBox candidateCard = new VBox(8);
        candidateCard.setPadding(new Insets(12));
        candidateCard.setStyle(
                "-fx-background-color: #1a1a2e; -fx-background-radius: 8; "
                + "-fx-border-color: #2a2a4a; -fx-border-radius: 8;");

        Label candidateTitle = new Label("CANDIDATE CLASSES");
        candidateTitle.setStyle("-fx-text-fill: #8888aa; -fx-font-size: 10px; "
                + "-fx-font-weight: bold;");

        candidateCard.getChildren().addAll(
                candidateTitle,
                candidateRow(candidateLabel1, candidateBar1, "#00d4ff"),
                candidateRow(candidateLabel2, candidateBar2, "#4444cc"),
                candidateRow(candidateLabel3, candidateBar3, "#cc4444"));

        // Chart Card
        VBox chartCard = new VBox(8);
        chartCard.setPadding(new Insets(12));
        chartCard.setStyle(
                "-fx-background-color: #1a1a2e; -fx-background-radius: 8; "
                + "-fx-border-color: #2a2a4a; -fx-border-radius: 8;");
        VBox.setVgrow(chartCard, Priority.ALWAYS);

        Label chartTitle = new Label("RUNTIME ACROSS INPUT SIZES");
        chartTitle.setStyle("-fx-text-fill: #8888aa; -fx-font-size: 10px; "
                + "-fx-font-weight: bold;");

        // X and Y Axes
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Input Size (n)");
        xAxis.setStyle("-fx-tick-label-fill: #8888aa; -fx-font-size: 10px;");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Time (ms)");
        yAxis.setStyle("-fx-tick-label-fill: #8888aa; -fx-font-size: 10px;");

        // LineChart
        LineChart<Number, Number> runtimeChart = new LineChart<>(xAxis, yAxis);
        runtimeChart.setStyle("-fx-background-color: #0d0d1a;");
        runtimeChart.setLegendVisible(false);
        runtimeChart.setAnimated(false);
        runtimeChart.setPrefHeight(160);
        runtimeChart.setCreateSymbols(true);
        VBox.setVgrow(runtimeChart, Priority.ALWAYS);

        // BEST / AVG / WORST 
        HBox statsRow = new HBox();
        statsRow.setAlignment(Pos.CENTER);
        statsRow.getChildren().addAll(
                statBox("BEST",  bestLabel),
                statBox("AVG",   avgLabel),
                statBox("WORST", worstLabel));

        chartCard.getChildren().addAll(chartTitle, runtimeChart, statsRow);

        resultPanel.getChildren().addAll(resultsTitle, complexityCard, candidateCard, chartCard);

        HBox centerLayout = new HBox();
        centerLayout.getChildren().addAll(inputPanel, resultPanel);

        root.setTop(topBar);
        root.setCenter(centerLayout);

        // CONNECT CONTROLLER
        DashboardController controller = new DashboardController(
                codeArea, arrayField, sizeField,
                manualBtn, runBtn,
                complexityValue, complexityDesc, confidenceBar,
                bestLabel, avgLabel, worstLabel,
                candidateBar1, candidateBar2, candidateBar3,
                candidateLabel1, candidateLabel2, candidateLabel3,
                runtimeChart   
        );

        runBtn.setOnAction(e -> controller.onRunAnalysis());
    }

    private void styleToggleButton(ToggleButton btn) {
        String base     = "-fx-background-color: #1e1e3a; -fx-text-fill: #aaaacc; "
                        + "-fx-font-size: 13px; -fx-padding: 7 22 7 22; "
                        + "-fx-background-radius: 6; -fx-border-color: #2a2a4a; "
                        + "-fx-border-radius: 6;";
        String selected = "-fx-background-color: #2a2a5a; -fx-text-fill: #ffffff; "
                        + "-fx-font-size: 13px; -fx-font-weight: bold; "
                        + "-fx-padding: 7 22 7 22; -fx-background-radius: 6; "
                        + "-fx-border-color: #00d4ff; -fx-border-radius: 6;";

        btn.setStyle(btn.isSelected() ? selected : base);
        btn.selectedProperty().addListener((obs, wasSelected, isSelected) ->
                btn.setStyle(isSelected ? selected : base));
    }

    private HBox candidateRow(Label lbl, ProgressBar bar, String color) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        lbl.setStyle("-fx-text-fill: #ccccee; -fx-font-size: 12px; -fx-pref-width: 90;");

        bar.setPrefWidth(Double.MAX_VALUE);
        bar.setStyle("-fx-accent: " + color + ";");
        HBox.setHgrow(bar, Priority.ALWAYS);

        row.getChildren().addAll(lbl, bar);
        return row;
    }

    private VBox statBox(String title, Label valueLabel) {
        VBox box = new VBox(2);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(100);

        Label t = new Label(title);
        t.setStyle("-fx-text-fill: #8888aa; -fx-font-size: 10px; -fx-font-weight: bold;");
        valueLabel.setStyle("-fx-text-fill: #00d4ff; -fx-font-size: 13px; -fx-font-weight: bold;");

        box.getChildren().addAll(t, valueLabel);
        return box;
    }

    public BorderPane getView() {
        return root;
    }
}