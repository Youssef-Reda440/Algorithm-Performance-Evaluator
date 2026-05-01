package evaluator.controller;

import evaluator.model.AnalysisResponse;
import evaluator.network.ApiClient;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.*;

public class DashboardController {

    // UI References
    private final TextArea codeArea;
    private final TextField arrayField;
    private final TextField sizeField;
    private final ToggleButton manualBtn;
    // private final ToggleButton autoBtn;
    private final Button runBtn;

    // Result UI References 
    private final Label complexityValue;
    private final Label complexityDesc;
    private final ProgressBar confidenceBar;
    private final Label bestLabel;
    private final Label avgLabel;
    private final Label worstLabel;

    // Network
    private final ApiClient apiClient = new ApiClient();

    public DashboardController(
            TextArea codeArea,
            TextField arrayField,
            TextField sizeField,
            ToggleButton manualBtn,
            Button runBtn,
            Label complexityValue,
            Label complexityDesc,
            ProgressBar confidenceBar,
            Label bestLabel,
            Label avgLabel,
            Label worstLabel) {

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
    }

    public void onRunAnalysis() {

        // Input Validation
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
                String arrayInput = arrayField.getText().trim();
                String sizeInput  = sizeField.getText().trim();
                return apiClient.analyze(code, arrayInput, sizeInput, mode);
            }
        };

        task.setOnSucceeded(e -> {
            AnalysisResponse response = task.getValue();
            Platform.runLater(() -> {
                updateResults(response);
                setLoadingState(false);
            });
        });

        task.setOnFailed(e -> {
            Platform.runLater(() -> {
                showError("Connection Failed!",
                        "Could not connect to Python backend.\n"
                        + "Make sure FastAPI server is running on port 8000.");
                setLoadingState(false);
            });
        });

        // Start background thread ──────────────────────────────────
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    // UPDATE UI 
    private void updateResults(AnalysisResponse response) {
        complexityValue.setText(response.getComplexity());
        complexityDesc.setText(response.getDescription());
        confidenceBar.setProgress(response.getConfidence());
        bestLabel.setText(response.getBest());
        avgLabel.setText(response.getAvg());
        worstLabel.setText(response.getWorst());
    }

    private void setLoadingState(boolean loading) {
        runBtn.setDisable(loading);
        runBtn.setText(loading ? "⏳ Analyzing..." : "▶  Run Analysis");
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}