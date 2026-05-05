import random

class InputGenerator:
 
    def get_adaptive_sizes(self, probe_time: float) -> list[int]:
        """
        Choose sizes based on how fast the algorithm is.
        probe_time = time in ms for n=100
        """
        if probe_time < 0.1:
            # Very fast → O(n) or O(n log n)
            return [5000, 10000, 20000, 30000, 50000]
 
        elif probe_time < 1.0:
            # Medium speed
            return [1000, 2000, 3000, 5000, 8000, 10000]
 
        elif probe_time < 10.0:
            # Slow → probably O(n²)
            return [500, 1000, 1500, 2000, 2500, 3000]
 
        else:
            # Very slow → O(n²) or O(n³)
            return [100, 200, 300, 500, 700, 1000]
    
    def generate_random(self, n: int) -> list[int]:
        if n <= 0:
            raise ValueError("Size must be positive")
        return [random.randint(1, 1000) for _ in range(n)]

    def generate_sorted(self, n: int) -> list[int]:
        if n <= 0:
            return []
        return list(range(1, n + 1))

    def generate_reversed(self, n: int) -> list[int]:
        if n <= 0:
            return []
        return list(range(n, 0, -1))

    def parse_array(self, array_str: str) -> list[int]:
        if not array_str.strip():
            return []
        try:
            return [int(x) for x in array_str.strip().split()]
        except ValueError:
            raise ValueError("Invalid array input. Use space-separated integers.")