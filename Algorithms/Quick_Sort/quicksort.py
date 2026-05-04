"""
def my_algorithm(arr):
    arr = arr.copy()

    def quick_sort(a):
        if len(a) <= 1:
            return a
        pivot = a[len(a) // 2]

        left = [x for x in a if x < pivot]
        middle = [x for x in a if x == pivot]
        right = [x for x in a if x > pivot]
        return quick_sort(left) + middle + quick_sort(right)

    return quick_sort(arr)

"""

# ====================

"""

complexities = 
{
    "Best": "O(n log n)",
    "Average": "O(n log n)",
    "Worst": "O(n^2)"
}

"""
