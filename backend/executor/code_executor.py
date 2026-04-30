import traceback

class CodeExecutor:

    def execute(self, code: str, arr: list[int]) -> list[int]:

        # Create safe environment
        safe_globals = {"__builtins__": self._safe_builtins()}
        local_vars   = {"arr": arr.copy()}

        # Execute code
        try:
            exec(code, safe_globals, local_vars)
        except Exception as e:
            raise RuntimeError(f"Code execution error:\n{traceback.format_exc()}")

        if "my_algorithm" not in local_vars:
            raise RuntimeError(
                "Function 'my_algorithm' not found!\n"
                "Please define your algorithm as:\n"
                "def my_algorithm(arr):\n"
                "    ...")

        try:
            result = local_vars["my_algorithm"](arr.copy())
        except Exception as e:
            raise RuntimeError(f"Error running my_algorithm:\n{traceback.format_exc()}")

        return result

    # SAFE BUILTINS
    def _safe_builtins(self) -> dict:
        return {
            "range"     : range,
            "len"       : len,
            "print"     : print,
            "enumerate" : enumerate,
            "zip"       : zip,
            "map"       : map,
            "filter"    : filter,
            "sorted"    : sorted,
            "reversed"  : reversed,
            "min"       : min,
            "max"       : max,
            "sum"       : sum,
            "abs"       : abs,
            "round"     : round,

            "int"       : int,
            "float"     : float,
            "str"       : str,
            "list"      : list,
            "tuple"     : tuple,
            "bool"      : bool,

            # Math
            "pow"       : pow,
            "divmod"    : divmod,
        }