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
            return self._build_result("O(n)", 0.5, [])
        
        cv_map = {}
        for name, func in self.FUNCTIONS.items():
            cv = self._coefficient_of_variation(func, results)
            cv_map[name] = cv

        candidates = self._build_candidates(cv_map)

        if not candidates:
            return self._build_result("O(n)", 0.5, [])

        best_match = candidates[0]["name"]
        confidence = candidates[0]["score"]

        return self._build_result(best_match, confidence, candidates)

    def _coefficient_of_variation(self, func, results: list[dict]) -> float:
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

        std_dev = math.sqrt(
            sum((r - mean) ** 2 for r in ratios) / len(ratios)
        )

        return std_dev / mean

    def _build_candidates(self, cv_map: dict) -> list[dict]:
        finite_cvs = {name: cv for name, cv in cv_map.items()
                      if cv != float("inf")}

        if not finite_cvs:
            return []
        
        max_cv = max(finite_cvs.values())
        if max_cv == 0:
            return []

        candidates = []
        for name, cv in finite_cvs.items():
            score = round(1.0 - (cv / max_cv), 3)
            score = max(0.0, min(1.0, score))
            candidates.append({
                "name" : name,
                "score": score
            })

        candidates.sort(key=lambda x: x["score"], reverse=True)

        return candidates

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
    
    def _build_result(self, complexity: str, confidence: float, candidates: list) -> dict:
        return {
            "complexity"  : complexity,
            "description" : self._get_description(complexity),
            "confidence"  : confidence,
            "candidates"  : candidates,
        }