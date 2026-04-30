import random

class InputGenerator:

    # GENERATE RANDOM ARRAY
    def generate_random(self, n: int) -> list[int]:
        return [random.randint(1, 1000) for _ in range(n)]

    # GENERATE SORTED ARRAY 
    def generate_sorted(self, n: int) -> list[int]:
        return list(range(1, n + 1))

    # GENERATE REVERSED ARRAY
    def generate_reversed(self, n: int) -> list[int]:
        return list(range(n, 0, -1))

    # PARSE USER ARRAY
    def parse_array(self, array_str: str) -> list[int]:
        try:
            return [int(x) for x in array_str.strip().split()]
        except ValueError:
            raise ValueError(f"Invalid array input: '{array_str}'"
                             " — please enter integers separated by spaces.")

    # GET ALL AUTO SIZES
    def get_auto_sizes(self) -> list[int]:
        return [100, 500, 1000]