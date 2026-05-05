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
            Label        candidateLabel1,
            Label        candidateLabel2,
            Label        candidateLabel3,
            ProgressBar  candidateBar1,
            ProgressBar  candidateBar2,
            ProgressBar  candidateBar3,
            LineChart<Number, Number> runtimeChart,
            Label        bestLabel,
            Label        avgLabel,
            Label        worstLabel) 
    {
        this.codeArea        = codeArea;
        this.arrayField      = arrayField;
        this.sizeField       = sizeField;
        this.manualBtn       = manualBtn;
        this.runBtn          = runBtn;
        this.complexityValue = complexityValue;
        this.complexityDesc  = complexityDesc;
        this.confidenceBar   = confidenceBar;
        this.candidateLabel1 = candidateLabel1;
        this.candidateLabel2 = candidateLabel2;
        this.candidateLabel3 = candidateLabel3;
        this.candidateBar1   = candidateBar1;
        this.candidateBar2   = candidateBar2;
        this.candidateBar3   = candidateBar3;
        this.runtimeChart    = runtimeChart;
        this.bestLabel       = bestLabel;
        this.avgLabel        = avgLabel;
        this.worstLabel      = worstLabel;
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
                updateCharts(response);
                setLoadingState(false);
            });
        });

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            String errorMsg = ex != null ? ex.getMessage() : "Unknown error";
            Platform.runLater(() -> {
                showError("Failed!", errorMsg); 
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

            var candidates = response.getCandidates();
            candidateLabel1.setText(candidates.get(0).getName());
            candidateBar1.setProgress(candidates.get(0).getScore());
            candidateLabel2.setText(candidates.get(1).getName());
            candidateBar2.setProgress(candidates.get(1).getScore());
            candidateLabel3.setText(candidates.get(2).getName());
            candidateBar3.setProgress(candidates.get(2).getScore());    
    }

    private void updateCharts(AnalysisResponse response) {
        // Best Charts
        var bestchartData = response.getBestChartData();
        if (bestchartData != null && !bestchartData.isEmpty()){
            XYChart.Series<Number, Number> best_series = new XYChart.Series<>();
            best_series.setName("BEST");
            best_series.setNode(null);

            for (var point : bestchartData)
                best_series.getData().add(new XYChart.Data<>(point.getN(), point.getTime()));
            
            runtimeChart.getData().add(best_series);
            runtimeChart.lookup(".chart-series-line.series0")
            .setStyle("-fx-stroke: #0fea84;");
        }

        // Avg Charts
        var avgchartData = response.getAvgChartData();
        if (avgchartData != null && !avgchartData.isEmpty()){
            XYChart.Series<Number, Number> avg_series = new XYChart.Series<>();
            avg_series.setName("AVG");
            avg_series.setNode(null);

            for (var point : avgchartData) 
                avg_series.getData().add(new XYChart.Data<>(point.getN(), point.getTime()));
            
            runtimeChart.getData().add(avg_series);
            runtimeChart.lookup(".chart-series-line.series1")
            .setStyle("-fx-stroke: #ffaa00;");
        }

        // Worst Charts
        var worstchartData = response.getWorstChartData();
        if (worstchartData != null && !worstchartData.isEmpty()){
            XYChart.Series<Number, Number> worst_series = new XYChart.Series<>();
            worst_series.setName("WORST");
            worst_series.setNode(null);

            for (var point : worstchartData)
                worst_series.getData().add(new XYChart.Data<>(point.getN(), point.getTime()));
            
            runtimeChart.getData().add(worst_series);
            runtimeChart.lookup(".chart-series-line.series2")
            .setStyle("-fx-stroke: #ee0e0e;");
        }

        runtimeChart.setStyle("-fx-background-color: #0d0d1a;");
    }

    private void setLoadingState(boolean loading) {
        runBtn.setDisable(loading);
        if (loading){
            runBtn.setText("⏳ Analyzing...");
            complexityValue.setText("...");
            bestLabel.setText("...");
            avgLabel.setText("...");
            worstLabel.setText("...");
            candidateLabel1.setText("...");
            candidateLabel2.setText("...");
            candidateLabel3.setText("...");
            candidateBar1.setProgress(0);
            candidateBar2.setProgress(0);
            candidateBar3.setProgress(0);
            confidenceBar.setProgress(0);
            runtimeChart.getData().clear();
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