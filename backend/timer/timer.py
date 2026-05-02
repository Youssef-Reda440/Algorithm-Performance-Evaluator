import time
import traceback
from executor.code_executor import CodeExecutor

class Timer:

    def __init__(self):
        self.executor = CodeExecutor()

    def measure(self, code: str, arr: list[int]) -> float:

        # Warm up => run once before measuring
        try:
            self.executor.execute(code, arr)
        except Exception as e:
            raise RuntimeError(f"Warm up failed:\n{traceback.format_exc()}")

        start = time.perf_counter()
        self.executor.execute(code, arr)
        end   = time.perf_counter()

        time_ms = (end - start) * 1000
        return round(time_ms, 4)

    def measure_average(self, code: str, arr: list[int], runs: int = 5) -> float:

        times = []
        for _ in range(runs):
            t = self.measure(code, arr)
            times.append(t)

        return round(sum(times) / len(times), 4)