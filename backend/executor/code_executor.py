import traceback

class CodeExecutor:

    def execute(self, code: str, arr: list[int]):

        # Create environment with full builtins 
        safe_globals = {"__builtins__": __builtins__}
        local_vars   = {"arr": arr.copy()}

        # Execute user code 
        try:
            exec(code, safe_globals, local_vars)
        except Exception as e:
            raise RuntimeError(f"Code execution error:\n{traceback.format_exc()}")

        func = None
        for name, obj in local_vars.items():
            if callable(obj):
                func = obj
                break

        if func is None:
            raise RuntimeError(
                "No function found!\n"
                "Please define at least one function.\n"
                "Example:\n"
                "def my_algorithm(arr):\n"
                "    ...")

        try:
            result = func(arr.copy())
        except Exception as e:
            raise RuntimeError(f"Error running function:\n{traceback.format_exc()}")

        return result