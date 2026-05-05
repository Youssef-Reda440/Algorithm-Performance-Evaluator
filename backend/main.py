import sys
import os

sys.path.append(os.path.dirname(__file__))

from fastapi import FastAPI, HTTPException, Request
from fastapi.exceptions import RequestValidationError
from fastapi.responses import JSONResponse
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

MAX_TIME_MS  = 2000 
MIN_TIME_MS  = 0.1   

def probe_speed(code: str) -> float:
    """Run quick test on small array to detect algorithm speed"""
    try:
        probe_arr  = generator.generate_random(100)
        probe_time = timer.measure_average(code, probe_arr, runs=2)
        print(f"Probe time at n=100: {probe_time}ms")
        return probe_time
    except Exception:
        return 1.0  # default to medium speed if probe fails

def run_benchmark(code: str, sizes: list[int]) -> tuple:

    results          = []
    best_results     = []
    worst_results    = []
    best_chart_data  = []
    avg_chart_data   = []
    worst_chart_data = []

    for n in sizes:
        # AVG
        t_avg   = timer.measure_average(code, generator.generate_random(n))

        # BEST
        t_best  = timer.measure_average(code, generator.generate_sorted(n))

        # WORST
        t_worst = timer.measure_average(code, generator.generate_reversed(n))

        # Skip noisy measurements
        if t_avg < MIN_TIME_MS and t_best < MIN_TIME_MS and t_worst < MIN_TIME_MS:
            continue

        results.append(      {"n": n, "time": t_avg})
        best_results.append( {"n": n, "time": t_best})
        worst_results.append({"n": n, "time": t_worst})

        avg_chart_data.append(  ChartPoint(n=n, time=t_avg))
        best_chart_data.append( ChartPoint(n=n, time=t_best))
        worst_chart_data.append(ChartPoint(n=n, time=t_worst))

        if t_avg > MAX_TIME_MS or t_best > MAX_TIME_MS or t_worst > MAX_TIME_MS:
            break

    return results, best_results, worst_results, best_chart_data, avg_chart_data, worst_chart_data

def build_response(results, best_results, worst_results,
                   best_chart_data, avg_chart_data, worst_chart_data) -> AnalysisResponse:

    analysis       = analyzer.analyze(results)
    best_analysis  = analyzer.analyze(best_results)
    worst_analysis = analyzer.analyze(worst_results)

    candidates = [
        Candidate(name=c["name"], score=c["score"])
        for c in analysis["candidates"][:3]
    ]

    return AnalysisResponse(
        complexity       = analysis["complexity"],
        description      = analysis["description"],
        confidence       = analysis["confidence"],
        candidates       = candidates,
        best_chart_data  = best_chart_data,
        avg_chart_data   = avg_chart_data,
        worst_chart_data = worst_chart_data,
        best             = best_analysis["complexity"],
        avg              = analysis["complexity"],
        worst            = worst_analysis["complexity"]
    )

@app.exception_handler(RequestValidationError)
async def validation_exception_handler(request: Request, exc: RequestValidationError):
    print(f"422 Error: {exc.errors()}")
    print(f"Body: {await request.body()}")
    return JSONResponse(status_code=422, content={"detail": exc.errors()})

@app.get("/")
def health_check():
    return {"status": "running", "message": "Algorithm Performance Evaluator API"}

@app.post("/analyze", response_model=AnalysisResponse)
def analyze(request: AnalysisRequest):

    try:
        # Probe speed → get adaptive sizes 
        probe_time = probe_speed(request.code)
        sizes      = generator.get_adaptive_sizes(probe_time)

        if request.mode == "MANUAL":
            arr     = generator.parse_array(request.array)
            time_ms = timer.measure_average(request.code, arr)

            results, best_results, worst_results,best_chart_data, avg_chart_data, worst_chart_data = run_benchmark(request.code, sizes)

            if len(arr) >= 100:
                results.insert(0,       {"n": len(arr), "time": time_ms})
                best_results.insert(0,  {"n": len(arr), "time": time_ms})
                worst_results.insert(0, {"n": len(arr), "time": time_ms})

            avg_chart_data.insert(0,   ChartPoint(n=len(arr), time=time_ms))
            best_chart_data.insert(0,  ChartPoint(n=len(arr), time=time_ms))
            worst_chart_data.insert(0, ChartPoint(n=len(arr), time=time_ms))

            return build_response(results, best_results, worst_results,
                                  best_chart_data, avg_chart_data, worst_chart_data)

        elif request.mode == "AUTO":
            results, best_results, worst_results,best_chart_data, avg_chart_data, worst_chart_data = run_benchmark(request.code, sizes)

            return build_response(results, best_results, worst_results,
                                  best_chart_data, avg_chart_data, worst_chart_data)
        
        else:
            raise HTTPException(status_code=400,
                                detail=f"Unknown mode: {request.mode}")

    except RuntimeError as e:
        raise HTTPException(status_code=400, detail=str(e))

    except Exception as e:
        raise HTTPException(status_code=500,
                            detail=f"Internal server error: {str(e)}")