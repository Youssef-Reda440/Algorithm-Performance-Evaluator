import time
import statistics
import traceback
from executor.code_executor import CodeExecutor

class Timer:

    def __init__(self):
        self.executor = CodeExecutor()

    def measure(self, code: str, arr: list[int]) -> float:        
        start = time.perf_counter()
        self.executor.execute(code, arr)
        end   = time.perf_counter()

        time_ms = (end - start) * 1000
        return time_ms

    def measure_average(self, code: str, arr: list[int], runs: int = 5) -> float:
        try :
            # Warm Up 
            self.executor.execute(code, arr)

            times = []
            for _ in range(runs):
                t = self.measure(code, arr)
                times.append(t)

            # Remove min and max then -> take median
            times.sort()
            trimmed = times[1:-1] if len(times) > 3 else times
            return round(statistics.median(trimmed), 4)

        except Exception as e:
            raise RuntimeError(f"Measurement failed at n={len(arr)}:\n{traceback.format_exc()}")