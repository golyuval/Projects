import tensorflow as tf

# Tensor Creation and Basic Operations

inputs = tf.constant([[0.1, 0.2, 0.3]])  # Example: Input data for the dense layer
tensor_const = tf.constant([1, 2, 3])  # Creating a constant tensor
tensor_var = tf.Variable([4, 5, 6])    # Creating a variable tensor
tensor_sum = tensor_const + tensor_var  # Element-wise addition of tensors
tensor_product = tensor_const * tensor_var  # Element-wise multiplication of tensors

mat1 = tf.constant([[1, 2], [3, 4]])  # Creating a matrix tensor
mat2 = tf.constant([[5, 6], [7, 8]])
mat_product = tf.matmul(mat1, mat2)  # Matrix multiplication

# Layers

dense_layer = tf.keras.layers.Dense(units=128, activation='relu')  # Creating a dense layer
lstm_layer = tf.keras.layers.LSTM(units=64, return_sequences=True)  # Creating an LSTM layer
conv_layer = tf.keras.layers.Conv2D(filters=32, kernel_size=(3, 3), activation='relu')  # Creating a convolutional layer

output_dense = dense_layer(inputs)  # Forward pass through a dense layer
output_conv = conv_layer(inputs)  # Forward pass through a convolutional layer
output_lstm = lstm_layer(inputs)  # Forward pass through an LSTM layer

dense_layer1 = tf.keras.layers.Dense(units=64, activation='relu')
dense_layer2 = tf.keras.layers.Dense(units=32, activation='relu')
output_layer = tf.keras.layers.Dense(units=10, activation='softmax')

# Loss Functions and Optimizers

gradients = [tf.constant([0.1, 0.2, 0.3])]
variables = [tf.Variable([1.0, 2.0, 3.0])]

loss_mse = tf.keras.losses.MeanSquaredError()  # Mean squared error loss
loss_categorical = tf.keras.losses.CategoricalCrossentropy()  # Categorical cross-entropy loss

optimizer = tf.keras.optimizers.Adam(learning_rate=0.001)  # Creating an optimizer (Adam optimizer)
optimizer.apply_gradients(zip(gradients, variables))  # Applying gradients and updating variables using optimizer

# Model Evaluation and Training

num_epochs = 20  # Num of iterations for training and validation
batch_size = 64  # Batch size for training and validation

x_train = tf.random.normal((1000, 28, 28, 3))  # X synthetic training values
y_train = tf.one_hot(tf.random.uniform((1000,), maxval=10, dtype=tf.int32), depth=10)  # y synthetic training values

x_val = tf.random.normal((200, 28, 28, 3))  # X synthetic validation values
y_val = tf.one_hot(tf.random.uniform((200,), maxval=10, dtype=tf.int32), depth=10)  # y synthetic validation values

x_test = tf.random.normal((300, 28, 28, 3))  # X synthetic test values
y_test = tf.one_hot(tf.random.uniform((300,), maxval=10, dtype=tf.int32), depth=10)  # y synthetic test values

train_dataset = tf.data.Dataset.from_tensor_slices((x_train, y_train)).batch(batch_size)  # Training dataset
val_dataset = tf.data.Dataset.from_tensor_slices((x_val, y_val)).batch(batch_size)        # Validation dataset
test_dataset = tf.data.Dataset.from_tensor_slices((x_test, y_test)).batch(batch_size)     # Test dataset

model = tf.keras.Sequential([dense_layer1, dense_layer2, output_layer])  # Creating a model
model.compile(loss=loss_mse, optimizer=optimizer, metrics=['accuracy'])  # Compiling the model with loss and optimizer
history = model.fit(train_dataset, epochs=num_epochs, validation_data=val_dataset)  # Training the model
accuracy = model.evaluate(test_dataset)  # Evaluating the model

# Saving and Loading Models

model.save('my_model')  # Saving the model architecture and weights
loaded_model = tf.keras.models.load_model('my_model')  # Loading a saved model
