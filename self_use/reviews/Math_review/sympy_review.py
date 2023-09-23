import sympy as sp

# ------ Symbols and Expressions ---------------------------------------------------------------------------------------

x = sp.symbols('x')             # Declare 'x' as a symbolic variable
expr = x**2 + 3*x - 2           # Define a symbolic expression

# ------ Simplification and Algebraic Manipulation ---------------------------------------------------------------------

sp.simplify(expr)               # Simplify the expression
sp.expand(expr)                 # Expand the expression
sp.factor(expr)                 # Factorize the expression
sp.collect(expr, x)             # Collect terms with respect to 'x'

# ------ Solving Equations and Systems of Equations --------------------------------------------------------------------

sp.solve(expr, x)               # Solve the equation 'expr' for 'x'
sp.solve([eq1, eq2], (x, y))    # Solve a system of equations [eq1, eq2] for variables (x, y)

# ------ Calculus ------------------------------------------------------------------------------------------------------

sp.diff(expr, x)                # Differentiate the expression with respect to 'x'
sp.integrate(expr, x)           # Integrate the expression with respect to 'x'
sp.limit(expr, x, a)            # Calculate the limit of the expression as 'x' approaches 'a'
sp.series(expr, x, n)           # Generate a Taylor series of the expression up to the 'n' term

# ------ Linear Algebra ------------------------------------------------------------------------------------------------

sp.Matrix([[1, 2], [3, 4]])     # Create a 2x2 matrix
A * B                           # Matrix multiplication
A.inv()                         # Inverse of matrix 'A'
A.det()                         # Determinant of matrix 'A'

# ------ Trigonometry --------------------------------------------------------------------------------------------------

sp.sin(x)                       # Sine of 'x'
sp.cos(x)                       # Cosine of 'x'
sp.tan(x)                       # Tangent of 'x'
sp.atan2(y, x)                  # Arctangent of 'y/x'

# ------ Exponential and Logarithmic Functions -------------------------------------------------------------------------

sp.exp(x)                       # Exponential function e^x
sp.log(x)                       # Natural logarithm (base e) of 'x'
sp.log(x, b)                    # Logarithm of 'x' with base 'b'

# ------ Series and Limits ---------------------------------------------------------------------------------------------

sp.Sum(expr, (i, a, b))         # Summation of 'expr' with 'i' ranging from 'a' to 'b'
sp.limit(expr, x, sp.oo)        # Limit of 'expr' as 'x' approaches infinity

# ------ Calculus Operations -------------------------------------------------------------------------------------------

sp.diff(expr, x, 2)             # Second derivative of 'expr' with respect to 'x'
sp.integrate(expr, (x, a, b))   # Definite integral of 'expr' with 'x' ranging from 'a' to 'b'

# ------ Numeric Approximations ----------------------------------------------------------------------------------------

expr.evalf(subs={x: 1.5})       # Evaluate 'expr' numerically with 'x' replaced by 1.5
