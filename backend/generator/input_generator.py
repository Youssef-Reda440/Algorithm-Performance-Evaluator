import random

class InputGenerator:

    AUTO_SIZES = [500, 1000, 1500, 2000, 2500, 3000, 3500, 4000]
    
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

    def get_auto_sizes(self) -> list[int]:
        return self.AUTO_SIZES