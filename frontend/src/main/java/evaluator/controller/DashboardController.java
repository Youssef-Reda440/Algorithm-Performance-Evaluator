package evaluator.controller;

import evaluator.model.AnalysisResponse;
import evaluator.network.ApiClient;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

public class DashboardController {

    // UI References
    private final TextArea      codeArea;
    private final TextField     arrayField;
    private final TextField     sizeField;
    private final ToggleButton  manualBtn;
    private final Button        runBtn;

    // Result UI References 
    private final Label         complexityValue;
    private final Label         complexityDesc;
    private final ProgressBar   confidenceBar;
    private final Label         bestLabel;
    private final Label         avgLabel;
    private final Label         worstLabel;

    // Candidate Classes
    private final ProgressBar   candidateBar1;
    private final ProgressBar   candidateBar2;
    private final ProgressBar   candidateBar3;
    private final Label         candidateLabel1;
    private final Label         candidateLabel2;
    private final Label         candidateLabel3;

    // Chart 
    private final LineChart<Number, Number> runtimeChart;

    private final ApiClient apiClient = new ApiClient();

    public DashboardController(
            TextArea     codeArea,
            TextField    arrayField,
            TextField    sizeField,
            ToggleButton manualBtn,
            Button       runBtn,
            Label        complexityValue,
            Label        complexityDesc,
            ProgressBar  confidenceBar,
            Label        bestLabel,
            Label        avgLabel,
            Label        worstLabel,
            ProgressBar  candidateBar1,
            ProgressBar  candidateBar2,
            ProgressBar  candidateBar3,
            Label        candidateLabel1,
            Label        candidateLabel2,
            Label        candidateLabel3,
            LineChart<Number, Number> runtimeChart) {

        this.codeArea        = codeArea;
        this.arrayField      = arrayField;
        this.sizeField       = sizeField;
        this.manualBtn       = manualBtn;
        this.runBtn          = runBtn;
        this.complexityValue = complexityValue;
        this.complexityDesc  = complexityDesc;
        this.confidenceBar   = confidenceBar;
        this.bestLabel       = bestLabel;
        this.avgLabel        = avgLabel;
        this.worstLabel      = worstLabel;
        this.candidateBar1   = candidateBar1;
        this.candidateBar2   = candidateBar2;
        this.candidateBar3   = candidateBar3;
        this.candidateLabel1 = candidateLabel1;
        this.candidateLabel2 = candidateLabel2;
        this.candidateLabel3 = candidateLabel3;
        this.runtimeChart    = runtimeChart;
    }

    public void onRunAnalysis() {

        // Validate inputs 
        String code = codeArea.getText().trim();
        if (code.isEmpty()) {
            showError("Code is empty!", "Please write your algorithm in the code editor.");
            return;
        }

        String mode = manualBtn.isSelected() ? "MANUAL" : "AUTO";

        if (mode.equals("MANUAL")) {
            if (arrayField.getText().trim().isEmpty()) {
                showError("Array is empty!", "Please enter an array for Manual mode.");
                return;
            }
            if (sizeField.getText().trim().isEmpty()) {
                showError("Size is empty!", "Please enter the input size (n).");
                return;
            }
        }

        setLoadingState(true);

        // Run in background thread 
        Task<AnalysisResponse> task = new Task<>() {
            @Override
            protected AnalysisResponse call() throws Exception {
                String arrayInput = mode.equals("MANUAL") ? arrayField.getText().trim() : null;
                String sizeInput  = mode.equals("MANUAL") ? sizeField.getText().trim()  : null;
                return apiClient.analyze(code, arrayInput, sizeInput, mode);
            }
        };

        task.setOnSucceeded(e -> {
            AnalysisResponse response = task.getValue();
            Platform.runLater(() -> {
                updateResults(response);
                updateCandidates(response);
                updateChart(response);
                setLoadingState(false);
            });
        });

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            String errorMsg = ex != null ? ex.getMessage() : "Unknown error";
            Platform.runLater(() -> {
                showError("Failed!", errorMsg);  // ← shows real error
                setLoadingState(false);
            });
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void updateResults(AnalysisResponse response) {
        complexityValue.setText(response.getComplexitySafe());
        complexityDesc.setText(response.getDescriptionSafe());
        confidenceBar.setProgress(response.getConfidence());
        bestLabel.setText(response.getBestSafe());
        avgLabel.setText(response.getAvgSafe());
        worstLabel.setText(response.getWorstSafe());
    }

    private void updateCandidates(AnalysisResponse response) {
        var candidates = response.getCandidates();
        if (candidates == null || candidates.isEmpty()) return;

        if (candidates.size() > 0) {
            candidateLabel1.setText(candidates.get(0).getName());
            candidateBar1.setProgress(candidates.get(0).getScore());
        }
        if (candidates.size() > 1) {
            candidateLabel2.setText(candidates.get(1).getName());
            candidateBar2.setProgress(candidates.get(1).getScore());
        }
        if (candidates.size() > 2) {
            candidateLabel3.setText(candidates.get(2).getName());
            candidateBar3.setProgress(candidates.get(2).getScore());
        }
    }

    private void updateChart(AnalysisResponse response) {

        // Clear old data
        runtimeChart.getData().clear();

        var chartData = response.getChartData();
        if (chartData == null || chartData.isEmpty()) return;

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Runtime (ms)");

        for (var point : chartData) {
            series.getData().add(
                new XYChart.Data<>(point.getN(), point.getTime())
            );
        }

        runtimeChart.getData().add(series);
        runtimeChart.setStyle("-fx-background-color: #0d0d1a;");
    }

    private void setLoadingState(boolean loading) {
        runBtn.setDisable(loading);
        if (loading){
            runBtn.setText("⏳ Analyzing...");
            complexityValue.setText("...");
            complexityDesc.setText("Running benchmark, please wait...");
        }
        else 
            runBtn.setText("▶  Run Analysis");
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}