import math

class ComplexityAnalyzer:

    FUNCTIONS = {
        "O(1)"      : lambda n: 1,
        "O(log n)"  : lambda n: math.log2(n),
        "O(n)"      : lambda n: n,
        "O(n log n)": lambda n: n * math.log2(n),
        "O(n²)"     : lambda n: n ** 2,
        "O(n³)"     : lambda n: n ** 3,
    }

    def analyze(self, results: list[dict]) -> dict:
        if len(results) < 2:
            return self._build_result("O(n)", 0.5)

        variance_map = {}
        for name, func in self.FUNCTIONS.items():
            variance = self._calculate_variance(func, results)
            variance_map[name] = variance

        best_match = min(variance_map, key=variance_map.get)

        confidence = self._calculate_confidence(variance_map, best_match)

        candidates = sorted(variance_map.items(), key=lambda x: x[1])

        return self._build_result(best_match, confidence, candidates)

    def _calculate_variance(self, func, results: list[dict]) -> float:
        # Calculate ratios => time / f(n) for each result
        ratios = []
        for r in results:
            n    = r["n"]
            time = r["time"]
            f    = func(n)
            if f > 0:
                ratios.append(time / f)

        if not ratios:
            return float("inf")

        mean = sum(ratios) / len(ratios)
        variance = sum((r - mean) ** 2 for r in ratios) / len(ratios)

        return variance

    def _calculate_confidence(self, variance_map: dict, best: str) -> float:
        best_variance   = variance_map[best]
        total_variance  = sum(variance_map.values())

        if total_variance == 0:
            return 1.0

        # Lower variance = higher confidence
        confidence = 1.0 - (best_variance / total_variance)
        return round(min(max(confidence, 0.0), 1.0), 2)

    def _get_description(self, complexity: str) -> str:
        descriptions = {
            "O(1)"      : "Constant — execution time does not grow with input",
            "O(log n)"  : "Logarithmic — very efficient, typical in binary search",
            "O(n)"      : "Linear — execution time grows with input size",
            "O(n log n)": "Linearithmic — efficient sorting like merge sort",
            "O(n²)"     : "Quadratic — nested loop pattern detected",
            "O(n³)"     : "Cubic — triple nested loop pattern detected",
        }
        return descriptions.get(complexity, "Unknown complexity")

    #  BUILD RESULT
    def _build_result(self, complexity: str, confidence: float, candidates: list = None) -> dict:
        return {
            "complexity"  : complexity,
            "description" : self._get_description(complexity),
            "confidence"  : confidence,
            "candidates"  : candidates or [],
        }