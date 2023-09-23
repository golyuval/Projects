import torch

# ------ Tensor Creation and Basic Operations ------------------------------------------------------------------------

tensor = torch.tensor([1, 2, 3])  # Creating a tensor
tensor = tensor.to(torch.float32)  # Converting tensor data type

tensor_sum = tensor + tensor  # Element-wise addition of tensors
tensor_product = tensor * tensor  # Element-wise multiplication of tensors

mat1 = torch.tensor([[1, 2], [3, 4]])  # Creating a matrix tensor
mat2 = torch.tensor([[5, 6], [7, 8]])
mat_product = torch.mm(mat1, mat2)  # Matrix multiplication

# ------ Neural Network Operations ----------------------------------------------------------------------------------

input_size = 128
output_size = 64
batch_size = 32
seq_length = 10
num_layers = 2

input_1 = torch.randn(batch_size, input_size)
input_2 = torch.randn(batch_size, 3, 64, 64)
input_3 = torch.randn(seq_length, batch_size, input_size)

linear_layer = torch.nn.Linear(in_features=128, out_features=64)  # Creating a linear layer
output = linear_layer(input_1)  # Forward pass through a linear layer

conv_layer = torch.nn.Conv2d(in_channels=3, out_channels=32, kernel_size=(3, 3))  # Creating a convolutional layer
output_conv = conv_layer(input_2)  # Forward pass through a convolutional layer

lstm_layer = torch.nn.LSTM(input_size=input_size, hidden_size=output_size, num_layers=num_layers)  # Creating an LSTM layer
output_lstm, (hidden, cell) = lstm_layer(input_3)  # Forward pass through an LSTM layer

# ------ Loss Functions and Optimizers ------------------------------------------------------------------------------

loss_mse = torch.nn.MSELoss()  # Mean squared error loss
loss_cross_entropy = torch.nn.CrossEntropyLoss()  # Cross-entropy loss

model = torch.nn.Linear(input_size, output_size)
optimizer = torch.optim.Adam(model.parameters(), lr=0.001)  # Creating an optimizer (Adam optimizer)
optimizer.zero_grad()  # Resetting gradients
optimizer.step()  # Updating model parameters based on gradients

# ------ Model Evaluation and Training ------------------------------------------------------------------------------


model = torch.nn.Sequential(
    torch.nn.Linear(input_size, output_size),
    torch.nn.ReLU(),
    torch.nn.Linear(output_size, output_size),
    torch.nn.Softmax(dim=1)
)  # Creating a model

criterion = torch.nn.CrossEntropyLoss()  # Defining a loss criterion
optimizer = torch.optim.Adam(model.parameters(), lr=0.001)  # Creating an optimizer

inputs = torch.randn(batch_size, input_size)
labels = torch.randint(0, output_size, (batch_size,))
outputs = model(inputs)  # Forward pass through the model
loss = criterion(outputs, labels)  # Calculating the loss
loss.backward()  # Backpropagation
optimizer.step()  # Updating model parameters

# ------ Saving and Loading Models ---------------------------------------------------------------------------------

torch.save(model.state_dict(), 'my_model.pth')  # Saving the model parameters
loaded_model = torch.nn.Sequential(
    torch.nn.Linear(input_size, output_size),
    torch.nn.ReLU(),
    torch.nn.Linear(output_size, output_size),
    torch.nn.Softmax(dim=1)
)  # Creating an instance of the model
loaded_model.load_state_dict(torch.load('my_model.pth'))  # Loading model parameters
