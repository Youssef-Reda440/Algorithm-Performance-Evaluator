"""
def my_algorithm(arr):
    arr = arr.copy()
    n = len(arr)
    for i in range(n):
        min_idx = i
        for j in range(i + 1, n):
            if arr[j] < arr[min_idx]:
                min_idx = j
        arr[i], arr[min_idx] = arr[min_idx], arr[i]
    return arr

"""

# ====================

"""

complexities = 
{
    "Best": "O(n^2)",
    "Average": "O(n^2)",
    "Worst": "O(n^2)"
}

"""