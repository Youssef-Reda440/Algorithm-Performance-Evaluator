package evaluator.model;

import java.util.List;

public class AnalysisResponse {

    private String complexity;     
    private String description;    
    private double confidence;      
    private String best;           
    private String avg;             
    private String worst;         
    private List<ChartPoint> best_chart_data;
    private List<ChartPoint> avg_chart_data; 
    private List<ChartPoint> worst_chart_data; 
    private List<Candidate> candidates;
    
    public static class ChartPoint {
        public int n;         
        public double time;    

        public int getN()         { return n; }
        public double getTime()   { return time; }
    }

    public static class Candidate {
        public String name;
        public double score;

        public String getName()  { return name; }
        public double getScore() { return score; }
    }

    public String getComplexity()               { return complexity; }
    public String getDescription()              { return description; }
    public double getConfidence()               { return confidence; }
    public String getBest()                     { return best; }
    public String getAvg()                      { return avg; }
    public String getWorst()                    { return worst; }
    public List<ChartPoint> getBestChartData()  { return best_chart_data; }
    public List<ChartPoint> getAvgChartData()   { return avg_chart_data; }
    public List<ChartPoint> getWorstChartData() { return worst_chart_data; }
    public List<Candidate>  getCandidates()     { return candidates; }

    // FALLBACK => Return safe defaults if Python returns null
    public String getComplexitySafe() {
        return complexity != null ? complexity : "Unknown";
    }
    public String getDescriptionSafe() {
        return description != null ? description : "No description available";
    }
    public String getBestSafe()  { return best  != null ? best  : "N/A"; }
    public String getAvgSafe()   { return avg   != null ? avg   : "N/A"; }
    public String getWorstSafe() { return worst != null ? worst : "N/A"; }
}