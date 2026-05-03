import sys
import os

sys.path.append(os.path.dirname(__file__))

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware

from models.schemas import AnalysisRequest, AnalysisResponse, ChartPoint, Candidate
from timer.timer import Timer
from analyzer.complexity_analyzer import ComplexityAnalyzer
from generator.input_generator import InputGenerator

app = FastAPI(
    title="Algorithm Performance Evaluator",
    description="Analyzes user-submitted algorithms and detects complexity",
    version="1.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

timer     = Timer()
analyzer  = ComplexityAnalyzer()
generator = InputGenerator()

def run_benchmark(code: str, sizes: list[int]) -> tuple:

    results       = []
    best_results  = []
    worst_results = []
    chart_data    = []

    for n in sizes:
        # AVG 
        t_avg = timer.measure_average(code, generator.generate_random(n))

        # BEST
        t_best = timer.measure_average(code, generator.generate_sorted(n))

        # WORST
        t_worst = timer.measure_average(code, generator.generate_reversed(n))

        results.append({"n": n, "time": t_avg})
        best_results.append({"n": n, "time": t_best})
        worst_results.append({"n": n, "time": t_worst})
        chart_data.append(ChartPoint(n=n, time=t_avg))

    return results, best_results, worst_results, chart_data

def build_response(results, best_results, worst_results, chart_data) -> AnalysisResponse:

    analysis       = analyzer.analyze(results)
    best_analysis  = analyzer.analyze(best_results)
    worst_analysis = analyzer.analyze(worst_results)

    candidates = [
        Candidate(name=c["name"], score=c["score"])
        for c in analysis["candidates"][:3]
    ]

    return AnalysisResponse(
        complexity  = analysis["complexity"],
        description = analysis["description"],
        confidence  = analysis["confidence"],
        best        = best_analysis["complexity"],
        avg         = analysis["complexity"],
        worst       = worst_analysis["complexity"],
        chart_data  = chart_data,
        candidates  = candidates 
    )

@app.get("/")
def health_check():
    return {"status": "running", "message": "Algorithm Performance Evaluator API"}

@app.post("/analyze", response_model=AnalysisResponse)
def analyze(request: AnalysisRequest):

    try:
        sizes = generator.get_auto_sizes()

        if request.mode == "MANUAL":
            # Parse user array & measure it
            arr     = generator.parse_array(request.array)
            time_ms = timer.measure_average(request.code, arr)

            results, best_results, worst_results, chart_data = run_benchmark(request.code, sizes)

            # Insert user data point at the beginning
            if len(arr) >= 100 :
                results.insert(0, {"n": len(arr), "time": time_ms}) 
                best_results.insert(0, {"n": len(arr), "time": time_ms})
                worst_results.insert(0, {"n": len(arr), "time": time_ms})

            chart_data.insert(0, ChartPoint(n=len(arr), time=time_ms))

            return build_response(results, best_results, worst_results, chart_data)

        elif request.mode == "AUTO":
            results, best_results, worst_results, chart_data = run_benchmark(request.code, sizes)

            return build_response(results, best_results, worst_results, chart_data)

        else:
            raise HTTPException(status_code=400,
                                detail=f"Unknown mode: {request.mode}")

    except RuntimeError as e:
        raise HTTPException(status_code=400, detail=str(e))

    except Exception as e:
        raise HTTPException(status_code=500,
                            detail=f"Internal server error: {str(e)}")