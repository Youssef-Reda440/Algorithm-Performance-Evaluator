"""
def my_algorithm(arr):
    arr = arr.copy()

    def merge_sort(a):
        if len(a) <= 1:
            return a
        mid = len(a) // 2
        left = merge_sort(a[:mid])
        right = merge_sort(a[mid:])
        return merge(left, right)

    def merge(left, right):
        result = []
        i = j = 0
        while i < len(left) and j < len(right):
            if left[i] <= right[j]:
                result.append(left[i])
                i += 1
            else:
                result.append(right[j])
                j += 1
        result.extend(left[i:])
        result.extend(right[j:])
        return result

    return merge_sort(arr)

"""

# ====================

"""

complexities = 
{
    "Best": "O(n log n)",
    "Average": "O(n log n)",
    "Worst": "O(n log n)"
}

"""