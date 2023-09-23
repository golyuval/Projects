import sklearn as sk
from sklearn import *

# Variables

sk_version = sk.__version__     # Get the version of scikit-learn installed

# Data Preprocessing

sk.preprocessing.StandardScaler()        # Standardize features by removing the mean and scaling to unit variance
sk.preprocessing.MinMaxScaler()          # Scale features to a specified range (usually between 0 and 1)
sk.preprocessing.RobustScaler()          # Scale features using statistics that are robust to outliers
sk.preprocessing.OneHotEncoder()         # Encode categorical integer features as one-hot vectors
sk.preprocessing.LabelEncoder()          # Encode target labels with values between 0 and n_classes-1

# Model Selection and Evaluation

sk.model_selection.train_test_split()    # Split arrays into random train and test subsets
sk.model_selection.cross_val_score()     # Evaluate a score by cross-validation
sk.model_selection.GridSearchCV()        # Perform an exhaustive search over specified parameter values for an estimator
sk.model_selection.RandomizedSearchCV()  # Perform a randomized search over specified parameter values for an estimator
sk.model_selection.KFold()              # K-Folds cross-validator

# Regression Models

sk.linear_model.LinearRegression()      # Ordinary least squares Linear Regression
sk.linear_model.Ridge()                 # Linear predictBy_regression with L2 regularization
sk.linear_model.Lasso()                 # Linear predictBy_regression with L1 regularization
sk.linear_model.ElasticNet()            # Linear predictBy_regression with a combination of L1 and L2 regularization

# Classification Models

sk.linear_model.LogisticRegression()    # Logistic Regression for binary and multiclass classification
sk.svm.SVC()                            # Support Vector Classification
sk.svm.LinearSVC()                      # Linear Support Vector Classification
sk.tree.DecisionTreeClassifier()        # Decision Tree Classifier
sk.ensemble.RandomForestClassifier()    # Random Forest Classifier

# Clustering Models

sk.cluster.KMeans()                     # K-Means clustering algorithm

# Model Evaluation Metrics

sk.metrics.accuracy_score()             # Accuracy classification score
sk.metrics.precision_score()            # Precision score
sk.metrics.recall_score()               # Recall score
sk.metrics.f1_score()                   # F1 score (harmonic mean of precision and recall)
sk.metrics.mean_squared_error()         # Mean squared error for predictBy_regression tasks
sk.metrics.mean_absolute_error()        # Mean absolute error for predictBy_regression tasks
sk.metrics.confusion_matrix()           # Compute confusion matrix to evaluate classification performance

# Model Persistence

sk.externals.joblib.dump()              # Save a model to a file using joblib
sk.externals.joblib.load()              # Load a model from a file using joblib
