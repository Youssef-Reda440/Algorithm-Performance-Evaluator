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

    def measure_average(self, code: str, arr: list[int], runs: int = 4) -> float:
        try :
            # Warm Up 
            self.executor.execute(code, arr)

            times = []
            for _ in range(runs):
                t = self.measure(code, arr)
                times.append(t)

            times.sort()
            return round(statistics.mean(times), 4)

        except Exception as e:
            raise RuntimeError(f"Measurement failed at n={len(arr)}:\n{traceback.format_exc()}")