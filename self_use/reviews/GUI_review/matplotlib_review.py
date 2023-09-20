import matplotlib.pyplot as plt
import numpy as np

# ------ Creating a Line Plot --------------------------------------------------------------------------------------

x_line = np.linspace(0, 10, 100)
y_line = np.sin(x_line)
plt.plot(x_line, y_line, label='Sine Curve')
plt.xlabel('X Values')  # Label for the x-axis
plt.ylabel('Y Values')  # Label for the y-axis
plt.title('Line Plot Example')  # Title of the plot
plt.legend()  # Display legend
plt.show()

# ------ Creating a Scatter Plot -----------------------------------------------------------------------------------

x_scatter = np.random.rand(50)
y_scatter = np.random.rand(50)
plt.scatter(x_scatter, y_scatter, label='Random Data', marker='o')
plt.xlabel('X Values')  # Label for the x-axis
plt.ylabel('Y Values')  # Label for the y-axis
plt.title('Scatter Plot Example')  # Title of the plot
plt.legend()  # Display legend
plt.show()

# ------ Creating a Bar Chart --------------------------------------------------------------------------------------

categories = ['A', 'B', 'C', 'D']
values = [10, 25, 15, 30]
plt.bar(categories, values, label='Bar Chart')
plt.xlabel('Categories')  # Label for the x-axis
plt.ylabel('Values')  # Label for the y-axis
plt.title('Bar Chart Example')  # Title of the plot
plt.legend()  # Display legend
plt.show()

# ------ Creating a Histogram --------------------------------------------------------------------------------------

data_hist = np.random.randn(1000)
plt.hist(data_hist, bins=20, edgecolor='black')
plt.xlabel('Value')  # Label for the x-axis
plt.ylabel('Frequency')  # Label for the y-axis
plt.title('Histogram Example')  # Title of the plot
plt.show()

# ------ Adding Annotations and Text -------------------------------------------------------------------------------

x_ann = 2
y_ann = 0.5
x_text = 4
y_text = 0.8
plt.annotate('Annotation', xy=(x_ann, y_ann), xytext=(x_text, y_text), arrowprops=dict(arrowstyle='->'))
plt.text(6, 0.3, 'Text Example', fontsize=12)
plt.plot(x_line, y_line)
plt.show()

# ------ Customizing Plot Appearance -------------------------------------------------------------------------------

plt.figure(figsize=(8, 6))  # Set the figure size
plt.xticks(rotation=45)  # Rotate x-axis tick labels
plt.yticks(fontsize=10)  # Set font size for y-axis tick labels
plt.grid(True)  # Display grid
plt.tight_layout()  # Automatically adjust subplot parameters for a neat layout
plt.plot(x_line, y_line)
plt.show()

# ------ Saving a Plot to File -------------------------------------------------------------------------------------

plt.plot(x_line, y_line)
plt.savefig('line_plot.png', dpi=300, bbox_inches='tight')  # Save plot to a file with high resolution
plt.show()

# ------ Working with Subplots -------------------------------------------------------------------------------------

plt.subplot(2, 2, 1)  # Create a 2x2 grid of subplots, and select the first subplot
plt.plot(x_line, y_line)
plt.subplot(2, 2, 2)  # Select the second subplot
plt.scatter(x_scatter, y_scatter)
plt.subplot(2, 2, 3)  # Select the third subplot
plt.bar(categories, values)
plt.subplot(2, 2, 4)  # Select the fourth subplot
plt.hist(data_hist, bins=20, edgecolor='black')
plt.tight_layout()  # Automatically adjust subplot parameters for a neat layout
plt.show()
