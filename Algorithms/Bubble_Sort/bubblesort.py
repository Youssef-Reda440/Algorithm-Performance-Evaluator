"""
def my_algorithm(arr):
    arr = arr.copy()
    n = len(arr)
    for i in range(n):
        for j in range(0, n - i - 1):
            if arr[j] > arr[j + 1]:
                arr[j], arr[j + 1] = arr[j + 1], arr[j]
    return arr

"""

# ====================

"""

complexities = 
{
    "Best": "O(n)",
    "Average": "O(n^2)",
    "Worst": "O(n^2)"
}

"""