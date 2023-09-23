import sklearn.linear_model as sk_lm
import numpy as np

# ------ Model ---------------------------------------------------------------------------------------------------------

linear = sk_lm.LinearRegression()

# ------ Data ----------------------------------------------------------------------------------------------------------

np.random.seed(42)  # For reproducibility
num_samples = 6

X1 = np.random.rand(num_samples)  # feature 1 size 4
X2 = np.random.rand(num_samples)  # feature 2 size 4
X3 = np.random.rand(num_samples)  # feature 3 size 4
X = np.column_stack((X1, X2, X3))  # each of 4 inputs format = [f1,f2,f3]

b = 4
W = np.array([2,3,4])
y = np.dot(W,X.T) + b  # linear relationship

# ------ Methods -------------------------------------------------------------------------------------------------------

linear.fit(X, y)               # Fit linear predictBy_regression model to the data 'X' and target 'y'
linear.predict(X)              # Predict target values for the data 'X'
linear.score(X, y)             # Returns the coefficient of determination R^2 of the prediction
linear.get_params()            # Get the parameters of the linear predictBy_regression model
linear.set_params(fit_intercept=False, positive=True)    # Set the parameters of the linear predictBy_regression model

# ------ Attributes ----------------------------------------------------------------------------------------------------

linear.coef_                   # Coefficients of the features (slope of the linear predictBy_regression line) [W=w1,w2..]
linear.intercept_              # Intercept of the linear predictBy_regression line [W]
linear.rank_                   # Rank of the feature matrix 'X'
linear.singular_               # Singular values of the feature matrix 'X'
linear.predict(X)              # Predicted target values for the data 'X'

# ---------------- Example ---------------------------------------------------------------------------------------------

print("X  :  ",X)
print("W  :  ",W)
print("b  :  ",b)
print("y  :  ",y)

print("coef  :  ",linear.coef_  )
print("intercept  :  ",linear.intercept_  )
print("rank  :  ",linear.rank_  )
print("singular  :  ",linear.singular_  )
