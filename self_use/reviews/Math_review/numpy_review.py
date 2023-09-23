import numpy as np

# ------ Numpy Variables -----------------------------------------------------------------------------------------------

pi = np.pi            # Numeric value of pi (3.141592653589793)
e = np.e              # Numeric value of Euler's number (2.718281828459045)

# ------ Arithmetic Functions ------------------------------------------------------------------------------------------

a = np.array([1, 2, 3])
b = np.array([4, 5, 6])

np.add(a, b)          # Element-wise addition of arrays 'a' and 'b'
np.subtract(a, b)     # Element-wise subtraction of 'b' from 'a'
np.multiply(a, b)     # Element-wise multiplication of arrays 'a' and 'b'
np.divide(a, b)       # Element-wise division of array 'a' by 'b'
np.power(a, b)        # Element-wise raising 'a' to the power of 'b'
np.sqrt(a)            # Element-wise square root of array 'a'

# ------ Array Functions -----------------------------------------------------------------------------------------------
a1 = np.array([1, 2, 3, 4])
a2 = np.array([5, 6, 7, 8])
m1 = np.array([[1, 2, 3, 4],
               [4, 5, 6, 7]])
m2 = np.array([[4, 5, 6, 8],
               [9, 2, 1, 4]])

a1.reshape(2, 2)  # reshapes a1 to [[1,2],[3,4]]
a1.reshape(1, 4)  # reshapes a1 to [1,2,3,4]
np.dot(a1, a2)  # a1 (dot product) a2  =  a1.T*a2
np.matmul(m1,m2)  # -alternative- m1 @ m2  =  m1.T*m2

# ------ Trigonometric Functions ---------------------------------------------------------------------------------------

x = np.pi/4           # Numeric value of pi/4

np.sin(x)             # Sine of 'x' (in radians)
np.cos(x)             # Cosine of 'x' (in radians)
np.tan(x)             # Tangent of 'x' (in radians)
np.arcsin(x)          # Inverse sine (in radians)
np.arccos(x)          # Inverse cosine (in radians)
np.arctan(x)          # Inverse tangent (in radians)

# ------- Exponential and Logarithmic Functions ------------------------------------------------------------------------

x = np.array([1, 2, 3])

np.exp(x)             # Exponential function e^x
np.log(x)             # Natural logarithm (base e) of 'x'
np.log10(x)           # Base-10 logarithm of 'x'

# ------ Sum, Mean, and Statistical Functions --------------------------------------------------------------------------

array = np.array([1, 2, 3, 4, 5])

np.sum(array)         # Sum of all elements in the array
np.mean(array)        # Mean (average) of all elements in the array
np.min(array)         # Minimum value in the array
np.max(array)         # Maximum value in the array
np.std(array)         # Standard deviation of the elements in the array
np.var(array)         # Variance of the elements in the array

# ------ Array Creation and Manipulation -------------------------------------------------------------------------------

start = 0
stop = 10
step = 2
num = 5
shape = (3, 4)

np.arange(start, stop, step)  # Array with evenly spaced values from 'start' to 'stop' (exclusive), with 'step' increment
np.linspace(start, stop, num) # Array with 'num' evenly spaced values from 'start' to 'stop' (inclusive)
np.zeros(shape)       # Array of zeros with the given 'shape'
np.ones(shape)        # Array of ones with the given 'shape'
np.random.rand(*shape) # Array of random numbers with the given 'shape' from a uniform distribution [0, 1)
np.random.randn(*shape) # Array of random numbers with the given 'shape' from a standard normal distribution

# ------ Array Indexing and Slicing ------------------------------------------------------------------------------------

array = np.array([1, 2, 3, 4, 5, 6, 7, 8, 9])
condition = array > 3
index = 2
start = 1
stop = 5
row = 1
column = 2

array[index]          # Accessing element at the specified 'index'
array[start:stop]     # Slicing the array from 'start' index to 'stop' index (exclusive)
array[condition]      # Selecting elements from the array based on 'condition'
array.reshape(3,3)[:, row]         # Selecting all rows of a specific 'column'
array.reshape(3,3)[:, column]      # Selecting all columns of a specific 'row'

# ------ Adding np.c_ for array concatenation along the second axis (column-wise) --------------------------------------

a = np.array([1, 2, 3])
b = np.array([4, 5, 6])

np.c_[a, b]  # [ [1 4] [2 5] [3 6] ]


# ------ Extras --------------------------------------------------------------------------------------------------------

x = np.arange(4)

y = np.where(x<3, x, 20*x)  # return array y when each y[i] is x[i] (if x[i]<2) or 20*x[i] (otherwise)
np.argmax(x)  # ?????????????????????????????????????

if __name__ == "__main__":

    print("NUMPY REVIEW")