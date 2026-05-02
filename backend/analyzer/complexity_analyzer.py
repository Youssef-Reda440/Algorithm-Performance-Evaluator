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
            cv = self._coefficient_of_variation(func, results)
            variance_map[name] = cv

        best_match = min(variance_map, key=variance_map.get)
        confidence = self._calculate_confidence(variance_map, best_match)
        candidates = sorted(variance_map.items(), key=lambda x: x[1])

        return self._build_result(best_match, confidence, candidates)

    def _coefficient_of_variation(self, func, results: list[dict]) -> float:
        """
        CV = std_dev / mean
        This normalizes across different scales automatically!
        O(n²) and O(n³) are now fairly compared
        """
        ratios = []
        for r in results:
            n    = r["n"]
            time = r["time"]
            f    = func(n)
            if f > 0 and time > 0:
                ratios.append(time / f)

        if len(ratios) < 2:
            return float("inf")

        mean = sum(ratios) / len(ratios)

        if mean == 0:
            return float("inf")

        # Standard deviation
        std_dev = math.sqrt(
            sum((r - mean) ** 2 for r in ratios) / len(ratios)
        )

        # Coefficient of variation = std_dev / mean
        # This removes scale bias completely!
        cv = std_dev / mean
        return cv

    def _calculate_confidence(self, variance_map: dict, best: str) -> float:
        sorted_variances = sorted(variance_map.values())
        best_cv   = sorted_variances[0]
        second_cv = sorted_variances[1]

        if second_cv == 0:
            return 1.0

        # Bigger gap between best and second = higher confidence
        confidence = 1.0 - (best_cv / second_cv)
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

    def _build_result(self, complexity: str,
                      confidence: float,
                      candidates: list = None) -> dict:
        return {
            "complexity"  : complexity,
            "description" : self._get_description(complexity),
            "confidence"  : confidence,
            "candidates"  : candidates or [],
        }