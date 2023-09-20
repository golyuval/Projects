import random
import numpy as np
from tqdm import tqdm
import sorting_impl
import time

class Sorter:

    tqdm_settings = {
        "total": 10,
        "unit": "iter",
        "bar_format": "{desc}: {n:.2f}s",
    }

    def insertion_sort(self,pbar,array):

        start = time.time()
        pbar.set_description("insertion sort running ....  ")
        sorting_impl.Heap_sort(pbar, array)
        end = time.time()
        timer = f"{(end-start):.6f}"

        return array, timer

    def bubble_sort(self,pbar,array):

        start = time.time()
        pbar.set_description("bubble sort running ....  ")
        sorting_impl.Heap_sort(pbar, array)
        end = time.time()
        timer = f"{(end-start):.6f}"

        return array, timer

    def heap_sort(self,pbar,array):

        start = time.time()
        pbar.set_description("heap sort running ....  ")
        sorting_impl.Heap_sort(pbar, array)
        end = time.time()
        timer = f"{(end-start):.6f}"

        return array, timer

    def merge_sort(self, pbar, array, left, right):
        start = time.time()
        pbar.set_description_str("merge sort running ....  ")
        sorting_impl.Merge_sort(pbar, start, array, left, right)
        end = time.time()
        timer = f"{(end - start):.6f}"

        return array, timer

    def quick_sort(self, pbar, array, low, high):
        start = time.time()
        pbar.set_description_str("quick sort running ....  ")
        sorting_impl.Quick_sort(pbar, start, array, low, high)
        end = time.time()
        timer = f"{(end - start):.6f}"

        return array, timer

    def bucket_sort(self, pbar, array, buckets):
        start = time.time()
        pbar.set_description_str("bucket sort running ....  ")
        sorting_impl.Bucket_nsort(pbar, start, array, buckets)
        end = time.time()
        timer = f"{(end - start):.6f}"

        return array, timer

    def counting_sort(self, pbar,  array, k):
        start = time.time()
        pbar.set_description_str("counting sort running ....  ")
        sorting_impl.Counting_nsort(pbar, start, array, k)
        end = time.time()
        timer = f"{(end - start):.6f}"

        return array, timer

    def counting_sort_digit(self, pbar, array, place):
        start = time.time()
        pbar.set_description_str("counting by digit sort running ....  ")
        sorting_impl.countingSort_byDigit(pbar, start, array, place)
        end = time.time()
        timer = f"{(end - start):.6f}"

        return array, timer

    def radix_sort(self, pbar, array):
        start = time.time()
        pbar.set_description_str("radix sort running ....  ")
        sorting_impl.Radix_nsort(pbar, start, array)
        end = time.time()
        timer = f"{(end - start):.6f}"

        return array, timer



    def show_statistics(self, size=0,array=[]):
        with tqdm(**self.tqdm_settings) as pbar:

            array = np.arange(size).tolist() if array == [] else array
            random.shuffle(array)

            insertion, insertion_time = self.insertion_sort(pbar, array)
            self.print_stats(insertion)
            self.reset_all(array, size, pbar)

            bubble, bubble_time = self.bubble_sort(pbar, array)
            self.print_stats(bubble)
            self.reset_all(array, size, pbar)

            heap, heap_time = self.heap_sort(pbar, array)
            self.print_stats(heap)
            self.reset_all(array, size, pbar)

            merge, merge_time = self.merge_sort(pbar, array, 0, len(array) - 1)
            self.print_stats(merge)
            self.reset_all(array, size, pbar)

            quick, quick_time = self.quick_sort(pbar, array, 0, len(array) - 1)
            self.print_stats(quick)
            self.reset_all(array, size, pbar)

            bucket, bucket_time = self.bucket_sort(pbar, array, size)
            self.print_stats(bucket)
            self.reset_all(array, size, pbar)

            counting, counting_time = self.counting_sort(pbar, array, size)
            self.print_stats(counting)
            self.reset_all(array, size, pbar)

            digit, digit_time = self.counting_sort_digit(pbar, array, 1)
            self.print_stats(digit)
            self.reset_all(array, size, pbar)

            radix, radix_time = self.radix_sort(pbar, array)
            self.print_stats(radix)

    def show_known(self):
        print("small data: ")
        print("large data: ")
        print("ordered data: ")
        print("large values: ")
        print("small values: ")

    def print_stats(self, array=[], sort_type="none", time=0):

        s = ""
        if sort_type != "none":
            s += f"-  {sort_type} sort:"
            s += f"\n\t-- array: {array[:5]} ... {array[-5:]}"
            s += f"\n\t-- time: {time}\n"
        else:
            arr_p = "[" + f"{array[:3]} ... {array[-3:]}".replace("[","").replace("]","") + "]"
            s +=  f" | ----> array: {arr_p}"

        print(s)

    def reset_all(self, array, size, pbar):
        array = np.arange(size).tolist() if array == [] else array
        random.shuffle(array)
        pbar.reset(10)



sorter = Sorter()
size_6 = 10**6
sorter.show_statistics(size_6)





