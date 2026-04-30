import time
import traceback
from executor.code_executor import CodeExecutor

class Timer:

    def __init__(self):
        self.executor = CodeExecutor()

    # ════════════════════════════════════════════════════════════════════════
    #  MAIN METHOD — Measure execution time of user code
    # ════════════════════════════════════════════════════════════════════════
    def measure(self, code: str, arr: list[int]) -> float:

        # Step 1: Warm up — run once before measuring
        # (first run is always slower due to Python internals)
        try:
            self.executor.execute(code, arr)
        except Exception as e:
            raise RuntimeError(f"Warm up failed:\n{traceback.format_exc()}")

        # Step 2: Measure actual execution time
        start = time.perf_counter()
        self.executor.execute(code, arr)
        end   = time.perf_counter()

        # Step 3: Return time in milliseconds
        time_ms = (end - start) * 1000
        return round(time_ms, 4)

    # ════════════════════════════════════════════════════════════════════════
    #  MEASURE MULTIPLE RUNS — Average over N runs for accuracy
    # ════════════════════════════════════════════════════════════════════════
    def measure_average(self, code: str, arr: list[int], runs: int = 3) -> float:

        times = []
        for _ in range(runs):
            t = self.measure(code, arr)
            times.append(t)

        # Return average time
        return round(sum(times) / len(times), 4)