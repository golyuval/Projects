import time
import numpy as np
from tqdm import tqdm

# ------ TQDM ---------------------------------------------------------------------------------------------------------

def update_tqdm(pbar, start):
    now = time.time()
    pbar.update(now-start)
    return now

def reset_tqdm(pbar,total,str):
    pbar.set_description(str)
    pbar.reset(total)

# ------ Sorts ---------------------------------------------------------------------------------------------------------

# Intro sort:
# combination of Quicksort, Heapsort, Insertionsort
# * partition data (if recursion too deep, switch to heap sort - guarantee O(nlog(n)) worst time complexity)
# * on small sub arrays preform insertionsort (quicker)

def Intro_sort():
    return None

# Insertion Sort :
# 1. Start with i = 1.
# 2. Pick a[i] as the "key" element.
# 3. While j >= 0 and a[j] > key, move a[j] to the right and decrement j.
# 4. Place the "key" element in its correct sorted position.
# 5. Increment i and repeat steps 2-4 until the entire array is sorted.
# Insertion Sort has O(n^2) worst-case time complexity.

def Insertion_sort(pbar,arr):

    n = len(arr)  # Get the length of the array
    start = time.time()

    if n <= 1:
        return  # If the array has 0 or 1 element, it is already sorted, so return

    for i in range(1, n):  # Iterate over the array starting from the second element
        key = arr[i]  # Store the current element as the key to be inserted in the right position
        j = i - 1
        while j >= 0 and key < arr[j]:  # Move elements greater than key one position ahead
            arr[j + 1] = arr[j]  # Shift elements to the right
            j -= 1
        arr[j + 1] = key  # Insert the key in the correct position
        start = update_tqdm(pbar,start)

# ------------------- Bubble sort : -----------------------

# Bubble Sort:
# 1. from i = 0 to len-1
# 2. from j = 1 to len-1
# 3. for each a[i]>a[j] when i<j swap a[i] and a[j]

def Bubble_sort(pbar,arr):
    n = len(arr)
    swapped = False
    start = time.time()

    for i in range(n - 1):
        for j in range(0, n - i - 1):
            if arr[j] > arr[j + 1]:
                swapped = True
                arr[j], arr[j + 1] = arr[j + 1], arr[j]

        if not swapped:
            # if we haven't needed to make a single swap, we
            # can just exit the main loop.
            return
        start = update_tqdm(pbar,start)


# ------------------- Heap sort : -----------------------

# Heapify:
# 1. find maximum between root,right,left
# 2. if root is maximum - done
# 3. if child is maximum - swap root and child, heapify on swapped child
#
# arr - array
# n - size of arr
# i -


def heapify(arr, n, i):

    root = i  # Initialize root
    left = 2 * i + 1  # Initialize left
    right = 2 * i + 2  # Initialize right

    # find maximum
    maximum =   left if left < n and arr[left] > arr[root] else root
    maximum =   right if right < n and arr[right] > arr[maximum] else maximum

    # maximum is child
    if maximum != i:
        (arr[i], arr[maximum]) = (arr[maximum], arr[i])  # swap root with greater child
        heapify(arr, n, maximum)  # heapify the child

    # maximum is root
    #else:
    #    return

# Heap sort:
# 1. build complete binary tree from arr
# 2. max heapify binary tree
# 3. remove maximum and add to sorted_array
# 4. return to 2. until empty
# 5. return sorted_array
# best: O(nlog(n)) | worst: O(nlog(n)) | inplace: no | stable: yes | space: O(log(n))


