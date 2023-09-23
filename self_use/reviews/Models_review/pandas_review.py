import pandas as pd

# ------ DataFrame Creation ------------------------------------------------------------------------------------------

data = {'Name': ['Alice', 'Bob', 'Charlie', 'David', 'Eve'],
        'Age': [25, 30, 35, 40, 45],
        'City': ['New York', 'Los Angeles', 'Chicago', 'Houston', 'Miami']}

df = pd.DataFrame(data)  # Create a DataFrame from a dictionary

# ------ Data Exploration --------------------------------------------------------------------------------------------

df.head()  # Display the first few rows of the DataFrame
df.describe()  # Generate descriptive statistics of the DataFrame
df.info()  # Display information about the DataFrame's columns and data types

# ------ Data Selection ----------------------------------------------------------------------------------------------

df['Name']  # Select a single column by name
df[['Name', 'Age']]  # Select multiple columns by name
df.iloc[2]  # Select a single row by integer index
df.iloc[1:4]  # Select multiple rows by integer index
df.loc[df['Age'] > 30]  # Select rows based on a condition

# ------ Data Manipulation --------------------------------------------------------------------------------------------

df['Age'].mean()  # Calculate the mean of a numeric column
df.sort_values(by='Age', ascending=False)  # Sort the DataFrame by a column
df['City'].unique()  # Get unique values in a column
df['City'].value_counts()  # Count the occurrences of each unique value

# ------ Missing Data Handling ----------------------------------------------------------------------------------------

df.dropna()  # Remove rows with missing values
df.fillna(value=0)  # Fill missing values with a specific value

# ------ DataFrame Operations -----------------------------------------------------------------------------------------

df1 = df[df['Age'] > 30]  # Create a new DataFrame based on a condition
df['FullName'] = df['Name'] + ' Doe'  # Add a new column to the DataFrame

# ------ Example -------------------------------------------------------------------------------------------------------

print("DataFrame: ")
print(df)

print("First Few Rows: ")
print(df.head())

print("Descriptive Statistics: ")
print(df.describe())

print("Column Names and Data Types: ")
print(df.info())

print("Average Age: ", df['Age'].mean())

print("Sorted by Age: ")
print(df.sort_values(by='Age', ascending=False))
