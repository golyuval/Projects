import xgboost as xgb
import numpy as np

# ------ Model ---------------------------------------------------------------------------------------------------------

xgboost_model = xgb.XGBRegressor(n_estimators=100, max_depth=3, learning_rate=0.1, objective='reg:squarederror')

# ------ Data ----------------------------------------------------------------------------------------------------------

np.random.seed(42)  # For reproducibility
num_samples = 100
num_features = 3

X = np.random.rand(num_samples, num_features)  # Feature matrix, shape (num_samples, num_features)
y = np.random.rand(num_samples)  # Target vector, shape (num_samples,)

# ------ Methods -------------------------------------------------------------------------------------------------------

xgboost_model.fit(X, y)  # Fit the XGBoost model to the data 'X' and target 'y'
xgboost_model.predict(X)  # Predict target values for the data 'X'
xgboost_model.score(X, y)  # Returns the coefficient of determination R^2 of the prediction
xgboost_model.get_params()  # Get the parameters of the XGBoost model
xgboost_model.set_params(max_depth=5, learning_rate=0.01)  # Set the parameters of the XGBoost model

# ------ Attributes ----------------------------------------------------------------------------------------------------

xgboost_model.feature_importances_  # Importance scores of each feature
xgboost_model.booster  # Get the underlying booster object (e.g., 'gbtree', 'gblinear')
xgboost_model.n_features_in_  # Number of features seen during fit

# ------ Classification Example ----------------------------------------------------------------------------------------

from xgboost import XGBClassifier

xt = yt = []
model = XGBClassifier()
model.fit(xt, yt)
prediction = model.predict(xt)

# ------ Regression Example ----------------------------------------------------------------------------------------

from xgboost import XGBRegressor

xt = yt = []
model = XGBRegressor()
model.fit(xt, yt)
prediction = model.predict(xt)