def Heap_sort(pbar,arr):

    n = len(arr)
    start = time.time()
    # build maximum heap: heapify from middle-1 to beginning
    for i in range(n // 2 - 1, -1, -1):
        heapify(arr, n, i)


    # remove root (maximum) and put in the last leaf in tree
    for i in range(n - 1, 0, -1):
        (arr[i], arr[0]) = (arr[0], arr[i])  # swap
        heapify(arr, i, 0)
        start = update_tqdm(pbar,start)



# ------------------- Merge sort : -----------------------


# merge: reunite 2 sorted halves
# best: O(n) | worst: O(n) | inplace: yes | stable: yes | space: O(1)
#
# [sortedL_1,...,sortedL_k][sortedR_1,...,sortedR_k] --------> [sorted_1, ... ,sorted_k, ... ,sorted_2k]
# [     4 , 5 , 7 , 8 , 9 , 1 , 1 , 3 , 5 , 6      ] --------> [ 1 , 1 , 3 , 4 , 5 , 5 , 6 , 7 , 8 , 9 ]

def merge(arr, left, middle, right):

    # left array
    L_size = middle - left + 1
    L = [0] * (L_size)

    # right array
    R_size = right - middle
    R = [0] * (R_size)

    # Copy data to temp arrays L[] and R[]
    for i in range(0, L_size):
        L[i] = arr[left + i]
    for i in range(0, R_size):
        R[i] = arr[middle + 1 + i]

    i = j = 0  # Initial index of subarray
    k = left  # Initial index of merged subarray

    while i < L_size and j < R_size:
        if L[i] <= R[j]:  # left step
            arr[k] = L[i]
            i += 1
        else:             # right step
            arr[k] = R[j]
            j += 1
        k += 1

    # Copy the remaining elements of L[], if there
    while i < L_size:
        arr[k] = L[i]
        i += 1
        k += 1

    # Copy the remaining elements of R[], if there
    while j < R_size:
        arr[k] = R[j]
        j += 1
        k += 1

# Merge Sort: recursive call to right and left halves, then re-unites merged halves into 1 sorted array (merge)
# best: O(n*log(n)) | worst: O(nlog(n)) | inplace: no | stable: yes | space: O(n)
#
# [left_1,....,left_k][right_1,...,right_k] --------> [sortedL_1,...,sortedL_k][sortedR_1,...,sortedR_k]
# [ 4 , 9 , 8 , 5 , 7 , 1 , 3 , 6 , 5 , 1 ] --------> [   4 , 5 , 7 , 8 , 9   ][   1 , 1 , 3 , 5 , 6   ]
# [ 4 , 5 , 7 , 8 , 9 , 1 , 1 , 3 , 5 , 6 ] --------> [   1 , 1 , 3 , 4 , 5 , 5 , 6 , 7 , 8 , 9   ]

def Merge_sort(pbar,start,arr, left, right):

    if left < right:

        middle = left + (right - left) // 2  # avoids overflow

        start = Merge_sort(pbar,start,arr, left, middle)  # recursive left
        start = Merge_sort(pbar, start, arr, middle + 1, right)  # recursive right
        merge(arr, left, middle, right)  # merge

    return update_tqdm(pbar, start)




# ------------------- Quick sort : -----------------------


# Function to find the pivot's position
# best: O(n) | worst: O(n) | inplace: yes | stable: no | space: O(1)
#
# [low, ..-elements-.. ,high-1] [_pivot_] --------> [smaller_1,...,smaller_k] [_pivot_] [greater_1,...,greater_t]
# [ 4 , 9 , 8 , 5 , 7 , 1 , 3 ] [   6   ] --------> [  4  ,  5  ,  1  ,  3  ] [   6   ] [   8   ,   9   ,   7   ]

def partition(array, low, high):

    pivot = array[high]  # choose the rightmost element as pivot
    sort_index = low - 1  # index for greater element

    # iterate over range(low,high), when smaller is indeed smaller than pivot ---> swap greater and smaller
    for smaller in range(low, high):
        if array[smaller] <= pivot:
            sort_index += 1
            (array[sort_index], array[smaller]) = (array[smaller], array[sort_index])

    # there is no smaller element ---> pivot's sorted place is first
    if sort_index == low - 1:
        sort_index = low
        (array[sort_index], array[high]) = (array[high], array[sort_index])

    # all k smaller elements are sorted ----> pivot's sorted place is k+1
    else:
        sort_index += 1
        (array[sort_index], array[high]) = (array[high], array[sort_index])

    return sort_index

# [_1_] Quick Sort (partition) : recursive sort by finding pivots location each recursive step (by partition function)
# array - array
# [low,high] - sorting range
# best: O(n*log(n)) | worst: O(n^2) | inplace: yes | stable: no | space: O(1)

def Quick_sort(pbar, start, array, low, high):  # best: O(n*log(n)) | worst: O(n^2) | inplace: yes | stable: no |

    if low < high:

        pivot = partition(array, low, high)  # put pivot in its position and return pivot's index
        start = Quick_sort(pbar,start,array, low, pivot - 1)  # Recursive call on the left of pivot (smaller values)
        start = Quick_sort(pbar,start,array, pivot + 1, high)  # Recursive call on the right of pivot (bigger values)

    return update_tqdm(pbar, start)

# [_2_] Quick Sort : list comprehension
# array - array
# [low,high] - sorting range
# best: O(n*log(n)) | worst: O(n*log(n)) | inplace: no | stable: yes | space: O(n)

def Quick_sort_list(array):

    # stop
    if len(array) <= 1:
        return array

    # divide
    pivot = array[0]  # pivot element
    left = [x for x in array[1:] if x < pivot]  # smaller elements
    right = [x for x in array[1:] if x >= pivot]  # bigger elements

    # pivot is ordered, continue sorting on left and right
    return Quick_sort_list(left) + [pivot] + Quick_sort_list(right)


# ------------------- Bucket sort : -----------------------

# arr - array
# num_buckets - range of the value of a[i] for each i, is [0,k] -----> a[i] has infinite options between 0-k
# optimal when values are normal distribution between a,b constant values
# best: O(n) | worst: O(n^2) | inplace: no | stable: yes

def Bucket_nsort(pbar, start,arr, num_buckets):
    if len(arr) == 0:
        return arr

    # Find the maximum and minimum values in the array
    max_val = max(arr)
    min_val = min(arr)

    # Calculate the range of each bucket
    bucket_range = (max_val - min_val + 1) / num_buckets

    # Create empty buckets
    buckets = [[] for _ in range(num_buckets)]

    # Distribute elements into buckets
    for num in arr:
        bucket_index = int((num - min_val) / bucket_range)
        buckets[bucket_index].append(num)


    # Sort each bucket and concatenate them
    sorted_arr = []
    for bucket in buckets:
        start = update_tqdm(pbar,start)
        sorted_arr.extend(sorted(bucket))

    update_tqdm(pbar, start)
    return sorted_arr


# ------------------- Counting sort : -----------------------

# a - array
# k - a[i] for each i is in range of k single values ----> a[i] has k options
# optimal when k in O(1)
# best: O(n) | worst: O(n+k) | inplace: no  | stable: yes | additional space: O(n+k)

def Counting_nsort(pbar, start,a,k):  # a is array, each element is an int in [0,k]

    size = len(a)  # size of array
    sorted_array = [0] * size  # new sorted output array
    k_array = [0] * (k+1)  # array of possible values

    # Count the appearances of each index of k_array in array
    for v in a:
        k_array[v] += 1

    # Sum up k_array[0] -------> k_array[k-1]
    for i in range(k+1):
        k_array[i] += k_array[i - 1]

    # Place the elements in sorted order
    i = size - 1
    while i >= 0:
        start = update_tqdm(pbar, start)
        sort_value = a[i]
        sort_index = k_array[sort_value] - 1
        sorted_array[sort_index] = sort_value

        k_array[sort_value] -= 1
        i -= 1

    # copy sorted array to array
    for i in range(size):
        a[i] = sorted_array[i]

    update_tqdm(pbar, start)

# array - array
# place - 10^k when k > 0, refers to the wanted digit we want to sort by
# optimal when k in O(1)
# best: O(n) | worst: O(n+k) | inplace: no  | stable: yes | additional space: O(n+k)

def countingSort_byDigit(pbar, start,array, place):  # sort by the place given array=[...],place=100 ----> sorted by [10^3 digit]

    size = len(array)  # size of array
    sorted_array = [0] * size  # new sorted output array
    k_array = [0] * 10  # array of possible values

    # Count the appearances of each index of k_array in array (in the given digit)
    for i in range(0, size):
        digit_value = int((array[i] // place) % 10)
        k_array[digit_value] += 1

    # Sum up k_array[0] -------> k_array[9]
    for i in range(1, 10):
        k_array[i] += k_array[i - 1]

    # Place the elements in sorted order
    i = size - 1
    while i >= 0:
        start = update_tqdm(pbar, start)
        digit_value = int((array[i] // place) % 10)  # digit index

        sort_value = array[i]
        sort_index = k_array[digit_value] - 1
        sorted_array[sort_index] = sort_value

        k_array[digit_value] -= 1
        i -= 1

    # copy sorted array to array
    for i in range(size):
        array[i] = sorted_array[i]

    return update_tqdm(pbar, start)

# ------------------- Radix sort : -----------------------

# array - array
# optimal when number of digits is constant (a)
# best: O(n) | worst: O(an) | inplace: no | stable: yes

def Radix_nsort(pbar, start,array):
    # Get maximum element
    max_element = max(array)

    # Apply counting sort to sort elements based on place value.
    place = 1
    while max_element // place > 0:

        start = countingSort_byDigit(pbar, start,array, place)
        place *= 10




